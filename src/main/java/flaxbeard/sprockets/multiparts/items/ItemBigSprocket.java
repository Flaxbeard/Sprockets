package flaxbeard.sprockets.multiparts.items;


import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartBigSprocketCenter;
import flaxbeard.sprockets.multiparts.PartBigSprocketEdge;
import flaxbeard.sprockets.multiparts.PartBigSprocketPlaceholder;

public class ItemBigSprocket extends ItemSprocketMultipart
{
		
	public ItemBigSprocket()
	{
		super("bigSprocket");
		MultipartRegistry.registerPart(PartBigSprocketCenter.class, Sprockets.MODID + ":" + "bigSprocketCenter");
		MultipartRegistry.registerPart(PartBigSprocketPlaceholder.class, Sprockets.MODID + ":" + "bigSprocketPlaceholder");
		MultipartRegistry.registerPart(PartBigSprocketEdge.class, Sprockets.MODID + ":" + "bigSprocketEdge");
		setHasSubtypes(true);
		subnames = new String[] {"wooden", "stone", "iron"};
	}
	
	@Override
	public boolean place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player)
	{

		if (!player.canPlayerEdit(pos, side, stack)) return false;
		
		IMultipart mb = createPart(world, pos, side, hit, stack, player);
		PartBigSprocketPlaceholder[] additional = { 
				new PartBigSprocketPlaceholder(), 
				new PartBigSprocketPlaceholder(), 
				new PartBigSprocketPlaceholder(), 
				new PartBigSprocketPlaceholder(), 
		};
		
		PartBigSprocketEdge[] additionalEdge = { 
				new PartBigSprocketEdge(), 
				new PartBigSprocketEdge(), 
				new PartBigSprocketEdge(), 
				new PartBigSprocketEdge(), 
		};
		
		int index = 0;
		int edgeIndex = 0;
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i != 0 || j != 0)
				{
					BlockPos pos2 = pos.add(0, i, j);
					if (side.ordinal() < 2)
					{
						pos2 = pos.add(j, 0, i);
					}
					else if (side.ordinal() < 4)
					{
						pos2 = pos.add(j, i, 0);
					}
					
					if (i == 0 || j == 0 )
					{
						additionalEdge[edgeIndex].setSlot(side.getOpposite().ordinal());
						additionalEdge[edgeIndex].setIndex(edgeIndex);
						additionalEdge[edgeIndex].setDamage(stack.getItemDamage());
						additionalEdge[edgeIndex].markDirty();
						additionalEdge[edgeIndex].setParent(pos2.subtract(pos));
						
						if (!MultipartHelper.canAddPart(world, pos2, additionalEdge[edgeIndex]))
						{
							return false;
						}
						edgeIndex++;
					}
					else
					{
						additional[index].setSlot(side.getOpposite().ordinal());
						additional[index].setDamage(stack.getItemDamage());
						additional[index].markDirty();
						additional[index].setParent(pos2.subtract(pos));
						
						if (!MultipartHelper.canAddPart(world, pos2, additional[index]))
						{
							return false;
						}
						index++;
					}
					
				}
			}
		}
		
		if (mb != null && MultipartHelper.canAddPart(world, pos, mb))
		{
			if (!world.isRemote)
			{
				MultipartHelper.addPart(world, pos, mb);
				
				index = 0;
				edgeIndex = 0;
				for (int i = -1; i <= 1; i++)
				{
					for (int j = -1; j <= 1; j++)
					{
						if ( i != 0 || j != 0)
						{
							BlockPos pos2 = pos.add(0, i, j);
							if (side.ordinal() < 2)
							{
								pos2 = pos.add(j, 0, i);
							}
							else if (side.ordinal() < 4)
							{
								pos2 = pos.add(j, i, 0);
							}
							
							if (i == 0 || j == 0 )
							{
								MultipartHelper.addPart(world, pos2, additionalEdge[edgeIndex]);
								edgeIndex++;
							}
							else
							{
								MultipartHelper.addPart(world, pos2, additional[index]);
								index++;
							}
							
						}
					}
				}
				
			}
			consumeItem(stack);
			
			SoundType sound = getPlacementSound(stack);
			if (sound != null)
				world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, sound.getVolume(),
					sound.getPitch());
			
			return true;
		}
		
		return false;
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		PartBigSprocketCenter sprocket = new PartBigSprocketCenter();
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
