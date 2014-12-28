package com.roripantsu.largesign.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.roripantsu.largesign.manager.ETextureResource;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

/**
 *Block class of Large Sign,some methods content same as BlockSign.
 *@author ShenTeng Tu(RoriPantsu)
 */
public class Block_LargeSign extends BlockContainer {

	private Class<?> tileEntityClass;

	
	//for Sub Block or Item>>
	public static final PropertyDirection PROP_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum PROP_SUB_TYPE = PropertyEnum.create("sub_type", Block_LargeSign.ESubType.class);
    public static final String[] textureNames =ETextureResource.Enttity_large_sign.textureName;
    //<<for Sub Block or Item
    
	public Block_LargeSign(Class<?> tileEntityLargeSign) {
		super(Material.wood);
		this.tileEntityClass = tileEntityLargeSign;
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROP_FACING, EnumFacing.NORTH));
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROP_SUB_TYPE, Block_LargeSign.ESubType.OAK));
		this.disableStats();
	}
	
    /** create BlockState instance using IProperty array*/
    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {PROP_FACING,PROP_SUB_TYPE});
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            enumfacing = EnumFacing.NORTH;

        return this.getDefaultState().withProperty(PROP_FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(PROP_FACING)).getIndex();
    }
    
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		try {
			TileEntityLargeSign tileEntity=(TileEntityLargeSign) this.tileEntityClass.newInstance();
			tileEntity.setWorldObj(world);
			return (TileEntity) tileEntity;
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	/*LargeSign*/
	//For a user uses the creative pick block button on this block.
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos) {
			return Item.getItemFromBlock(world.getBlockState(pos).getBlock());
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		return Item.getItemFromBlock(state.getBlock());
	}
	
	//for Sub Block or Item
	@Override
    public int damageDropped(IBlockState state) {
		return ((Block_LargeSign.ESubType)state.getValue(PROP_SUB_TYPE)).getSubID();
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
		this.setBlockBoundsBasedOnState(world, pos);
		return super.getSelectedBoundingBox(world, pos);
	}
	
	/*LargeSign*/
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, BlockPos pos) {

		EnumFacing enumfacing = (EnumFacing)blockAccess.getBlockState(pos).getValue(PROP_FACING);
		float thickness = 0.08F;
		float width = 1F;
		float height = 0.885F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		if (enumfacing == EnumFacing.NORTH)
			this.setBlockBounds(0.0F, 0.5F-height*0.5F, 1.0F - thickness, 1.0F, 0.5F+height*0.5F, width);
		if (enumfacing == EnumFacing.SOUTH)
			this.setBlockBounds(0.0F, 0.5F-height*0.5F, 0.0F, width, 0.5F+height*0.5F, thickness);
		if (enumfacing == EnumFacing.WEST)
			this.setBlockBounds(1.0F - thickness, 0.5F-height*0.5F, 0.0F, 1.0F, 0.5F+height*0.5F, width);
		if (enumfacing == EnumFacing.EAST)
			this.setBlockBounds(0.0F, 0.5F-height*0.5F, 0.0F, thickness,0.5F+height*0.5F, width);
	}
	
	@Override
    public boolean isFullCube()
    {
        return false;
    }
    
    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos)
    {
        return true;
    }

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
			
	//for Sub Block or Item
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        Block_LargeSign.ESubType[] subType = Block_LargeSign.ESubType.values();
    	
    	for(int i=0;i<subType.length;i++)
        	list.add(new ItemStack(item, 1, subType[i].getSubID()));

    }
    
	/*LargeSign*/
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state,
			Block theBlock) {
		
		EnumFacing enumfacing = (EnumFacing)state.getValue(PROP_FACING);
		
		/*when the specified neighbor block is not solid,
		this block will be destroyed and drops its item. */
        if (!world.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid())
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
        
        super.onNeighborBlockChange(world, pos, state, theBlock);
					
	}
          
    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer entityPlayer) {
    	//for Large Sign Rotation.
    	if(entityPlayer.isSneaking()){
    		TileEntityLargeSign tileEntity = (TileEntityLargeSign) world.getTileEntity(pos);
    		if(tileEntity!=null){
    				tileEntity.rotate+=45F;
    				if(tileEntity.rotate==360F)
    					tileEntity.rotate=0;
    		}
    	}
    	
    }
    
    public static enum ESubType implements IStringSerializable{
		
        OAK(0, "oak"),
        SPRUCE(1, "spruce"),
        BIRCH(2, "birch"),
        JUNGLE(3, "jungle"),
        ACACIA(4, "acacia"),
        DARK_OAK(5, "dark_oak");
        
        private final int subID;
        private final String name;
        
        private ESubType(int subID ,String name) {
        	this.subID=subID;
        	this.name=name;
        }
        
        public int getSubID()
        {
            return subID;
        }
        
        public String toString()
        {
            return name;
        }
        
        public static Block_LargeSign.ESubType getType(int subID)
        {
        	int i = MathHelper.clamp_int(subID, 0, values().length);

            return values()[i];
        }
        
        public static String[] getNames(){
        	List<String> list = new ArrayList<String>();
        	for(ESubType e: values())
        		list.add(e.name);
        	return list.toArray(new String[list.size()]);
        }
        
		@Override
		public String getName() {
			return name;
		}
    	
    }
}
