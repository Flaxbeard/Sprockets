package flaxbeard.sprockets.client.render.tile;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import mcmultipart.multipart.IMultipart;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelAxle;
import flaxbeard.sprockets.client.render.model.ModelFrictionPad;
import flaxbeard.sprockets.multiparts.PartFlywheelFrictionPad;

public class RenderPartFlywheelFrictionPad extends MultipartSpecialRenderer
{
	private static ModelFrictionPad model = new ModelFrictionPad();
	private static String textureBase = "sprockets:textures/models/sprocket_wooden";

	private static final float EIGHTH = (float) 22.5F;
	
	@Override
	public void renderMultipartAt(IMultipart te, double x, double y,
			double z, float partialTicks, int arg5)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		

		PartFlywheelFrictionPad pad = (PartFlywheelFrictionPad) te;
		
		float rotate = 0.0f;
		
		MechanicalNetwork network = pad.getNetwork();
		if (network != null)
		{
			rotate = ClientUtils.getRotation(pad, partialTicks);
		}
		
		int facing = pad.facing;
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
		
		ClientUtils.bindTexture(textureBase + ".png");
		
		if (network != null && !pad.getState())
		{
			//GL11.glRotatef(EIGHTH, 0.0F, 0.0F, 1.0F);
			rotate *= -1;
		}
		
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		
		

		model.render(null, 0, 0, 0, 0, 0, .0625f);
		
		
		GL11.glPopMatrix();
		
	}

}
