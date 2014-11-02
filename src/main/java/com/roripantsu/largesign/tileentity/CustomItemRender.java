package com.roripantsu.largesign.tileentity;

import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Extends RenderItem and override some metheod.
 *Most code same as RenderItem but modify some code for large Sign to render item icon.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomItemRender extends RenderItem {
	
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private RenderBlocks renderBlocksRi = new RenderBlocks();
	
	@Override
    public void renderEffect(TextureManager manager, int x, int y)
    {
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        manager.bindTexture(RES_ITEM_GLINT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
        this.renderGlint(x * 431278612 + y * 32178161, x, y, 1, 1);//Modify for Large Sign
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }
	
	@Override
    public void renderIcon(int theX, int theY, IIcon iIcon, int scaleX, int scaleY)
    {
        GL11.glPushMatrix();//Modify for Large Sign
        GL11.glScalef(-11/16F, 11/16F, 1.0F);//Modify for Large Sign
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);//Modify for Large Sign
        double shiftX=-0.5;//Modify for Large Sign
    double shiftY=-0.5;//Modify for Large Sign
        double shiftZ=-0.425;//Modify for Large Sign
        
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(theX + shiftX + 0), (double)(theY + shiftY + scaleY), (double)(this.zLevel + shiftZ), (double)iIcon.getMinU(), (double)iIcon.getMaxV());//Modify for Large Sign
        tessellator.addVertexWithUV((double)(theX + shiftX + scaleX), (double)(theY + shiftY + scaleY), (double)(this.zLevel + shiftZ), (double)iIcon.getMaxU(), (double)iIcon.getMaxV());//Modify for Large Sign
        tessellator.addVertexWithUV((double)(theX + shiftX + scaleX), (double)(theY + shiftY + 0), (double)(this.zLevel + shiftZ), (double)iIcon.getMaxU(), (double)iIcon.getMinV());//Modify for Large Sign
        tessellator.addVertexWithUV((double)(theX + shiftX + 0), (double)(theY + shiftY + 0), (double)(this.zLevel + shiftZ), (double)iIcon.getMinU(), (double)iIcon.getMinV());//Modify for Large Sign
        tessellator.draw();
        
        GL11.glPopMatrix();//Modify for Large Sign
    }
	
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public void renderItemAndEffectIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, final ItemStack itemStack, int theX, int theY){
	       if (itemStack != null)
	        {
	            //this.zLevel += 50.0F; //Modify for Large Sign

	            try
	            {
	                if (!ForgeHooksClient.renderInventoryItem(this.field_147909_c, textureManager, itemStack, renderWithColor, zLevel, (float)theX, (float)theY))
	                {
	                    this.renderItemIntoGUI(fontRenderer, textureManager, itemStack, theX, theY, true);
	                }
	            }
	            catch (Throwable throwable)
	            {
	                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
	                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
	                crashreportcategory.addCrashSectionCallable("Item Type", new Callable<Object>()
	                {
	                    private static final String __OBFID = "CL_00001004";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.getItem());
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Item Aux", new Callable<Object>()
	                {
	                    private static final String __OBFID = "CL_00001005";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.getItemDamage());
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Item NBT", new Callable<Object>()
	                {
	                    private static final String __OBFID = "CL_00001006";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.getTagCompound());
	                    }
	                });
	                crashreportcategory.addCrashSectionCallable("Item Foil", new Callable<Object>()
	                {
	                    private static final String __OBFID = "CL_00001007";
	                    public String call()
	                    {
	                        return String.valueOf(itemStack.hasEffect());
	                    }
	                });
	                throw new ReportedException(crashreport);
	            }

	            // Forge: Bugfix, Move this to a per-render pass, modders must handle themselves
	            if (false && itemStack.hasEffect())
	            {
	            	this.renderEffect(textureManager, theX, theY);//Modify for Large Sign
	            }

	            //this.zLevel -= 50.0F; //Modify for Large Sign
	        }
	}
	
	@Override
	public void renderItemIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, ItemStack itemStack, int theX, int theY, boolean renderEffect){
	       int k = itemStack.getItemDamage();
	        Object object = itemStack.getIconIndex();
	        int l;
	        float f;
	        float f3;
	        float f4;

	        if (itemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType()))
	        {
	            textureManager.bindTexture(TextureMap.locationBlocksTexture);
	            Block block = Block.getBlockFromItem(itemStack.getItem());
	            GL11.glEnable(GL11.GL_ALPHA_TEST);

	            if (block.getRenderBlockPass() != 0)
	            {
	                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	                GL11.glEnable(GL11.GL_BLEND);
	                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	            }
	            else
	            {
	                GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
	                GL11.glDisable(GL11.GL_BLEND);
	            }

	            GL11.glPushMatrix();
	            GL11.glTranslatef((float)(theX), (float)(theY), this.zLevel-0.41F);//Modify for Large Sign
	            GL11.glScalef(6.5F/16F, 6.5F/16F, 6.5F/16F);//Modify for Large Sign
	            //GL11.glTranslatef(1.0F, 0.5F, 1.0F); //Modify for Large Sign
	            //GL11.glScalef(1.0F, 1.0F, -1.0F); //Modify for Large Sign
	            GL11.glRotatef(12.0F, 1.0F, 0.0F, 0.0F); //Modify for Large Sign
	            //GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F); //Modify for Large Sign
	            l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
	            f3 = (float)(l >> 16 & 255) / 255.0F;
	            f4 = (float)(l >> 8 & 255) / 255.0F;
	            f = (float)(l & 255) / 255.0F;

	            if (this.renderWithColor)
	            {
	                GL11.glColor4f(f3, f4, f, 1.0F);
	            }

	            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
	            this.renderBlocksRi.useInventoryTint = this.renderWithColor;
	            this.renderBlocksRi.renderBlockAsItem(block, k, 1.0F);
	            this.renderBlocksRi.useInventoryTint = true;

	            if (block.getRenderBlockPass() == 0)
	            {
	                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	            }

	            GL11.glPopMatrix();
	        }
	        else if (itemStack.getItem().requiresMultipleRenderPasses())
	        {
	            GL11.glDisable(GL11.GL_LIGHTING);
	            GL11.glEnable(GL11.GL_ALPHA_TEST);
	            textureManager.bindTexture(TextureMap.locationItemsTexture);
	            GL11.glDisable(GL11.GL_ALPHA_TEST);
	            GL11.glDisable(GL11.GL_TEXTURE_2D);
	            GL11.glEnable(GL11.GL_BLEND);
	            OpenGlHelper.glBlendFunc(0, 0, 0, 0);
	            GL11.glColorMask(false, false, false, true);
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            Tessellator tessellator = Tessellator.instance;
	            tessellator.startDrawingQuads();
	            tessellator.setColorOpaque_I(-1);
	            tessellator.addVertex((double)(theX - 2), (double)(theY + 18), (double)this.zLevel);
	            tessellator.addVertex((double)(theX + 18), (double)(theY + 18), (double)this.zLevel);
	            tessellator.addVertex((double)(theX + 18), (double)(theY - 2), (double)this.zLevel);
	            tessellator.addVertex((double)(theX - 2), (double)(theY - 2), (double)this.zLevel);
	            tessellator.draw();
	            GL11.glColorMask(true, true, true, true);
	            GL11.glEnable(GL11.GL_TEXTURE_2D);
	            GL11.glEnable(GL11.GL_ALPHA_TEST);

	            Item item = itemStack.getItem();
	            for (l = 0; l < item.getRenderPasses(k); ++l)
	            {
	                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	                textureManager.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
	                IIcon iicon = item.getIcon(itemStack, l);
	                int i1 = itemStack.getItem().getColorFromItemStack(itemStack, l);
	                f = (float)(i1 >> 16 & 255) / 255.0F;
	                float f1 = (float)(i1 >> 8 & 255) / 255.0F;
	                float f2 = (float)(i1 & 255) / 255.0F;

	                if (this.renderWithColor)
	                {
	                    GL11.glColor4f(f, f1, f2, 1.0F);
	                }

	                GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, ad renderEffect can derp them up.
	                GL11.glEnable(GL11.GL_ALPHA_TEST);

	                this.renderIcon(theX, theY, iicon, 1, 1);//Modify for Large Sign

	                GL11.glDisable(GL11.GL_ALPHA_TEST);
	                GL11.glEnable(GL11.GL_LIGHTING);

	                if (renderEffect && itemStack.hasEffect(l))
	                {
	                    renderEffect(textureManager, theX, theY);
	                }
	            }

	            GL11.glEnable(GL11.GL_LIGHTING);
	        }
	        else
	        {
	            GL11.glDisable(GL11.GL_LIGHTING);
	            GL11.glEnable(GL11.GL_BLEND);
	            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	            ResourceLocation resourcelocation = textureManager.getResourceLocation(itemStack.getItemSpriteNumber());
	            textureManager.bindTexture(resourcelocation);

	            if (object == null)
	            {
	                object = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
	            }

	            l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
	            f3 = (float)(l >> 16 & 255) / 255.0F;
	            f4 = (float)(l >> 8 & 255) / 255.0F;
	            f = (float)(l & 255) / 255.0F;

	            if (this.renderWithColor)
	            {
	                GL11.glColor4f(f3, f4, f, 1.0F);
	            }

	            GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, a renderEffect can derp them up.
	            GL11.glEnable(GL11.GL_ALPHA_TEST);
	            GL11.glEnable(GL11.GL_BLEND);
	            

	            this.renderIcon(theX, theY, (IIcon)object, 1, 1); //Modify for Large Sign
	           

	            GL11.glEnable(GL11.GL_LIGHTING);
	            GL11.glDisable(GL11.GL_ALPHA_TEST);
	            GL11.glDisable(GL11.GL_BLEND);

	            if (renderEffect && itemStack.hasEffect(0))
	            {
	                renderEffect(textureManager, theX, theY);
	            }
	            GL11.glEnable(GL11.GL_LIGHTING);
	        }

	        GL11.glEnable(GL11.GL_CULL_FACE);
	}
    
    private void renderGlint(int p_77018_1_, int theX, int theY, int scaleX, int scaleY)
    {
        
        GL11.glPushMatrix();//Modify for Large Sign
        GL11.glScalef(-11/16F, 11/16F, 1.0F);//Modify for Large Sign
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);//Modify for Large Sign
        double shiftX=-0.5;//Modify for Large Sign
        double shiftY=-0.5;//Modify for Large Sign
        double shiftZ=-0.425;//Modify for Large Sign
        float scale=20F;//Modify for Large Sign
        
    	for (int j1 = 0; j1 < 2; ++j1)
        {
            OpenGlHelper.glBlendFunc(772, 1, 0, 0);
            float f = 0.00390625F*scale;//Modify for Large Sign
            float f1 = 0.00390625F*scale;//Modify for Large Sign
            float f2 = (float)(Minecraft.getSystemTime() % (long)(3000 + j1 * 1873)) / (3000.0F + (float)(j1 * 1873)) * 256.0F/scale;//Modify for Large Sign
            float f3 = 0.0F;
            Tessellator tessellator = Tessellator.instance;
            float f4 = 4.0F;

            if (j1 == 1)
            {
                f4 = -1.0F;
            }
            
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(theX + shiftX + 0), (double)(theY + shiftY  + scaleY), (double)(this.zLevel + shiftZ), (double)((f2 + (float)scaleY * f4) * f), (double)((f3 + (float)scaleY) * f1));
            tessellator.addVertexWithUV((double)(theX + shiftX  + scaleX), (double)(theY + shiftY + scaleY), (double)(this.zLevel + shiftZ), (double)((f2 + (float)scaleX + (float)scaleY * f4) * f), (double)((f3 + (float)scaleY) * f1));
            tessellator.addVertexWithUV((double)(theX + shiftX  + scaleX), (double)(theY + shiftY + 0), (double)(this.zLevel + shiftZ), (double)((f2 + (float)scaleX) * f), (double)((f3 + 0.0F) * f1));
            tessellator.addVertexWithUV((double)(theX + shiftX  + 0), (double)(theY + shiftY + 0), (double)(this.zLevel + shiftZ), (double)((f2 + 0.0F) * f), (double)((f3 + 0.0F) * f1));
            tessellator.draw();
            
        }
    	
    	GL11.glPopMatrix();//Modify for Large Sign
    }

}
