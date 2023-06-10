package com.ofek2608.emc_interface;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EMCInterfaceBlockEntity extends BlockEntity {
	protected EMCInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	public EMCInterfaceBlockEntity(BlockPos pos, BlockState state) {
		super(EMCInterfaceMod.EMC_INTERFACE_BLOCK_ENTITY.get(), pos, state);
	}
	
	private UUID owner = Util.NIL_UUID;
	
	public UUID getOwner() {
		return owner;
	}
	
	public void setOwner(UUID owner) {
		this.owner = owner;
		setChanged();
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putUUID("owner", owner);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		owner = tag.getUUID("owner");
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, direction);
	}
	
	
	
	public final LazyOptional<EMCInterfaceItemHandler> itemHandler =
			LazyOptional.of(() -> new EMCInterfaceItemHandler(this));
}
