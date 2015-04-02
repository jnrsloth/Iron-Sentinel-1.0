package com.Supernaturali.ironSentinel;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import com.Supernaturali.ironSentinel.blocks.RedPumpkin;
import com.Supernaturali.ironSentinel.entity.EntitySentinel;
import com.Supernaturali.ironSentinel.gui.GuiHandler;
import com.Supernaturali.ironSentinel.item.SentinelRemote;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = IronSentinel.MODID, version = IronSentinel.VERSION)
public class IronSentinel
{
    public static final String MODID = "ironsentinel";
    public static final String VERSION = "0.1";

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="com.Supernaturali.ironSentinel.ClientProxy", serverSide="com.Supernaturali.ironSentinel.CommonProxy")
    //public static ClientProxy proxy;
    public static CommonProxy proxy;
    
    @Instance(MODID)
    public static IronSentinel instance;
    
    public static Item sentinelRemote;
    public static Block redPumpkin;
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	sentinelRemote = new SentinelRemote();
        GameRegistry.registerItem(sentinelRemote, "sentinelRemote");
    	redPumpkin = new RedPumpkin();
    	GameRegistry.registerBlock(redPumpkin, "redPumpkin");
    	
    	EntityRegistry.registerGlobalEntityID(EntitySentinel.class, "Iron Sentinel", EntityRegistry.findGlobalUniqueEntityId());
    	EntityRegistry.registerModEntity(EntitySentinel.class, "Iron Sentinel", EntityRegistry.findGlobalUniqueEntityId(), instance, 64, 1, false);
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	
    	proxy.registerRenderers();
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
		// some example code
//        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
