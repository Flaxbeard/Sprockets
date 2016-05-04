package flaxbeard.sprockets.api.network;

import java.util.EnumSet;

import mcmultipart.multipart.PartSlot;

public interface IConditionalPart
{
	public EnumSet<PartSlot> getOccupiedSlots();
}
