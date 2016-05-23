package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBigMillstone extends ModelBase
{

	public ModelRenderer base;
	public ModelRenderer post;
	
	public ModelRenderer sidePost1;
	public ModelRenderer sidePost2;

	public ModelRenderer wheel1;
	public ModelRenderer wheel2;
	
	public ModelBigMillstone()
	{
		this.textureHeight = 98;
		this.textureWidth = 192;
		this.base = new ModelRenderer(this, 0, 0);
		this.base.addBox(-24F, -24f, -24F, 48, 16, 48, 0.0f);
		
		this.post = new ModelRenderer(this, 36, 0);
		this.post.addBox(-1.5F, -8f, -1.5F, 3, 32, 3, 0.0f);
		
		this.sidePost1 = new ModelRenderer(this, 0, 0);
		this.sidePost1.addBox(-1.5F, -1.5f, -11.5F, 3, 3, 10, 0.0f);
		
		this.sidePost2 = new ModelRenderer(this, 0, 0);
		this.sidePost2.addBox(-1.5F, -1.5f, 1.5F, 3, 3, 10, 0.0f);
		
		this.wheel1 = new ModelRenderer(this, 0, 64);
		this.wheel1.addBox(-12F, -12F, -21.5F, 24, 24, 10, 0.0f);
		this.sidePost1.addChild(wheel1);

		this.wheel2 = new ModelRenderer(this, 0, 64);
		this.wheel2.addBox(-12F, -12F, 11.5F, 24, 24, 10, 0.0f);
		this.sidePost2.addChild(wheel2);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.post.render(f5);
	}
	
	public void renderNoRotate(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.base.render(f5);
	}
	
	public void renderDualRotate1(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.sidePost1.render(f5);
	}
	
	public void renderDualRotate2(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.sidePost2.render(f5);
	}

	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
