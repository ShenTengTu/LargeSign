package com.roripantsu.largesign.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 *the model of block of Large Sign
 *@author ShenTeng Tu(RoriPantsu)
 */
public class Model_LargeSign extends ModelBase {

	public ModelRenderer LargeSign;

	public Model_LargeSign() {
		textureWidth = 64;
		textureHeight = 32;

		LargeSign = new ModelRenderer(this, 0, 0);
		LargeSign.addBox(-8F, -8F, -1F, 16, 16, 2);// Offset(RotationPoint) and
													// Dimensions
		LargeSign.setTextureSize(64, 32);
		LargeSign.setRotationPoint(0F, 0F, 15F);
		LargeSign.mirror = true;
		setRotation(LargeSign, 0F, 0F, 0F);
	}

	public void renderLargeSign(Entity entity, float f, float f1, float f2,
			float f3, float f4, float scale) {
		//super.render(entity, f, f1, f2, f3, f4, scale);
		//setRotationAngles(f, f1, f2, f3, f4, scale, entity);
		LargeSign.render(scale);
	}

	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
