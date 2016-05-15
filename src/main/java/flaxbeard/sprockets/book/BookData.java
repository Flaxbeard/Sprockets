package flaxbeard.sprockets.book;

import flaxbeard.sprockets.api.SprocketsAPI;
import flaxbeard.sprockets.api.book.BookCategory;
import flaxbeard.sprockets.api.book.BookEntry;
import flaxbeard.sprockets.book.pages.BookPageText;

public class BookData
{
	public static BookCategory test;
	public static BookEntry index;
	
	public static void postInit()
	{
		test = new BookCategory("sprockets:book.category.test");
		SprocketsAPI.addCategory(test);
		SprocketsAPI.addEntry(index = new BookIndex().addPage(new BookPageText("*****************************************************************************************************************")).addPage(new BookPageText("test2")).addPage(new BookPageText("test3")).addPage(new BookPageText("test3")));
	}
}
