package flaxbeard.sprockets.common.handler;

import java.util.HashSet;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class CrouchHandler
{
	private static HashSet<String> sneakers = new HashSet<String>();
	
	@SubscribeEvent
	public void handleCrouch2(PlayerTickEvent event)
	{
		if (event.phase == Phase.START)
		{
			EntityPlayer player = (EntityPlayer) event.player;

			if (!player.isSneaking())
			{
				if (sneakers.contains(player.getName()))
				{
					if (!player.isElytraFlying() && !player.isPlayerSleeping())
					{
						float h = 1.8F;
						AxisAlignedBB axisalignedbb = player.getEntityBoundingBox();
						AxisAlignedBB newAxis = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)player.width, axisalignedbb.minY + (double) h, axisalignedbb.minZ + (double)player.width);

						
						if (player.worldObj.collidesWithAnyBlock(newAxis))
						{
							player.setSneaking(true);
							if (player instanceof EntityPlayerSP)
							{
								((EntityPlayerSP) player).movementInput.sneak = true;
							}
							return;
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void handleCrouch(PlayerTickEvent event)
	{
		if (event.phase == Phase.END)
		{
			EntityPlayer player = (EntityPlayer) event.player;
			
			if (!player.isElytraFlying() && !player.isPlayerSleeping() && player.isSneaking())
			{
				player.height = 1.5F;
				AxisAlignedBB axisalignedbb = player.getEntityBoundingBox();
				player.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)player.width, axisalignedbb.minY + (double)player.height, axisalignedbb.minZ + (double)player.width));
				sneakers.add(player.getName());
			}
			else if (!player.isSneaking())
			{
				if (sneakers.contains(player.getName()))
				{
					if (!player.isElytraFlying() && !player.isPlayerSleeping())
					{
						float h = 1.8F;
						AxisAlignedBB axisalignedbb = player.getEntityBoundingBox();
						AxisAlignedBB newAxis = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)player.width, axisalignedbb.minY + (double) h, axisalignedbb.minZ + (double)player.width);

						
						if (player.worldObj.collidesWithAnyBlock(newAxis))
						{
							player.setSneaking(true);
							if (player instanceof EntityPlayerSP)
							{
								((EntityPlayerSP) player).movementInput.sneak = true;
							}
							return;
						}
					}
					
					sneakers.remove(player.getName());
					System.out.println("B");
					
				}
			}
		}
	}
	
	@SubscribeEvent
	public void handleRender(RenderPlayerEvent.Pre event)
	{
		if (sneakers.contains(event.getEntityPlayer().getName()))
		{
		}
		event.getEntityPlayer().setSneaking(true);

	}
	
}
