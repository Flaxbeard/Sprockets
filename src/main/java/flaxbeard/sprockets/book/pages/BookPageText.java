package flaxbeard.sprockets.book.pages;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.translation.I18n;
import flaxbeard.sprockets.api.book.BookPage;
import flaxbeard.sprockets.api.book.IBookGui;

public class BookPageText extends BookPage
{
	private final String textUnlocalized;

	public BookPageText(String textUnlocalized)
	{
		this.textUnlocalized = textUnlocalized;
	}

	@Override
	public void renderPage(int x, int y, IBookGui book, int mouseX, int mouseY)
	{
		FontRenderer fr = book.getFontRenderer();
		int yOffset = y + 30;
		String stringLeft = I18n.translateToLocal(textUnlocalized);
		while (stringLeft.contains("<br>"))
		{
			String output = stringLeft.substring(0, stringLeft.indexOf("<br>"));
			fr.drawSplitString(output, x + 40, yOffset, 110, 0);
			yOffset += this.getSplitStringHeight(fr, output, x + 40, yOffset, 110);
			yOffset += 10;
			stringLeft = stringLeft.substring(stringLeft.indexOf("<br>") + 4, stringLeft.length());

		}
		String output = stringLeft;
		fr.drawSplitString(output, x + 40, yOffset, 110, 0);
	}
	
	protected int getSplitStringHeight(FontRenderer fontRenderer, String par1Str, int par2, int par3, int par4)
	{
		List list = fontRenderer.listFormattedStringToWidth(par1Str, par4);
		int initialPar3 = par3;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); par3 += fontRenderer.FONT_HEIGHT)
		{
		    String s1 = (String) iterator.next();
		}
		return par3 - initialPar3;
	}

}
