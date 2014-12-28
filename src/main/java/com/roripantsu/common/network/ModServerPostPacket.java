package com.roripantsu.common.network;

import net.minecraftforge.fml.relauncher.Side;

import com.roripantsu.largesign.network.NetHandlerPlayClientSide;

/**
 *Define a packet how to handle self when client receive it.
 *@author ShenTeng Tu(RoriPantsu)
 */
public abstract class ModServerPostPacket implements IModPacket{
	
	protected abstract void handleClientSide(NetHandlerPlayClientSide netHandler);
	
	@Override
	public Side getTarget() {
		return Side.CLIENT;
	}

}
