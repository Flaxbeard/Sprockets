package flaxbeard.sprockets.multiparts.items;


import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import mcmultipart.multipart.PartSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartScotchYoke;

public class ItemScotchYoke extends ItemSprocketMultipart
{
		
	public ItemScotchYoke()
	{
		super("yoke");
		MultipartRegistry.registerPart(PartScotchYoke.class, Sprockets.MODID + ":" + name);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IMultipartContainer c = MultipartHelper.getPartContainer(world, pos);
		if (c != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle && !world.isRemote)
		{
			PartAxle axleToReplace = (PartAxle) c.getPartInSlot(PartSlot.CENTER);
			
			if (side.ordinal() != axleToReplace.facing && side.ordinal() != EnumFacing.VALUES[axleToReplace.facing].getOpposite().ordinal())
			{
				c.removePart(axleToReplace);
				
				PartScotchYoke yoke = new PartScotchYoke();
				yoke.setFacing(axleToReplace.facing);
				yoke.top = axleToReplace.top;
				yoke.bottom = axleToReplace.bottom;
				yoke.setDamage(axleToReplace.getDamage());
				yoke.markDirty();
				yoke.setYokeFacing(side.getOpposite().ordinal());
				c.addPart(yoke);
				consumeItem(stack);
				return EnumActionResult.PASS;
			}
		}
		
		BlockPos pos2 = pos.add(side.getDirectionVec());
		c = MultipartHelper.getPartContainer(world, pos2);
		if (c != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle && !world.isRemote)
		{
			PartAxle axleToReplace = (PartAxle) c.getPartInSlot(PartSlot.CENTER);
			
			if (side.ordinal() != axleToReplace.facing && side.ordinal() != EnumFacing.VALUES[axleToReplace.facing].getOpposite().ordinal())
			{
				c.removePart(axleToReplace);
				
				PartScotchYoke yoke = new PartScotchYoke();
				yoke.setFacing(axleToReplace.facing);
				yoke.top = axleToReplace.top;
				yoke.bottom = axleToReplace.bottom;
				yoke.setDamage(axleToReplace.getDamage());
				yoke.markDirty();
				yoke.setYokeFacing(side.getOpposite().ordinal());
				c.addPart(yoke);
				consumeItem(stack);
				return EnumActionResult.PASS;
			}
		}
		
		return EnumActionResult.PASS;
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

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
