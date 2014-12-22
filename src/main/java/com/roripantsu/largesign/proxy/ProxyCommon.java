package com.roripantsu.largesign.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.roripantsu.common.network.PacketPipeline;
import com.roripantsu.largesign.manager.ModBlocks;
import com.roripantsu.largesign.manager.ModRecipes;

/**
 *Differentiate between the combined client and the dedicated server.
 *Superclass of ProxyClient and ProxyServer.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyCommon {
	
	public void init(FMLInitializationEvent e) {
		PacketPipeline.init();
	}

    public void postInit(FMLPostInitializationEvent e) {
    	PacketPipeline.instance().postInitialise();
    }

    public void preInit(FMLPreInitializationEvent e) {
		/* Blocks */
    	ModBlocks.init();
    	ModBlocks.register();

		/* Recipes */
    	ModRecipes.register();
		
    }
    
}
