package com.roripantsu.largesign.packet;

import java.util.List;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

import com.roripantsu.common.network.PacketPipeline;

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
		PacketPipeline.instance().decode(e.packet,this.packetList);
	}

	@SubscribeEvent
	public void listener_ServerCustomPacketEvent(ServerCustomPacketEvent e)
			throws Exception {
		PacketPipeline.instance().decode(e.packet,this.packetList);
	}

}
