package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityFrictionHeater;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelFrictionHeater;
import flaxbeard.sprockets.lib.LibConstants;

public class TileEntityFrictionHeaterRenderer extends TileEntitySpecialRenderer
{

	private static ModelFrictionHeater model = new ModelFrictionHeater();
	private static String textureBase = "sprockets:textures/models/frictionHeater";

		
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		TileEntityFrictionHeater heater = (TileEntityFrictionHeater) te;
		
		float rotate = 0.0f;
		
		int facing = 3;
		
		if (heater != null)
		{
			MechanicalNetwork network = heater.getNetwork();
			if (network != null)
			{
				rotate = ClientUtils.getRotation(heater, partialTicks);
			}
			if (network != null && heater.getState())
			{
				rotate *= -1;
			}
			
			facing = heater.facing;
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
	
		model.renderNoRotate(null, 0, 0, 0, 0, 0, .0625f);

		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
	

		model.render(null, 0, 0, 0, 0, 0, .0625f);
		
		
		GL11.glPopMatrix();
	}

}
