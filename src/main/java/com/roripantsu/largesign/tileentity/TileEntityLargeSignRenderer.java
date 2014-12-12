package com.roripantsu.largesign.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.apache.commons.lang3.text.StrBuilder;
import org.lwjgl.opengl.GL11;

import com.roripantsu.common.BasePath;
import com.roripantsu.common.texture.ETextureResource;
import com.roripantsu.largesign.gui.CustomGuiTextAndFontStyleEditor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	private boolean multipleLine=false;
	public boolean showWarning=true;
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double render_xCoord,
			double render_yCoord, double render_zCoord, float FL) {
		this.renderLargeSign((TileEntityLargeSign) tileEntity, render_xCoord,
				render_yCoord, render_zCoord, FL);

	}

	void renderLargeSign(TileEntityLargeSign tileEntityLargeSign,
			double render_xCoord, double render_yCoord, double render_zCoord,
			float FL) {

		int modeNumber = tileEntityLargeSign.modeNumber;
		int side = MC.theWorld.getBlockMetadata(tileEntityLargeSign.xCoord,
				tileEntityLargeSign.yCoord, tileEntityLargeSign.zCoord);
		
		GL11.glPushMatrix();
		float rotateAngle = 0.0F;
		if (side == 2)
			rotateAngle = 180.0F;
		if (side == 4)
			rotateAngle = 90.0F;
		if (side == 5)
			rotateAngle = -90.0F;

		GL11.glTranslatef((float) render_xCoord + 0.5F,
				(float) render_yCoord + 0.5F, (float) render_zCoord + 0.5F);
		GL11.glRotatef(-rotateAngle, 0.0F, 1.0F, 0.0F);
		
		//for Sub Block or Item
		int i = MathHelper.clamp_int(tileEntityLargeSign.getTheMetadata(), 0, textureLocation.length-1);
		this.bindTexture(this.completeResourceLocation(textureLocation[i]));
		
		GL11.glPushMatrix();
		GL11.glScalef(1F, -0.875F, -0.5F);
		this.modelLargeSign.renderLargeSign((Entity) null, 0.0F, 0.0F, 0.0F,
				0.0F, 0.0F, 1 / 16F);
		GL11.glPopMatrix();

		switch (modeNumber) {
		case 0:
			this.renderString(tileEntityLargeSign);
			break;
		case 1:
			this.renderItemIcon(tileEntityLargeSign);
			break;
		}

		GL11.glPopMatrix();

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
	

	private void renderItemIcon(TileEntityLargeSign tileEntity) {
		
		World world=tileEntity.getWorldObj();
		int x=tileEntity.xCoord;
		int y=tileEntity.yCoord;
		int z=tileEntity.zCoord;
		int direction=Direction.facingToDirection[tileEntity.getSide()];
		ItemStack itemStack = tileEntity.getItemStack();
		
		if(!tileEntity.getNBTTC().hasKey("itemStack")){
			 itemStack=new ItemStack(Item.getItemById(tileEntity.itemID),1,tileEntity.itemMetadata);
		}
		
		if (itemStack != null){
            EntityItem entityitem = new EntityItem(world,0, 0, 0, itemStack);
            Item item = entityitem.getEntityItem().getItem();
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
           
            
            if (item == Items.compass)
            {
                TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
                texturemanager.bindTexture(TextureMap.locationItemsTexture);
                TextureAtlasSprite textureatlassprite1 = ((TextureMap)texturemanager.getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(Items.compass.getIconIndex(entityitem.getEntityItem()).getIconName());

                if (textureatlassprite1 instanceof TextureCompass){
                    TextureCompass texturecompass = (TextureCompass)textureatlassprite1;
                    double d0 = texturecompass.currentAngle;
                    double d1 = texturecompass.angleDelta;
                    texturecompass.currentAngle = 0.0D;
                    texturecompass.angleDelta = 0.0D;
                    texturecompass.updateCompass(world, x, z, (double)MathHelper.wrapAngleTo180_float((float)(180 + direction*90)), false, true);
                    texturecompass.currentAngle = d0;
                    texturecompass.angleDelta = d1;
                }
            }
            
            boolean flag = itemStack.getItemSpriteNumber() == 0 
            		&& itemStack.getItem() instanceof ItemBlock 
            		&& RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType());
            double scale=flag?1.85D:1.5D;
            double rotateY=flag?-90D:180D;
            double rotateZ=flag?-15D:0D;
           
    		RenderHelper.enableStandardItemLighting();
            GL11.glPushMatrix();
    		GL11.glScaled(scale, scale, scale);
    		GL11.glRotated(rotateY, 0.0D, 1.0D, 0.0D);
    		GL11.glRotated(rotateZ, 0.0D, 0.0D, 1.0D);
            RenderManager.instance.renderEntityWithPosYaw(entityitem,(flag?-0.4D/scale:0.0D), -(flag?0.15D:0.21285D), (flag?0.0D:0.43725D/scale), 0.0F, 0.0F);
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
    		
            if (item == Items.compass){
                TextureAtlasSprite textureatlassprite = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(Items.compass.getIconIndex(entityitem.getEntityItem()).getIconName());

                if (textureatlassprite.getFrameCount() > 0)
                    textureatlassprite.updateAnimation();
            }  
        }
		
		if(!tileEntity.getNBTTC().hasKey("itemStack") && showWarning ){
			 GL11.glPushMatrix();
			 GL11.glTranslated(0.0D, 0.2D, 0.5D);
			 tileEntity.largeSignText[0]="This Icon Will Disappear In After Version."
			 		+ "Please Replace And You Will Not See These Words.";
			 multipleLine=true;
			 this.renderString(tileEntity);
			 multipleLine=false;
			 GL11.glPopMatrix();
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
		GL11.glEnable(GL11.GL_BLEND);
		
		if(multipleLine){
			List<String> list=this.fontrenderer.listFormattedStringToWidth(str, 80);
			int c=0;
			for(String s:list){
			this.fontrenderer.drawString(s,
					-stringWidth / 2.0F,
					-4.5F+c, color, hasShadow);
			c+=this.fontrenderer.FONT_HEIGHT;
			}
		}else{
		this.fontrenderer.drawString(displayString,
			-stringWidth / 2.0F + adjust[1],
			-4.5F + adjust[2], color, hasShadow);
		}


		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	}
}


