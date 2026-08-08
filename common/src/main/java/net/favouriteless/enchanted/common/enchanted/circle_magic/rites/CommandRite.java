package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CommandRite extends Rite implements CommandSource {

    private final List<List<String>> commands;
    private final int delay;

    private int current = 1;

    public CommandRite(BaseRiteParams baseParams, RiteParams params, List<List<String>> commands, int delay) {
        super(baseParams, params);
        this.commands = commands;
        this.delay = delay;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        CommandSourceStack sourceStack = new CommandSourceStack(
                this, Vec3.atCenterOf(pos),
                new Vec2(0, 0), level, 2, "Command Rite",
                Component.literal("Command Rite"), level.getServer(), null
        );

        if (delay == 0) {
            commands.forEach(list -> list.forEach(command -> level.getServer().getCommands().performPrefixedCommand(
                    sourceStack,
                    replacedVars(command, params.caster, params.target)
            )));
        } else {
            commands.getFirst().forEach(c -> level.getServer().getCommands().performPrefixedCommand(
                    sourceStack,
                    replacedVars(c, params.caster, params.target)
            ));
        }
        return delay > 0 && commands.size() > 1;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        if (params.ticks() % delay == 0) {
            CommandSourceStack sourceStack = new CommandSourceStack(
                    this, Vec3.atCenterOf(pos),
                    new Vec2(0, 0), level, 2, "Command Rite",
                    Component.literal("Command Rite"), level.getServer(), null
            );

            commands.get(current++).forEach(c -> level.getServer().getCommands().performPrefixedCommand(
                    sourceStack,
                    replacedVars(c, params.caster, params.caster)
            ));
        }
        return current < commands.size();
    }

    protected String replacedVars(String command, @Nullable UUID caster, @Nullable UUID target) {
        return command
                .replaceAll("@caster", caster != null ? caster.toString() : "@caster")
                .replaceAll("@target", target != null ? target.toString() : "@target");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, ServerLevel level) {
        tag.putInt("current", current);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, ServerLevel level) {
        current = tag.getInt("current");
    }

    @Override
    public void sendSystemMessage(Component component) {

    }

    @Override
    public boolean acceptsSuccess() {
        return false;
    }

    @Override
    public boolean acceptsFailure() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }

}
