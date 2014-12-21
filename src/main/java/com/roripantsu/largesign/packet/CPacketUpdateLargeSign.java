package com.roripantsu.largesign.packet;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

import com.roripantsu.common.network.ClientPacket;
import com.roripantsu.common.network.NetHandlerPlayServerSide;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *the packet which handle Updating Large Sign for client
 *@author ShenTeng Tu(RoriPantsu)
 */
public class CPacketUpdateLargeSign extends ClientPacket {

	private NBTTagCompound mainNBTTC;
	private boolean hasShadow;
	private int itemID;
	private int itemMetadata;
	private int modeNumber;
	private int theColor;
	private String[] theString = new String[1];
	private int xCoordinate;
	private int yCoordinate;
	private int zCoordinate;
	private int side;
	private ItemStack itemStack;

	//for Server side
	public CPacketUpdateLargeSign() {
	}
	
	//for Client side
	@SideOnly(Side.CLIENT)
	public CPacketUpdateLargeSign(TileEntityLargeSign tileEntity) {
		tileEntity.writeToNBT(this.mainNBTTC=new NBTTagCompound());
		
	}

	public String[] geTheString() {
		return this.theString;
	}

	public int getItemID() {
		return itemID;
	}

	public int getItemMetadata() {
		return itemMetadata;
	}

	public int getModeNumber() {
		return modeNumber;
	}

	public NBTTagCompound getMainNBTTC() {
		return mainNBTTC;
	}

	public int getTheColor() {
		return theColor;
	}

	public int getXCoordinate() {
		return this.xCoordinate;
	}

	public int getYCoordinate() {
		return this.yCoordinate;
	}

	public int getZCoordinate() {
		return this.zCoordinate;
	}

	public int getSide() {
		return side;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public boolean isHasShadow() {
		return hasShadow;
	}

	public void setHasShadow(boolean hasShadow) {
		this.hasShadow = hasShadow;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setItemMetadata(int itemMetadata) {
		this.itemMetadata = itemMetadata;
	}

	public void setModeNumber(int modeNumber) {
		this.modeNumber = modeNumber;
	}

	public void setTheColor(int theColor) {
		this.theColor = theColor;
	}
	
	@Override
	protected void handleServerSide(NetHandlerPlayServerSide netHandler) {
		netHandler.handleUpdateLargeSign(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.mainNBTTC = buffer.readNBTTagCompoundFromBuffer();
		this.xCoordinate = this.mainNBTTC.getInteger("x");
		this.yCoordinate = this.mainNBTTC.getInteger("y");
		this.zCoordinate = this.mainNBTTC.getInteger("z");
		this.side=this.mainNBTTC.getInteger("side");
		this.setModeNumber(this.mainNBTTC.getInteger("modeNumber"));
		this.setItemID(mainNBTTC.getInteger("itemID"));
		this.setItemMetadata(mainNBTTC.getInteger("itemMetadata"));
		this.setTheColor(this.mainNBTTC.getInteger("largeSignTextColor"));
		this.setHasShadow(this.mainNBTTC.getBoolean("hasShadow"));
		this.theString[0] = this.mainNBTTC.getString("largeSignText");
		if(this.mainNBTTC.hasKey("itemStack"))
			this.itemStack=ItemStack.loadItemStackFromNBT(this.mainNBTTC.getCompoundTag("itemStack"));
		
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeNBTTagCompoundToBuffer(this.mainNBTTC);
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.handleServerSide((NetHandlerPlayServerSide)netHandler);
	}

}
