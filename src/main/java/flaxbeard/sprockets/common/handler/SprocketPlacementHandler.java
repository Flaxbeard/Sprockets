package flaxbeard.sprockets.common.handler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.multiparts.items.ItemSprocketMultipart;

public class SprocketPlacementHandler
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void handleRender(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer != null)
		{
			EntityPlayerSP player = mc.thePlayer;
			float partialTicks = event.getPartialTicks();
			ItemStack held = player.getHeldItemMainhand();
			
			if (held != null && held.getItem() != null)
			{
				if (held.getItem() instanceof ItemSprocketMultipart && player.isSneaking())
				{
					ItemSprocketMultipart item = (ItemSprocketMultipart) held.getItem();
					
					if (item.hasBoundingBox())
					{
						RayTraceResult mop = mc.objectMouseOver;
					
						
						if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK)
						{	
							BlockPos blockpos = mop.getBlockPos();
							IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);
							if (iblockstate.getMaterial() != Material.AIR && mc.theWorld.getWorldBorder().contains(blockpos))
							{
								EnumFacing facing = mop.sideHit;
								Vec3i offset = facing.getDirectionVec();
								double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
								double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
								double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
								
								GlStateManager.enableBlend();
								GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
								GlStateManager.color(1.0F, 1.0F, 1.0F, 0.4F);
								GlStateManager.glLineWidth(4.0F);
								GlStateManager.disableTexture2D();
								GlStateManager.depthMask(false);
					            
								/*mc.renderGlobal.drawSelectionBoundingBox(item.boundingBox(mc.theWorld, blockpos, facing.getOpposite(), mop.hitVec.subtract(blockpos.getX(), blockpos.getY(), blockpos.getZ())).contract(0.0020000000949949026D)
										.offset(blockpos)
										.offset(-d0, -d1, -d2)
										.offset(offset.getX(), offset.getY(), offset.getZ()));*/ //TODO
								
								GlStateManager.depthMask(true);
								GlStateManager.enableTexture2D();
								GlStateManager.disableBlend();
							}
						}
					}
				}
			}
		}
	}
}
