package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.api.circle_magic.RiteFactoryRegistry;
import net.favouriteless.enchanted.common.circle_magic.rites.factory.*;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.factory.*;

public class ERiteFactories {

    public static void load() {
        RiteFactoryRegistry registry = RiteFactoryRegistry.get();
        
        registry.register(ApplyCurseFactory.ID, ApplyCurseFactory.CODEC);
        registry.register(BindFamiliarFactory.ID, BindFamiliarFactory.CODEC);
        registry.register(BindTalismanFactory.ID, BindTalismanFactory.CODEC);
        registry.register(BlightFactory.ID, BlightFactory.CODEC);
        registry.register(BroilingFactory.ID, BroilingFactory.CODEC);
        registry.register(CommandFactory.ID, CommandFactory.CODEC);
        registry.register(CreateItemFactory.ID, CreateItemFactory.CODEC);
        registry.register(DuplicateItemFactory.ID, DuplicateItemFactory.CODEC);
        registry.register(EntityBoundCreateItemFactory.ID, EntityBoundCreateItemFactory.CODEC);
        registry.register(FertilityFactory.ID, FertilityFactory.CODEC);
        registry.register(ForestFactory.ID, ForestFactory.CODEC);
        registry.register(ImprisonmentFactory.ID, ImprisonmentFactory.CODEC);
        registry.register(LocationBoundCreateItemFactory.ID, LocationBoundCreateItemFactory.CODEC);
        registry.register(ProtectionFactory.ID, ProtectionFactory.CODEC);
        registry.register(RemoveCurseFactory.ID, RemoveCurseFactory.CODEC);
        registry.register(SanctityFactory.ID, SanctityFactory.CODEC);
        registry.register(SkyWrathFactory.ID, SkyWrathFactory.CODEC);
        registry.register(SummonEntityFactory.ID, SummonEntityFactory.CODEC);
        registry.register(SummonFamiliarFactory.ID, SummonFamiliarFactory.CODEC);
        registry.register(TotalEclipseFactory.ID, TotalEclipseFactory.CODEC);
        registry.register(TransposeBlocksFactory.ID, TransposeBlocksFactory.CODEC);
        registry.register(TransposeCasterFactory.ID, TransposeCasterFactory.CODEC);
    }

}
