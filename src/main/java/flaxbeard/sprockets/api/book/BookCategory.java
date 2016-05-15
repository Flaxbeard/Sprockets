package flaxbeard.sprockets.api.book;

import java.util.ArrayList;
import java.util.List;

public class BookCategory implements Comparable<BookCategory>
{
	private List<BookEntry> entries = new ArrayList<BookEntry>();
	
	private final String unlocalizedName;
	private int priority = 10;
	
	private static int idCounter = 0;
	private final int id;

	public BookCategory(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
		id = idCounter++;
	}
	
	public BookCategory setPriority(int priority)
	{
		this.priority = priority;
		return this;
	}
		
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public List<BookEntry> getEntries()
	{
		return entries;
	}
	
	public void addEntry(BookEntry entry)
	{
		entries.add(entry);
	}
	
	@Override
	public int compareTo(BookCategory other)
	{
		return priority == other.priority ? id - other.id : priority - other.priority;
	}

}
