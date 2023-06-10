package com.ofek2608.emc_interface;

import moze_intel.projecte.api.ItemInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class EMCInterfaceItemHandler implements IItemHandler {
	private final EMCInterfaceBlockEntity blockEntity;
	
	public EMCInterfaceItemHandler(EMCInterfaceBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}
	
	private EMCContent getContent() {
		return EMCContent.get(blockEntity.getOwner());
	}
	
	@Override
	public int getSlots() {
		EMCContent content = getContent();
		return content.count + 1;
	}
	
	@Override
	public @NotNull ItemStack getStackInSlot(int slot) {
		slot--;
		if (slot < 0) {
			return ItemStack.EMPTY;
		}
		EMCContent content = getContent();
		return slot < content.count ? content.stacks[slot].copy() : ItemStack.EMPTY;
	}
	
	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		if (slot != 0) {
			return stack;
		}
		EMCContent content = getContent();
		return content.insert(stack, simulate) ? ItemStack.EMPTY : stack;
	}
	
	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		slot--;
		if (slot < 0) {
			return ItemStack.EMPTY;
		}
		EMCContent content = getContent();
		if (slot >= content.count) {
			return ItemStack.EMPTY;
		}
		ItemInfo info = content.infos[slot];
		int extracted = content.extract(info, amount, simulate);
		if (extracted <= 0) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = info.createStack();
		stack.setCount(extracted);
		return stack;
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return slot == 0;
	}
}
