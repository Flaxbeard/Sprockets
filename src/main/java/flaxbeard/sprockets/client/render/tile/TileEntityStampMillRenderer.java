package flaxbeard.sprockets.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.tiles.TileEntityStampMill;
import flaxbeard.sprockets.client.ClientUtils;
import flaxbeard.sprockets.client.render.model.ModelStampMill;

public class TileEntityStampMillRenderer extends TileEntitySpecialRenderer
{

	private static ModelStampMill model = new ModelStampMill();
	private static String textureBase = "sprockets:textures/models/stampMill";

		
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x+.5, y+.5, z+.5);
		TileEntityStampMill stampMill = (TileEntityStampMill) te;
		
		
		float rotate = 0.0f;
		
		int facing = 3;
		
		if (stampMill != null)
		{
			MechanicalNetwork network = stampMill.getNetwork();
			if (network != null)
			{
				rotate = ClientUtils.getRotation(stampMill, partialTicks);
			}
			if (network != null && stampMill.getState())
			{
				rotate *= -1;
			}

				rotate += 90F * ((stampMill.getPos().getX() + stampMill.getPos().getZ()) % 4);
			
			
			facing = stampMill.facing;
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

		model.renderNoRotate(.0625f,
				true,
				true);

		
		ClientUtils.bindTexture(textureBase + ".png");
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 2.0F / 16F, 0.0F);
		float modRot = Math.abs(rotate);

		if (modRot % 360 > 100 && modRot % 360 < 360)
		{

			modRot = modRot - 70;
			GL11.glTranslated(0.0F, Math.max(0F, Math.sin(Math.toRadians(modRot - 10) * -1) * 7.7F / 16F), 0.0F);
			 
			if (stampMill != null)
			{
				stampMill.ticksFalling = -1;
			}
		}
		else
		{
			if (stampMill != null)
			{
				if (stampMill.ticksFalling == -1)
				{
					stampMill.ticksFalling = 7;
				}
				else if (stampMill.ticksFalling > 0)
				{
					stampMill.ticksFalling--;
					
					if (stampMill.ticksFalling == 0)
					{
						stampMill.doEffect = true;
					}
				}

				
				
				GL11.glTranslated(0.0F, (stampMill.ticksFalling / 7F) * 7.7F / 16F, 0.0F);

			}
		}
		model.renderHammer(.0625f);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glRotatef(rotate, 0.0F, 0.0F, 1.0F);
		model.render(null, 0, 0, 0, 0, 0, .0625f);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		if (stampMill != null && stampMill.getNetwork() != null && (stampMill.getNetwork().getSpeedForConduit(stampMill) < 0 ^ stampMill.getState()))
		{
			GL11.glTranslatef(-6.0F / 16F, 4.0F / 16F, 0.0F);
			GL11.glRotatef(-rotate - 22F, 0.0F, 0.0F, 1.0F);

		}
		else
		{
			GL11.glTranslatef(6.0F / 16F, 4.0F / 16F, 0.0F);
			GL11.glRotatef(-rotate + 22F, 0.0F, 0.0F, 1.0F);

		}
		model.renderCam(.0625f);
		GL11.glPopMatrix();


		
		GL11.glPopMatrix();
	}

}
