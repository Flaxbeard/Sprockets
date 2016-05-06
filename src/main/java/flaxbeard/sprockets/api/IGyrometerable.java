package flaxbeard.sprockets.api;

import java.util.List;

import net.minecraft.util.text.ITextComponent;

/**
 * @author Flaxbeard
 *
 * Implement this interface if your block/te/multipart should give additional data when
 * right clicked with a Gyrometer
 * 
 */
public interface IGyrometerable
{
	/**
	 * Add to the chat output when a Gyrometer is used on this block/multipart
	 * 
	 * @param list The current chat output to add to
	 */
	public void addInfo(List<ITextComponent> list);
}
