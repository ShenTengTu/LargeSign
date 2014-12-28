package com.roripantsu.largesign.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.manager.ModBlocks;

/**
 *Tile entity of Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
public class TileEntityLargeSign extends TileEntity {

	public boolean hasShadow = false;
	public String[] largeSignText = new String[] { "" };
	public int largeSignTextColor = -2039584;
	public int modeNumber = 0;
	public float scaleAdjust = 0F;
	public float XAdjust = 0F;
	public float YAdjust = 0F;
	public float rotate = 0F;
	private boolean Editable = true;
	private EntityPlayer entityPlayer;
	private NBTTagCompound NBTTC = new NBTTagCompound();
	private ItemStack itemStack;
	
	
	public TileEntityLargeSign() {
		this.blockType=ModBlocks.LargeSign;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
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

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
			
		TileEntity tileentity = Minecraft.getMinecraft().theWorld.getTileEntity(pkt.func_179823_a());
		if (tileentity != null)
			if (tileentity instanceof TileEntityLargeSign)
				tileentity.readFromNBT(pkt.getNbtCompound());

	}

	@Override
	public void readFromNBT(NBTTagCompound NBTTC) {
		this.Editable = false;
		super.readFromNBT(NBTTC);
		this.modeNumber = NBTTC.getInteger("modeNumber");
		this.largeSignTextColor = NBTTC.getInteger("largeSignTextColor");
		this.hasShadow = NBTTC.getBoolean("hasShadow");
		this.largeSignText[0] = NBTTC.getString("largeSignText");
		this.XAdjust = NBTTC.getFloat("XAdjust");
		this.YAdjust = NBTTC.getFloat("YAdjust");
		this.rotate = NBTTC.getFloat("rotate");
		this.scaleAdjust = NBTTC.getFloat("scaleAdjust");
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
		
		super.writeToNBT(NBTTC);
		NBTTC.setInteger("modeNumber", this.modeNumber);
		NBTTC.setInteger("largeSignTextColor", this.largeSignTextColor);
		NBTTC.setBoolean("hasShadow", this.hasShadow);
		NBTTC.setFloat("XAdjust", this.XAdjust);
		NBTTC.setFloat("YAdjust", this.YAdjust);
		NBTTC.setFloat("scaleAdjust", this.scaleAdjust);
		NBTTC.setFloat("rotate", this.rotate);
		NBTTC.setString("largeSignText", this.largeSignText[0]);
		if(this.itemStack!=null)
			NBTTC.setTag("itemStack", this.itemStack.writeToNBT(new NBTTagCompound()));
		this.NBTTC = NBTTC;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	@Override
    public boolean canRenderBreaking()
    {
        Block block = this.getBlockType();
        
        return super.canRenderBreaking() || block instanceof Block_LargeSign;
    }
	
	@Override
    public void updateContainingBlockInfo()
    {
		this.blockType=ModBlocks.LargeSign;
    }

}
