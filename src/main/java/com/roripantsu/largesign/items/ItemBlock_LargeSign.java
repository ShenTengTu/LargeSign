package com.roripantsu.largesign.items;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.network.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.texture.CustomTextureSprite;
import com.roripantsu.largesign.texture.ETextureResource;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlock_LargeSign extends ItemBlockWithMetadata {
	private final Block theBlock;
	
	//for Sub Block or Item>>
	public static final String[] textureNames =ETextureResource.Item_large_sign.textureName;
    @SideOnly(Side.CLIENT)
    private IIcon[] subIcons;
    //<<for Sub Block or Item
    
	public ItemBlock_LargeSign(Block block) {
		super(block,block);
		this.theBlock=block;
		this.maxStackSize = 8;
		this.setUnlocalizedName(ItemBlock_LargeSign.class.getSimpleName());
		this.setTextureName(textureNames[0]);
	}
	
	//for Sub Block or Item
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String[] suffix=ETextureResource.Enttity_large_sign.fileNameSuffix;
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, suffix.length-1);
		return this.getUnlocalizedName()+"_"+
				ETextureResource.Enttity_large_sign.fileNameSuffix[i];
	}
	
	//for Sub Block or Item
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		subIcons=new IIcon[textureNames.length];
		
		 for (int i = 0; i < subIcons.length;i++){
		//load the specified resource to be block icon and register it.
		CustomTextureSprite textureSprite=
				new CustomTextureSprite(0,0,
						ETextureResource.BasePath.Items,
						textureNames[i]);
		((TextureMap)iconRegister).setTextureEntry(textureNames[i], textureSprite);
		subIcons[i]=textureSprite;
		 }
	}
	
	//for Sub Block or Item
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		int i = MathHelper.clamp_int(meta, 0, textureNames.length-1);
	    return this.subIcons[i];
	}
	
	//for Sub Block or Item
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < textureNames.length; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer,
			World world, int x, int y, int z, int side, float hitX,
			float hitY, float hitZ) {
		
		Block prevBlock = world.getBlock(x,y,z);
		
		if (side == 0)//bottom
			return false;
		else if (!prevBlock.getMaterial().isSolid())

			return false;
		else {
			if (side == 1)//top
				return false;

			if (side == 2)//north
				--z;

			if (side == 3)//south
				++z;

			if (side == 4)//west
				--x;

			if (side == 5)//east
				++x;

			if (!entityPlayer.canPlayerEdit(x, y, z, side, itemStack))
				return false;
			else if (!this.theBlock.canPlaceBlockAt(world, x,
					y, z))
				return false;
			else if (world.isRemote)
				return true;
			else {
				this.theBlock.onBlockPlacedBy(world, x, y, z, entityPlayer, itemStack);//for Sub Block or Item
				world.setBlock(x, y, z, this.theBlock, side, 3);
	            --itemStack.stackSize;

				TileEntityLargeSign tileentitylargesign = (TileEntityLargeSign) world
						.getTileEntity(x, y, z);
				
				if (tileentitylargesign != null){
					this.func_LargeSign(entityPlayer, tileentitylargesign);
				}

				return true;
			}
		}
	}

	private void func_LargeSign(EntityPlayer entityPlayer,
			TileEntityLargeSign tileEntity) {
		if (entityPlayer instanceof EntityPlayerMP) {
			tileEntity.setEntityPlayer(entityPlayer);
			
			SPacketLargeSignEditorOpen thePacket = new SPacketLargeSignEditorOpen(
					tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
					tileEntity.getTheMetadata());//for Sub Block or Item

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
