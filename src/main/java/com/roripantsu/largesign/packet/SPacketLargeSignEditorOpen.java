package com.roripantsu.largesign.packet;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.common.network.ModServerPostPacket;
import com.roripantsu.largesign.network.NetHandlerPlayClientSide;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *the packet which handle command for opening Large Sign editor
 *@author ShenTeng Tu(RoriPantsu)
 */
public class SPacketLargeSignEditorOpen extends ModServerPostPacket {

	private BlockPos blockPos;
	
	//for Class.newInstance()
	public SPacketLargeSignEditorOpen() {}

	public SPacketLargeSignEditorOpen(TileEntityLargeSign tileEntity) {
		this.blockPos = tileEntity.getPos();
	}

	@SideOnly(Side.CLIENT)
	public BlockPos getBlockPos() {
		return blockPos;
	}

	@Override
	protected void handleClientSide(NetHandlerPlayClientSide netHandler) {
		netHandler.handleLargeSignEditorOpen(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.blockPos = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());

	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeInt(this.blockPos.getX());
		buffer.writeInt(this.blockPos.getY());
		buffer.writeInt(this.blockPos.getZ());
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.handleClientSide((NetHandlerPlayClientSide)netHandler);
	}

}
