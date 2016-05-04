package flaxbeard.sprockets.common.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.sprockets.api.IWrench;
import flaxbeard.sprockets.api.IWrenchable;

public class WrenchHandler
{
	@SubscribeEvent
	public void handle(PlayerInteractEvent.RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		
		if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof IWrench)
		{
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();	
			TileEntity te = world.getTileEntity(pos);
			
			if (block instanceof IWrenchable)
			{
				((IWrenchable) block).wrench(player, world, pos, state, event.getFace());
			}
			if (te != null && te instanceof IWrenchable)
			{
				((IWrenchable) te).wrench(player, world, pos, state, event.getFace());
			}
		}
		
	}
	
}
