package flaxbeard.sprockets.client.render.tile;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import mcmultipart.multipart.IMultipart;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelBigSprocket;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.PartBigSprocketCenter;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class RenderPartBigSprocket extends MultipartSpecialRenderer
{
	private static ModelBigSprocket model = new ModelBigSprocket();
	private static String textureBase = "sprockets:textures/models/sprocket";

	private static final float EIGHTH = (float) 22.5F;
	
	@Override
	public void renderMultipartAt(IMultipart te, double x, double y,
			double z, float partialTicks, int arg5)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		PartBigSprocketCenter sprocket = (PartBigSprocketCenter) te;
		
		float rotate = 0.0f;
		
		MechanicalNetwork network = sprocket.getNetwork();
		if (network != null)
		{
			rotate = (network.rotation + network.speed * partialTicks) * LibConstants.RENDER_ROTATION_SPEED_MULTIPLIER;
		}
		
		int facing = sprocket.facing;
		switch (facing)
		{
			case 0:
				GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
				break;
			case 1:
				GL11.glRotatef(90F, -1.0F, 0.0F, 0.0F);
				break;
			case 2:
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				break;
			case 3:
				break;
			case 4:
				GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
				break;
			case 5:
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				break;
		}
		
		ClientUtils.bindTexture(textureBase + "_" + SprocketsMultiparts.sprocket.subnames[sprocket.getDamage()] + ".png");
		
		model.renderNub(null, 0, 0, 0, 0, 0, .0625f);
		
		if (network != null && !sprocket.getState())
		{
			GL11.glRotatef(EIGHTH, 0.0F, 0.0F, 1.0F);
			rotate *= -1;
		}

		
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
	

		model.render(null, 0, 0, 0, 0, 0, .0625f);
		
		GL11.glPopMatrix();
		
	}

}
