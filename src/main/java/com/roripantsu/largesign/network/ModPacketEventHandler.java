package com.roripantsu.largesign.network;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

import com.roripantsu.common.network.IModPacketEventHandler;
import com.roripantsu.largesign.Mod_LargeSign;

/**
 * Implements {@link IModPacketEventHandler}.
 * Subscribe {@link ClientCustomPacketEvent} and 
 * {@link ServerCustomPacketEvent} and handle them.
 * @author ShenTeng Tu(RoriPantsu)
 *
 */
public class ModPacketEventHandler implements IModPacketEventHandler{
	
	@Override
	@SubscribeEvent
	public void handleClientReceivedPacket(ClientCustomPacketEvent e)
			throws Exception {
		Mod_LargeSign.packetPipeline.decode(e.packet);
	}

	@Override
	@SubscribeEvent
	public void handleServerReceivedPacket(ServerCustomPacketEvent e)
			throws Exception {
		Mod_LargeSign.packetPipeline.decode(e.packet);
	}

}
