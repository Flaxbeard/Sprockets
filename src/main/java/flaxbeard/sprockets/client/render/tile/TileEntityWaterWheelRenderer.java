package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheel;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelWaterWheel;

public class TileEntityWaterWheelRenderer extends TileEntitySpecialRenderer
{

	private static ModelWaterWheel model = new ModelWaterWheel();
	private static String textureBase = "sprockets:textures/models/waterwheel";

		
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		TileEntityWaterWheel waterWheel = (TileEntityWaterWheel) te;
		
		float rotate = 0.0f;
		
		int facing = 3;
		
		if (waterWheel != null)
		{
			MechanicalNetwork network = waterWheel.getNetwork();
			if (network != null)
			{
				rotate = ClientUtils.getRotation(waterWheel, partialTicks);
			}
			if (network != null && waterWheel.getState())
			{
				rotate *= -1;
			}
			
			facing = waterWheel.facing;
		}
		else
		{
			GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
			
			GL11.glScalef(0.28F, 0.28F, 0.28F);
			GL11.glTranslatef(0.0F, 0.0F, -0.2F);

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
			case 3:
				break;
			case 4:
			case 5:
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				break;
		}
		
		ClientUtils.bindTexture(textureBase + ".png");

		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
	

		model.render(null, 0, 0, 0, 0, 0, .0625f);
		

		model.renderLeft(.0625f, waterWheel == null || !waterWheel.connLeft);


		model.renderRight(.0625f, waterWheel == null || !waterWheel.connRight);
		
		
		GL11.glPopMatrix();
	}

}
