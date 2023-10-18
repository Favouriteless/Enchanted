package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.rites.binding.*;
import com.favouriteless.enchanted.common.rites.curse.*;
import com.favouriteless.enchanted.common.rites.entity.*;
import com.favouriteless.enchanted.common.rites.entity.protection.RiteProtection;
import com.favouriteless.enchanted.common.rites.entity.protection.RiteProtectionLarge;
import com.favouriteless.enchanted.common.rites.entity.protection.RiteProtectionTemporary;
import com.favouriteless.enchanted.common.rites.processing.RiteBroiling;
import com.favouriteless.enchanted.common.rites.processing.RiteBroilingCharged;
import com.favouriteless.enchanted.common.rites.processing.RiteChargingStone;
import com.favouriteless.enchanted.common.rites.processing.RiteInfusionBroom;
import com.favouriteless.enchanted.common.rites.world.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RiteTypes {

    private static final Map<ResourceLocation, RiteType<?>> RITE_TYPES = new HashMap<>();

    public static final RiteType<RiteBindingTalisman> BINDING_TALISMAN = register("binding_talisman", RiteBindingTalisman::new);
    public static final RiteType<RiteBindingTalismanCharged> BINDING_TALISMAN_CHARGED = register("binding_talisman_charged", RiteBindingTalismanCharged::new);
    public static final RiteType<RiteBindingWaystone> BINDING_WAYSTONE = register("binding_waystone", RiteBindingWaystone::new);
    public static final RiteType<RiteBindingWaystoneCharged> BINDING_WAYSTONE_CHARGED = register("binding_waystone_charged", RiteBindingWaystoneCharged::new);
    public static final RiteType<RiteBindingWaystoneDuplicate> BINDING_WAYSTONE_DUPLICATE = register("binding_waystone_duplicate", RiteBindingWaystoneDuplicate::new);
    public static final RiteType<RiteBindingWaystoneDuplicateCharged> BINDING_WAYSTONE_DUPLICATE_CHARGED = register("binding_waystone_duplicate_charged", RiteBindingWaystoneDuplicateCharged::new);
    public static final RiteType<RiteBindingWaystonePlayer> BINDING_WAYSTONE_PLAYER = register("binding_waystone_player", RiteBindingWaystonePlayer::new);
    public static final RiteType<RiteBindingWaystonePlayerCharged> BINDING_WAYSTONE_PLAYER_CHARGED = register("binding_waystone_player_charged", RiteBindingWaystonePlayerCharged::new);
    public static final RiteType<RiteBindingFamiliar> BINDING_FAMILIAR = register("binding_familiar", RiteBindingFamiliar::new);
    public static final RiteType<RiteBroiling> BROILING = register("broiling", RiteBroiling::new);
    public static final RiteType<RiteBroilingCharged> BROILING_CHARGED = register("broiling_charged", RiteBroilingCharged::new);
    public static final RiteType<RiteChargingStone> CHARGING_STONE = register("charging_stone", RiteChargingStone::new);
    public static final RiteType<RiteCurseOfBlight> CURSE_OF_BLIGHT = register("curse_of_blight", RiteCurseOfBlight::new);
    public static final RiteType<RiteCurseMisfortune> CURSE_OF_MISFORTUNE = register("curse_of_misfortune", RiteCurseMisfortune::new);
    public static final RiteType<RiteCurseOverheating> CURSE_OF_OVERHEATING = register("curse_of_overheating", RiteCurseOverheating::new);
    public static final RiteType<RiteCurseSinking> CURSE_OF_SINKING = register("curse_of_sinking", RiteCurseSinking::new);
    public static final RiteType<RiteFertility> FERTILITY = register("fertility", RiteFertility::new);
    public static final RiteType<RiteFertilityCharged> FERTILITY_CHARGED = register("fertility_charged", RiteFertilityCharged::new);
    public static final RiteType<RiteForest> FOREST = register("forest", RiteForest::new);
    public static final RiteType<RiteImprisonment> IMPRISONMENT = register("imprisonment", RiteImprisonment::new);
    public static final RiteType<RiteInfusionBroom> INFUSION_BROOM = register("infusion_broom", RiteInfusionBroom::new);
    public static final RiteType<RiteProtection> PROTECTION = register("protection", RiteProtection::new);
    public static final RiteType<RiteProtectionLarge> PROTECTION_LARGE = register("protection_large", RiteProtectionLarge::new);
    public static final RiteType<RiteProtectionTemporary> PROTECTION_TEMPORARY = register("protection_temporary", RiteProtectionTemporary::new);
    public static final RiteType<RiteRemoveMisfortune> REMOVE_MISFORTUNE = register("remove_misfortune", RiteRemoveMisfortune::new);
    public static final RiteType<RiteRemoveOverheating> REMOVE_OVERHEATING = register("remove_overheating", RiteRemoveOverheating::new);
    public static final RiteType<RiteRemoveSinking> REMOVE_SINKING = register("remove_sinking", RiteRemoveSinking::new);
    public static final RiteType<RiteSanctity> SANCTITY = register("sanctity", RiteSanctity::new);
    public static final RiteType<RiteSkyWrath> SKY_WRATH = register("sky_wrath", RiteSkyWrath::new);
    public static final RiteType<RiteSkyWrathCharged> SKY_WRATH_CHARGED = register("sky_wrath_charged", RiteSkyWrathCharged::new);
    public static final RiteType<RiteSummonEntity> SUMMONING_ENTITY = register("summoning_entity", RiteSummonEntity::new);
    public static final RiteType<RiteSummonFamiliar> SUMMONING_FAMILIAR = register("summoning_familiar", RiteSummonFamiliar::new);
    public static final RiteType<RiteTotalEclipse> TOTAL_ECLIPSE = register("total_eclipse", RiteTotalEclipse::new);
    public static final RiteType<RiteTotalEclipseCharged> TOTAL_ECLIPSE_CHARGED = register("total_eclipse_charged", RiteTotalEclipseCharged::new);
    public static final RiteType<RiteTranspositionIron> TRANSPOSITION_IRON = register("transposition_iron", RiteTranspositionIron::new);
    public static final RiteType<RiteTranspositionPlayer> TRANSPOSITION_PLAYER = register("transposition_player", RiteTranspositionPlayer::new);
    public static final RiteType<RiteTranspositionPlayerBlooded> TRANSPOSITION_PLAYER_BLOODED = register("transposition_player_blooded", RiteTranspositionPlayerBlooded::new);

    /**
     * Register a {@link RiteType} to be used by Enchanted.
     */
    public static <T extends AbstractRite> RiteType<T> register(ResourceLocation id, RiteType.RiteFactory<T> factory) {
        RiteType<T> riteType = new RiteType<>(id, factory);
        RITE_TYPES.put(id, riteType);
        return riteType;
    }

    private static <T extends AbstractRite> RiteType<T> register(String id, RiteType.RiteFactory<T> factory) {
        return register(Enchanted.location(id), factory);
    }

    public static RiteType<?> get(ResourceLocation id) {
        return RITE_TYPES.get(id);
    }

    /**
     * Attempt to create a rite instance in the specified {@link ServerLevel} at the specified {@link BlockPos} if the
     * requirements for the rite are met at that position.
     *
     * @param level The {@link ServerLevel} to create the Rite in.
     * @param pos The {@link BlockPos} to create the Rite at.
     *
     * @return An instance of the {@link AbstractRite} implementation which has its requirements met at level, pos. Or
     * if no rites have their requirements met, null.
     */
    public static AbstractRite getRiteAt(ServerLevel level, BlockPos pos, UUID caster) {
        AbstractRite currentRite = null;
        int currentDiff = Integer.MAX_VALUE;

        for(RiteType<?> type : RITE_TYPES.values()) {
            AbstractRite rite = type.create(level, pos, caster);
            int diff = rite.differenceAt(level, pos);
            if(diff != -1 && diff < currentDiff) {
                currentRite = rite;
                currentDiff = diff;
            }
        }
        return currentRite;
    }

    /**
     * This method is only used by patchouli and/or JEI to grab the requirements of a rite.
     */
    public static AbstractRite getDefaultByName(ResourceLocation id) {
        RiteType<?> type = RITE_TYPES.get(id);
        return type != null ? type.create() : null;
    }

}
