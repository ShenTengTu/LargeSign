package com.roripantsu.common.network;

import net.minecraft.network.Packet;

/**
 *define packet's method to handle communication for Client and Server
 *@author ShenTeng Tu(RoriPantsu)
 */
public abstract class ServerPacket extends Packet{
	
	protected abstract void handleClientSide(NetHandlerPlayClientSide netHandler);

}
