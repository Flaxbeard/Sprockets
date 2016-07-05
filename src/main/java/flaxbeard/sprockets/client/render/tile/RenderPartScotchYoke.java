package flaxbeard.sprockets.client.render.tile;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import mcmultipart.multipart.IMultipart;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelScotchYoke;
import flaxbeard.sprockets.multiparts.PartScotchYoke;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class RenderPartScotchYoke extends MultipartSpecialRenderer
{
	private static ModelScotchYoke model = new ModelScotchYoke(false, false);
	private static ModelScotchYoke modelTop = new ModelScotchYoke(true, false);
	private static ModelScotchYoke modelBottom = new ModelScotchYoke(false, true);
	private static ModelScotchYoke modelTopBottom = new ModelScotchYoke(true, true);
	private static String textureBase = "sprockets:textures/models/yoke";
	
	@Override
	public void renderMultipartAt(IMultipart te, double x, double y,
			double z, float partialTicks, int arg5)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		

		PartScotchYoke yoke = (PartScotchYoke) te;
		
		float rotate = 0.0f;
		float backupRotate = 0.0F;
		
		MechanicalNetwork network = yoke.getNetwork();
		
		int test = yoke.facing - (yoke.facing % 2);

		if (network != null)
		{
			rotate = ClientUtils.getRotation(yoke, partialTicks);
			backupRotate = rotate;
		}
		
		int facing = yoke.facing;
		
		GL11.glPushMatrix();
		
		// START AXLE RENDER
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
		
		ClientUtils.bindTexture(textureBase + "_" + SprocketsMultiparts.axle.subnames[yoke.getDamage()] + ".png");
		
		if (network != null && !yoke.getState())
		{
			rotate *= -1;
		}
		
		
		boolean top = yoke.top;
		boolean bottom = yoke.bottom;

		
		ModelScotchYoke temp = model;
		if (top)
		{
			if (bottom)
			{
				temp = modelTopBottom;
			}
			else
			{
				temp = modelTop;
			}
		}
		else if (bottom)
		{
			temp = modelBottom;
		}
		
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		
		temp.render(null, 0, 0, 0, 0, 0, .0625f);
		
		// END AXLE RENDER
		GL11.glPopMatrix();
		
		// START NUB RENDER
		GL11.glPushMatrix();
		
		switch (facing)
		{
			case 0:
			case 1:
				GL11.glRotatef(90F, -1.0F, 0.0F, 0.0F);
				break;
			case 2:
			case 3:
				break;
			case 4:
			case 5:
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				break;
		}
		
		float nubRotate = -backupRotate;
		
		if (network != null && yoke.getState())
		{
			nubRotate *= -1;
		}
		
		if (network != null && yoke.facing % 2 == 0)
		{
			nubRotate *= -1;
		}
		
		
		switch (facing)
		{
			case 0:
			case 1:
				switch (yoke.yokeFacing)
				{
					case 2:
						nubRotate += 90F;
						break;
					case 3:
						nubRotate += 90F;
						break;
					case 4:
						nubRotate += 180F;
						break;
					case 5:
						nubRotate += 180F;
						break;
				}
				break;
			case 2:
			case 3:
				switch (yoke.yokeFacing)
				{
					case 0:
						nubRotate += 270F;
						break;
					case 1:
						nubRotate += 270F;
						break;
					case 4:
						nubRotate += 180F;
						break;
					case 5:
						nubRotate += 180F;
						break;
				}
				break;
			case 4:
			case 5:
				switch (yoke.yokeFacing)
				{
					case 0:
						nubRotate += 270F;
						break;
					case 1:
						nubRotate += 270F;
						break;
					case 3:
						break;
					case 2:
						break;

				}
				break;
		}
		GL11.glRotatef(nubRotate, 0.0F, 0.0F, 1.0F);	
		temp.renderNub(null, 0, 0, 0, 0, 0, .0625f, bottom);
				
		// END NUB RENDER
		GL11.glPopMatrix();

		// START YOKE RENDER
		GL11.glPushMatrix();
		
		if (network != null && (yoke.isNegativeDirection() ^ yoke.getState()))
		{
			backupRotate += 180;
		}
		
		switch (facing)
		{
			case 0:
			case 1:
				switch (yoke.yokeFacing)
				{
					case 2:
						GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
						break;
					case 3:
						GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(270F, 0.0F, 0.0F, 1.0F);
						backupRotate += 180F;
						break;
					case 4:
						GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
						break;
					case 5:
						GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
						backupRotate += 180F;
						break;
				}
				break;
			case 2:
			case 3:
				switch (yoke.yokeFacing)
				{
					case 0:
						GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
						break;
					case 1:
						GL11.glRotatef(270F, 0.0F, 0.0F, 1.0F);
						backupRotate += 180F;
						break;
					case 4:
						break;
					case 5:
						GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
						backupRotate += 180F;
						break;
				}
				break;
			case 4:
			case 5:
				switch (yoke.yokeFacing)
				{
					case 0:
						GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
						break;
					case 1:
						GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(270F, 0.0F, 0.0F, 1.0F);
						backupRotate += 180F;
						break;
					case 3:
						GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
						backupRotate += 180F;
						break;
					case 2:
						GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
						break;

				}
				break;
		}
		
		float move = (float) ((5.05F / 16F) * Math.sin(Math.toRadians(backupRotate)));
		GL11.glTranslatef(move, 0, 0);



		temp.renderArm(null, 0, 0, 0, 0, 0, .0625f, false);
		
		GL11.glPopMatrix();
		
		temp = null;
		
		GL11.glPopMatrix();

	}

}
