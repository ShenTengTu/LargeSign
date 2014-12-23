package com.roripantsu.guilib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

/**
 * Custom Slider.
 * Reference GuiOptionSlider and GameSettings.Options .
 * @author ShenTeng Tu(RoriPantsu)
 */
@SideOnly(Side.CLIENT)
public class CustomGuiSlider extends GuiButton {
	
	private float percent;
	private float value;
	private String name;
    public boolean isPressed;
    private final float min;
    private final float max;
	private final float step;
    
    public CustomGuiSlider(int id, int x, int y,
			int width, String name,
			float step,float min,float max,float value) {
    	super(id, x, y, width, 20, "");
    	this.step=step;
    	this.min=min;
		this.max=max;
		this.name=name;
		this.setValue(value);	
	}
    
    @Override
	public int getHoverState(boolean isHover)
    {
        return 0;
    }
    
    @Override
	protected void mouseDragged(Minecraft MC, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.isPressed)
            {
                this.percent = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.percent < 0.0F)
                    this.percent = 0.0F;
                if (this.percent > 1.0F)
                    this.percent = 1.0F;
                this.setValue(this.denormalize(this.percent));
            }

            this.drawSlider();
        }
    }


    @Override
	public boolean mousePressed(Minecraft MC, int mouseX, int mouseY)
    {
        if (super.mousePressed(MC, mouseX, mouseY))
        {
            this.percent = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.percent < 0.0F)
                this.percent = 0.0F;
            if (this.percent > 1.0F)
                this.percent = 1.0F;
            this.setValue(this.denormalize(this.percent));
            this.isPressed = true;
            return true;
        }
        else
        {
            return false;
        }
    }


    @Override
	public void mouseReleased(int mouseX, int mouseY)
    {
        this.isPressed = false;
    }
    
    private void drawSlider(){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.xPosition + (int)(this.percent * (this.width - 8)), this.yPosition,
        		0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + (int)(this.percent * (this.width - 8)) + 4, this.yPosition,
        		196, 66, 4, 20);
    }

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = this.snapToStepClamp(value);
		this.percent=this.normalize(this.value);
		this.displayString =this.name+":"+this.value;
	}
    
    private float snapToStep(float value)
    {
        if (this.step > 0.0F)
            value = this.step * (float)Math.round(value / this.step);

        return value;
    }
    
    private float snapToStepClamp(float value)
    {
        value = this.snapToStep(value);
        return MathHelper.clamp_float(value, this.min, this.max);
    }
    
    private float normalize(float value)
    {
        return MathHelper.clamp_float((this.snapToStepClamp(value) - this.min) / (this.max - this.min), 0.0F, 1.0F);
    }

    private float denormalize(float value)
    {
        return this.snapToStepClamp(this.min + (this.max - this.min) * MathHelper.clamp_float(value, 0.0F, 1.0F));
    }
}
