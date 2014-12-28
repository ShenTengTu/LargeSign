package com.roripantsu.largesign.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.roripantsu.common.network.ModPacketPipeline;
import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.manager.ModBlocks;
import com.roripantsu.largesign.manager.ModItemMeshDefinition;
import com.roripantsu.largesign.manager.ModItems;
import com.roripantsu.largesign.manager.ModRecipes;
import com.roripantsu.largesign.network.ModPacketEventHandler;
import com.roripantsu.largesign.network.NetHandlerPlayClientSide;
import com.roripantsu.largesign.network.NetHandlerPlayServerSide;
import com.roripantsu.largesign.packet.CPacketUpdateLargeSign;
import com.roripantsu.largesign.packet.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.packet.SPacketUpdateLargeSign;

/**
 *Differentiate between the combined client and the dedicated server.
 *Superclass of ProxyClient and ProxyServer.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class ProxyCommon {
	
	public void init(FMLInitializationEvent e) {
		/*Item Model */
		ModItemMeshDefinition.register();
		/*Packet Pipeline*/
		Mod_LargeSign.packetPipeline = 
				new ModPacketPipeline(Mod_LargeSign.MODID, new ModPacketEventHandler(),
						NetHandlerPlayClientSide.class,NetHandlerPlayServerSide.class);
		Mod_LargeSign.packetPipeline.registerPacket(CPacketUpdateLargeSign.class);
		Mod_LargeSign.packetPipeline.registerPacket(SPacketLargeSignEditorOpen.class);
		Mod_LargeSign.packetPipeline.registerPacket(SPacketUpdateLargeSign.class);
	}

    public void postInit(FMLPostInitializationEvent e) {
		/*Packet Pipeline*/
    	Mod_LargeSign.packetPipeline.postInitialise();
    }

    public void preInit(FMLPreInitializationEvent e) {
		/* Blocks */
    	ModBlocks.init();
    	ModBlocks.register();
    	
    	/*Items*/
    	ModItems.init();
    	ModItems.register();

		/* Recipes */
    	ModRecipes.register();
    	    	
    }
    
}
