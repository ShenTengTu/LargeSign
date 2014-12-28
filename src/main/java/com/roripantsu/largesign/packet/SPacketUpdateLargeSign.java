package com.roripantsu.largesign.packet;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.common.network.ModServerPostPacket;
import com.roripantsu.largesign.network.NetHandlerPlayClientSide;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *the packet which handle Updating Large Sign for sever
 *@author ShenTeng Tu(RoriPantsu)
 */
public class SPacketUpdateLargeSign extends ModServerPostPacket {

	private NBTTagCompound mainNBTTC;
	private boolean hasShadow;
	private int modeNumber;
	private int theColor;
	private String[] theString = new String[1];
	private BlockPos blockPos;
	private ItemStack itemStack;
	
	//for Class.newInstance()
	public SPacketUpdateLargeSign() {}

	public SPacketUpdateLargeSign(TileEntityLargeSign tileEntity) {
		tileEntity.writeToNBT(this.mainNBTTC=new NBTTagCompound());
		
	}

	public int getModeNumber() {
		return modeNumber;
	}

	@SideOnly(Side.CLIENT)
	public NBTTagCompound getMainNBTTC() {
		return this.mainNBTTC;
	}

	public int getTheColor() {
		return theColor;
	}

	@SideOnly(Side.CLIENT)
	public BlockPos getBlockPos() {
		return blockPos;
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

	public void setModeNumber(int modeNumber) {
		this.modeNumber = modeNumber;
	}

	public void setTheColor(int theColor) {
		this.theColor = theColor;
	}
	
	@Override
	protected void handleClientSide(NetHandlerPlayClientSide netHandler) {
		netHandler.handleUpdateLargeSign(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.mainNBTTC = buffer.readNBTTagCompoundFromBuffer();
		this.blockPos = new BlockPos(this.mainNBTTC.getInteger("x"),
				this.mainNBTTC.getInteger("y"),this.mainNBTTC.getInteger("z"));
		this.setModeNumber(this.mainNBTTC.getInteger("modeNumber"));
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
		this.handleClientSide((NetHandlerPlayClientSide)netHandler);
		
	}

}
