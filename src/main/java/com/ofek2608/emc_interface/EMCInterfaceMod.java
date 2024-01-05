package com.ofek2608.emc_interface;

import com.mojang.logging.LogUtils;
import moze_intel.projecte.gameObjs.registration.impl.ItemDeferredRegister;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.Set;

@Mod(EMCInterfaceMod.ID)
public class EMCInterfaceMod {
	public static final String ID = "emc_interface";
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ID);

	public static final RegistryObject<EMCInterfaceBlock> EMC_INTERFACE_BLOCK = BLOCKS.register("emc_interface",
			() -> new EMCInterfaceBlock(BlockBehaviour.Properties.of(Material.STONE).destroyTime(2).explosionResistance(5).requiresCorrectToolForDrops())
	);
	public static final RegistryObject<BlockItem> EMC_INTERFACE_ITEM = ITEMS.register("emc_interface",
			() -> new BlockItem(EMC_INTERFACE_BLOCK.get(), ItemDeferredRegister.getBaseProperties())
	);
	public static final RegistryObject<BlockEntityType<EMCInterfaceBlockEntity>> EMC_INTERFACE_BLOCK_ENTITY = BLOCK_ENTITIES.register("emc_interface",
			() -> new BlockEntityType<>(EMCInterfaceBlockEntity::new, Set.of(EMC_INTERFACE_BLOCK.get()), null)
	);

	public EMCInterfaceMod() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		BLOCKS.register(modEventBus);
		BLOCK_ENTITIES.register(modEventBus);
		ITEMS.register(modEventBus);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EMCInterfaceConfig.SPEC);
	}
}
