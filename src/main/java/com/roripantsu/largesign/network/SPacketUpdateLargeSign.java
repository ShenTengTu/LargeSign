package com.roripantsu.largesign.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *the packet which handle Updating Large Sign for sever
 *@author ShenTeng Tu(RoriPantsu)
 */
public class SPacketUpdateLargeSign extends AbsPacket {

	private boolean hasShadow;
	private int itemID;
	private int itemMetadata;
	private int modeNumber;
	private NBTTagCompound NBTTC;
	private int theColor;
	private String[] theString = new String[1];
	private int xCoordinate;
	private int yCoordinate;
	private int zCoordinate;

	public SPacketUpdateLargeSign() {
	}

	public SPacketUpdateLargeSign(NBTTagCompound NBTTC) {
		this.NBTTC = NBTTC;
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

	@SideOnly(Side.CLIENT)
	public NBTTagCompound getNBTTC() {
		return this.NBTTC;
	}

	public int getTheColor() {
		return theColor;
	}

	@SideOnly(Side.CLIENT)
	public int getxCoordinate() {
		return xCoordinate;
	}

	@SideOnly(Side.CLIENT)
	public int getyCoordinate() {
		return yCoordinate;
	}

	@SideOnly(Side.CLIENT)
	public int getzCoordinate() {
		return zCoordinate;
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
	protected void decodePacket(PacketBuffer buffer) throws IOException {
		this.NBTTC = buffer.readNBTTagCompoundFromBuffer();
		this.xCoordinate = this.NBTTC.getInteger("x");
		this.yCoordinate = this.NBTTC.getInteger("y");
		this.zCoordinate = this.NBTTC.getInteger("z");
		this.setModeNumber(this.NBTTC.getInteger("modeNumber"));
		this.setItemID(NBTTC.getInteger("itemID"));
		this.setItemMetadata(NBTTC.getInteger("itemMetadata"));
		this.setTheColor(this.NBTTC.getInteger("largeSignTextColor"));
		this.setHasShadow(this.NBTTC.getBoolean("hasShadow"));
		this.theString[0] = this.NBTTC.getString("largeSignText");

	}

	@Override
	protected void encodePacket(PacketBuffer buffer) throws IOException {
		buffer.writeNBTTagCompoundToBuffer(this.NBTTC);
	}

	@Override
	protected void handleClientSide(NetHandlerPlayClientSide netHandler,
			EntityPlayer player) {
		netHandler.handleUpdateLargeSign(this);
	}

	@Override
	protected void handleServerSide(NetHandlerPlayServerSide netHandler,
			EntityPlayer player) {

	}

}
