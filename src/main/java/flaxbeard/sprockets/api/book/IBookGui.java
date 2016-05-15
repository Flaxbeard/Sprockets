package flaxbeard.sprockets.api.book;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;

public interface IBookGui
{
	public FontRenderer getFontRenderer();
	
	public RenderItem getItemRenderer();
}
