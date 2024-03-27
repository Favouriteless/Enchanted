package com.favouriteless.enchanted.client.render.armor;

import com.favouriteless.enchanted.client.render.model.SimpleAnimatedGeoModel;
import com.favouriteless.enchanted.common.items.EarmuffsItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class EarmuffsRenderer extends GeoArmorRenderer<EarmuffsItem> {

    public EarmuffsRenderer() {
        super(new SimpleAnimatedGeoModel<>("armor", "earmuffs"));

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }

}