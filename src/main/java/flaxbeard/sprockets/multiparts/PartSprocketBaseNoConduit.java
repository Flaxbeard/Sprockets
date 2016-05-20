package flaxbeard.sprockets.multiparts;

import mcmultipart.multipart.Multipart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public abstract class PartSprocketBaseNoConduit extends Multipart
{
	private Material material = Material.WOOD;
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
		if (material.equals(Material.IRON) || material.equals(Material.ROCK))
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
		else if (material.equals(Material.WOOD))
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
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("damage", damage);
		return nbt;
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
		
		float breakSpeed = player.getDigSpeed(createBlockState().getBaseState(), getPos());
		
		if (!effective)
		{
			return breakSpeed / hardness / 100F;
		}
		else
		{
			return breakSpeed / hardness / 30F;
		}
	}
	

}
