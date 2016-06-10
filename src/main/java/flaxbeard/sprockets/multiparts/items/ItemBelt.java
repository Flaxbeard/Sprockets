package flaxbeard.sprockets.multiparts.items;


import mcmultipart.multipart.IMultipart;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.items.ItemSprocketBase;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartBelt;

public class ItemBelt extends ItemSprocketBase
{
		
	public ItemBelt()
	{
		super("belt");
		MultipartRegistry.registerPart(PartBelt.class, Sprockets.MODID + ":" + name);
		setHasSubtypes(true);
		subnames = new String[] {"leather"};
	}

	
	public boolean placeBelts(ItemStack stack, EntityPlayer player, World world, int axis, BlockPos start, int size, int meta)
	{
		if (stack.stackSize < size + 1 && !player.isCreative())  return false;
		BlockPos end = start.add(axis == 0 ? size : 0,
				axis == 1 ? size : 0,
				axis == 2 ? size : 0);
		PartAxle startAxle = ((PartAxle) MultipartHelper.getPartContainer(world, start).getPartInSlot(PartSlot.CENTER));
		PartAxle endAxle = ((PartAxle) MultipartHelper.getPartContainer(world, end).getPartInSlot(PartSlot.CENTER));
		if (startAxle.hasBelt > 0) return false;
		if (endAxle.hasBelt > 0) return false;

		PartBelt[] belts = new PartBelt[size - 1];
		for (int n = 1; n < size; n++)
		{
			BlockPos pos = start.add(axis == 0 ? n : 0,
					axis == 1 ? n : 0,
					axis == 2 ? n : 0);
			
			belts[n-1] = new PartBelt();
			belts[n-1].setSlot(meta);
			belts[n-1].setParent(start);
			belts[n-1].markDirty();
			
			//if (!player.canPlayerEdit(pos, EnumFacing.DOWN, stack)) return false;
			
			if (!MultipartHelper.canAddPart(world, pos, belts[n-1])) return false;
				
		}
		
		stack.stackSize -= size + 1;
		startAxle.setBeltBrain(end, size, axis, meta);
		endAxle.setBeltChild(start, meta);
		
		if (!world.isRemote)
		{
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
				
				int face1 = (((PartAxle) c.getPartInSlot(PartSlot.CENTER)).facing / 2) * 2;
				int face2 = stack.getTagCompound().getInteger("face");
				
				if (face1 == face2)
				{
					
					if (face1 == 0)
					{
						if (pos2.getY() == pos.getY() && pos2.getX() == pos.getX())
						{
							System.out.println("0 - 0");
							
							BlockPos min = pos;
							int diff = pos2.getZ() - pos.getZ();
							if (pos.getZ() > pos2.getZ())
							{
								min = pos2;
								diff = pos.getZ() - pos2.getZ();
							}
							
							System.out.println(placeBelts(stack, player, world, 2, min, diff, 1));
					
						}
						else if (pos2.getY() == pos.getY() && pos2.getZ() == pos.getZ())
						{
							System.out.println("0 - 1");
							
							BlockPos min = pos;
							int diff = pos2.getX() - pos.getX();
							if (pos.getX() > pos2.getX())
							{
								min = pos2;
								diff = pos.getX() - pos2.getX();
							}
							
							System.out.println(placeBelts(stack, player, world, 0, min, diff, 0));
						}
					}
					
					if (face1 == 4)
					{
						if (pos2.getX() == pos.getX() && pos2.getY() == pos.getY())
						{
							System.out.println("2 - 0");
							
							BlockPos min = pos;
							int diff = pos2.getZ() - pos.getZ();
							if (pos.getZ() > pos2.getZ())
							{
								min = pos2;
								diff = pos.getZ() - pos2.getZ();
							}
							
							System.out.println(placeBelts(stack, player, world, 2, min, diff, 5));
						}
						else if (pos2.getX() == pos.getX() && pos2.getZ() == pos.getZ())
						{
							System.out.println("2 - 1");
							
							BlockPos min = pos;
							int diff = pos2.getY() - pos.getY();
							if (pos.getY() > pos2.getY())
							{
								min = pos2;
								diff = pos.getY() - pos2.getY();
							}
							
							System.out.println(placeBelts(stack, player, world, 1, min, diff, 4));
					
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
							
							System.out.println(placeBelts(stack, player, world, 1, min, diff, 3));
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
							
							System.out.println(placeBelts(stack, player, world, 0, min, diff, 2));
						}
					}
					//return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
					
				}
				
				stack.getTagCompound().removeTag("x");
				
			}

		}

		return EnumActionResult.PASS;
	}

}
