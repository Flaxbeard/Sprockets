package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFrictionPad extends ModelBase
{

	public ModelRenderer nub;
	
	public ModelFrictionPad()
	{
		this.textureHeight = 64;
		this.textureWidth = 64;;
		this.nub = new ModelRenderer(this, 0, 0);
		this.nub.addBox(-1.5f, -1.5f, -8f, 3, 3, 1, 0.0f);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.nub.render(f5);
	}
	
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
