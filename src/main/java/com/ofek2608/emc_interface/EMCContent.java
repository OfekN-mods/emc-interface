package com.ofek2608.emc_interface;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.math.BigInteger;
import java.util.*;

public class EMCContent {
	private static final Map<UUID, EMCContent> MEMORIZED = new HashMap<>();
	
	public static EMCContent get(UUID owner) {
		return MEMORIZED.computeIfAbsent(owner, EMCContent::new);
	}
	
	private final ServerPlayer player;
	private final IEMCProxy emcProxy;
	private final IKnowledgeProvider knowledgeProvider;
	public final int count;
	public final ItemInfo[] infos;
	public final ItemStack[] stacks;
	
	private EMCContent(UUID owner) {
		this.player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(owner);
		this.emcProxy = ProjectEAPI.getEMCProxy();
		this.knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);
		BigInteger emcCount = knowledgeProvider.getEmc();
		
		int count = 0;
		List<ItemInfo> infos = new ArrayList<>();
		List<ItemStack> stacks = new ArrayList<>();
		
		for (ItemInfo info : knowledgeProvider.getKnowledge()) {
			long value = emcProxy.getValue(info);
			int maxExtract = getMaxExtract(emcCount, value);
			
			if (maxExtract <= 0) {
				continue;
			}
			ItemStack stack = info.createStack();
			stack.setCount(maxExtract);
			
			count++;
			infos.add(info);
			stacks.add(stack);
		}
		
		this.count = count;
		this.infos = infos.toArray(ItemInfo[]::new);
		this.stacks = stacks.toArray(ItemStack[]::new);
	}
	
	public boolean insert(ItemStack stack, boolean simulate) {
		if (player == null) {
			return false;
		}
		if (stack.isEmpty()) {
			return false;
		}
		long sellValue = emcProxy.getSellValue(stack);
		if (sellValue <= 0) {
			return false;
		}
		if (simulate) {
			return true;
		}
		BigInteger baseEMC = knowledgeProvider.getEmc();
		BigInteger addEMC = BigInteger.valueOf(sellValue).multiply(BigInteger.valueOf(stack.getCount()));
		BigInteger newEMC = baseEMC.add(addEMC);
		knowledgeProvider.setEmc(newEMC);
		sync(knowledgeProvider.addKnowledge(stack));
		return true;
	}
	
	public int extract(ItemInfo info, int max, boolean simulate) {
		if (player == null) {
			return 0;
		}
		long value = emcProxy.getValue(info);
		BigInteger baseEMC = knowledgeProvider.getEmc();
		int maxExtract = Math.min(getMaxExtract(baseEMC, value), max);
		if (maxExtract <= 0) {
			return 0;
		}
		if (simulate) {
			return maxExtract;
		}
		BigInteger removeEMC = BigInteger.valueOf(value).multiply(BigInteger.valueOf(maxExtract));
		BigInteger newEMC = baseEMC.subtract(removeEMC);
		knowledgeProvider.setEmc(newEMC);
		sync(false);
		return maxExtract;
	}
	
	private void sync(boolean syncKnowledge) {
		if (player == null) {
			return;
		}
		if (syncKnowledge) {
			knowledgeProvider.sync(player);
		} else {
			knowledgeProvider.syncEmc(player);
		}
	}
	
	
	
	private static int getMaxExtract(BigInteger emcCount, long cost) {
		if (emcCount.signum() <= 0 || cost <= 0) {
			return 0;
		}
		try {
			return emcCount.divide(BigInteger.valueOf(cost)).intValueExact();
		} catch (ArithmeticException e) {
			return Integer.MAX_VALUE;
		}
	}
	
	
	@Mod.EventBusSubscriber(modid = EMCInterfaceMod.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	private static class ForgeEvents {
		@SubscribeEvent
		public static void event(TickEvent.ServerTickEvent event) {
			if (event.phase != TickEvent.Phase.START) {
				return;
			}
			MEMORIZED.clear();
		}
	}
}
