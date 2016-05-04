package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSprocket extends ModelBase
{
	public ModelRenderer part1;
	public ModelRenderer part2;
	public ModelRenderer part3;
	public ModelRenderer part4;
	public ModelRenderer center;
	public ModelRenderer nub;
	public ModelRenderer center2;
	
	public ModelSprocket()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.part1 = new ModelRenderer(this, 0, 0);
		this.part1.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part1.addBox(-8.5F, -1.5F, -7.996F, 17, 3, 1, 0.0F);
		
		this.part2 = new ModelRenderer(this, 0, 0);
		this.part2.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part2.addBox(-1.5F, -8.5F, -7.997F, 3, 17, 1, 0.0F);
		this.part1.addChild(part2);
		
		this.part3 = new ModelRenderer(this, 0, 0);
		this.part3.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part3.addBox(-1.5F, -8.5F, -7.998F, 3, 17, 1, 0.0F);
		this.setRotateAngle(part3, 0.0F, 0.0F, (float) Math.toRadians(45.0F));
		this.part1.addChild(part3);
		
		this.part4 = new ModelRenderer(this, 0, 0);
		this.part4.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part4.addBox(-1.5F, -8.5F, -8.0F, 3, 17, 1, 0.0F);
		this.setRotateAngle(part4, 0.0F, 0.0F, (float) -Math.toRadians(45.0F));
		this.part1.addChild(part4);
		
		this.center = new ModelRenderer(this, 0, 0);
		this.center.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center.addBox(-6.0f, -6.0f, -8.002f, 12, 12, 1, 0.0f);
		this.part1.addChild(center);
		
		this.center2 = new ModelRenderer(this, 0, 0);
		this.center2.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center2.addBox(-6.0f, -6.0f, -8.001f, 12, 12, 1, 0.0f);
		this.setRotateAngle(center2, 0.0F, 0.0F, (float) -Math.toRadians(45.0F));
		this.part1.addChild(center2);
		
		this.nub = new ModelRenderer(this, 48, 48);
		this.nub.addBox(-1f, -1f, -8.003f, 2, 2, 2, 0.0f);
		
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.part1.render(f5);
	}
	
	public void renderNub(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
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
