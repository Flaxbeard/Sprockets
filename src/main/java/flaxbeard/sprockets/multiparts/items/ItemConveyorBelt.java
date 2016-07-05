package flaxbeard.sprockets.multiparts.items;


import java.util.List;

import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import mcmultipart.multipart.PartSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.items.ItemSprocketBase;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartAxleConveyorBelt;
import flaxbeard.sprockets.multiparts.PartConveyorBelt;

public class ItemConveyorBelt extends ItemSprocketBase
{
		
	public ItemConveyorBelt()
	{
		super("conveyorBelt");
		MultipartRegistry.registerPart(PartConveyorBelt.class, Sprockets.MODID + ":" + name);
		MultipartRegistry.registerPart(PartAxleConveyorBelt.class, Sprockets.MODID + ":" + "axleConveyorBelt");

		setHasSubtypes(true);
		subnames = new String[] {"leather"};
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("x"))
		{
			tooltip.add("Linked to:");
			tooltip.add("X: " + stack.getTagCompound().getInteger("x"));
			tooltip.add("Y: " + stack.getTagCompound().getInteger("y"));
			tooltip.add("Z: " + stack.getTagCompound().getInteger("z"));
		}
	}

	
	public boolean placeBelts(ItemStack stack, EntityPlayer player, World world, int axis, BlockPos start, int size, int meta, int damage)
	{
		if (size == 0) return false;
		if (stack.stackSize < size + 1 && !player.isCreative())  return false;
		BlockPos end = start.add(axis == 0 ? size : 0,
				axis == 1 ? size : 0,
				axis == 2 ? size : 0);
		
		IMultipartContainer sc = MultipartHelper.getPartContainer(world, start);
		IMultipartContainer ec = MultipartHelper.getPartContainer(world, end);

		PartAxle startAxle = ((PartAxle) sc.getPartInSlot(PartSlot.CENTER));
		PartAxle endAxle = ((PartAxle) ec.getPartInSlot(PartSlot.CENTER));
		if (startAxle.getClass() != PartAxle.class) return false;
		if (endAxle.getClass() != PartAxle.class) return false;
		
		PartSlot sF = axis == 1 ? PartSlot.UP : axis == 0 ? PartSlot.EAST : PartSlot.SOUTH;
		if (sc.getPartInSlot(sF) != null) return false;

		PartSlot eF = axis == 1 ? PartSlot.DOWN : axis == 0 ? PartSlot.WEST : PartSlot.NORTH;
		if (ec.getPartInSlot(eF) != null) return false;

		
		PartConveyorBelt[] belts = new PartConveyorBelt[size - 1];
		for (int n = 1; n < size; n++)
		{
			BlockPos pos = start.add(axis == 0 ? n : 0,
					axis == 1 ? n : 0,
					axis == 2 ? n : 0);
			
			belts[n-1] = new PartConveyorBelt();
			belts[n-1].setSlot(meta);
			belts[n-1].setParent(start);
			belts[n-1].markDirty();
			belts[n-1].setDamage(damage);
			
			//if (!player.canPlayerEdit(pos, EnumFacing.DOWN, stack)) return false;
			
			if (!MultipartHelper.canAddPart(world, pos, belts[n-1])) return false;
				
		}
		
		stack.stackSize -= size + 1;
		
		

		


		if (!world.isRemote)
		{
			PartAxleConveyorBelt newStart = new PartAxleConveyorBelt();
			newStart.setFacing(startAxle.facing);
			newStart.setDamage(startAxle.getDamage());
			newStart.setBeltBrain(end, size, axis, meta, damage);
			sc.removePart(startAxle);
			newStart.markDirty();
			sc.addPart(newStart);
			
			PartAxleConveyorBelt newEnd = new PartAxleConveyorBelt();
			newEnd.setFacing(endAxle.facing);
			newEnd.setDamage(endAxle.getDamage());
			newEnd.setBeltChild(start, meta, damage);
			ec.removePart(endAxle);
			newEnd.markDirty();
			ec.addPart(newEnd);

			for (int n = 1; n < size; n++)
			{
				BlockPos pos = start.add(axis == 0 ? n : 0,
						axis == 1 ? n : 0,
						axis == 2 ? n : 0);
				MultipartHelper.addPart(world, pos, belts[n-1]);
			}
		}
		
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
				
		if (!stack.getTagCompound().hasKey("x"))
		{
			IMultipartContainer c = MultipartHelper.getPartContainer(world, pos);
			if (c != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle)
			{
				stack.getTagCompound().setInteger("x", pos.getX());
				stack.getTagCompound().setInteger("y", pos.getY());
				stack.getTagCompound().setInteger("z", pos.getZ());
				stack.getTagCompound().setInteger("face", (((PartAxle) c.getPartInSlot(PartSlot.CENTER)).facing / 2) * 2);
			}
		}
		else
		{
			IMultipartContainer c = MultipartHelper.getPartContainer(world, pos);
			if (c != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle)
			{
				BlockPos pos2 = new BlockPos(stack.getTagCompound().getInteger("x"),
						stack.getTagCompound().getInteger("y"),
						stack.getTagCompound().getInteger("z"));
				if (pos2.equals(pos) || MultipartHelper.getPartContainer(world, pos2) == null || !(MultipartHelper.getPartContainer(world, pos2).getPartInSlot(PartSlot.CENTER) instanceof PartAxle))
				{
					if (!pos2.equals(pos))
					{
						stack.setTagCompound(null);
					}
					return EnumActionResult.PASS;
				}
				
				int face1 = (((PartAxle) c.getPartInSlot(PartSlot.CENTER)).facing / 2) * 2;
				int face2 = stack.getTagCompound().getInteger("face");
				
				if (face1 == face2)
				{
					int damage = stack.getMetadata();
					if (face1 == 0)
					{
						if (pos2.getY() == pos.getY() && pos2.getX() == pos.getX())
						{
							
							BlockPos min = pos;
							int diff = pos2.getZ() - pos.getZ();
							if (pos.getZ() > pos2.getZ())
							{
								min = pos2;
								diff = pos.getZ() - pos2.getZ();
							}
							
							placeBelts(stack, player, world, 2, min, diff, 1, damage);
					
						}
						else if (pos2.getY() == pos.getY() && pos2.getZ() == pos.getZ())
						{
							
							BlockPos min = pos;
							int diff = pos2.getX() - pos.getX();
							if (pos.getX() > pos2.getX())
							{
								min = pos2;
								diff = pos.getX() - pos2.getX();
							}
							
							placeBelts(stack, player, world, 0, min, diff, 0, damage);
						}
					}
					
					if (face1 == 4)
					{
						if (pos2.getX() == pos.getX() && pos2.getY() == pos.getY())
						{
							
							BlockPos min = pos;
							int diff = pos2.getZ() - pos.getZ();
							if (pos.getZ() > pos2.getZ())
							{
								min = pos2;
								diff = pos.getZ() - pos2.getZ();
							}
							
							placeBelts(stack, player, world, 2, min, diff, 5, damage);
						}
						else if (pos2.getX() == pos.getX() && pos2.getZ() == pos.getZ())
						{
							
							BlockPos min = pos;
							int diff = pos2.getY() - pos.getY();
							if (pos.getY() > pos2.getY())
							{
								min = pos2;
								diff = pos.getY() - pos2.getY();
							}
							
							placeBelts(stack, player, world, 1, min, diff, 4, damage);
					
						}
					}
					
					if (face1 == 2)
					{
						if (pos2.getZ() == pos.getZ() && pos2.getX() == pos.getX())
						{
							BlockPos min = pos;
							int diff = pos2.getY() - pos.getY();
							if (pos.getY() > pos2.getY())
							{
								min = pos2;
								diff = pos.getY() - pos2.getY();
							}
							
							placeBelts(stack, player, world, 1, min, diff, 3, damage);
						}
						else if (pos2.getZ() == pos.getZ() && pos2.getY() == pos.getY())
						{
							BlockPos min = pos;
							int diff = pos2.getX() - pos.getX();
							if (pos.getX() > pos2.getX())
							{
								min = pos2;
								diff = pos.getX() - pos2.getX();
							}
							
							placeBelts(stack, player, world, 0, min, diff, 2, damage);
						}
					}
					//return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
					
				}
				
				stack.setTagCompound(null);


			}

		}

		return EnumActionResult.PASS;
	}

}
