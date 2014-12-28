package com.roripantsu.common.network;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Mod packet interface.
 * @author ShenTeng Tu(RoriPantsu)
 *
 */
public interface IModPacket extends Packet {
	
	public Side getTarget();
}
