package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMillstone extends ModelBase
{

	public ModelRenderer top;
	
	
	public ModelMillstone()
	{
		this.textureHeight = 24;
		this.textureWidth = 64;
		this.top = new ModelRenderer(this, 0, 0);
		this.top.addBox(-8F, 0f, -8F, 16, 8, 16, 0.0f);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.top.render(f5);
	}

	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
