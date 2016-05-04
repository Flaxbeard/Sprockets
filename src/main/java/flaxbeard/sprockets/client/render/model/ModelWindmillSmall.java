package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWindmillSmall extends ModelBase
{

	public ModelRenderer nub;
	public ModelRenderer spokes1;
	public ModelRenderer spokes2;
	
	public ModelRenderer fin1;
	public ModelRenderer fin2;
	public ModelRenderer fin3;
	public ModelRenderer fin4;
	
	public ModelRenderer fin5;
	public ModelRenderer fin6;
	public ModelRenderer fin7;
	public ModelRenderer fin8;
	
	public ModelRenderer housing;
	public ModelRenderer housing2;
	
	public ModelWindmillSmall(boolean reversed)
	{
		this.textureHeight = 64;
		this.textureWidth = 64;
		this.nub = new ModelRenderer(this, 0, 0);
		this.nub.addBox(-1.5f, -1.5f, -8.0F, 3, 3, 19, 0.0f);
		
		this.spokes1 = new ModelRenderer(this, 0, 0);
		this.spokes1.addBox(-4.5f, -4.5f, 7.7F, 9, 9, 0, 0.0f);
		this.setRotateAngle(spokes1, 0.0F, 0F, (float) Math.toRadians(22F));
		this.nub.addChild(spokes1);
		
		this.spokes2 = new ModelRenderer(this, 0, 0);
		this.spokes2.addBox(-4.5f, -4.5f, 7.7F, 9, 9, 0, 0.0f);
		this.setRotateAngle(spokes2, 0.0F, 0F, (float) Math.toRadians(67F));
		this.nub.addChild(spokes2);
		
		this.housing = new ModelRenderer(this, 22, 31);
		this.housing.addBox(-2.5f, -8f, -3F, 5, 16, 6, 0.0f);

		this.housing2 = new ModelRenderer(this, 0, 31);
		this.housing2.addBox(-2.5f, -8f, -3F, 5, 11, 6, 0.0f);
		
		if (reversed)
		{
			this.fin1 = new ModelRenderer(this, 0, 22);
			this.fin1.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin1.addBox(-2.8F, 3F, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin1, 0.0F, (float) Math.toRadians(22.5F), 0F);
			this.nub.addChild(fin1);
			
			this.fin2 = new ModelRenderer(this, 0, 22);
			this.fin2.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin2.addBox(-0.2F, -11F, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin2, 0.0F, (float) Math.toRadians(-22.5F), 0F);
			this.nub.addChild(fin2);
			
			this.fin3 = new ModelRenderer(this, 8, 22);
			this.fin3.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin3.addBox(3F, 0.2F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin3,  (float) Math.toRadians(22.5F), 0.0F, 0F);
			this.nub.addChild(fin3);
			
			this.fin4 = new ModelRenderer(this, 8, 22);
			this.fin4.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin4.addBox(-11F, -2.8F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin4,  (float) Math.toRadians(-22.5F), 0.0F, 0F);
			this.nub.addChild(fin4);
			
			this.fin5 = new ModelRenderer(this, 0, 22);
			this.fin5.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin5.addBox(-2.8F, 3F, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin5, 0.0F, (float) Math.toRadians(22.5F), (float) Math.toRadians(45.0F));
			this.nub.addChild(fin5);
			
			this.fin6 = new ModelRenderer(this, 0, 22);
			this.fin6.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin6.addBox(-0.2F, -11F, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin6, 0.0F, (float) Math.toRadians(-22.5F), (float) Math.toRadians(45.0F));
			this.nub.addChild(fin6);
			
			this.fin7 = new ModelRenderer(this, 8, 22);
			this.fin7.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin7.addBox(3F, 0.2F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin7,  (float) Math.toRadians(22.5F), 0.0F, (float) Math.toRadians(45.0F));
			this.nub.addChild(fin7);
			
			this.fin8 = new ModelRenderer(this, 8, 22);
			this.fin8.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin8.addBox(-11F, -2.8F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin8,  (float) Math.toRadians(-22.5F), 0.0F, (float) Math.toRadians(45.0F));
			this.nub.addChild(fin8);
			
			this.setRotateAngle(spokes1, 0.0F, 0F, (float) Math.toRadians(-22F));
			
			this.setRotateAngle(spokes2, 0.0F, 0F, (float) Math.toRadians(-67F));
		}
		else
		{
			this.fin1 = new ModelRenderer(this, 0, 22);
			this.fin1.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin1.addBox(-2.8F, -11f, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin1, 0.0F, (float) Math.toRadians(22.5F), 0F);
			this.nub.addChild(fin1);
			
			this.fin2 = new ModelRenderer(this, 0, 22);
			this.fin2.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin2.addBox(-0.2F, 3F, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin2, 0.0F, (float) Math.toRadians(-22.5F), 0F);
			this.nub.addChild(fin2);
			
			this.fin3 = new ModelRenderer(this, 8, 22);
			this.fin3.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin3.addBox(-11f, 0.2F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin3,  (float) Math.toRadians(22.5F), 0.0F, 0F);
			this.nub.addChild(fin3);
			
			this.fin4 = new ModelRenderer(this, 8, 22);
			this.fin4.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin4.addBox(3F, -2.8F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin4,  (float) Math.toRadians(-22.5F), 0.0F, 0F);
			this.nub.addChild(fin4);
			
			this.fin5 = new ModelRenderer(this, 0, 22);
			this.fin5.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin5.addBox(-2.8F, -11f, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin5, 0.0F, (float) Math.toRadians(22.5F), (float) Math.toRadians(45.0F));
			this.nub.addChild(fin5);
			
			this.fin6 = new ModelRenderer(this, 0, 22);
			this.fin6.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin6.addBox(-0.2F, 3F, 8.00F, 3, 8, 1, 0.0F);
			this.setRotateAngle(fin6, 0.0F, (float) Math.toRadians(-22.5F), (float) Math.toRadians(45.0F));
			this.nub.addChild(fin6);
			
			this.fin7 = new ModelRenderer(this, 8, 22);
			this.fin7.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin7.addBox(-11f, 0.2F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin7,  (float) Math.toRadians(22.5F), 0.0F, (float) Math.toRadians(45.0F));
			this.nub.addChild(fin7);
			
			this.fin8 = new ModelRenderer(this, 8, 22);
			this.fin8.setRotationPoint(0.0f, 0.0f, 0.0f);
			this.fin8.addBox(3F, -2.8F, 8.00F, 8, 3, 1, 0.0F);
			this.setRotateAngle(fin8,  (float) Math.toRadians(-22.5F), 0.0F, (float) Math.toRadians(45.0F));
			this.nub.addChild(fin8);
		}
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.nub.render(f5);
	}
	
	public void renderHousing(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.housing2.render(f5);
	}
	
	public void renderHousingFull(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.housing.render(f5);
	}
	
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
