package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWaterWheel extends ModelBase
{

	public ModelRenderer nub;
	
	
	public ModelRenderer base1;
	public ModelRenderer base2;
	public ModelRenderer base3;
	public ModelRenderer base4;
	public ModelRenderer base5;
	public ModelRenderer base6;
	public ModelRenderer base7;
	public ModelRenderer base8;
	
	public ModelRenderer arm1;
	public ModelRenderer arm2;
	public ModelRenderer arm3;
	public ModelRenderer arm4;
	public ModelRenderer arm5;
	public ModelRenderer arm6;
	public ModelRenderer arm7;
	public ModelRenderer arm8;
	
	public ModelRenderer sideL1;
	public ModelRenderer sideL2;
	public ModelRenderer sideL3;
	public ModelRenderer sideL4;
	public ModelRenderer sideL5;
	public ModelRenderer sideL6;
	public ModelRenderer sideL7;
	public ModelRenderer sideL8;
	
	public ModelRenderer sideS1;
	public ModelRenderer sideS2;
	public ModelRenderer sideS3;
	public ModelRenderer sideS4;
	public ModelRenderer sideS5;
	public ModelRenderer sideS6;
	public ModelRenderer sideS7;
	public ModelRenderer sideS8;

	
	public ModelRenderer spokeL1;
	public ModelRenderer spokeL2;
	public ModelRenderer spokeL3;
	public ModelRenderer spokeL4;
	public ModelRenderer spokeL5;
	public ModelRenderer spokeL6;
	public ModelRenderer spokeL7;
	public ModelRenderer spokeL8;
	
	public ModelRenderer sideR1;
	public ModelRenderer sideR2;
	public ModelRenderer sideR3;
	public ModelRenderer sideR4;
	public ModelRenderer sideR5;
	public ModelRenderer sideR6;
	public ModelRenderer sideR7;
	public ModelRenderer sideR8;
	
	public ModelRenderer spokeR1;
	public ModelRenderer spokeR2;
	public ModelRenderer spokeR3;
	public ModelRenderer spokeR4;
	public ModelRenderer spokeR5;
	public ModelRenderer spokeR6;
	public ModelRenderer spokeR7;
	public ModelRenderer spokeR8;
	
	public ModelWaterWheel()
	{
		this.textureHeight = 64;
		this.textureWidth = 128;
		this.nub = new ModelRenderer(this, 0, 0);
		this.nub.addBox(-1.5F, -1.5f, -8.001F, 3, 3, 16, 0.0f);
		
		this.arm1 = new ModelRenderer(this, 0, 19);
		this.arm1.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm1, 0F, 0F, (float) Math.toRadians(22.5F));
		this.nub.addChild(arm1);
		
		this.arm2 = new ModelRenderer(this, 0, 19);
		this.arm2.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm2, 0F, 0F, (float) Math.toRadians(45F + 22.5F));
		this.nub.addChild(arm2);
		
		this.arm3 = new ModelRenderer(this, 0, 19);
		this.arm3.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm3, 0F, 0F, (float) Math.toRadians(90F + 22.5F));
		this.nub.addChild(arm3);
		
		this.arm4 = new ModelRenderer(this, 0, 19);
		this.arm4.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm4, 0F, 0F, (float) Math.toRadians(135F + 22.5F));
		this.nub.addChild(arm4);
		
		this.arm5 = new ModelRenderer(this, 0, 19);
		this.arm5.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm5, 0F, 0F, (float) Math.toRadians(180F + 22.5F));
		this.nub.addChild(arm5);
		
		this.arm6 = new ModelRenderer(this, 0, 19);
		this.arm6.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm6, 0F, 0F, (float) Math.toRadians(225F + 22.5F));
		this.nub.addChild(arm6);
		
		this.arm7 = new ModelRenderer(this, 0, 19);
		this.arm7.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm7, 0F, 0F, (float) Math.toRadians(270F + 22.5F));
		this.nub.addChild(arm7);
		
		this.arm8 = new ModelRenderer(this, 0, 19);
		this.arm8.addBox(-1F, 29.94F, -8F, 2, 12, 16, 0.0f);
		setRotateAngle(arm8, 0F, 0F, (float) Math.toRadians(315F + 22.5F));
		this.nub.addChild(arm8);
		
		this.base1 = new ModelRenderer(this, 38, 0);
		this.base1.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		this.nub.addChild(base1);
		
		this.base2 = new ModelRenderer(this, 38, 0);
		this.base2.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base2, 0F, 0F, (float) Math.toRadians(45F));
		this.nub.addChild(base2);
		
		this.base3 = new ModelRenderer(this, 38, 0);
		this.base3.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base3, 0F, 0F, (float) Math.toRadians(90F));
		this.nub.addChild(base3);
		
		this.base4 = new ModelRenderer(this, 38, 0);
		this.base4.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base4, 0F, 0F, (float) Math.toRadians(135F));
		this.nub.addChild(base4);
		
		this.base5 = new ModelRenderer(this, 38, 0);
		this.base5.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base5, 0F, 0F, (float) Math.toRadians(180F));
		this.nub.addChild(base5);
		
		this.base6 = new ModelRenderer(this, 38, 0);
		this.base6.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base6, 0F, 0F, (float) Math.toRadians(225F));
		this.nub.addChild(base6);
		
		this.base7 = new ModelRenderer(this, 38, 0);
		this.base7.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base7, 0F, 0F, (float) Math.toRadians(270F));
		this.nub.addChild(base7);
		
		this.base8 = new ModelRenderer(this, 38, 0);
		this.base8.addBox(-12F, 27.93F, -8F, 24, 1, 16, 0.0f);
		setRotateAngle(base8, 0F, 0F, (float) Math.toRadians(315F));
		this.nub.addChild(base8);
		
		this.sideS1 = new ModelRenderer(this, 38, 35);
		this.sideS1.addBox(-13F, 27.8F, -9.001F, 26, 6, 2, 0.0f);
		
		this.sideS2 = new ModelRenderer(this, 38, 35);
		this.sideS2.addBox(-13F, 27.8F, -9.002F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS2, 0F, 0F, (float) Math.toRadians(45F));
		this.sideS1.addChild(sideS2);
		
		this.sideS3 = new ModelRenderer(this, 38, 35);
		this.sideS3.addBox(-13F, 27.8F, -9.001F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS3, 0F, 0F, (float) Math.toRadians(90F));
		this.sideS1.addChild(sideS3);
		
		this.sideS4 = new ModelRenderer(this, 38, 35);
		this.sideS4.addBox(-13F, 27.8F, -9.002F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS4, 0F, 0F, (float) Math.toRadians(135F));
		this.sideS1.addChild(sideS4);
		
		this.sideS5 = new ModelRenderer(this, 38, 35);
		this.sideS5.addBox(-13F, 27.8F, -9.001F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS5, 0F, 0F, (float) Math.toRadians(180F));
		this.sideS1.addChild(sideS5);
		
		this.sideS6 = new ModelRenderer(this, 38, 35);
		this.sideS6.addBox(-13F, 27.8F, -9.002F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS6, 0F, 0F, (float) Math.toRadians(225F));
		this.sideS1.addChild(sideS6);
		
		this.sideS7 = new ModelRenderer(this, 38, 35);
		this.sideS7.addBox(-13F, 27.8F, -9.001F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS7, 0F, 0F, (float) Math.toRadians(270F));
		this.sideS1.addChild(sideS7);
		
		this.sideS8 = new ModelRenderer(this, 38, 35);
		this.sideS8.addBox(-13F, 27.8F, -9.002F, 26, 6, 2, 0.0f);
		setRotateAngle(sideS8, 0F, 0F, (float) Math.toRadians(315F));
		this.sideS1.addChild(sideS8);

		this.sideL1 = new ModelRenderer(this, 38, 19);
		this.sideL1.addBox(-16.5F, 27.8F, -8.001F, 33, 12, 2, 0.0f);
		
		this.sideL2 = new ModelRenderer(this, 38, 19);
		this.sideL2.addBox(-16.5F, 27.8F, -8.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL2, 0F, 0F, (float) Math.toRadians(45F));
		this.sideL1.addChild(sideL2);
		
		this.sideL3 = new ModelRenderer(this, 38, 19);
		this.sideL3.addBox(-16.5F, 27.8F, -8.001F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL3, 0F, 0F, (float) Math.toRadians(90F));
		this.sideL1.addChild(sideL3);
		
		this.sideL4 = new ModelRenderer(this, 38, 19);
		this.sideL4.addBox(-16.5F, 27.8F, -8.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL4, 0F, 0F, (float) Math.toRadians(135F));
		this.sideL1.addChild(sideL4);
		
		this.sideL5 = new ModelRenderer(this, 38, 19);
		this.sideL5.addBox(-16.5F, 27.8F, -8.001F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL5, 0F, 0F, (float) Math.toRadians(180F));
		this.sideL1.addChild(sideL5);
		
		this.sideL6 = new ModelRenderer(this, 38, 19);
		this.sideL6.addBox(-16.5F, 27.8F, -8.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL6, 0F, 0F, (float) Math.toRadians(225F));
		this.sideL1.addChild(sideL6);
		
		this.sideL7 = new ModelRenderer(this, 38, 19);
		this.sideL7.addBox(-16.5F, 27.8F, -8.001F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL7, 0F, 0F, (float) Math.toRadians(270F));
		this.sideL1.addChild(sideL7);
		
		this.sideL8 = new ModelRenderer(this, 38, 19);
		this.sideL8.addBox(-16.5F, 27.8F, -8.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideL8, 0F, 0F, (float) Math.toRadians(315F));
		this.sideL1.addChild(sideL8);
		
		this.spokeL1 = new ModelRenderer(this, 113, 19);
		this.spokeL1.addBox(-2.5F, 0.8F, -6F, 5, 27, 2, 0.0f);
		this.sideL1.addChild(spokeL1);

		this.spokeL2 = new ModelRenderer(this, 113, 19);
		this.spokeL2.addBox(-2.5F, 0.8F, -6.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL2, 0F, 0F, (float) Math.toRadians(45F));
		this.sideL1.addChild(spokeL2);
		
		this.spokeL3 = new ModelRenderer(this, 113, 19);
		this.spokeL3.addBox(-2.5F, 0.8F, -6.001F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL3, 0F, 0F, (float) Math.toRadians(90F));
		this.sideL1.addChild(spokeL3);
		
		this.spokeL4 = new ModelRenderer(this, 113, 19);
		this.spokeL4.addBox(-2.5F, 0.8F, -6.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL4, 0F, 0F, (float) Math.toRadians(135F));
		this.sideL1.addChild(spokeL4);
		
		this.spokeL5 = new ModelRenderer(this, 113, 19);
		this.spokeL5.addBox(-2.5F, 0.8F, -6.001F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL5, 0F, 0F, (float) Math.toRadians(180F));
		this.sideL1.addChild(spokeL5);
		
		this.spokeL6 = new ModelRenderer(this, 113, 19);
		this.spokeL6.addBox(-2.5F, 0.8F, -6.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL6, 0F, 0F, (float) Math.toRadians(225F));
		this.sideL1.addChild(spokeL6);
		
		this.spokeL7 = new ModelRenderer(this, 113, 19);
		this.spokeL7.addBox(-2.5F, 0.8F, -6.001F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL7, 0F, 0F, (float) Math.toRadians(270F));
		this.sideL1.addChild(spokeL7);
		
		this.spokeL8 = new ModelRenderer(this, 113, 19);
		this.spokeL8.addBox(-2.5F, 0.8F, -6.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeL8, 0F, 0F, (float) Math.toRadians(315F));
		this.sideL1.addChild(spokeL8);
		
		
		this.sideR1 = new ModelRenderer(this, 38, 19);
		this.sideR1.addBox(-16.5F, 27.8F, 6.001F, 33, 12, 2, 0.0f);
		
		this.sideR2 = new ModelRenderer(this, 38, 19);
		this.sideR2.addBox(-16.5F, 27.8F, 6.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR2, 0F, 0F, (float) Math.toRadians(45F));
		this.sideR1.addChild(sideR2);
		
		this.sideR3 = new ModelRenderer(this, 38, 19);
		this.sideR3.addBox(-16.5F, 27.8F, 6.001F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR3, 0F, 0F, (float) Math.toRadians(90F));
		this.sideR1.addChild(sideR3);
		
		this.sideR4 = new ModelRenderer(this, 38, 19);
		this.sideR4.addBox(-16.5F, 27.8F, 6.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR4, 0F, 0F, (float) Math.toRadians(135F));
		this.sideR1.addChild(sideR4);
		
		this.sideR5 = new ModelRenderer(this, 38, 19);
		this.sideR5.addBox(-16.5F, 27.8F, 6.001F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR5, 0F, 0F, (float) Math.toRadians(180F));
		this.sideR1.addChild(sideR5);
		
		this.sideR6 = new ModelRenderer(this, 38, 19);
		this.sideR6.addBox(-16.5F, 27.8F, 6.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR6, 0F, 0F, (float) Math.toRadians(225F));
		this.sideR1.addChild(sideR6);
		
		this.sideR7 = new ModelRenderer(this, 38, 19);
		this.sideR7.addBox(-16.5F, 27.8F, 6.001F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR7, 0F, 0F, (float) Math.toRadians(270F));
		this.sideR1.addChild(sideR7);
		
		this.sideR8 = new ModelRenderer(this, 38, 19);
		this.sideR8.addBox(-16.5F, 27.8F, 6.002F, 33, 12, 2, 0.0f);
		setRotateAngle(sideR8, 0F, 0F, (float) Math.toRadians(315F));
		this.sideR1.addChild(sideR8);
		
		this.spokeR1 = new ModelRenderer(this, 113, 19);
		this.spokeR1.addBox(-2.5F, 0.8F, 4.001F, 5, 27, 2, 0.0f);
		this.sideR1.addChild(spokeR1);

		this.spokeR2 = new ModelRenderer(this, 113, 19);
		this.spokeR2.addBox(-2.5F, 0.8F, 4.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR2, 0F, 0F, (float) Math.toRadians(45F));
		this.sideR1.addChild(spokeR2);
		
		this.spokeR3 = new ModelRenderer(this, 113, 19);
		this.spokeR3.addBox(-2.5F, 0.8F, 4.001F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR3, 0F, 0F, (float) Math.toRadians(90F));
		this.sideR1.addChild(spokeR3);
		
		this.spokeR4 = new ModelRenderer(this, 113, 19);
		this.spokeR4.addBox(-2.5F, 0.8F, 4.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR4, 0F, 0F, (float) Math.toRadians(135F));
		this.sideR1.addChild(spokeR4);
		
		this.spokeR5 = new ModelRenderer(this, 113, 19);
		this.spokeR5.addBox(-2.5F, 0.8F, 4.001F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR5, 0F, 0F, (float) Math.toRadians(180F));
		this.sideR1.addChild(spokeR5);
		
		this.spokeR6 = new ModelRenderer(this, 113, 19);
		this.spokeR6.addBox(-2.5F, 0.8F, 4.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR6, 0F, 0F, (float) Math.toRadians(225F));
		this.sideR1.addChild(spokeR6);
		
		this.spokeR7 = new ModelRenderer(this, 113, 19);
		this.spokeR7.addBox(-2.5F, 0.8F, 4.001F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR7, 0F, 0F, (float) Math.toRadians(270F));
		this.sideR1.addChild(spokeR7);
		
		this.spokeR8 = new ModelRenderer(this, 113, 19);
		this.spokeR8.addBox(-2.5F, 0.8F, 4.002F, 5, 27, 2, 0.0f);
		setRotateAngle(spokeR8, 0F, 0F, (float) Math.toRadians(315F));
		this.sideR1.addChild(spokeR8);
		
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.nub.render(f5);
	}
	
	public void renderLeft(float f5, boolean big)
	{
		if (big)
		{
			this.sideL1.render(f5);
		}
		else
		{
			this.sideS1.render(f5);
		}
	}

	public void renderRight(float f5, boolean big)
	{
		if (big)
		{
			this.sideR1.render(f5);
		}
	}
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
