package com.roripantsu.common.network;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

/**
 * Mod packet event handler interface.
 * Subscribe {@link ClientCustomPacketEvent} and {@link ServerCustomPacketEvent} .
 * @author ShenTeng Tu(RoriPantsu)
 *
 */
public interface IModPacketEventHandler {
	
	@SubscribeEvent
	public void handleClientReceivedPacket(ClientCustomPacketEvent e) throws Exception;

	@SubscribeEvent
	public void handleServerReceivedPacket(ServerCustomPacketEvent e) throws Exception; 
}
