package com.roripantsu.largesign.items;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.common.network.PacketPipeline;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.manager.NameManager;
import com.roripantsu.largesign.packet.SPacketLargeSignEditorOpen;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

public class ItemBlock_LargeSign extends ItemBlock {
	
	private final Block theBlock;
	
	public ItemBlock_LargeSign(Block block) {
		super(block);
		this.theBlock=block;
		this.maxStackSize = 8;
		this.setUnlocalizedName(NameManager.Unlocalized.ItemLargeSign);
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
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer,
			World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		
		IBlockState blockState = world.getBlockState(pos);
		
		if (side == EnumFacing.DOWN || side ==EnumFacing.UP)
			return false;
		else if (!blockState.getBlock().getMaterial().isSolid())

			return false;
		else {
			
			pos = pos.offset(side);

			if (!entityPlayer.func_175151_a(pos, side, itemStack))
				return false;
			else if (!this.theBlock.canPlaceBlockAt(world, pos))
				return false;
			else if (world.isRemote)
				return true;
			else {
				
				blockState = this.theBlock.getDefaultState();
				world.setBlockState(pos, blockState.withProperty(Block_LargeSign.PROP_FACING, side));
				world.setBlockState(pos, blockState.withProperty(Block_LargeSign.PROP_SUB_TYPE, Block_LargeSign.ESubType.getType(itemStack.getItemDamage())));
				
	            --itemStack.stackSize;

				TileEntityLargeSign tileentitylargesign = (TileEntityLargeSign) world
						.getTileEntity(pos);

				if (tileentitylargesign != null){			        
					tileentitylargesign.setSide(side);
					this.openLargeSignEditor(entityPlayer,tileentitylargesign);
				}

				return true;
			}
		}
	}

	private void openLargeSignEditor(EntityPlayer entityPlayer,
			TileEntityLargeSign tileEntity) {
		if (entityPlayer instanceof EntityPlayerMP) {
			tileEntity.setEntityPlayer(entityPlayer);
			
			SPacketLargeSignEditorOpen thePacket = 
					new SPacketLargeSignEditorOpen(tileEntity);

			try {
				List<Object> list = new LinkedList<Object>();
				PacketPipeline.instance().encode(thePacket, list);
				FMLProxyPacket pkt = (FMLProxyPacket) list.get(0);
				PacketPipeline.instance().sendTo(pkt,
						(EntityPlayerMP) entityPlayer);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
