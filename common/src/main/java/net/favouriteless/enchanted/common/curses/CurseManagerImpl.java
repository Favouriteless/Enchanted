package net.favouriteless.enchanted.common.curses;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.api.curses.Curse.Type;
import net.favouriteless.enchanted.api.curses.CurseInstance;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class CurseManagerImpl implements CurseManager {

	public static final CurseManagerImpl INSTANCE = new CurseManagerImpl();

	private final BiMap<ResourceLocation, Type<?>> types = HashBiMap.create();

	private final Codec<Type<?>> typeCodec = ResourceLocation.CODEC.flatXmap(
			r -> {
				Type<?> c = getType(r);
				return c != null ? DataResult.success(c) : DataResult.error(() -> "Unknown type " + r);
			},
			c -> {
				ResourceLocation location = types.inverse().get(c);
				return c != null ? DataResult.success(location) : DataResult.error(() -> "Unknown type " + location);
			}
	);
	private final Codec<Curse> codec = typeCodec.dispatch("type", Curse::type, Type::codec);

	@Override
	public void register(Type<?> type) {
		if(types.containsKey(type.id()))
			throw new IllegalArgumentException("Attempted to register a duplicate curse type: " + type.id());
		else
			types.put(type.id(), type);
	}

	@Override
	public void applyCurse(Type<?> type, UUID target, int curseLevel, ServerLevel level) {
		Collection<CurseInstance> curses = getCurses(target, level);
		curses.stream()
				.filter(instance -> instance.getCurse().type().equals(type))
				.findAny()
				.ifPresentOrElse(
						instance -> instance.setLevel(Math.max(instance.getLevel(), curseLevel)),
						() -> curses.add(new CurseInstanceImpl(type.supplier().get(), target, curseLevel, 0))
				);
		CurseSavedData.get(level).setDirty();
	}

	@Override
	public void removeCurse(Type<?> type, UUID target, ServerLevel level) {
		CurseSavedData.get(level).setDirty();
		getCurses(target, level).stream()
				.filter(instance -> instance.getCurse().type().equals(type))
				.findFirst()
				.ifPresent(c -> ((CurseInstanceImpl)c).setRemoved());
	}

	@Override
	public Collection<CurseInstance> getCurses(UUID target, ServerLevel level) {
		return CurseSavedData.get(level).get(target);
	}

	@Override
	public Type<?> getType(ResourceLocation id) {
		return types.get(id);
	}

	@Override
	public Codec<Type<?>> typeCodec() {
		return typeCodec;
	}

	@Override
	public Codec<Curse> codec() {
		return codec;
	}

	// ----------------------------------------- Non-API implementations below -----------------------------------------

	public void initialisePlayer(ServerPlayer player) {
		getCurses(player.getUUID(), player.serverLevel()).forEach(i -> i.getCurse().onInitialise(player, i.getLevel(), i.getAge()));
	}

	public void tick(ServerLevel level) {
		for(ServerPlayer player : level.getPlayers(p -> true)) {
			Iterator<CurseInstance> iterator = getCurses(player.getUUID(), level).iterator();
			while(iterator.hasNext()) {
				CurseInstanceImpl curse = (CurseInstanceImpl)iterator.next();

				if(curse.isRemoved()) {
					curse.getCurse().onRemove(player, curse.getLevel(), curse.getAge());
					iterator.remove();
					continue;
				}

				curse.tick(level);
			}
		}
	}

}
