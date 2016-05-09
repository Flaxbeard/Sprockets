package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.tiles.TileEntityMillstone;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelMillstone;
import flaxbeard.sprockets.lib.LibConstants;

public class TileEntityMillstoneRenderer extends TileEntitySpecialRenderer
{

	private static ModelMillstone model = new ModelMillstone();
	private static String textureBase = "sprockets:textures/models/millstone";

	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		TileEntityMillstone millstone = (TileEntityMillstone) te;

		
		if (millstone != null)
		{
			float rotate = 0.0f;
			MechanicalNetwork network = millstone.getNetwork();
			if (network != null)
			{
				rotate = (network.rotation + network.getSpeed() * partialTicks) * LibConstants.RENDER_ROTATION_SPEED_MULTIPLIER;
			}
			if (network != null && millstone.getState())
			{
				rotate *= -1;
			}
			
			GL11.glRotatef(-rotate, 0.0F, 1.0F, 0.0F);
		}
		ClientUtils.bindTexture(textureBase + ".png");
		model.render(null, 0, 0, 0, 0, 0, 0.0625f);


		
		GL11.glPopMatrix();
	}

}
