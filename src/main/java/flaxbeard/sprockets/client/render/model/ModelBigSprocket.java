package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBigSprocket extends ModelBase
{
	public ModelRenderer part1;
	public ModelRenderer part2;
	public ModelRenderer part3;
	public ModelRenderer part4;
	public ModelRenderer part5;
	public ModelRenderer part6;
	public ModelRenderer part7;
	public ModelRenderer part8;
	public ModelRenderer part9;
	public ModelRenderer part10;
	public ModelRenderer part11;
	public ModelRenderer part12;
	public ModelRenderer center;
	public ModelRenderer nub;
	public ModelRenderer center2;
	public ModelRenderer center3;
	public ModelRenderer center4;
	public ModelRenderer center5;
	public ModelRenderer center6;
	
	public ModelBigSprocket()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		
		this.part1 = new ModelRenderer(this, 0, 0);
		this.part1.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part1.addBox(-24.5F, -1.5F, -7.989F, 49, 3, 1, 0.0F);
		
		this.part2 = new ModelRenderer(this, 0, 0);
		this.part2.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part2.addBox(-1.5F, -24.5F, -7.990F, 3, 49, 1, 0.0F);
		this.part1.addChild(part2);
		
		this.part3 = new ModelRenderer(this, 0, 0);
		this.part3.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part3.addBox(-1.5F, -24.5F, -7.991F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part3, 0.0F, 0.0F, (float) Math.toRadians(15.0F));
		this.part1.addChild(part3);
		
		this.part4 = new ModelRenderer(this, 0, 0);
		this.part4.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part4.addBox(-1.5F, -24.5F, -7.992F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part4, 0.0F, 0.0F, (float) Math.toRadians(30.0F));
		this.part1.addChild(part4);
		
		this.part5 = new ModelRenderer(this, 0, 0);
		this.part5.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part5.addBox(-1.5F, -24.5F, -7.993F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part5, 0.0F, 0.0F, (float) Math.toRadians(45.0F));
		this.part1.addChild(part5);
		
		this.part6 = new ModelRenderer(this, 0, 0);
		this.part6.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part6.addBox(-1.5F, -24.5F, -7.994F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part6, 0.0F, 0.0F, (float) Math.toRadians(60.0F));
		this.part1.addChild(part6);
		
		this.part7 = new ModelRenderer(this, 0, 0);
		this.part7.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part7.addBox(-1.5F, -24.5F, -7.995F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part7, 0.0F, 0.0F, (float) Math.toRadians(75.0F));
		this.part1.addChild(part7);
		
		this.part8 = new ModelRenderer(this, 0, 0);
		this.part8.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part8.addBox(-1.5F, -24.5F, -7.996F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part8, 0.0F, 0.0F, (float) Math.toRadians(105.0F));
		this.part1.addChild(part8);
		
		this.part9 = new ModelRenderer(this, 0, 0);
		this.part9.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part9.addBox(-1.5F, -24.5F, -7.997F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part9, 0.0F, 0.0F, (float) Math.toRadians(120.0F));
		this.part1.addChild(part9);
		
		this.part10 = new ModelRenderer(this, 0, 0);
		this.part10.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part10.addBox(-1.5F, -24.5F, -7.998F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part10, 0.0F, 0.0F, (float) Math.toRadians(135.0F));
		this.part1.addChild(part10);
		
		this.part11 = new ModelRenderer(this, 0, 0);
		this.part11.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part11.addBox(-1.5F, -24.5F, -7.999F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part11, 0.0F, 0.0F, (float) Math.toRadians(150.0F));
		this.part1.addChild(part11);
		
		this.part12 = new ModelRenderer(this, 0, 0);
		this.part12.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.part12.addBox(-1.5F, -24.5F, -8.0F, 3, 49, 1, 0.0F);
		this.setRotateAngle(part12, 0.0F, 0.0F, (float) Math.toRadians(165.0F));
		this.part1.addChild(part12);
		
		
		
		
		this.center = new ModelRenderer(this, 0, 0);
		this.center.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center.addBox(-17.0f, -17.0f, -8.006f, 34, 34, 1, 0.0f);
		this.part1.addChild(center);
		
		this.center2 = new ModelRenderer(this, 0, 0);
		this.center2.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center2.addBox(-17.0f, -17.0f, -8.005f, 34, 34, 1, 0.0f);
		this.setRotateAngle(center2, 0.0F, 0.0F, (float) -Math.toRadians(45.0F));
		this.part1.addChild(center2);
		
		this.center3 = new ModelRenderer(this, 0, 0);
		this.center3.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center3.addBox(-17.0f, -17.0f, -8.004f, 34, 34, 1, 0.0f);
		this.setRotateAngle(center3, 0.0F, 0.0F, (float) -Math.toRadians(15.0F));
		this.part1.addChild(center3);
		
		this.center4 = new ModelRenderer(this, 0, 0);
		this.center4.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center4.addBox(-17.0f, -17.0f, -8.003f, 34, 34, 1, 0.0f);
		this.setRotateAngle(center4, 0.0F, 0.0F, (float) -Math.toRadians(30.0F));
		this.part1.addChild(center4);
		
		this.center5 = new ModelRenderer(this, 0, 0);
		this.center5.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center5.addBox(-17.0f, -17.0f, -8.002f, 34, 34, 1, 0.0f);
		this.setRotateAngle(center5, 0.0F, 0.0F, (float) -Math.toRadians(60.0F));
		this.part1.addChild(center5);
		
		this.center6 = new ModelRenderer(this, 0, 0);
		this.center6.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.center6.addBox(-17.0f, -17.0f, -8.001f, 34, 34, 1, 0.0f);
		this.setRotateAngle(center6, 0.0F, 0.0F, (float) -Math.toRadians(75.0F));
		this.part1.addChild(center6);
		
		this.nub = new ModelRenderer(this, 48, 48);
		this.nub.addBox(-1f, -1f, -8.007f, 2, 2, 2, 0.0f);
		
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
