package com.roripantsu.largesign.network;

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
	protected int channelKey;
	protected List<Object> packetList;

	public CustomPacketEventListener(int channelKey, List<Object> packetList) {
		this.channelKey = channelKey;
		this.packetList = packetList;
	}

	@SubscribeEvent
	public void listener_ClientCustomPacketEvent(ClientCustomPacketEvent e)
			throws Exception {
		Mod_LargeSign.proxy.packetPipeline.decode(e.packet, this.channelKey,
				this.packetList);
	}

	@SubscribeEvent
	public void listener_ServerCustomPacketEvent(ServerCustomPacketEvent e)
			throws Exception {
		Mod_LargeSign.proxy.packetPipeline.decode(e.packet, this.channelKey,
				this.packetList);
	}

}
