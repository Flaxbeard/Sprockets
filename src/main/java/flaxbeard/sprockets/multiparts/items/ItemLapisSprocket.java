package flaxbeard.sprockets.multiparts.items;


import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartLapisSprocket;
import flaxbeard.sprockets.multiparts.PartSprocket;

public class ItemLapisSprocket extends ItemSprocketMultipart
{
		
	public ItemLapisSprocket()
	{
		super("lapisSprocket");
		MultipartRegistry.registerPart(PartLapisSprocket.class, Sprockets.MODID + ":" + name);
		setHasSubtypes(true);
		subnames = new String[] {"wooden", "stone", "iron"};
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		PartSprocket sprocket = new PartLapisSprocket();
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


}
