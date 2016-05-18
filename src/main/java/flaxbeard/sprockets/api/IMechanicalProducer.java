package flaxbeard.sprockets.api;

public interface IMechanicalProducer extends IMechanicalConduit
{
	public float torqueProduced();
	public float speedProduced();
}
