package flaxbeard.sprockets.multiparts.items;


import mcmultipart.microblock.IMicroblock.IFaceMicroblock;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartClutch;

public class ItemClutch extends ItemSprocketMultipart
{
		
	public ItemClutch()
	{
		super("clutch");
		MultipartRegistry.registerPart(PartClutch.class, Sprockets.MODID + ":" + name);
		setHasSubtypes(true);
		subnames = new String[] {"wooden", "stone", "iron"};
	}
	
	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		PartClutch clutch = new PartClutch();
		clutch.setFacing(side.ordinal());
		
		IMultipartContainer contain = MultipartHelper.getPartContainer(world, pos);
		if (contain != null)
		{
			IMultipart topPart = contain.getPartInSlot(clutch.TOP_SLOT.get(side.ordinal()));
			clutch.top = topPart == null || (topPart instanceof IFaceMicroblock && ((IFaceMicroblock) topPart).isFaceHollow());
			
			IMultipart bottomPart = contain.getPartInSlot(clutch.BOTTOM_SLOT.get(side.ordinal()));
			clutch.bottom = bottomPart == null || (bottomPart instanceof IFaceMicroblock && ((IFaceMicroblock) bottomPart).isFaceHollow());
		}
		
		int damage = stack.getItemDamage();
		if (damage >= subnames.length)
		{
			damage = 0;
		}
		clutch.setDamage(damage);
		
		clutch.markDirty();
		return clutch;
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
