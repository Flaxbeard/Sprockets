package flaxbeard.sprockets.blocks.tiles;

import java.util.HashSet;

import mcmultipart.multipart.PartSlot;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3i;

public class TileEntityCreativeMotor extends TileEntitySprocketBase
{
	private static final HashSet<Tuple<Vec3i, PartSlot>> CIS;
	
	static
	{
		CIS = new HashSet<Tuple<Vec3i, PartSlot>>();
		CIS.add(new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN));
		CIS.add(new Tuple(new Vec3i(0, -1, 0), PartSlot.UP));
	}
	
	@Override
	public void update()
	{
		super.update();
		if (this.getNetwork() != null)
		{
			getNetwork().addSpeedFromBlock(this, 4f, 4f);
		}
	}


	@Override
	public boolean isNegativeDirection()
	{
		return false;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		// TODO Auto-generated method stub
		return CIS;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		// TODO Auto-generated method stub
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public HashSet<Vec3i> cisConnections()
	{
		// TODO Auto-generated method stub
		return new HashSet<Vec3i>();
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		// TODO Auto-generated method stub
		return new HashSet<Vec3i>();
	}
	
}
