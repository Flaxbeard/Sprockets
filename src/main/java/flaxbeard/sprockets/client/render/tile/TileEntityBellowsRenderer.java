package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.tiles.TileEntityBellows;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelBellows;

public class TileEntityBellowsRenderer extends TileEntitySpecialRenderer
{

	private static ModelBellows model = new ModelBellows();
	private static String textureBase = "sprockets:textures/models/bellows";

		
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		TileEntityBellows bellows = (TileEntityBellows) te;
		
		float rotate = 0.0f;
		
		int facing = 3;
		
		if (bellows != null)
		{
			MechanicalNetwork network = bellows.getNetwork();
			if (network != null)
			{
				rotate = ClientUtils.getRotation(bellows, partialTicks);
			}
			if (network != null && bellows.getState())
			{
				rotate *= -1;
			}
			
			facing = bellows.facing;
		}
		else
		{
			GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);

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
	
		model.renderNoMove(null, 0, 0, 0, 0, 0, .0625f);
	
		GL11.glPushMatrix();
		float move = (float) ((5.05F / 16F) * (Math.sin(Math.toRadians(rotate)) - 1));
		GL11.glTranslatef(0, 0, -move);
		model.render(null, 0, 0, 0, 0, 0, .0625f);
		GL11.glPopMatrix();
		
		float squish = ((12F / 16F) + move) / (12F / 16F);


		float size = 2F + squish * 10F;
		GL11.glTranslatef(0.0F, 0.0F, -(size / 32F) + (6F / 16F));
		
		
		GL11.glScalef(1.0F, 1.0F, size / 12F);

		model.renderSquish(null, 0, 0, 0, 0, 0, .0625f);

		
		GL11.glPopMatrix();
	}

}
