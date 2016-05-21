package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.tiles.TileEntityTorsionSpring;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelSpring;

public class TileEntityTorsionSpringRenderer extends TileEntitySpecialRenderer
{

	private static ModelSpring model = new ModelSpring();
	private static String textureBase = "sprockets:textures/models/spring";

		
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		
		TileEntityTorsionSpring spring = (TileEntityTorsionSpring) te;
		
		float rotate = 0.0f;
		float size = 1;

		int facing = 3;
		
		if (spring != null)
		{
			
			facing = spring.facing;
			
			MechanicalNetwork network = spring.getNetwork();
			if (network != null)
			{
				rotate = ClientUtils.getRotation(spring, partialTicks);
			}
			if (network != null && spring.getState())
			{
				rotate *= -1;
			}
			
			size = 1 - (spring.progress / spring.getMax());
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
	

		GL11.glPushMatrix();
		GL11.glScalef(.5f * size + .5f, .5f * size + .5f, 1F);
		model.render(null, 0, 0, 0, 0, 0, .0625f);
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		model.renderFrontConnector(null, 0, 0, 0, 0, 0, .0625f);
		GL11.glPopMatrix();
		
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		model.renderNub(null, 0, 0, 0, 0, 0, .0625f);

		
		GL11.glPopMatrix();
	}

}
