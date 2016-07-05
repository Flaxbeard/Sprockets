package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBellows extends ModelBase
{

	public ModelRenderer head;
	public ModelRenderer base;
	public ModelRenderer squish;

	public ModelBellows()
	{
		this.textureHeight = 36;
		this.textureWidth = 44;

		
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-6f, -6f, -8.0F, 12, 12, 2, 0.0f);
		
		this.base = new ModelRenderer(this, 0, 0);
		this.base.addBox(-6f, -6f, 6.0F, 12, 12, 2, 0.0f);
		
		this.squish = new ModelRenderer(this, 0, 14);
		this.squish.addBox(-5f, -5f, -6.0F, 10, 10, 12, 0.0F);
		
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.head.render(f5);
	}
	
	public void renderSquish(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.squish.render(f5);
	}
	
	public void renderNoMove(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.base.render(f5);
	}

	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
