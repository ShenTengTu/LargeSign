package com.roripantsu.common.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *Extends TextureAtlasSprite.
 *This Class will load the specified resource
 *with specified rectangular region to be an icon.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomTextureSprite extends TextureAtlasSprite {
	
	private static final Logger logger = LogManager.getLogger();
	int mipmapLevels=Minecraft.getMinecraft().gameSettings.mipmapLevels;
	private final String basePath;
	private boolean isSpecifiedRegion;
	
	/**
	 * normal
	 */
	public CustomTextureSprite(String basePath,String iconName) {
		super(iconName);
		this.basePath=basePath;
		this.originX=0;
		this.originY=0;
		this.isSpecifiedRegion=false;
	}
	
	/**
	 * Specified rectangular region width*height start from (originX,originY).
	 */
	public CustomTextureSprite(int originX,int originY,int width,int height,String basePath,String iconName) {
		super(iconName);
		this.basePath=basePath;
		this.originX=originX;
		this.originY=originY;
		this.width=width;
		this.height=height;
		this.isSpecifiedRegion=true;
	}
	
	

	@Override
	public boolean hasCustomLoader(IResourceManager manager,ResourceLocation location) {
		return true;
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
        try
        {
        	ResourceLocation locationNormal=this.completeResourceLocation(location,0);
        	IResource iresource = manager.getResource(locationNormal);
            BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
            if(isSpecifiedRegion)
            	abufferedimage[0] = ImageIO.read(iresource.getInputStream()).getSubimage(originX, originY, width, height);
            else
            	abufferedimage[0] = ImageIO.read(iresource.getInputStream());
            	
            TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");

            if (texturemetadatasection != null)
            {
                List<?> list = texturemetadatasection.getListMipmaps();

                if (!list.isEmpty())
                {
                    int w = abufferedimage[0].getWidth();
                    int h = abufferedimage[0].getHeight();

                    if (MathHelper.roundUpToPowerOfTwo(w) != w || MathHelper.roundUpToPowerOfTwo(h) != h)
                    {
                        throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                    }
                }

                Iterator<?> iterator3 = list.iterator();

                while (iterator3.hasNext())
                {
                    int index = ((Integer)iterator3.next()).intValue();

                    if (index > 0 && index < abufferedimage.length - 1 && abufferedimage[index] == null)
                    {
                        ResourceLocation locationNormalMipmaps = this.completeResourceLocation(location, index);

                        try
                        {
                        	if(isSpecifiedRegion)
                        		abufferedimage[index] = ImageIO.read(manager.getResource(locationNormalMipmaps).getInputStream()).getSubimage(originX, originY, this.width, this.height);
                        	else
                        		abufferedimage[index] = ImageIO.read(manager.getResource(locationNormalMipmaps).getInputStream());
                        }
                        catch (IOException ioexception)
                        {
                            logger.error("Unable to load miplevel {} from: {}", new Object[] {Integer.valueOf(index), locationNormalMipmaps, ioexception});
                        }
                    }
                }
            }

            AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
            //loadSprite
            this.func_180598_a(abufferedimage, animationmetadatasection);
        }
        catch (RuntimeException runtimeexception)
        {
            logger.error("Unable to parse metadata from " + location, runtimeexception);
        }
        catch (IOException ioexception1)
        {
            logger.error("Using missing texture, unable to load " + location, ioexception1);
        }
		return false;
	}
	
	
	/**
	 * 
	 * @param location ResourceLocation which has registered.
	 * @param num = <=0 is normal, >0 is mipmaps
	 * @return complete resource path
	 */
    private ResourceLocation completeResourceLocation(ResourceLocation location, int num)
    {
        return num <=0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath , location.getResourcePath(), ".png"})): new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s_%d%s", new Object[] { this.basePath , location.getResourcePath(), Integer.valueOf(num), ".png"}));
    }
	
}
