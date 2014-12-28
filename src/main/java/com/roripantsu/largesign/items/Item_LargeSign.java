package com.roripantsu.largesign.items;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.manager.ModBlocks;
import com.roripantsu.largesign.manager.ModCreativeTabs;
import com.roripantsu.largesign.manager.ModItemMeshDefinition;
import com.roripantsu.largesign.manager.NameManager;
import com.roripantsu.largesign.packet.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

public class Item_LargeSign extends Item {
	
	public Item_LargeSign() {
		this.setMaxStackSize(8);
		this.setCreativeTab(ModCreativeTabs.tab_LargeSign);
		//for Sub Block or Item
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	//for Sub Block or Item
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String[] suffix=NameManager.Unlocalized.SubLargeSign;
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, suffix.length-1);
		return this.getUnlocalizedName()+"_"+suffix[i];
	}
		
	//for Sub Block or Item
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < NameManager.Unlocalized.SubLargeSign.length; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}
	
	//for Sub Block or Item
	@Override
	 @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
		return new ModItemMeshDefinition().getModelLocation(stack);
    }
    
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer,
			World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		
		if (side == EnumFacing.DOWN || side ==EnumFacing.UP)
			return false;
		else if (!world.getBlockState(pos).getBlock().getMaterial().isSolid())

			return false;
		else {
			
			pos = pos.offset(side);

			if (!entityPlayer.func_175151_a(pos, side, itemStack))
				return false;
			else if (!ModBlocks.LargeSign.canPlaceBlockAt(world, pos))
				return false;
			else if (world.isRemote)
				return true;
			else {
				
				IBlockState blockState = ModBlocks.LargeSign.getDefaultState();
				world.setBlockState(pos, blockState.withProperty(Block_LargeSign.PROP_FACING, side));
				world.setBlockState(pos, blockState.withProperty(Block_LargeSign.PROP_SUB_TYPE, Block_LargeSign.ESubType.getType(itemStack.getItemDamage())));
				
	            --itemStack.stackSize;

				TileEntity tileentity = world.getTileEntity(pos);
				
				if (tileentity instanceof TileEntityLargeSign && !ItemBlock.setTileEntityNBT(world, pos, itemStack)){			        
					
					((TileEntityLargeSign)tileentity).setEntityPlayer(entityPlayer);
					
					SPacketLargeSignEditorOpen thePacket = new SPacketLargeSignEditorOpen((TileEntityLargeSign)tileentity);
						
						try {
							List<FMLProxyPacket> list = new LinkedList<FMLProxyPacket>();
							Mod_LargeSign.packetPipeline.encode(thePacket, list);
							FMLProxyPacket pkt = (FMLProxyPacket) list.get(0);
							Mod_LargeSign.packetPipeline.sendToPlayer(pkt,(EntityPlayerMP)entityPlayer);
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					
				}
					
				return true;
			}
		}
	}


}
