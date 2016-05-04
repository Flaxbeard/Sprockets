package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAxle extends ModelBase
{

	public ModelRenderer nub;
	
	public ModelAxle(boolean top, boolean bottom)
	{
		this.textureHeight = 72;
		this.textureWidth = 38;
		int start = top ? bottom ? 0 : 37 : bottom ? 19 : 55;
		this.nub = new ModelRenderer(this, 0, start);
		int size = top ? bottom ? 16 : 15 : bottom ? 15 : 14;
		float b = bottom ? -8.00F : -7.00F;
		this.nub.addBox(-1.5f, -1.5f, b, 3, 3, size, 0.0f);
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
