package com.ofek2608.emc_interface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EMCInterfaceBlock extends Block implements EntityBlock {
	public EMCInterfaceBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (!(entity instanceof Player) || level.isClientSide()) {
			return;
		}
		
		if (level.getBlockEntity(pos) instanceof EMCInterfaceBlockEntity blockEntity) {
			blockEntity.setOwner(entity.getUUID());
		} else {
			EMCInterfaceBlockEntity blockEntity = new EMCInterfaceBlockEntity(pos, state);
			level.setBlockEntity(blockEntity);
			blockEntity.setOwner(entity.getUUID());
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EMCInterfaceBlockEntity(pos, state);
	}
}
