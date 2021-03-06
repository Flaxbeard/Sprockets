package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelWindmillSmall;
import flaxbeard.sprockets.lib.LibConstants;

public class TileEntityWindmillSmallRenderer extends TileEntitySpecialRenderer
{

	private static ModelWindmillSmall model = new ModelWindmillSmall(false);
	private static ModelWindmillSmall modelReverse = new ModelWindmillSmall(true);
	private static String textureBase = "sprockets:textures/models/windmill";

		
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		TileEntityWindmillSmall windmill = (TileEntityWindmillSmall) te;
		
		float rotate = 0.0f;
		
		int facing = 3;
		
		if (windmill != null)
		{
			MechanicalNetwork network = windmill.getNetwork();
			if (network != null)
			{
				rotate = ClientUtils.getRotation(windmill, partialTicks);
			}
			if (network != null && windmill.getState())
			{
				rotate *= -1;
			}
			
			facing = windmill.facing;
		}
		else
		{
			GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);

			GL11.glTranslatef(0.0F, 0.0F, -0.4F);
		}
		
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

		ModelWindmillSmall temp = model;
		
		if (windmill != null && !windmill.directionFlipped)
		{
			temp = modelReverse;
		}
		
		if (windmill != null && windmill.connectedToTop())
		{
			temp.renderHousingFull(null, 0, 0, 0, 0, 0, .0625f);
		}
		else
		{
			temp.renderHousing(null, 0, 0, 0, 0, 0, .0625f);
		}

		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
	

		temp.render(null, 0, 0, 0, 0, 0, .0625f);
		
		temp = null;

		
		GL11.glPopMatrix();
	}

}
