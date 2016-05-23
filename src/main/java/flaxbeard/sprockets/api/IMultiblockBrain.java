package flaxbeard.sprockets.api;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface IMultiblockBrain
{
	public void addMultiblock(Multiblock mb, boolean swapXZ, boolean flipX, boolean flipZ);
	public void destroy();
	public boolean hasCapability(Capability<?> capability, EnumFacing facing, int loc);
	public <T> T getCapability(Capability<T> capability, EnumFacing facing, int loc);
}
