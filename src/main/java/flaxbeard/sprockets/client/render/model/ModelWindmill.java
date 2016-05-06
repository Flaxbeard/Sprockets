package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWindmill extends ModelBase
{

	public ModelRenderer nub;
	public ModelRenderer spokes1;
	public ModelRenderer spokes2;
	
	public ModelRenderer fin1;
	public ModelRenderer fin2;
	public ModelRenderer fin3;
	public ModelRenderer fin4;
	
	public ModelRenderer blade1;
	public ModelRenderer blade2;
	public ModelRenderer blade3;
	public ModelRenderer blade4;
	
	
	public ModelWindmill(boolean reversed)
	{
		this.textureHeight = 64;
		this.textureWidth = 64;
		this.nub = new ModelRenderer(this, 0, 0);
		this.nub.addBox(-1.5F, -1.5f, -8F, 3, 3, 12, 0.0f);
		
		this.fin1 = new ModelRenderer(this, 52, 0);
		this.fin1.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.fin1.addBox(-1, -54 - 1, -1.00F, 3, 54, 3, 0.0F);
		this.setRotateAngle(fin1, 0.0F, (float) Math.toRadians(10F), 0F);
		this.nub.addChild(fin1);
		
		this.blade1 = new ModelRenderer(this, 0, 15);
		this.blade1.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.blade1.addBox(2, -54 - 1, -0.50F, 16, 44, 2, 0.0F);
		this.fin1.addChild(blade1);
		
		this.fin2 = new ModelRenderer(this, 52, 0);
		this.fin2.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.fin2.addBox(-1, -54 - 1, -1.00F, 3, 54, 3, 0.0F);
		this.setRotateAngle(fin2, 0F, (float) Math.toRadians(10F), (float) Math.toRadians(90F));
		this.nub.addChild(fin2);
		
		this.blade2 = new ModelRenderer(this, 0, 15);
		this.blade2.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.blade2.addBox(2, -54 - 1, -0.50F, 16, 44, 2, 0.0F);
		this.fin2.addChild(blade2);
		
		this.fin3 = new ModelRenderer(this, 52, 0);
		this.fin3.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.fin3.addBox(-1, -54 - 1, -1.00F, 3, 54, 3, 0.0F);
		this.setRotateAngle(fin3, 0f, (float) Math.toRadians(10F), (float) Math.toRadians(180F));
		this.nub.addChild(fin3);
		
		this.blade3 = new ModelRenderer(this, 0, 15);
		this.blade3.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.blade3.addBox(2, -54 - 1, -0.50F, 16, 44, 2, 0.0F);
		this.fin3.addChild(blade3);
		
		this.fin4 = new ModelRenderer(this, 52, 0);
		this.fin4.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.fin4.addBox(-1, -54 - 1, -1.00F, 3, 54, 3, 0.0F);
		this.setRotateAngle(fin4, 0F, (float) Math.toRadians(10F), (float) Math.toRadians(270F));
		this.nub.addChild(fin4);
		
		this.blade4 = new ModelRenderer(this, 0, 15);
		this.blade4.setRotationPoint(0.0f, 0.0f, 0.0f);
		this.blade4.addBox(2, -54 - 1, -0.50F, 16, 44, 2, 0.0F);
		this.fin4.addChild(blade4);
		
		
		if (reversed)
		{
			this.setRotateAngle(fin1, 0F, (float) Math.toRadians(170F), 0F);
			this.setRotateAngle(fin2, 0F, (float) Math.toRadians(170F), (float) Math.toRadians(90F));
			this.setRotateAngle(fin3, 0F, (float) Math.toRadians(170F), (float) Math.toRadians(180F));
			this.setRotateAngle(fin4, 0F, (float) Math.toRadians(170F), (float) Math.toRadians(270F));
		}
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
