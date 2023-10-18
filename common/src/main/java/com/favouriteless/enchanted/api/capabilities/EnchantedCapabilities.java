package com.favouriteless.enchanted.api.capabilities;

import com.favouriteless.enchanted.api.familiars.IFamiliarCapability;

public class EnchantedCapabilities {

	public static Capability<IBedCapability> BED = CapabilityManager.get(new CapabilityToken<>() {});
	public static Capability<IFamiliarCapability> FAMILIAR = CapabilityManager.get(new CapabilityToken<>() {});

}
