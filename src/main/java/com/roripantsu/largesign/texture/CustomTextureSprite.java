package com.roripantsu.largesign.texture;

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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *Extends TextureAtlasSprite.
 *This Class will load the specified resource
 *with specified rectangular region to be an icon.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomTextureSprite extends TextureAtlasSprite {
	
	private static final Logger logger = LogManager.getLogger();
	int anisotropicFiltering=Minecraft.getMinecraft().gameSettings.anisotropicFiltering;
	int mipmapLevels=Minecraft.getMinecraft().gameSettings.mipmapLevels;
	private final String basePath;
	
	public CustomTextureSprite(int originX,int originY,String basePath,String iconName) {
		super(iconName);
		this.basePath=basePath;
		this.originX=originX;
		this.originY=originY;
		this.width=16;
		this.height=16;
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager,
			ResourceLocation location) {
		return true;
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
        try
        {
        	ResourceLocation locationNormal=this.completeResourceLocation(location,0);
        	IResource iresource = manager.getResource(locationNormal);
            BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
            //load image with specified rectangular region
            abufferedimage[0] = ImageIO.read(iresource.getInputStream()).getSubimage(originX, originY, 16, 16);
            TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");

            if (texturemetadatasection != null)
            {
                List<?> list = texturemetadatasection.getListMipmaps();
                int l;

                if (!list.isEmpty())
                {
                    int k = abufferedimage[0].getWidth();
                    l = abufferedimage[0].getHeight();

                    if (MathHelper.roundUpToPowerOfTwo(k) != k || MathHelper.roundUpToPowerOfTwo(l) != l)
                    {
                        throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                    }
                }

                Iterator<?> iterator3 = list.iterator();

                while (iterator3.hasNext())
                {
                    l = ((Integer)iterator3.next()).intValue();

                    if (l > 0 && l < abufferedimage.length - 1 && abufferedimage[l] == null)
                    {
                        ResourceLocation locationNormalMipmaps = this.completeResourceLocation(location, l);

                        try
                        {
                            abufferedimage[l] = ImageIO.read(manager.getResource(locationNormalMipmaps).getInputStream());
                        }
                        catch (IOException ioexception)
                        {
                            logger.error("Unable to load miplevel {} from: {}", new Object[] {Integer.valueOf(l), locationNormalMipmaps, ioexception});
                        }
                    }
                }
            }

            AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
            this.loadSprite(abufferedimage, animationmetadatasection, this.anisotropicFiltering > 1.0F);
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
	
	
    private ResourceLocation completeResourceLocation(ResourceLocation location, int num)
    {
        return num==0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath , location.getResourcePath(), ".png"})): new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] { this.basePath , location.getResourcePath(), Integer.valueOf(num), ".png"}));
    }
	
}
