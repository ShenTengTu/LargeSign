package com.roripantsu.largesign.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *the packet which handle command for opening Large Sign editor
 *@author ShenTeng Tu(RoriPantsu)
 */
public class SPacketLargeSignEditorOpen extends AbsPacket {

	private int xCoordinate;
	private int yCoordinate;
	private int zCoordinate;
	private int theMetadata;////for Sub Block or Item

	public SPacketLargeSignEditorOpen() {
	}

	public SPacketLargeSignEditorOpen(int xCoord, int yCoord, int zCoord,int theMetadata) {
		this.xCoordinate = xCoord;
		this.yCoordinate = yCoord;
		this.zCoordinate = zCoord;
		this.theMetadata=theMetadata;//for Sub Block or Item
	}

	@SideOnly(Side.CLIENT)
	public int getXCoordinate() {
		return this.xCoordinate;
	}

	@SideOnly(Side.CLIENT)
	public int getYCoordinate() {
		return this.yCoordinate;
	}

	@SideOnly(Side.CLIENT)
	public int getZCoordinate() {
		return this.zCoordinate;
	}
	
	//for Sub Block or Item
	@SideOnly(Side.CLIENT)
	public int getTheMetadata() {
		return theMetadata;
	}

	@Override
	protected void decodePacket(PacketBuffer buffer) throws IOException {
		this.xCoordinate = buffer.readInt();
		this.yCoordinate = buffer.readInt();
		this.zCoordinate = buffer.readInt();
		this.theMetadata=buffer.readInt();//for Sub Block or Item
	}

	@Override
	protected void encodePacket(PacketBuffer buffer) throws IOException {
		buffer.writeInt(this.xCoordinate);
		buffer.writeInt(this.yCoordinate);
		buffer.writeInt(this.zCoordinate);
		buffer.writeInt(this.theMetadata);//for Sub Block or Item

	}

	@Override
	protected void handleClientSide(NetHandlerPlayClientSide netHandler,
			EntityPlayer player) {
		netHandler.handleLargeSignEditorOpen(this);

	}

	@Override
	protected void handleServerSide(NetHandlerPlayServerSide netHandler,
			EntityPlayer player) {

	}

}
