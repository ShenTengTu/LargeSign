package com.roripantsu.largesign.tileentity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

/**
 *Extends FontRenderer,Some methods' parameters change type "int" to "float".
 *This class using in TileEntitySpecialRenderer.
 *@author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomFontRenderer extends FontRenderer {
	private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
	private float alpha;
	private boolean bidiFlag;
	private float blue;
	private boolean boldStyle;
	private int[] charWidth = new int[256];
	private int[] colorCode = new int[32];
	private byte[] glyphWidth = new byte[65536];
	private float green;
	private boolean italicStyle;
	private final ResourceLocation locationFontTexture;
	private float posX;
	private float posY;
	private boolean randomStyle;
	private float red;
	private final TextureManager renderEngine;
	private boolean strikethroughStyle;
	private int textColor;
	private boolean underlineStyle;
	private boolean unicodeFlag;

	public CustomFontRenderer(GameSettings gameSettings,
			ResourceLocation resourceLocation,
			TextureManager textureManager, boolean unicode) {
		super(gameSettings, resourceLocation, textureManager,
				unicode);
		this.locationFontTexture = resourceLocation;
		this.renderEngine = textureManager;
		this.unicodeFlag = unicode;
		textureManager.bindTexture(this.locationFontTexture);

		for (int i = 0; i < 32; ++i) {
			int j = (i >> 3 & 1) * 85;
			int k = (i >> 2 & 1) * 170 + j;
			int l = (i >> 1 & 1) * 170 + j;
			int i1 = (i >> 0 & 1) * 170 + j;

			if (i == 6) {
				k += 85;
			}

			if (gameSettings.anaglyph) {
				int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
				int k1 = (k * 30 + l * 70) / 100;
				int l1 = (k * 30 + i1 * 70) / 100;
				k = j1;
				l = k1;
				i1 = l1;
			}

			if (i >= 16) {
				k /= 4;
				l /= 4;
				i1 /= 4;
			}

			this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
		}

		this.readGlyphSizes();
	}

    /**
     * Splits and draws a String with wordwrap (maximum length is parameter k)
     */
	public void drawSplitString(String str, float x, float y,
			int wrapWidth, int textColor) {
		this.resetStyles();
		this.textColor = textColor;
		str = this.trimStringNewline(str);
		this.renderSplitString(str, x, y, wrapWidth, false);
	}

	public int drawString(String str, float x, float y, int textColor) {
		return this.drawString(str, x, y, textColor, false);
	}

	public int drawString(String str, float x, float y, int textColor,
			boolean addShadow) {
		GL11.glEnable(GL11.GL_ALPHA_TEST);//1.8 GlStateManager.enableAlpha();
		this.resetStyles();
		int l;

		if (addShadow) {
			l = this.renderString(str, x + 1, y + 1, textColor, true);
			l = Math.max(l, this.renderString(str, x, y, textColor, false));
		} else {
			l = this.renderString(str, x, y, textColor, false);
		}

		return l;
	}

	public int drawStringWithShadow(String str, float x, float y,
			int textColor) {
		return this.drawString(str, x, y, textColor, true);
	}

	private String bidiReorder(String str) {
		try {
			Bidi bidi = new Bidi((new ArabicShaping(8)).shape(str), 127);
			bidi.setReorderingMode(0);
			return bidi.writeReordered(2);
		} catch (ArabicShapingException arabicshapingexception) {
			return str;
		}
	}

	private ResourceLocation getUnicodePageLocation(int index) {
		if (unicodePageLocations[index] == null) {
			unicodePageLocations[index] = new ResourceLocation(String.format(
					"textures/font/unicode_page_%02x.png",
					new Object[] { Integer.valueOf(index) }));
		}

		return unicodePageLocations[index];
	}

	private void loadGlyphTexture(int index) {
		this.renderEngine.bindTexture(this.getUnicodePageLocation(index));
	}

	private void readGlyphSizes() {
		
		InputStream inputstream = null;
		try {
			inputstream = Minecraft.getMinecraft()
					.getResourceManager()
					.getResource(new ResourceLocation("font/glyph_sizes.bin"))
					.getInputStream();
			inputstream.read(this.glyphWidth);
		} catch (IOException ioexception) {
			throw new RuntimeException(ioexception);
		}finally{
            IOUtils.closeQuietly(inputstream);
        }
	}

	private float renderCharAtPos(int par1, char par2, boolean par3) {
		return par2 == 32 ? 4.0F
				: ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
						.indexOf(par2) != -1 && !this.unicodeFlag ? this
						.renderDefaultChar(par1, par3) : this
						.renderUnicodeChar(par2, par3));
	}

	private float renderDefaultChar(int par1, boolean par2) {
		float f = (float)(par1 % 16 * 8);
		float f1 = (float)(par1 / 16 * 8);
		float f2 = par2 ? 1.0F : 0.0F;
		this.renderEngine.bindTexture(this.locationFontTexture);
		float f3 = (float)this.charWidth[par1] - 0.01F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(f / 128.0F, f1 / 128.0F);
		GL11.glVertex3f(this.posX + f2, this.posY, 0.0F);
		GL11.glTexCoord2f(f / 128.0F, (f1 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX - f2, this.posY + 7.99F, 0.0F);
		GL11.glTexCoord2f((f + f3 - 1.0F) / 128.0F, f1 / 128.0F);
		GL11.glVertex3f(this.posX + f3 - 1.0F + f2, this.posY, 0.0F);
		GL11.glTexCoord2f((f + f3 - 1.0F) / 128.0F, (f1 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX + f3 - 1.0F - f2, this.posY + 7.99F, 0.0F);
		GL11.glEnd();
		return this.charWidth[par1];
	}

	private void renderSplitString(String str, float x, float y,
			int warpWidth, boolean addShadow) {
		List<?> list = this.listFormattedStringToWidth(str, warpWidth);

		for (Iterator<?> iterator = list.iterator(); iterator.hasNext(); y += this.FONT_HEIGHT) {
			String s1 = (String) iterator.next();
			this.renderStringAligned(s1, x, y, warpWidth, this.textColor, addShadow);
		}
	}

	private int renderString(String str, float x, float y, int textColor,
			boolean addShadow) {
		if (str == null) {
			return 0;
		} else {
			if (this.bidiFlag) {
				str = this.bidiReorder(str);
			}

			if ((textColor & -67108864) == 0) {
				textColor |= -16777216;
			}

			if (addShadow) {
				textColor = (textColor & 16579836) >> 2 | textColor & -16777216;
			}
			this.red = (float)(textColor >> 16 & 255) / 255.0F;
			this.blue = (float)(textColor >> 8 & 255) / 255.0F;
			this.green = (float)(textColor & 255) / 255.0F;
			this.alpha = (float)(textColor >> 24 & 255) / 255.0F;
			//1.8  GlStateManager.color(float, float, float, float);
			GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
			this.posX = x;
			this.posY = y;
			this.renderStringAtPos(str, addShadow);
			return (int) this.posX;
		}
	}

	private int renderStringAligned(String str, float x, float y,
			int warpWidth, int textColor, boolean addShadow) {
		if (this.bidiFlag) {
			int i1 = this.getStringWidth(this.bidiReorder(str));
			x = x + warpWidth - i1;
		}

		return this.renderString(str, x, y, textColor, addShadow);
	}

	private void renderStringAtPos(String str, boolean addShadow) {
		for (int i = 0; i < str.length(); ++i) {
			char c0 = str.charAt(i);
			int j;
			int k;

			if (c0 == 167 && i + 1 < str.length()) {
				j = "0123456789abcdefklmnor".indexOf(str.toLowerCase()
						.charAt(i + 1));

				if (j < 16) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;

					if (j < 0 || j > 15) {
						j = 15;
					}

					if (addShadow) {
						j += 16;
					}

					k = this.colorCode[j];
					this.textColor = k;
					//1.8  GlStateManager.color(float, float, float, float);
					GL11.glColor4f((float)(k >> 16) / 255.0F,
							(float)(k >> 8 & 255) / 255.0F,
							(float)(k & 255) / 255.0F, this.alpha);
				} else if (j == 16) {
					this.randomStyle = true;
				} else if (j == 17) {
					this.boldStyle = true;
				} else if (j == 18) {
					this.strikethroughStyle = true;
				} else if (j == 19) {
					this.underlineStyle = true;
				} else if (j == 20) {
					this.italicStyle = true;
				} else if (j == 21) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					//1.8  GlStateManager.color(float, float, float, float);
					GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
				}

				++i;
			} else {
				j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
						.indexOf(c0);

				if (this.randomStyle && j != -1) {
					do {
						k = this.fontRandom.nextInt(this.charWidth.length);
					} while (this.charWidth[j] != this.charWidth[k]);

					j = k;
				}

				float f1 = this.unicodeFlag ? 0.5F : 1.0F;
				boolean flag1 = (c0 == 0 || j == -1 || this.unicodeFlag)
						&& addShadow;

				if (flag1) {
					this.posX -= f1;
					this.posY -= f1;
				}

				float f = this.renderCharAtPos(j, c0, this.italicStyle);

				if (flag1) {
					this.posX += f1;
					this.posY += f1;
				}

				if (this.boldStyle) {
					this.posX += f1;

					if (flag1) {
						this.posX -= f1;
						this.posY -= f1;
					}

					this.renderCharAtPos(j, c0, this.italicStyle);
					this.posX -= f1;

					if (flag1) {
						this.posX += f1;
						this.posY += f1;
					}

					++f;
				}

				Tessellator tessellator;
				WorldRenderer worldrenderer;

				if (this.strikethroughStyle) {
					tessellator = Tessellator.getInstance();
					worldrenderer = tessellator.getWorldRenderer();
					GL11.glDisable(GL11.GL_TEXTURE_2D);//1.8 GlStateManager.func_179090_x();
					worldrenderer.startDrawingQuads();
					worldrenderer.addVertex((double)this.posX,
							(double)(this.posY+(float)(this.FONT_HEIGHT / 2) + 0.25F),
							0.0D);
					worldrenderer.addVertex((double)(this.posX + f),
							(double)(this.posY+ (float)(this.FONT_HEIGHT / 2) + 0.25F),
							0.0D);
					worldrenderer.addVertex((double)(this.posX + f),
							(double)(this.posY+ (float)(this.FONT_HEIGHT / 2) - 0.25F),
							0.0D);
					worldrenderer.addVertex((double)this.posX,
							(double)(this.posY+ (float)(this.FONT_HEIGHT / 2) - 0.25F),
							0.0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);//1.8 GlStateManager.func_179098_w();
				}

				if (this.underlineStyle) {
					tessellator = Tessellator.getInstance();
					worldrenderer = tessellator.getWorldRenderer();
					GL11.glDisable(GL11.GL_TEXTURE_2D);//1.8 GlStateManager.func_179090_x();
					worldrenderer.startDrawingQuads();
					int l = this.underlineStyle ? -1 : 0;
					worldrenderer.addVertex((double)(this.posX + l),
							(double)(this.posY+ this.FONT_HEIGHT - 0.5F),
									0.0D);
					worldrenderer.addVertex((double)(this.posX + f),
							(double)(this.posY+ this.FONT_HEIGHT - 0.5F),
									0.0D);
					worldrenderer.addVertex((double)(this.posX + f),
							(double)(this.posY+ this.FONT_HEIGHT - 1F),
									0.0D);
					worldrenderer.addVertex((double)(this.posX + l),
							(double)(this.posY+ this.FONT_HEIGHT - 1F),
									0.0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);//1.8 GlStateManager.func_179098_w();
				}

				this.posX += ((int) f);
			}
		}
	}

	private float renderUnicodeChar(char par1, boolean par2) {
		if (this.glyphWidth[par1] == 0) {
			return 0.0F;
		} else {
			int i = par1 / 256;
			this.loadGlyphTexture(i);
			int j = this.glyphWidth[par1] >>> 4;
			int k = this.glyphWidth[par1] & 15;
			float f = (float)j;
			float f1 = (float)(k + 1);
			float f2 = (float)(par1 % 16 * 16) + f;
			float f3 = (float)((par1 & 255) / 16 * 16);
			float f4 = f1 - f - 0.02F;
			float f5 = par2 ? 1.0F : 0.0F;
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
			GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
			GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
			GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
			GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
			GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
			GL11.glEnd();
			return (f1 - f) / 2.0F + 1.0F;
		}
	}

	private void resetStyles() {
		this.randomStyle = false;
		this.boldStyle = false;
		this.italicStyle = false;
		this.underlineStyle = false;
		this.strikethroughStyle = false;
	}

	private String trimStringNewline(String str) {
		while (str != null && str.endsWith("\n")) {
			str = str.substring(0, str.length() - 1);
		}

		return str;
	}

}