package com.roripantsu.largesign.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.text.StrBuilder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.roripantsu.largesign.gui.CustomGuiTextAndFontStyleEditor;
import com.roripantsu.largesign.texture.ETextureResource;

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
	private CustomItemRender renderItem;

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
		String str = tileEntityLargeSign.largeSignText[0];
		float[] adjust = { tileEntityLargeSign.scaleAdjust,
				tileEntityLargeSign.XAdjust, tileEntityLargeSign.YAdjust };
		int color = tileEntityLargeSign.largeSignTextColor;
		int itemID = tileEntityLargeSign.itemID;
		int itemMetadata = tileEntityLargeSign.itemMetadata;
		boolean hasShadow = tileEntityLargeSign.hasShadow;
		int j = MC.theWorld.getBlockMetadata(tileEntityLargeSign.xCoord,
				tileEntityLargeSign.yCoord, tileEntityLargeSign.zCoord);
		float rotateAngle = 0.0F;

		if (str == null)
			str = "";

		GL11.glPushMatrix();
		if (j == 2)
			rotateAngle = 180.0F;
		if (j == 4)
			rotateAngle = 90.0F;
		if (j == 5)
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
			this.renderString(str, color, adjust, hasShadow);
			break;
		case 1:
			this.renderItemIcon(new ItemStack(Item.getItemById(itemID), 1,
					itemMetadata));
			break;
		}

		GL11.glPopMatrix();

	}

	private ResourceLocation completeResourceLocation(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {ETextureResource.BasePath.Entity , location.getResourcePath(), ".png"}));
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
	

	private void renderItemIcon(final ItemStack itemStack) {

		this.renderItem = new CustomItemRender();
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableStandardItemLighting();
		this.renderItem.zLevel=0F;
		this.renderItem.renderItemAndEffectIntoGUI(this.MC.fontRenderer,
				this.MC.getTextureManager(), itemStack, 0, 0);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

	}
	
    private void renderString(String str, int color, float[] adjust,
			boolean hasShadow) {

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
		

		this.fontrenderer.drawString(displayString,
			-stringWidth / 2.0F + adjust[1],
			-4.5F + adjust[2], color, hasShadow);


		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	}
}


