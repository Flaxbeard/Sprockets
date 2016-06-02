package flaxbeard.sprockets.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelStampMill extends ModelBase
{

	public ModelRenderer nub;
	public ModelRenderer nub2;
	public ModelRenderer nub3;
	
	public ModelRenderer gear11;
	public ModelRenderer gear12;
	public ModelRenderer gear13;
	public ModelRenderer gear14;
	
	public ModelRenderer gear21;
	public ModelRenderer gear22;
	public ModelRenderer gear23;
	public ModelRenderer gear24;

	public ModelRenderer cam;
	
	public ModelRenderer sideL;
	public ModelRenderer sideR;
	public ModelRenderer sideLS;
	public ModelRenderer sideRS;
	public ModelRenderer top;
	public ModelRenderer middle;
	
	public ModelRenderer sideLB;
	public ModelRenderer sideRB;

	public ModelRenderer hammer;
	public ModelRenderer hammerBox;
	public ModelRenderer hammerHead;
	
	public ModelRenderer hammerHolder;


	public ModelStampMill()
	{
		this.textureHeight = 64;
		this.textureWidth = 128;
		this.nub = new ModelRenderer(this, 0, 0);
		this.nub.addBox(-1.5F, -1.5f, -24F, 3, 3, 8, 0.0f);
		
		this.nub2 = new ModelRenderer(this, 0, 0);
		this.nub2.addBox(-1.5F, -1.5f, 16F, 3, 3, 8, 0.0f);
		this.nub.addChild(nub2);
		
		
		this.nub3 = new ModelRenderer(this, 0, 0);
		this.nub3.addBox(-1F, -1f, -17F, 2, 2, 34, 0.0f);
		
		
		
		this.gear11 = new ModelRenderer(this, 14, 0);
		this.gear11.addBox(-4F, -1F, 15F, 8, 2, 1, 0.0f);
		this.nub.addChild(gear11);
		
		this.gear12 = new ModelRenderer(this, 0, 11);
		this.gear12.addBox(-1F, -4F, 15F, 2, 8, 1, 0.0f);
		this.nub.addChild(gear12);

		this.gear13 = new ModelRenderer(this, 14, 0);
		this.gear13.addBox(-4F, -1F, 15F, 8, 2, 1, 0.0f);
		this.nub3.addChild(gear13);
		
		this.gear14 = new ModelRenderer(this, 0, 11);
		this.gear14.addBox(-1F, -4F, 15F, 2, 8, 1, 0.0f);
		this.nub3.addChild(gear14);
		
		this.gear21 = new ModelRenderer(this, 14, 0);
		this.gear21.addBox(-4F, -1F, -16F, 8, 2, 1, 0.0f);
		this.nub.addChild(gear21);
		
		this.gear22 = new ModelRenderer(this, 0, 11);
		this.gear22.addBox(-1F, -4F, -16F, 2, 8, 1, 0.0f);
		this.nub.addChild(gear22);
		
		this.gear23 = new ModelRenderer(this, 14, 0);
		this.gear23.addBox(-4F, -1F, -16F, 8, 2, 1, 0.0f);
		this.nub3.addChild(gear23);
		
		this.gear24 = new ModelRenderer(this, 0, 11);
		this.gear24.addBox(-1F, -4F, -16F, 2, 8, 1, 0.0f);
		this.nub3.addChild(gear24);
		
		

		this.cam = new ModelRenderer(this, 38, 0);
		this.cam.addBox(-1.01F, -2f, -4.5F, 2, 8, 2, 0.0f);
		this.nub3.addChild(cam);
		
		this.top = new ModelRenderer(this, 52, 30);
		this.top.addBox(-3F, 15.99f, -15.99F, 6, 2, 32, 0.0f);
		
		this.hammer = new ModelRenderer(this, 46, 0);
		this.hammer.addBox(-1.5F, -8f, -1.5F, 3, 27, 3, 0.0f);
		
		this.hammerBox = new ModelRenderer(this, 58, 0);
		this.hammerBox.addBox(-1F, -1f, -5.5F, 2, 2, 4, 0.0f);
		this.hammer.addChild(hammerBox);
		
		this.hammerHolder = new ModelRenderer(this, 70, 0);
		this.hammerHolder.addBox(-2F, 16f, -2F, 4, 1, 4, 0.0f);
		this.hammer.addChild(hammerHolder);
		
		this.hammerHead = new ModelRenderer(this, 58, 6);
		this.hammerHead.addBox(-3F, -10f, -3F, 6, 6, 6, 0.0f);
		this.hammer.addChild(hammerHead);
		
		this.middle = new ModelRenderer(this, 52, 30);
		this.middle.addBox(-3F, 12f, -15.99F, 6, 2, 32, 0.0f);
		this.top.addChild(middle);
		
		this.sideL = new ModelRenderer(this, 112, 0);
		this.sideL.addBox(-2F, -24f, -14.99F, 4, 48, 4, 0.0f);
		this.top.addChild(sideL);

		this.sideR = new ModelRenderer(this, 112, 0);
		this.sideR.addBox(-2F, -24f, 10.99F, 4, 48, 4, 0.0f);
		this.top.addChild(sideR);
		
		this.sideLB = new ModelRenderer(this, 0, 46);
		this.sideLB.addBox(-8F, -24f, -17F, 16, 10, 8, 0.0f);
		this.top.addChild(sideLB);

		this.sideRB = new ModelRenderer(this, 0, 46);
		this.sideRB.addBox(-8F, -24f, 9F, 16, 10, 8, 0.0f);
		this.top.addChild(sideRB);
		
		this.sideLS = new ModelRenderer(this, 0, 40);
		this.sideLS.addBox(-8F, 2f, -13.98F, 16, 4, 2, 0.0f);
		this.top.addChild(sideLS);

		this.sideRS = new ModelRenderer(this, 0, 40);
		this.sideRS.addBox(-8F, 2f, 11.98F, 16, 4, 2, 0.0f);
		this.top.addChild(sideRS);



	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.nub.render(f5);
	}
	
	public void renderHammer(float f5)
	{
		this.hammer.render(f5);
	}
	
	public void renderNoRotate(float f5, boolean left, boolean right)
	{
		this.top.render(f5);
	}
	
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void renderCam(float f)
	{
		this.nub3.render(f);
		
	}
}
