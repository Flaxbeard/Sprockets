package flaxbeard.sprockets.multiparts.items;


import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartSprocket;

public class ItemSprocket extends ItemSprocketMultipart
{
		
	public ItemSprocket()
	{
		super("sprocket");
		MultipartRegistry.registerPart(PartSprocket.class, Sprockets.MODID + ":" + name);
		setHasSubtypes(true);
		subnames = new String[] {"wooden", "stone", "iron"};
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		side = getFaceBasedOnPos(hit, side);
		PartSprocket sprocket = new PartSprocket();
		sprocket.setSlot(side.getOpposite().ordinal());
		sprocket.markDirty();
		
		int damage = stack.getItemDamage();
		if (damage >= subnames.length)
		{
			damage = 0;
		}
		sprocket.setDamage(damage);
		
		return sprocket;
	}

	@Override
	public boolean hasBoundingBox()
	{
		return true;
	}
	
	public Tuple<BlockPos, EnumFacing> getPlacePos(World world, BlockPos blockpos, EnumFacing hitFace, Vec3d hitVec)
	{
		BlockPos ret = blockpos;
		EnumFacing face = getFaceBasedOnPos(hitVec, hitFace).getOpposite();
		IMultipartContainer container = MultipartHelper.getPartContainer(world, blockpos);
		if (container != null && (hitVec.xCoord != 0.0 && hitVec.xCoord != 1.0) && (hitVec.yCoord != 0.0 && hitVec.yCoord != 1.0) && (hitVec.zCoord != 0.0 && hitVec.zCoord != 1.0))
		{
			if (container.getPartInSlot(PartSprocket.SLOT_FROM_FACING.get(face.ordinal())) == null)
			{

				ret = blockpos.add(hitFace.getDirectionVec());
			}
			else
			{
				ret = blockpos.add(hitFace.getDirectionVec());
				face = face.getOpposite();
			}
		}
		return new Tuple(ret, face);
	}

	@Override
	public AxisAlignedBB boundingBox(World world, BlockPos blockpos, EnumFacing hitFace, Vec3d hitVec)
	{
		Tuple<BlockPos, EnumFacing> op = getPlacePos(world, blockpos, hitFace, hitVec);
		AxisAlignedBB box = PartSprocket.BOUNDS[op.getSecond().ordinal()];
		box = box.offset(op.getFirst().subtract(blockpos));
		return box;
	}


}
