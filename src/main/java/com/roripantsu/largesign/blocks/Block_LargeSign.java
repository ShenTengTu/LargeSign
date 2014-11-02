package com.roripantsu.largesign.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.roripantsu.largesign.References;
import com.roripantsu.largesign.items.Item_LargeSign;
import com.roripantsu.largesign.texture.CustomTextureSprite;
import com.roripantsu.largesign.texture.ETextureResource;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Block class of Large Sign,some methods content same as BlockSign
 *@author ShenTeng Tu(RoriPantsu)
 */
public class Block_LargeSign extends BlockContainer {

	private Class<?> tileEntity;
 
	public Block_LargeSign(Class<?> tileEntityLargeSign) {
		super(Material.wood);
		this.tileEntity = tileEntityLargeSign;
		this.setHardness(1.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockTextureName(ETextureResource.Enttity_large_sign.textureName);
		this.setBlockName(Block_LargeSign.class.getSimpleName());
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		try {
			return (TileEntity) this.tileEntity.newInstance();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess blockAccess, int x, int y,
			int z) {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x,
			int y, int z) {
		return null;
	}
	
	/*This fuction will affect destroy particle*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metaData) {
		
		return this.blockIcon;
	}
	
	/*LargeSign*/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return GameRegistry.findItem(References.MODID,
				Item_LargeSign.class.getSimpleName());
	}
	
	/*LargeSign*/
	@Override
	public Item getItemDropped(int par1, Random random, int par2) {
		return GameRegistry.findItem(References.MODID,
				Item_LargeSign.class.getSimpleName());
	}
	
	
	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x,
			int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	/*LargeSign*/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block theBlock) {

		boolean flag = false;

		int l = world.getBlockMetadata(x, y, z);
		flag = true;

		if (l == 2 && world.getBlock(x, y, z + 1).getMaterial().isSolid()) {
			flag = false;
		}

		if (l == 3 && world.getBlock(x, y, z - 1).getMaterial().isSolid()) {
			flag = false;
		}

		if (l == 4 && world.getBlock(x + 1, y, z).getMaterial().isSolid()) {
			flag = false;
		}

		if (l == 5 && world.getBlock(x - 1, y, z).getMaterial().isSolid()) {
			flag = false;
		}
		
		/*when the specified neighbor block is not solid,
		this block will be destroyed and drops its item. */
		if (flag) {
			this.dropBlockAsItem(world, x, y, z,
					world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}

		super.onNeighborBlockChange(world, x, y, z, theBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		//load the specified resource to be block icon and register it.
		CustomTextureSprite textureSprite=
				new CustomTextureSprite(2,2,
						ETextureResource.BasePath.Entity,
						this.getTextureName());
		((TextureMap)iconRegister).setTextureEntry(this.getTextureName(), textureSprite);
		this.blockIcon=textureSprite;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	/*LargeSign*/
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,
			int y, int z) {

		int l = blockAccess.getBlockMetadata(x, y, z);
		float thickness = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		if (l == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
		}

		if (l == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
		}

		if (l == 4) {
			this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if (l == 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
		}
	}
}
