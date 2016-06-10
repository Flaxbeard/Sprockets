package flaxbeard.sprockets.multiparts.items;


import mcmultipart.microblock.IMicroblock.IFaceMicroblock;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartFlywheel;
import flaxbeard.sprockets.multiparts.PartFlywheelFrictionPad;

public class ItemFlywheel extends ItemSprocketMultipart
{
		
	public ItemFlywheel()
	{
		super("flywheel");
		MultipartRegistry.registerPart(PartFlywheel.class, Sprockets.MODID + ":" + name);
		MultipartRegistry.registerPart(PartFlywheelFrictionPad.class, Sprockets.MODID + ":" + "flywheelPad");
		setHasSubtypes(true);
		subnames = new String[] {"wooden"};
	}
	
	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		PartFlywheel flywheel = new PartFlywheel();
		flywheel.setFacing(side.ordinal());
		
		IMultipartContainer contain = MultipartHelper.getPartContainer(world, pos);
		if (contain != null)
		{
			
			IMultipart bottomPart = contain.getPartInSlot(flywheel.BOTTOM_SLOT.get(side.ordinal()));
			flywheel.bottom = bottomPart == null || (bottomPart instanceof IFaceMicroblock && ((IFaceMicroblock) bottomPart).isFaceHollow());
		}
		
		int damage = stack.getItemDamage();
		if (damage >= subnames.length)
		{
			damage = 0;
		}
		flywheel.setDamage(damage);
		
		flywheel.markDirty();
		return flywheel;
	}
	
	@Override
	public boolean place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player)
	{

		if (!player.canPlayerEdit(pos, side, stack)) return false;

		IMultipart part = createPart(world, pos, side, hit, stack, player);
		
		PartFlywheelFrictionPad pad = new PartFlywheelFrictionPad();
		pad.setSlot(side.getOpposite().ordinal());
		pad.markDirty();
		

		if (part != null && MultipartHelper.canAddPart(world, pos, part) && MultipartHelper.canAddPart(world, pos, pad))
		{
			if (!world.isRemote)
			{
				MultipartHelper.addPart(world, pos, part);
				MultipartHelper.addPart(world, pos, pad);
			}
			consumeItem(stack);

			SoundType sound = getPlacementSound(stack);
			if (sound != null)
			{
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean hasBoundingBox()
	{
		return false;
	}

	@Override
	public AxisAlignedBB boundingBox(World world, BlockPos blockpos, EnumFacing hitFace, Vec3d hitVec)
	{
		return null;
	}

}
