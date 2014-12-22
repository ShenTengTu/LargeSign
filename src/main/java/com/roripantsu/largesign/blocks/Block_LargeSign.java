package com.roripantsu.largesign.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import com.roripantsu.common.BasePath;
import com.roripantsu.common.texture.CustomTextureSprite;
import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.manager.ETextureResource;
import com.roripantsu.largesign.manager.ModBlocks;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *Block class of Large Sign,some methods content same as BlockSign.
 *Original block metadata represent sides of block.
 *"TheMetadata" represent id of sub blocks.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class Block_LargeSign extends Block implements ITileEntityProvider {

	private Class<?> tileEntityClass;

	
	//for Sub Block or Item>>
    public static final String[] textureNames =ETextureResource.Enttity_large_sign.textureName;
    @SideOnly(Side.CLIENT)
    private static IIcon[] subIcons;
    private int theMetadata;
    private TileEntityLargeSign thetileEntity;
    //<<for Sub Block or Item
    
	public Block_LargeSign(Class<?> tileEntityLargeSign) {
		super(Material.wood);
		this.tileEntityClass = tileEntityLargeSign;
		this.setHardness(1.0F);
		this.setStepSound(soundTypeWood);
		this.setCreativeTab(Mod_LargeSign.tab_modRoriPantsu);
		this.setBlockName(Block_LargeSign.class.getSimpleName());
		this.setBlockTextureName(textureNames[0]);
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		try {
			TileEntityLargeSign tileEntity=(TileEntityLargeSign) this.tileEntityClass.newInstance();
			tileEntity.setTheMetadata(this.theMetadata);//for Sub Block or Item
			return (TileEntity) tileEntity;
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
	
	//This fuction will affect destroy particle
	//for Sub Block or Item
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metaData) {
		int i = MathHelper.clamp_int(this.theMetadata, 0, subIcons.length-1);
		return subIcons[i];
	}
	
	/*LargeSign*/
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		TileEntityLargeSign tileentitylargesign = (TileEntityLargeSign) world
				.getTileEntity(x, y, z);
		if(tileentitylargesign != null)
			return Item.getItemFromBlock(ModBlocks.LargeSign);	
		else
			return null;
	}
	
	/*LargeSign*/
	@Override
	public Item getItemDropped(int metadata, Random random, int fortune) {
		return Item.getItemFromBlock(ModBlocks.LargeSign);
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

		int side = world.getBlockMetadata(x, y, z);
		flag = true;

		if (side == 2 && world.getBlock(x, y, z + 1).getMaterial().isSolid()) {
			flag = false;
		}

		if (side == 3 && world.getBlock(x, y, z - 1).getMaterial().isSolid()) {
			flag = false;
		}

		if (side == 4 && world.getBlock(x + 1, y, z).getMaterial().isSolid()) {
			flag = false;
		}

		if (side == 5 && world.getBlock(x - 1, y, z).getMaterial().isSolid()) {
			flag = false;
		}
		
		/*when the specified neighbor block is not solid,
		this block will be destroyed and drops its item. */
		if (flag) {
	       
			this.thetileEntity = (TileEntityLargeSign) world.getTileEntity(x, y, z);
			if(this.thetileEntity != null){
				//for Sub Block or Item
				this.dropBlockAsItem(world, x, y, z,
						this.thetileEntity.getTheMetadata(), 0);
				
				world.setBlockToAir(x, y, z);
				
			}
		}
	}
	
	//for Sub Block or Item
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		subIcons=new IIcon[textureNames.length];
		
		 for (int i = 0; i < subIcons.length;i++){
		//load the specified resource to be block icon and register it.
		CustomTextureSprite textureSprite=
				new CustomTextureSprite(2,2,16,16,BasePath.Entity,textureNames[i]);
		((TextureMap)iconRegister).setTextureEntry(textureNames[i], textureSprite);
		subIcons[i]=textureSprite;
		 }
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
	
	//for Sub Block or Item
	@Override
    public int damageDropped(int Metadata)
    {
        return Metadata;
    }

	//for Sub Block or Item
	public int getTheMetadata() {
		return this.theMetadata;
	}

	//for Sub Block or Item
	public void setTheMetadata(int theMetadata) {
		this.theMetadata = theMetadata;
	}
	
	//for Sub Block or Item
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        for(int i=0;i<textureNames.length;i++)
        	list.add(new ItemStack(item, 1, i));

    }
    
    //for Sub Block or Item
    @Override
    public void onBlockPlacedBy(World world,int x,int y,int z,EntityLivingBase entityPlayer,ItemStack itemStack){
    	this.theMetadata=itemStack.getItemDamage();
    	this.thetileEntity = (TileEntityLargeSign) world.getTileEntity(x, y, z);
    }
   
    //for Sub Block or Item
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {	
    	return this.removedByPlayer(world, player, x, y, z);
    }
    
    //for Sub Block or Item
    @Override
    public boolean removedByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z)
    {
        this.thetileEntity = (TileEntityLargeSign) world.getTileEntity(x, y, z);      
    	return world.setBlockToAir(x, y, z);
    }
    
    //for Sub Block or Item
    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, int x, int y, int z, int side)
    {
        entityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        entityPlayer.addExhaustion(0.025F);
        
        if(thetileEntity!=null){
	        if (this.canSilkHarvest(world, entityPlayer, x, y, z, side) && EnchantmentHelper.getSilkTouchModifier(entityPlayer))
	        {
	
	        	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	            ItemStack itemstack = 
	            		new ItemStack(ModBlocks.LargeSign,1,
	            				thetileEntity.getTheMetadata());//for Sub Block or Item
	
	            if (itemstack != null)
	            {
	                items.add(itemstack);
	            }
	
	            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, side, 0, 1.0f, true, entityPlayer);
	            for (ItemStack is : items)
	            {
	                this.dropBlockAsItem(world, x, y, z, is);
	            }
	        }
	        else
	        {
	            harvesters.set(entityPlayer);
	            int fortune = EnchantmentHelper.getFortuneModifier(entityPlayer);
	            this.dropBlockAsItem(world, x, y, z, 
	            		thetileEntity.getTheMetadata(), fortune);//for Sub Block or Item
	            harvesters.set(null);
	        }
        }
    }
    
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
    	//for Large Sign Rotation.
    	if(entityPlayer.isSneaking()){
    		TileEntityLargeSign tileEntity = (TileEntityLargeSign) world.getTileEntity(x, y, z);
    		if(tileEntity!=null){
    				tileEntity.rotate+=45F;
    				if(tileEntity.rotate==360F)
    					tileEntity.rotate=0;
    		}
    	}
    	
    }
}
