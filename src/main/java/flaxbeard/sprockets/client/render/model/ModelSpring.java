package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSpring extends ModelBase
{

	public ModelRenderer springTop;
	public ModelRenderer springBottom;
	public ModelRenderer springLeft;
	public ModelRenderer springRight;
	
	public ModelRenderer connectorFront;
	public ModelRenderer connectorBack;

	public ModelRenderer nub;

	
	
	public ModelSpring()
	{
		this.textureWidth = 84;
		this.textureHeight = 50;
		this.springTop = new ModelRenderer(this, 0, 16);
		this.springTop.addBox(-6F, 4f, -7.95F, 12, 2, 14, 0.0f);
		this.springBottom = new ModelRenderer(this, 0, 0);
		this.springBottom.addBox(-6F, -6f, -7.95F, 12, 2, 14, 0.0f);
		this.springTop.addChild(springBottom);
		
		this.springLeft = new ModelRenderer(this, 52, 22);
		this.springLeft.addBox(4F, -4f, -7.95F, 2, 8, 14, 0.0f);
		this.springTop.addChild(springLeft);
		this.springRight = new ModelRenderer(this, 52, 0);
		this.springRight.addBox(-6F, -4f, -7.95F, 2, 8, 14, 0.0f);
		this.springTop.addChild(springRight);
		

		this.connectorBack = new ModelRenderer(this, 0, 32);
		this.connectorBack.addBox(-4.5F, -4.5f, 5.90F, 9, 9, 0, 0.0f);
		this.springTop.addChild(connectorBack);
		
		this.nub = new ModelRenderer(this, 16, 32);
		this.nub.addBox(-1.5F, -1.5f, -8F, 3, 3, 15, 0.0f);
		
		this.connectorFront = new ModelRenderer(this, 0, 32);
		this.connectorFront.addBox(-4.5F, -4.5f, -7.75F, 9, 9, 0, 0.0f);
		
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.springTop.render(f5);
	}
	
	public void renderNub(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.nub.render(f5);
	}
	
	public void renderFrontConnector(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.connectorFront.render(f5);
	}

	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
