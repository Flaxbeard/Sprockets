package flaxbeard.sprockets.api.book;

import java.util.ArrayList;
import java.util.List;

public class BookEntry implements Comparable<BookEntry>
{
	private final String unlocalizedName;
	private int priority = 10;
	
	private static int idCounter = 0;
	private final int id;
	
	private final BookCategory category;
	
	private final List<BookPage> pages = new ArrayList<BookPage>();

	public BookEntry(String unlocalizedName, BookCategory category)
	{
		this.unlocalizedName = unlocalizedName;
		id = idCounter++;
		this.category = category;
	}
	
	public BookEntry addPage(BookPage page)
	{
		this.pages.add(page);
		return this;
	}
	
	public BookEntry setPriority(int priority)
	{
		this.priority = priority;
		return this;
	}
		
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public BookCategory getCategory()
	{
		return category;
	}
	
	@Override
	public int compareTo(BookEntry other)
	{
		return priority == other.priority ? id - other.id : priority - other.priority;
	}

	public List<BookPage> getPages()
	{
		return pages;
	}

}
