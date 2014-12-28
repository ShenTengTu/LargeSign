package com.roripantsu.common.network;

import net.minecraftforge.fml.relauncher.Side;

import com.roripantsu.largesign.network.NetHandlerPlayServerSide;

/**
 *Define a packet how to handle self when server receive it. 
 *@author ShenTeng Tu(RoriPantsu)
 */
public abstract class ModClientPostPacket implements IModPacket{
	
	protected abstract void handleServerSide(NetHandlerPlayServerSide netHandler);

	@Override
	public Side getTarget() {
		return Side.SERVER;
	}

}
