package com.roripantsu.largesign.packet;

import java.util.List;

import com.roripantsu.largesign.Mod_LargeSign;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

/**
 *Detect Packet then decode them
 *@author ShenTeng Tu(RoriPantsu)
 */
public class CustomPacketEventListener {
	protected List<Object> packetList;

	public CustomPacketEventListener(List<Object> packetList) {
		this.packetList = packetList;
	}

	@SubscribeEvent
	public void listener_ClientCustomPacketEvent(ClientCustomPacketEvent e)
			throws Exception {
		Mod_LargeSign.proxy.packetPipeline.decode(e.packet,this.packetList);
	}

	@SubscribeEvent
	public void listener_ServerCustomPacketEvent(ServerCustomPacketEvent e)
			throws Exception {
		Mod_LargeSign.proxy.packetPipeline.decode(e.packet,this.packetList);
	}

}
