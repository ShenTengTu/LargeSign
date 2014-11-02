package com.roripantsu.largesign.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

/**
 *define packet's method to handle communication for Client and Server
 *@author ShenTeng Tu(RoriPantsu)
 */
public abstract class AbsPacket {
	
	protected abstract void decodePacket(PacketBuffer buffer) throws IOException;

	protected abstract void encodePacket(PacketBuffer buffer) throws IOException;

	protected abstract void handleClientSide(
			NetHandlerPlayClientSide netHandler, EntityPlayer player);

	protected abstract void handleServerSide(
			NetHandlerPlayServerSide netHandler, EntityPlayer player);

}
