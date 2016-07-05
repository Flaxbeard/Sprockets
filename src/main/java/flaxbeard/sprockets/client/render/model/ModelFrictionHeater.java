package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFrictionHeater extends ModelBase
{

	public ModelRenderer nub;
	public ModelRenderer frictionPad;
	public ModelRenderer netherBrick;
	
	public ModelFrictionHeater()
	{
		this.textureHeight = 44;
		this.textureWidth = 36;
		this.nub = new ModelRenderer(this, 0, 0);
		this.nub.addBox(-1.5f, -1.5f, -8.0F, 3, 3, 12, 0.0f);
		
		this.frictionPad = new ModelRenderer(this, 0, 15);
		this.frictionPad.addBox(-4.5f, -4.5f, 4.0F, 9, 9, 2, 0.0f);
		this.nub.addChild(frictionPad);
		
		this.netherBrick = new ModelRenderer(this, 0, 26);
		this.netherBrick.addBox(-8f, -8f, 6.0F, 16, 16, 2, 0.0f);
		
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.nub.render(f5);
	}
	
	public void renderNoRotate(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.netherBrick.render(f5);
	}

	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
