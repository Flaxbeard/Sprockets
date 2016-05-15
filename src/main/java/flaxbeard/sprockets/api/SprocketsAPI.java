package flaxbeard.sprockets.api;

import java.util.ArrayList;
import java.util.List;

import flaxbeard.sprockets.api.book.BookCategory;
import flaxbeard.sprockets.api.book.BookEntry;

public class SprocketsAPI
{
	private static final List<BookCategory> categories = new ArrayList<BookCategory>();
	private static final List<BookEntry> entries = new ArrayList<BookEntry>();

	public static void addCategory(BookCategory category)
	{
		categories.add(category);
	}
	
	public static List<BookCategory> getCategories()
	{
		return categories;
	}
	
	public static void addEntry(BookEntry entry)
	{
		entries.add(entry);
		if (entry.getCategory() != null)
		{
			entry.getCategory().addEntry(entry);
		}
	}
}
