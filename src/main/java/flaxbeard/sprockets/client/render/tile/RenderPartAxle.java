package flaxbeard.sprockets.client.render.tile;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import mcmultipart.multipart.IMultipart;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelAxle;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class RenderPartAxle extends MultipartSpecialRenderer
{
	private static ModelAxle model = new ModelAxle(false, false);
	private static ModelAxle modelTop = new ModelAxle(true, false);
	private static ModelAxle modelBottom = new ModelAxle(false, true);
	private static ModelAxle modelTopBottom = new ModelAxle(true, true);
	private static String textureBase = "sprockets:textures/models/axle";

	private static final float EIGHTH = (float) 22.5F;
	
	@Override
	public void renderMultipartAt(IMultipart te, double x, double y,
			double z, float partialTicks, int arg5)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		

		PartAxle axle = (PartAxle) te;
		
		float rotate = 0.0f;
		
		MechanicalNetwork network = axle.getNetwork();
		if (network != null)
		{
			rotate = ClientUtils.getRotation(axle, partialTicks);
		}
		
		int facing = axle.facing;
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
		
		ClientUtils.bindTexture(textureBase + "_" + SprocketsMultiparts.axle.subnames[axle.getDamage()] + ".png");
		
		if (network != null && !axle.getState())
		{
			//GL11.glRotatef(EIGHTH, 0.0F, 0.0F, 1.0F);
			rotate *= -1;
		}
		
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		
		boolean top = axle.top;
		boolean bottom = axle.bottom;
		
		if (axle.isNegativeDirection())
		{
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
			boolean temp = top;
			top = bottom;
			bottom = temp;
		}
		
		
		
		if (top)
		{
			if (bottom)
			{
				modelTopBottom.render(null, 0, 0, 0, 0, 0, .0625f);
			}
			else
			{
				modelTop.render(null, 0, 0, 0, 0, 0, .0625f);
			}
		}
		else if (bottom)
		{
			modelBottom.render(null, 0, 0, 0, 0, 0, .0625f);
		}
		else
		{
			model.render(null, 0, 0, 0, 0, 0, .0625f);
		}
		
		GL11.glPopMatrix();
		
	}

}
