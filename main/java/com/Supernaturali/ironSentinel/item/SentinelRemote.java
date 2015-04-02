package com.Supernaturali.ironSentinel.item;

import com.Supernaturali.ironSentinel.IronSentinel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class SentinelRemote extends Item{
	
	public SentinelRemote(){
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabMisc);
		setTextureName(IronSentinel.MODID + ":" + "sentinel_remote");
		setUnlocalizedName("Remote");
	}
}
