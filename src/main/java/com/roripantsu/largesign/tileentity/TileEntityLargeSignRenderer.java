package com.roripantsu.largesign.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.text.StrBuilder;
import org.lwjgl.opengl.GL11;

import com.roripantsu.common.BasePath;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.gui.CustomGuiTextAndFontStyleEditor;
import com.roripantsu.largesign.manager.ETextureResource;

/**
 *Tile entity special renderer of Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class TileEntityLargeSignRenderer extends TileEntitySpecialRenderer {

	public static TileEntityLargeSignRenderer instance = new TileEntityLargeSignRenderer();
	//for Sub Block or Item>>
	private static final ResourceLocation[] textureLocation;
			
	static{
		int n=ETextureResource.Enttity_large_sign.fileNameSuffix.length;
		textureLocation=new ResourceLocation[n];
		for(int i=0;i<n;i++){
			textureLocation[i]=new ResourceLocation(ETextureResource.Enttity_large_sign.textureName[i]);
		}
	}
	//<<for Sub Block or Item
	private CustomFontRenderer fontrenderer;
	private Minecraft MC = Minecraft.getMinecraft();
	private final Model_LargeSign modelLargeSign = new Model_LargeSign();
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double render_xCoord,
			double render_yCoord, double render_zCoord, float FL,int destoryStage) {
		this.renderLargeSign((TileEntityLargeSign) tileEntity, render_xCoord,
				render_yCoord, render_zCoord, FL, destoryStage);

	}

	void renderLargeSign(TileEntityLargeSign tileEntityLargeSign,
			double render_xCoord, double render_yCoord, double render_zCoord,
			float FL,int destoryStage) {
		
		IBlockState blockState = ((Block_LargeSign)tileEntityLargeSign.getBlockType()).getDefaultState();
		EnumFacing side = (EnumFacing) blockState.getValue(Block_LargeSign.PROP_FACING);
		Block_LargeSign.ESubType subType = (Block_LargeSign.ESubType) blockState.getValue(Block_LargeSign.PROP_SUB_TYPE);
		int modeNumber = tileEntityLargeSign.modeNumber;
		float modelRotate = tileEntityLargeSign.rotate;
		
		GlStateManager.pushMatrix();
		float rotateAngle = 0.0F;
		if (side == EnumFacing.NORTH)
			rotateAngle = 180.0F;
		if (side == EnumFacing.WEST)
			rotateAngle = 90.0F;
		if (side == EnumFacing.EAST)
			rotateAngle = -90.0F;
		
		GlStateManager.translate((float) render_xCoord + 0.5F,
				(float) render_yCoord + 0.5F, (float) render_zCoord + 0.5F);
		GlStateManager.rotate(-rotateAngle, 0.0F, 1.0F, 0.0F);
		
		
        if (destoryStage >= 0){
        	this.bindTexture(DESTROY_STAGES[destoryStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }else{
		//for Sub Block or Item
        	int i = MathHelper.clamp_int(subType.getSubID(), 0, textureLocation.length-1);
        	this.bindTexture(this.completeResourceLocation(textureLocation[i]));
        }
		
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
		float f1=1F;
		float f2=(float) (1/Math.sqrt(2));
		float f3=0.875F;
		float[] xScale8Dir={f1,f2,f3,f2,f1,f2,f3,f2};
		float[] yScale8Dir={-f3,-f2,-f1,-f2,-f3,-f2,-f1,-f2};
		GlStateManager.scale( xScale8Dir[(int) (modelRotate/45F)], yScale8Dir[(int) (modelRotate/45F)], -0.5F);
		this.modelLargeSign.setRotation(this.modelLargeSign.LargeSign, 0F, 0F, modelRotate/180F*(float)Math.PI);
		this.modelLargeSign.renderLargeSign((Entity) null, 0.0F, 0.0F, 0.0F,
				0.0F, 0.0F, 1 / 16F);
		GlStateManager.popMatrix();

		switch (modeNumber) {
		case 0:
			this.renderString(tileEntityLargeSign);
			break;
		case 1:
			this.renderItemIcon(tileEntityLargeSign,side);
			break;
		}
		GlStateManager.popMatrix();
		
        if (destoryStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

	}

	private ResourceLocation completeResourceLocation(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {BasePath.Entity , location.getResourcePath(), ".png"}));
    }

	private String formatStringClear(String str) {

		String[] displycodes = {
				CustomGuiTextAndFontStyleEditor.FontStyles.BOLD.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.ITALIC.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.RESET.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.STRIKETHROUGH.styleCode,
				CustomGuiTextAndFontStyleEditor.FontStyles.UNDERLINE.styleCode };

		StrBuilder strb = new StrBuilder(str);
		if (!strb.isEmpty())
			for (String s : displycodes)
				strb.deleteAll(s);

		return strb.toString();
	}
	

	private void renderItemIcon(TileEntityLargeSign tileEntity, EnumFacing side) {
		
		World world=tileEntity.getWorld();
		int x=tileEntity.getPos().getX();
		int y=tileEntity.getPos().getY();
		int z=tileEntity.getPos().getZ();
		int direction=side.getHorizontalIndex();
		ItemStack itemStack = tileEntity.getItemStack();
			
		if (itemStack != null){
            EntityItem entityitem = new EntityItem(world,0, 0, 0, itemStack);
            Item item = entityitem.getEntityItem().getItem();
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
           
            TextureAtlasSprite textureatlassprite = null;
            
            if (item == Items.compass)
            {
                textureatlassprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                
                if (textureatlassprite instanceof TextureCompass){
                    TextureCompass texturecompass = (TextureCompass)textureatlassprite;
                    double d0 = texturecompass.currentAngle;
                    double d1 = texturecompass.angleDelta;
                    texturecompass.currentAngle = 0.0D;
                    texturecompass.angleDelta = 0.0D;
                    texturecompass.updateCompass(world, x, z, (double)MathHelper.wrapAngleTo180_float((float)(180 + direction*90)), false, true);
                    texturecompass.currentAngle = d0;
                    texturecompass.angleDelta = d1;
                }else{
                    textureatlassprite = null;
                }
            }
            
            boolean flag =itemStack.getItem() instanceof ItemBlock;
            double scale=flag?1.85D:1.5D;
            double rotateY=flag?-90D:180D;
            double rotateZ=flag?-15D:0D;
           
    		RenderHelper.enableStandardItemLighting();
            GL11.glPushMatrix();
    		GL11.glScaled(scale, scale, scale);
    		GL11.glRotated(rotateY, 0.0D, 1.0D, 0.0D);
    		GL11.glRotated(rotateZ, 0.0D, 0.0D, 1.0D);
    		Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entityitem,(flag?-0.4D/scale:0.0D), -(flag?0.15D:0.21285D), (flag?0.0D:0.43725D/scale), 0.0F, 0.0F);
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
    		
            if (textureatlassprite != null && textureatlassprite.getFrameCount() > 0){
                textureatlassprite.updateAnimation();
            } 
        }

	}
	
    private void renderString(TileEntityLargeSign tileEntity) {
    	
		String str = tileEntity.largeSignText[0];
		float[] adjust = { tileEntity.scaleAdjust,
				tileEntity.XAdjust, tileEntity.YAdjust };
		int color = tileEntity.largeSignTextColor;
		boolean hasShadow = tileEntity.hasShadow;
    	
		if (str == null)
			str = "";
		
		this.fontrenderer = new CustomFontRenderer(this.MC.gameSettings,
				new ResourceLocation("textures/font/ascii.png"),
				this.MC.renderEngine, true);
		this.fontrenderer.setUnicodeFlag(true);
		this.fontrenderer.setBidiFlag(true);
		String displayString=this.fontrenderer.trimStringToWidth(str, 80);
		int stringWidth = this.fontrenderer.getStringWidth(displayString);
		
		float scaleParam = stringWidth <= 72 ? 
				(stringWidth <= 63 ? 
						(stringWidth <= 54 ? 
								(stringWidth <= 45 ? 
										(stringWidth <= 36 ? 
												(stringWidth <= 27 ? 
														(stringWidth <= 18 ? 
																(stringWidth <= 9 ? 90F: 50F)
														: 35F)
												: 26F)
										: 21F)
								: 17F)
						: 15F)
				: 12.5F): 12.5F;

		if (str.getBytes().length == 1)
			scaleParam = 110F;
		if (this.formatStringClear(str).getBytes().length == 1)
			scaleParam = 100F;
		
		GL11.glTranslatef(0.0F, 0F, -0.435F);
		GL11.glScalef((scaleParam + adjust[0]) / 1000F,
				-(scaleParam + adjust[0]) / 1000F,
				(scaleParam + adjust[0]) / 1000F);
		GL11.glNormal3f(0.0F, 0.0F, -1.75F * (scaleParam + adjust[0]) / 1000F);// luminance
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);//?
		
		this.fontrenderer.drawString(displayString,
			-stringWidth / 2.0F + adjust[1],
			-4.5F + adjust[2], color, hasShadow);

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
	}
}


