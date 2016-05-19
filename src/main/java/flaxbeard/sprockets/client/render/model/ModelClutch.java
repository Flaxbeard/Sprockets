package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelClutch extends ModelBase
{

	public ModelRenderer axleTop;
	public ModelRenderer clutchTopEngaged;
	public ModelRenderer clutchTopUnengaged;

	public ModelRenderer axleBottom;
	public ModelRenderer clutchBottomEngaged;
	public ModelRenderer clutchBottomUnengaged;

	public ModelClutch()
	{
		this(true, true);
	}
	
	public ModelClutch(boolean top, boolean bottom)
	{
		this.textureHeight = 28;
		this.textureWidth = 20;
		this.axleTop = new ModelRenderer(this, 0, bottom ? 0 : 10);
		this.axleTop.addBox(-1.5f, -1.5f, bottom ? -8 : -7, 3, 3, bottom ? 7 : 6, 0.0f);
		
		this.clutchTopEngaged = new ModelRenderer(this, 0, 19);
		this.clutchTopEngaged.addBox(-3.5f, -3.5f, -2, 7, 7, 2, 0.0f);
		
		this.clutchTopUnengaged = new ModelRenderer(this, 0, 19);
		this.clutchTopUnengaged.addBox(-3.5f, -3.5f, -2.48f, 7, 7, 2, 0.0f);
		
		this.axleBottom = new ModelRenderer(this, 0, top ? 0 : 10);
		this.axleBottom.addBox(-1.5f, -1.5f, 1, 3, 3, top ? 7 : 6, 0.0f);
		
		this.clutchBottomEngaged = new ModelRenderer(this, 0, 19);
		this.clutchBottomEngaged.addBox(-3.5f, -3.5f, 0, 7, 7, 2, 0.0f);
		
		this.clutchBottomUnengaged = new ModelRenderer(this, 0, 19);
		this.clutchBottomUnengaged.addBox(-3.5f, -3.5f, 0.48f, 7, 7, 2, 0.0f);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
	
	}
	
	public void renderTop(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean engaged)
	{
		this.axleTop.render(f5);
		if (engaged)
		{
			this.clutchTopEngaged.render(f5);
		}
		else
		{
			this.clutchTopUnengaged.render(f5);
		}
	}
	
	public void renderBottom(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean engaged)
	{
		this.axleBottom.render(f5);
		if (engaged)
		{
			this.clutchBottomEngaged.render(f5);
		}
		else
		{
			this.clutchBottomUnengaged.render(f5);
		}
	}
	
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
