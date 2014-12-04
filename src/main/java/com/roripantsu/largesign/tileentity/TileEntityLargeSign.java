package com.roripantsu.largesign.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Tile entity of Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
public class TileEntityLargeSign extends TileEntity {

	public boolean hasShadow = false;
	public int itemID = 1;
	public int itemMetadata = 0;
	public String[] largeSignText = new String[] { "" };
	public int largeSignTextColor = -2039584;
	public int modeNumber = 0;
	public float scaleAdjust = 0F;
	public float XAdjust = 0F;
	public float YAdjust = 0F;
	private boolean Editable = true;
	private EntityPlayer entityPlayer;
	private NBTTagCompound NBTTC = new NBTTagCompound();
	private int theMetadata=0;//for Sub Block or Item
	private int side;
	private ItemStack itemStack;

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 6, nbttagcompound);
	}

	public EntityPlayer getEntityPlayer() {
		return this.entityPlayer;
	}

	public NBTTagCompound getNBTTC() {
		return NBTTC;
	}

	public boolean isEditable() {
		return this.Editable;
	}

	public int getSide() {
		return this.side;
	}

	public void setSide(int value) {
		this.side = value;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

		if (Minecraft.getMinecraft().theWorld.blockExists(pkt.func_148856_c(),
				pkt.func_148855_d(), pkt.func_148854_e())) {
			TileEntity tileentity = Minecraft.getMinecraft().theWorld
					.getTileEntity(pkt.func_148856_c(), pkt.func_148855_d(),
							pkt.func_148854_e());

			if (tileentity != null)
				if (tileentity instanceof TileEntityLargeSign)
					tileentity.readFromNBT(pkt.func_148857_g());
		}

	}

	@Override
	public void readFromNBT(NBTTagCompound NBTTC) {
		this.Editable = false;
		super.readFromNBT(NBTTC);
		this.theMetadata=NBTTC.getInteger("theMetadata");//for Sub Block or Item
		this.modeNumber = NBTTC.getInteger("modeNumber");
		this.itemID = NBTTC.getInteger("itemID");
		this.itemMetadata = NBTTC.getInteger("itemMetadata");
		this.largeSignTextColor = NBTTC.getInteger("largeSignTextColor");
		this.hasShadow = NBTTC.getBoolean("hasShadow");
		this.largeSignText[0] = NBTTC.getString("largeSignText");
		this.XAdjust = NBTTC.getFloat("XAdjust");
		this.YAdjust = NBTTC.getFloat("YAdjust");
		this.scaleAdjust = NBTTC.getFloat("scaleAdjust");
		this.side=NBTTC.getInteger("side");
		if(NBTTC.hasKey("itemStack"))
			this.itemStack=ItemStack.loadItemStackFromNBT(NBTTC.getCompoundTag("itemStack"));
		this.NBTTC = NBTTC;

	}

	@SideOnly(Side.CLIENT)
	public void setEditable(boolean editable) {
		this.Editable = editable;

		if (!editable) {
			this.entityPlayer = null;
		}
	}

	public void setEntityPlayer(EntityPlayer entityPlayer) {
		this.entityPlayer = entityPlayer;
	}

	public void setNBTTC(NBTTagCompound nBTTC) {
		NBTTC = nBTTC;
	}

	@Override
	public void writeToNBT(NBTTagCompound NBTTC) {
		
		NBTTC.setString("id", TileEntityLargeSign.class.getSimpleName());
		NBTTC.setInteger("x", this.xCoord);
		NBTTC.setInteger("y", this.yCoord);
		NBTTC.setInteger("z", this.zCoord);
		NBTTC.setInteger("theMetadata", this.theMetadata);//for Sub Block or Item
		NBTTC.setInteger("modeNumber", this.modeNumber);
		NBTTC.setInteger("largeSignTextColor", this.largeSignTextColor);
		NBTTC.setInteger("itemID", this.itemID);
		NBTTC.setInteger("itemMetadata", this.itemMetadata);
		NBTTC.setBoolean("hasShadow", this.hasShadow);
		NBTTC.setFloat("XAdjust", this.XAdjust);
		NBTTC.setFloat("YAdjust", this.YAdjust);
		NBTTC.setFloat("scaleAdjust", this.scaleAdjust);
		NBTTC.setString("largeSignText", this.largeSignText[0]);
		NBTTC.setInteger("side",this.side);
		if(this.itemStack!=null)
			NBTTC.setTag("itemStack", this.itemStack.writeToNBT(new NBTTagCompound()));
		this.NBTTC = NBTTC;
	}
	
	//for Sub Block or Item
	public int getTheMetadata() {
		return this.theMetadata;
	}
	
	//for Sub Block or Item
	public void setTheMetadata(int Metadata) {
		this.theMetadata = Metadata;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

}
