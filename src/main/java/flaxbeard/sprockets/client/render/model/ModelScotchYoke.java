package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelScotchYoke extends ModelBase
{

	public ModelRenderer axleTop;
	public ModelRenderer squareTop;

	public ModelRenderer axleBottom;
	public ModelRenderer squareBottom;
	
	public ModelRenderer nub;
	
	public ModelRenderer arm;
	public ModelRenderer arm2;
	public ModelRenderer arm3;
	public ModelRenderer arm4;
	public ModelRenderer arm5;
	public ModelRenderer arm6;

	public ModelScotchYoke()
	{
		this(true, true);
	}
	
	public ModelScotchYoke(boolean top, boolean bottom)
	{
		this.textureHeight = 36;
		this.textureWidth = 26;
		this.axleTop = new ModelRenderer(this, 0, bottom ? 0 : 8);
		this.axleTop.addBox(-1.5f, -1.5f, bottom ? -8 : -7, 3, 3, bottom ? 5 : 4, 0.0f);
		
		this.squareTop = new ModelRenderer(this, 16, 0);
		this.squareTop.addBox(-1.5f, -6.5f, -3F, 3, 8, 2, 0.0f);
		
		this.nub = new ModelRenderer(this, 16, 10);
		this.nub.addBox(-1f, -6f, -1F, 2, 2, 2, 0.0f);
		this.squareTop.addChild(nub);
		
		this.axleBottom = new ModelRenderer(this, 0, top ? 0 : 8);
		this.axleBottom.addBox(-1.5f, -1.5f, 3F, 3, 3, top ? 5 : 4, 0.0f);
		this.axleTop.addChild(axleBottom);
		
		this.squareBottom = new ModelRenderer(this, 16, 0);
		this.squareBottom.addBox(-1.5f, -6.5f, 1F, 3, 8, 2, 0.0f);
		this.squareTop.addChild(squareBottom);
		
		this.arm = new ModelRenderer(this, 0, 15);
		this.arm.addBox(-3f, -7.5f, -1F, 2, 15, 2, 0.0f);
		
		this.arm2 = new ModelRenderer(this, 0, 15);
		this.arm2.addBox(1f, -7.5f, -1F, 2, 15, 2, 0.0f);
		this.arm.addChild(arm2);
		
		this.arm3 = new ModelRenderer(this, 8, 15);
		this.arm3.addBox(-1f, -7.5f, -1F, 2, 2, 2, 0.0f);
		this.arm.addChild(arm3);
		
		this.arm4 = new ModelRenderer(this, 8, 15);
		this.arm4.addBox(-1f, 5.5f, -1F, 2, 2, 2, 0.0f);
		this.arm.addChild(arm4);
		
		this.arm5 = new ModelRenderer(this, 0, 32);
		this.arm5.addBox(-13.05f, -1f, -1F, 11, 2, 2, 0.0f);
		this.arm.addChild(arm5);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.axleTop.render(f5);
	}
	
	public void renderNub(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean engaged)
	{
		this.squareTop.render(f5);
	}
	
	public void renderArm(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean engaged)
	{
		this.arm.render(f5);
	}

	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
