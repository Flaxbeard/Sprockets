package flaxbeard.sprockets.client.render.tile;

import mcmultipart.client.multipart.MultipartSpecialRenderer;
import mcmultipart.multipart.IMultipart;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelClutch;
import flaxbeard.sprockets.multiparts.PartClutch;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class RenderPartClutch extends MultipartSpecialRenderer
{
	private static ModelClutch model = new ModelClutch(false, false);
	private static ModelClutch modelTop = new ModelClutch(true, false);
	private static ModelClutch modelBottom = new ModelClutch(false, true);
	private static ModelClutch modelTopBottom = new ModelClutch(true, true);
	private static String textureBase = "sprockets:textures/models/clutch";
	
	@Override
	public void renderMultipartAt(IMultipart te, double x, double y,
			double z, float partialTicks, int arg5)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		

		PartClutch clutch = (PartClutch) te;
		
		float rotate = 0.0f;
		
		MechanicalNetwork network = clutch.getNetwork();
		if (network != null)
		{
			rotate = ClientUtils.getRotation(clutch, partialTicks);
		}
		
		int facing = clutch.facing;
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
		
		ClientUtils.bindTexture(textureBase + "_" + SprocketsMultiparts.axle.subnames[clutch.getDamage()] + ".png");
		
		if (network != null && !clutch.getState())
		{
			//GL11.glRotatef(EIGHTH, 0.0F, 0.0F, 1.0F);
			rotate *= -1;
		}
		
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		
		boolean top = clutch.top;
		boolean bottom = clutch.bottom;

		
		ModelClutch temp = model;
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

		GL11.glPushMatrix();
		
		if (clutch != null && !clutch.engaged)
		{
			float upSpeed = 0F;
			float rot = 0F;
			float upMult = 1F;
			boolean flip = false;
			if (clutch.upConduit != null && clutch.upConduit.getNetwork() != null)
			{
				flip = (clutch.upConduit.isNegativeDirection() == clutch.isNegativeDirection()) ^ clutch.upConduit.getState();
				upSpeed = clutch.upConduit.getNetwork().getSpeed();
				rot = clutch.upConduit.getNetwork().rotation;
				upMult = clutch.upConduit.getMultiplier();
			}
	
			GL11.glRotatef(ClientUtils.getRotation(upSpeed, partialTicks, upMult, rot)  * (flip ? -1 : 1), 0.0F, 0.0F, 1.0F);
		}
		temp.renderTop(null, 0, 0, 0, 0, 0, .0625f, clutch == null ? false : clutch.engaged);
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		
		if (clutch != null && !clutch.engaged)
		{
			float downSpeed = 0F;
			float rot = 0F;
			float downMult = 1F;
			boolean flip = false;
			if (clutch.downConduit != null && clutch.downConduit.getNetwork() != null)
			{
				flip = (clutch.downConduit.isNegativeDirection() == clutch.isNegativeDirection()) ^ clutch.downConduit.getState();
				downSpeed = clutch.downConduit.getNetwork().getSpeed();
				rot = clutch.downConduit.getNetwork().rotation;
				downMult = clutch.downConduit.getMultiplier();
			}
	
			GL11.glRotatef(ClientUtils.getRotation(downSpeed, partialTicks, downMult, rot)  * (flip ? -1 : 1), 0.0F, 0.0F, 1.0F);
		}
		temp.renderBottom(null, 0, 0, 0, 0, 0, .0625f, clutch == null ? false : clutch.engaged);
		
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();

		
	}

}
