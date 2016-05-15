package flaxbeard.sprockets.api.book;

public abstract class BookPage
{
	public abstract void renderPage(int x, int y, IBookGui book, int mouseX, int mouseY);
}
