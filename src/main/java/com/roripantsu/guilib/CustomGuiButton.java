package com.roripantsu.guilib;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
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
		/**Display just text in button**/
		TEXT,
		/**Display text with minecraft item icon in button**/
		ITEMICON_TEXT,
	}
	
	private Minecraft mc = Minecraft.getMinecraft();
	private Color color;
	private ItemStack itemStack;
	private ButtonFrontStyle frontStyle;
	private RenderItem renderItem = mc.getRenderItem();
	private int ltX;
	private int ltY;
	private int rbX;
	private int rbY;
	
	/**
	 *For color block button.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,Color color) {
		super(ID, X, Y, W, H, "");
		this.ltX = X;
		this.ltY = Y;
		this.rbX = X+W-1;
		this.rbY = Y+H-1;
		this.color = color;
		this.frontStyle=ButtonFrontStyle.COLORBLOCK;
	}
	
	/**
	 *For common button.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,String text) {
		super(ID, X, Y, W, H, text);
		this.ltX = X;
		this.ltY = Y;
		this.rbX = X+W-1;
		this.rbY = Y+H-1;
		this.frontStyle=ButtonFrontStyle.TEXT;
	}
	
	/**
	 * For item icon button.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,ItemStack itemStack) {
		super(ID, X, Y, W, H, "");
		this.ltX = X;
		this.ltY = Y;
		this.rbX = X+W-1;
		this.rbY = Y+H-1;
		this.itemStack = itemStack;
		this.frontStyle=ButtonFrontStyle.ITEMICON;
	}
	
	/**
	 * For text button with item icon.
	 */
	public CustomGuiButton(int ID, int X, int Y, int W, int H,ItemStack itemStack,String text) {
		super(ID, X, Y, W, H, text);
		this.ltX = X;
		this.ltY = Y;
		this.rbX = X+W-1;
		this.rbY = Y+H-1;
		this.itemStack =itemStack;
		this.frontStyle=ButtonFrontStyle.ITEMICON_TEXT;
	}
	
	@Override
	public void drawButton(Minecraft MC, int mouseX, int mouseY) {
		if (this.visible) {
						
			if (frontStyle == ButtonFrontStyle.COLORBLOCK)
				this.drawColorBlockButton(MC, mouseX, mouseY);
			
			if (frontStyle == ButtonFrontStyle.TEXT)
				super.drawButton(MC, mouseX, mouseY);
			
			if (frontStyle == ButtonFrontStyle.ITEMICON)
				this.drawItemIconButton(MC, mouseX, mouseY);

			if (frontStyle == ButtonFrontStyle.ITEMICON_TEXT){

			}
			
		}
	}

	private void drawColorBlockButton(Minecraft MC, int mouseX, int mouseY) {
		this.hovered = this.getHorverd(mouseX,mouseY);
		int k = this.getHoverState(this.hovered);
		Color quadColor=new Color(225,225,225, 32 * k);
		Gui.drawRect(ltX, ltY, rbX, rbY, quadColor.getRGB());
		Gui.drawRect(ltX+1, ltY+1, rbX-1, rbY-1,  this.color.getRGB());
	}

	private void drawItemIconButton(Minecraft MC, int mouseX, int mouseY) {
		
		this.hovered = this.getHorverd(mouseX,mouseY);
		int k = this.getHoverState(this.hovered);
		Color quadColor=new Color(225,225,225, 32 * k);
		
		GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableBlend();
		Gui.drawRect(ltX, ltY, rbX, rbY, quadColor.getRGB());
		this.renderItemIcon(MC.fontRendererObj, MC.getTextureManager(),
				this.itemStack, this.xPosition + (this.width - 16) / 2,
				this.yPosition + (this.height - 16) / 2);
					
	}

	private void renderItemIcon(FontRenderer fontRenderer,
			TextureManager textureManager, final ItemStack itemStack, int X,
			int Y) {

		  	this.zLevel = 100.0F;
	        this.renderItem.zLevel = 100.0F;
	        GlStateManager.enableLighting();
	        GlStateManager.enableRescaleNormal();
	        this.renderItem.func_180450_b(itemStack, X, Y);
	        this.renderItem.func_175030_a(fontRenderer,itemStack, X, Y);
	        GlStateManager.disableLighting();
	        this.renderItem.zLevel = 0.0F;
	        this.zLevel = 0.0F;
	}
	 
    private boolean getHorverd(int mouseX, int mouseY){
    	return mouseX >= this.ltX && mouseY >= this.ltY
    			&& mouseX <= this.rbX && mouseY <= this.rbY;
    }
    
	public Color getColor() {
		return color;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
