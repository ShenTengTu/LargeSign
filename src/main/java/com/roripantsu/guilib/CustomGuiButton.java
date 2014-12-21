package com.roripantsu.guilib;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *Custom Button for rendering color and icon
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiButton extends GuiButton {
	public static enum ButtonFrontStyle {
		/**Display colored block in button**/
		COLORBLOCK,
		/**Display minecraft item icon in button**/
		ITEMICON,
		/**Display custom icon in button**/
		ICON,
		/**Display just text in button**/
		TEXT,
		/**Display text with minecraft item icon in button**/
		ITEMICON_TEXT,
		/**Display text with custom icon in button**/
		ICON_TEXT;
	}
	
	public static enum ButtonBackStyle {
		/**Display default button background**/
		Default,
		/**Display default colored quad button background**/
		QUAD,
		/**Display custom texture button background**/
		CUSTOM_TEXTURE;
	}
	private Color color;
	private ItemStack itemStack;
	private IIcon icon;
	private RenderItem renderItem = new RenderItem();
	private ButtonFrontStyle frontStyle;
	/*private ButtonBackStyle backStyle;*/
	
	/**
	 *For color block button.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,Color color) {
		super(ID, X, Y, W, H, "");
			this.color = color;
			this.frontStyle=ButtonFrontStyle.COLORBLOCK;
	}
	
	/**
	 *For common button.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,String text) {
		super(ID, X, Y, W, H, text);	
		this.frontStyle=ButtonFrontStyle.TEXT;
	}
	
	/**
	 * For item icon button.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,ItemStack itemStack) {
		super(ID, X, Y, W, H, "");
			this.itemStack = itemStack;
			this.frontStyle=ButtonFrontStyle.ITEMICON;
	}
	
	/**
	 * For text button with item icon.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,ItemStack itemStack,String text) {
		super(ID, X, Y, W, H, text);
			this.itemStack =itemStack;
			this.frontStyle=ButtonFrontStyle.ITEMICON_TEXT;
	}
	
	/**
	 * For custom icon button 
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,IIcon icon) {
		super(ID, X, Y, W, H, "");
			this.icon = icon;
			this.frontStyle=ButtonFrontStyle.ICON;
	}
	
	/**
	 * For text button with custom icon. 
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,IIcon icon,String text) {
		super(ID, X, Y, W, H, text);
		this.icon = icon;
		this.frontStyle=ButtonFrontStyle.ICON_TEXT;
	}


	@Override
	public void drawButton(Minecraft MC, int X, int Y) {
		if (this.visible) {
						
			if (frontStyle == ButtonFrontStyle.COLORBLOCK)
				this.drawColorBlockButton(MC, X, Y);
			
			if (frontStyle == ButtonFrontStyle.TEXT)
				super.drawButton(MC, X, Y);
			
			if (frontStyle == ButtonFrontStyle.ITEMICON)
				this.drawItemIconButton(MC, X, Y);

			if (frontStyle == ButtonFrontStyle.ITEMICON_TEXT){

			}
			
			if (frontStyle == ButtonFrontStyle.ICON){
				
			}
			
			if (frontStyle == ButtonFrontStyle.ICON_TEXT){

			}

		}
	}

	private void drawColorBlockButton(Minecraft MC, int X, int Y) {
		//field_146123_n == isHover
		this.field_146123_n = X >= this.xPosition && Y >= this.yPosition
				&& X < this.xPosition + this.width
				&& Y < this.yPosition + this.height;
		
		int k = this.getHoverState(this.field_146123_n);
		Color quadColor=new Color(225,225,225, 32 * k);
		this.renderQuad(this.xPosition, this.yPosition, this.xPosition
				+ this.width, this.yPosition + this.height, quadColor);
		
		this.renderQuad(this.xPosition + 1, this.yPosition + 1, this.xPosition
				+ this.width - 1, this.yPosition + this.height - 1, this.color);
	}

	private void drawItemIconButton(Minecraft MC, int X, int Y) {
		
		
		//field_146123_n == isHover
		this.field_146123_n = X >= this.xPosition && Y >= this.yPosition
				&& X < this.xPosition + this.width
				&& Y < this.yPosition + this.height;
		
		int k = this.getHoverState(this.field_146123_n);
		Color quadColor=new Color(225,225,225, 32 * k);
		this.renderQuad(this.xPosition, this.yPosition, this.xPosition
				+ this.width, this.yPosition + this.height, quadColor);

		this.renderItemIcon(MC.fontRenderer, MC.getTextureManager(),
				this.itemStack, this.xPosition + (this.width - 16) / 2,
				this.yPosition + (this.height - 16) / 2);
					
	}

	private void renderItemIcon(FontRenderer fontRenderer,
			TextureManager textureManager, final ItemStack itemStack, int X,
			int Y) {

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.renderItem.renderItemAndEffectIntoGUI(fontRenderer,
				textureManager, itemStack, X, Y);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

	}
	
    private void renderQuad(int X, int Y, int W, int H, Color color) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(color.getRed() / 225F, color.getGreen() / 225F,
				color.getBlue() / 225F, color.getAlpha() / 225F);
		tessellator.startDrawingQuads();
		tessellator.addVertex(X, H, 0.0D);
		tessellator.addVertex(W, H, 0.0D);
		tessellator.addVertex(W, Y, 0.0D);
		tessellator.addVertex(X, Y, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
    
    /*private void renderDefaultTexture(Minecraft MC, int X, int Y){
        MC.getTextureManager().bindTexture(buttonTextures);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146123_n = X >= this.xPosition && Y >= this.yPosition && X < this.xPosition + this.width && Y < this.yPosition + this.height;
        int k = this.getHoverState(this.field_146123_n);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
    }*/

	public Color getColor() {
		return color;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public IIcon getIcon() {
		return icon;
	}

}
