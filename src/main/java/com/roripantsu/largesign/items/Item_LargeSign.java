package com.roripantsu.largesign.items;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.network.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.texture.ETextureResource;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

/**
 *Item class of Large Sign,reference ItemSign
 *@author ShenTeng Tu(RoriPantsu)
 */
public class Item_LargeSign extends Item {

	public Item_LargeSign() {
		this.maxStackSize = 8;
		this.setCreativeTab(Mod_LargeSign.tab_modRoriPantsu);
		this.setUnlocalizedName(Item_LargeSign.class.getSimpleName());
		this.setTextureName(ETextureResource.Item_large_sign.textureName);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer,
			World world, int x, int y, int z, int metaData, float par8,
			float par9, float par10) {
		if (metaData == 0)
			return false;
		else if (!world.getBlock(x, y, z).getMaterial().isSolid())

			return false;
		else {
			if (metaData == 1)
				return false;

			if (metaData == 2)
				--z;

			if (metaData == 3)
				++z;

			if (metaData == 4)
				--x;

			if (metaData == 5)
				++x;

			if (!entityPlayer.canPlayerEdit(x, y, z, metaData, itemStack))
				return false;
			else if (!Mod_LargeSign.proxy.block_LargeSign.canPlaceBlockAt(world, x,
					y, z))
				return false;
			else if (world.isRemote)
				return true;
			else {
				world.setBlock(x, y, z, Mod_LargeSign.proxy.block_LargeSign,
						metaData, 3);

				--itemStack.stackSize;
				TileEntityLargeSign tileentitylargesign = (TileEntityLargeSign) world
						.getTileEntity(x, y, z);

				if (tileentitylargesign != null)
					this.func_LargeSign(entityPlayer, tileentitylargesign);

				return true;
			}
		}
	}

	private void func_LargeSign(EntityPlayer entityPlayer,
			TileEntityLargeSign tileEntity) {
		if (entityPlayer instanceof EntityPlayerMP) {
			tileEntity.setEntityPlayer(entityPlayer);
			SPacketLargeSignEditorOpen thePacket = new SPacketLargeSignEditorOpen(
					tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

			try {
				List<Object> list = new LinkedList<Object>();
				Mod_LargeSign.proxy.packetPipeline.encode(thePacket,
						Mod_LargeSign.proxy.channelKey, list);
				FMLProxyPacket pkt = (FMLProxyPacket) list.get(0);
				Mod_LargeSign.proxy.packetPipeline.sendTo(pkt,
						(EntityPlayerMP) entityPlayer);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
