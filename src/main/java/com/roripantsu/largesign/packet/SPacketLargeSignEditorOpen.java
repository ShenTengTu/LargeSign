package com.roripantsu.largesign.packet;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.common.network.NetHandlerPlayClientSide;
import com.roripantsu.common.network.ServerPacket;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *the packet which handle command for opening Large Sign editor
 *@author ShenTeng Tu(RoriPantsu)
 */
public class SPacketLargeSignEditorOpen extends ServerPacket {

	private int xCoordinate;
	private int yCoordinate;
	private int zCoordinate;
	private int theMetadata;////for Sub Block or Item
	private EnumFacing side;

	public SPacketLargeSignEditorOpen() {
	}

	public SPacketLargeSignEditorOpen(TileEntityLargeSign tileEntity) {
		this.xCoordinate = tileEntity.getPos().getX();
		this.yCoordinate = tileEntity.getPos().getY();
		this.zCoordinate = tileEntity.getPos().getZ();
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
	public EnumFacing getSide() {
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
		this.side=EnumFacing.getHorizontal(buffer.readInt());
		
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeInt(this.xCoordinate);
		buffer.writeInt(this.yCoordinate);
		buffer.writeInt(this.zCoordinate);
		buffer.writeInt(this.theMetadata);//for Sub Block or Item
		buffer.writeInt(this.side.getHorizontalIndex());
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.handleClientSide((NetHandlerPlayClientSide)netHandler);
	}

}
