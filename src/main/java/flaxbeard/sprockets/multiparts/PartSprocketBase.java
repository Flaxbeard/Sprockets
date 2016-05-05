package flaxbeard.sprockets.multiparts;

import java.util.List;

import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import flaxbeard.sprockets.api.PartMechanicalConduit;
import flaxbeard.sprockets.common.handler.WrenchHandler;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.lib.LibConstants;

public abstract class PartSprocketBase extends PartMechanicalConduit
{
	private Material material = Material.wood;
	private float hardness = 1.0F;
	protected int damage;

		
	public void setHardness(float hardness)
	{
		this.hardness = hardness;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	@Override
	public float getHardness(PartMOP hit)
	{
		return hardness;
	}

	@Override
	public Material getMaterial()
	{
		return material;
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
	}
	
	@Override
	public boolean isToolEffective(String type, int level)
	{
		if (material.equals(Material.iron) || material.equals(Material.rock))
		{
			if (type == "pickaxe")
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (material.equals(Material.wood))
		{
			if (type == "axe")
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		setDamage(nbt.getInteger("damage"));
	}


	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("damage", damage);
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(damage);
	
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		setDamage(buf.readByte());
	}
	
	
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	
	@Override
	public float getStrength(EntityPlayer player, PartMOP hit)
	{
		float hardness = getHardness(hit);
		Material mat = getMaterial();
		ItemStack stack = player.getHeldItemMainhand();
		boolean effective = mat.isToolNotRequired();
		if (!effective && stack != null)
		{
			for (String tool : stack.getItem().getToolClasses(stack))
			{
				effective = isToolEffective(tool, stack.getItem().getHarvestLevel(stack, tool));
				if (effective)
				{
					break;
				}
			}
		}
		
		float breakSpeed = player.getBreakSpeed(createBlockState().getBaseState(), getPos());
		
		if (!effective)
		{
			return breakSpeed / hardness / 100F;
		}
		else
		{
			return breakSpeed / hardness / 30F;
		}
	}
	
	
	@Override
	public ResourceLocation getModelPath()
	{
		return null;
	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit)
	{
		WrenchHandler.handle(player, getWorld(), getPos(), null, null, heldItem, this);

		return false;
	}
	

}
