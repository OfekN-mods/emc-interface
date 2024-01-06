package com.ofek2608.emc_interface;

import net.minecraftforge.common.ForgeConfigSpec;

public final class EMCInterfaceConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec.ConfigValue<Integer> MAX_ITEMS = BUILDER.comment("Defines the maximum how many items every slot will have").defineInRange("maxItems", 1000000000, 64, Integer.MAX_VALUE);
	public static final ForgeConfigSpec SPEC = BUILDER.build();
}