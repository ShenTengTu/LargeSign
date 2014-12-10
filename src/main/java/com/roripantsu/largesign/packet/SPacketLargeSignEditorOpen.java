package com.roripantsu.largesign.packet;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

import com.roripantsu.common.network.NetHandlerPlayClientSide;
import com.roripantsu.common.network.ServerPacket;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *the packet which handle command for opening Large Sign editor
 *@author ShenTeng Tu(RoriPantsu)
 */
public class SPacketLargeSignEditorOpen extends ServerPacket {

	private int xCoordinate;
	private int yCoordinate;
	private int zCoordinate;
	private int theMetadata;////for Sub Block or Item
	private int side;

	public SPacketLargeSignEditorOpen() {
	}

	public SPacketLargeSignEditorOpen(TileEntityLargeSign tileEntity) {
		this.xCoordinate = tileEntity.xCoord;
		this.yCoordinate = tileEntity.yCoord;
		this.zCoordinate = tileEntity.zCoord;
		this.theMetadata=tileEntity.getTheMetadata();//for Sub Block or Item
		this.side=tileEntity.getSide();
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
	
	@SideOnly(Side.CLIENT)
	public int getSide() {
		return side;
	}

	@Override
	protected void handleClientSide(NetHandlerPlayClientSide netHandler) {
		netHandler.handleLargeSignEditorOpen(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.xCoordinate = buffer.readInt();
		this.yCoordinate = buffer.readInt();
		this.zCoordinate = buffer.readInt();
		this.theMetadata=buffer.readInt();//for Sub Block or Item
		this.side=buffer.readInt();
		
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeInt(this.xCoordinate);
		buffer.writeInt(this.yCoordinate);
		buffer.writeInt(this.zCoordinate);
		buffer.writeInt(this.theMetadata);//for Sub Block or Item
		buffer.writeInt(this.side);
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.handleClientSide((NetHandlerPlayClientSide)netHandler);
	}

}
