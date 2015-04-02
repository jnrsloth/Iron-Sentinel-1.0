package com.Supernaturali.ironSentinel;

import com.Supernaturali.ironSentinel.entity.EntitySentinel;
import com.Supernaturali.ironSentinel.entity.RenderSentinel;

import net.minecraft.entity.Entity;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy{
	   
    @Override
    public void registerRenderers() {
    	 RenderingRegistry.registerEntityRenderingHandler(EntitySentinel.class, new RenderSentinel()); 
    
   
    }
    @Override
    public void initSounds() {

    }
    }
