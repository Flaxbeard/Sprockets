package flaxbeard.sprockets.common.handler;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.multipart.IMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.IMechanicalConsumer;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.api.tool.IWrench;
import flaxbeard.sprockets.api.tool.IWrenchable;
import flaxbeard.sprockets.items.ItemGyrometer;

public class WrenchHandler
{
	@SubscribeEvent
	public void handle(PlayerInteractEvent.RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = world.getBlockState(pos);

		ItemStack itemStack = event.getItemStack();
		
		EnumFacing face = event.getFace();
		
		this.handle(player, world, pos, state, face, itemStack, null);
		
		
	}
	
	public static void handle(EntityPlayer player, World world, BlockPos pos, IBlockState state, EnumFacing face, ItemStack itemStack, IMultipart part)
	{
		Block block = state != null ? state.getBlock() : null;	
		TileEntity te = state != null ?  world.getTileEntity(pos) : null;
		if (itemStack != null && itemStack.getItem() instanceof IWrench)
		{
			if (block != null && block instanceof IWrenchable)
			{
				((IWrenchable) block).wrench(player, world, pos, state, face);
			}
			if (te != null && te instanceof IWrenchable)
			{
				((IWrenchable) te).wrench(player, world, pos, state, face);
			}
			if (part != null && part instanceof IWrenchable)
			{
				((IWrenchable) part).wrench(player, world, pos, state, face);
			}
		}
		if (itemStack != null && itemStack.getItem() instanceof ItemGyrometer)
		{
			
			List<ITextComponent> textComponents = new ArrayList<ITextComponent>();
			
			if (block != null && block instanceof IGyrometerable)
			{
				((IGyrometerable) block).addInfo(textComponents);
			}
			if (te != null)
			{
				
				if (te instanceof IGyrometerable)
				{
					((IGyrometerable) te).addInfo(textComponents);
				}
				if (te instanceof IMechanicalConduit)
				{
					getInfo((IMechanicalConduit) te, textComponents);
				}
			}
			if (part != null)
			{
				
				if (part instanceof IGyrometerable)
				{
					((IGyrometerable) part).addInfo(textComponents);
				}
				if (part instanceof IMechanicalConduit)
				{
					getInfo((IMechanicalConduit) part, textComponents);
				}
			}
			
			if (world.isRemote)
			{
				for (ITextComponent text : textComponents)
				{
					player.addChatMessage(text);
				}
			}
			
		}
	}

	private static void getInfo(IMechanicalConduit part, List<ITextComponent> list)
	{
		MechanicalNetwork network = part.getNetwork();
		if (network != null)
		{
			System.out.println(part.getMultiplier());
			float speed = Math.abs(network.getSpeedForConduit(part));
			float torque = (speed == 0 ? 0 : network.getTorqueForConduit(part));
			list.add(new TextComponentString("This network has a torque of " + torque));
			list.add(new TextComponentString("This network has a speed of " + speed));
			list.add(new TextComponentString("Mult: " + part.getMultiplier()));
			//list.add(new TextComponentString("CONSUMERS: " + network.networkConsumers.size()));
			//for (IMechanicalConsumer consumer : network.networkConsumers.keySet())
			//{
				//if (getNetwork().networkConsumers.get(consumer) != 5.0)
				//{
				//	System.out.println(network.networkConsumers.get(consumer));
				//}
				//System.out.println(getNetwork().id.substring(0, 10) + " " + getNetwork().networkConsumers.get(consumer));
			//}
			if (network.isTorqueCapped() && part.maxTorque() * part.getMultiplier() == network.getMaxTorque())
			{
				list.add(new TextComponentString("This component seems to be throttling the torque of nearby components"));
			}
			if (network.isTorqueJammed() && ((network.getMinTorque() == part.minTorque()  * part.getMultiplier() && network.getTorque() >= network.consumerTorqueNeeded)|| (part instanceof IMechanicalConsumer && network.getTorque() <= network.consumerTorqueNeeded)))
			{
				list.add(new TextComponentString("The torque seems too low to turn this component"));
			}
		}
	}
	
}
