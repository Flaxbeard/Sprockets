package flaxbeard.sprockets.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import flaxbeard.sprockets.api.book.BookEntry;
import flaxbeard.sprockets.api.book.BookPage;
import flaxbeard.sprockets.api.book.IBookGui;
import flaxbeard.sprockets.book.BookData;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen implements IBookGui
{
	private static final ResourceLocation bookGuiTextures = new ResourceLocation("sprockets:textures/gui/book.png");
	private static final ResourceLocation revBookGuiTextures = new ResourceLocation("sprockets:textures/gui/bookReverse.png");

	/** The player editing the book */
	private final EntityPlayer player;
	private final ItemStack stack;

	/** Update ticks since the gui was opened */
	private int updateCount;
	private int bookImageWidth = 192;
	private int bookImageHeight = 192;

	private List<ITextComponent> field_175386_A;
	private int field_175387_B = -1;
	private GuiBook.NextPageButton buttonNextPage;
	private GuiBook.NextPageButton buttonPreviousPage;
	
	private BookEntry entryViewing;
	private List<BookPage> pages;
	private int bookTotalPages = 1;
	private int currPage;
	

	public GuiBook(EntityPlayer player, ItemStack book)
	{
		this.player = player;
		this.stack = book;


		this.entryViewing = BookData.index;
		this.pages = entryViewing.getPages();
		this.bookTotalPages = (int) Math.ceil(pages.size() / 2);
		this.currPage = 0;
		
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();
		++this.updateCount;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		this.buttonList.clear();

		int i = (this.width - this.bookImageWidth) / 2;
		int j = (this.height - this.bookImageHeight) / 2;
		this.buttonList.add(this.buttonNextPage = new GuiBook.NextPageButton(1, i + 120 + 67, j + 154, true));
		this.buttonList.add(this.buttonPreviousPage = new GuiBook.NextPageButton(2, i + 38 - 67, j + 154, false));
		this.updateButtons();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	private void updateButtons()
	{
		this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
		this.buttonPreviousPage.visible = this.currPage > 0;
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				this.mc.displayGuiScreen((GuiScreen)null);
			}
			else if (button.id == 1)
			{
				if (this.currPage < this.bookTotalPages - 1)
				{
					++this.currPage;
				}
			}
			else if (button.id == 2)
			{
				if (this.currPage > 0)
				{
					--this.currPage;
				}
			}

			this.updateButtons();
		}
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

	}



	/**
	 * Draws the screen and all the components in it.
	 *  
	 * @param mouseX Mouse x coordinate
	 * @param mouseY Mouse y coordinate
	 * @param partialTicks How far into the current tick (1/20th of a second) the game is
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		int d = Mouse.getDWheel();
		try
		{
			if (d < 0 && this.buttonNextPage.visible)
			{
				this.actionPerformed(this.buttonNextPage);
			}
			if (d > 0 && this.buttonPreviousPage.visible)
			{
				this.actionPerformed(this.buttonPreviousPage);
			}
		}
		catch (IOException e)
		{
			
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.width - this.bookImageWidth) / 2;
		int j = (this.height - this.bookImageHeight) / 2;
		this.mc.getTextureManager().bindTexture(bookGuiTextures);
		this.drawTexturedModalRect(i + 67, j, 0, 0, this.bookImageWidth, this.bookImageHeight);
		
		this.mc.getTextureManager().bindTexture(revBookGuiTextures);
		this.drawTexturedModalRect(i - 67, j, 0, 0, this.bookImageWidth, this.bookImageHeight);
		
		String s = stack.getDisplayName();
		int l = this.fontRendererObj.getStringWidth(s);
		
		this.fontRendererObj.drawStringWithShadow(s, i + this.bookImageWidth / 2 - l / 2 - 3, j - 15, 0xFFFFFF);
		
		boolean unicode = fontRendererObj.getUnicodeFlag();
		fontRendererObj.setUnicodeFlag(true);
		
		s = Integer.valueOf((currPage - 1) * 2 + 4) + "/" + Integer.valueOf(bookTotalPages * 2);
		l = this.fontRendererObj.getStringWidth(s);
		this.fontRendererObj.drawString(s, i - l + this.bookImageWidth - 44 + 67, j + 16, 0x3F3F3F);
		
		s = Integer.valueOf((currPage - 1) * 2 + 3) + "/" + Integer.valueOf(bookTotalPages * 2);
		l = this.fontRendererObj.getStringWidth(s);
		this.fontRendererObj.drawString(s, i + l - this.bookImageWidth + 86 + 67, j + 16, 0x3F3F3F);
		
		if ((currPage * 2) < pages.size())
		{
			pages.get(currPage * 2).renderPage(i - 67, j, this, mouseX, mouseY);
		}
		if ((currPage * 2) + 1 < pages.size())
		{
			pages.get(currPage * 2 + 1).renderPage(i + 67, j, this, mouseX, mouseY);
		}
		
		fontRendererObj.setUnicodeFlag(unicode);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 0)
		{
			ITextComponent itextcomponent = this.func_175385_b(mouseX, mouseY);

			if (this.handleComponentClick(itextcomponent))
			{
				return;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Executes the click event specified by the given chat component
	 */
	protected boolean handleComponentClick(ITextComponent component)
	{
		ClickEvent clickevent = component == null ? null : component.getStyle().getClickEvent();

		if (clickevent == null)
		{
			return false;
		}
		else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE)
		{
			String s = clickevent.getValue();

			try
			{
				int i = Integer.parseInt(s) - 1;

				if (i >= 0 && i < this.bookTotalPages && i != this.currPage)
				{
					this.currPage = i;
					this.updateButtons();
					return true;
				}
			}
			catch (Throwable var5)
			{
				;
			}

			return false;
		}
		else
		{
			boolean flag = super.handleComponentClick(component);

			if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
			{
				this.mc.displayGuiScreen((GuiScreen)null);
			}

			return flag;
		}
	}

	public ITextComponent func_175385_b(int p_175385_1_, int p_175385_2_)
	{
		if (this.field_175386_A == null)
		{
			return null;
		}
		else
		{
			int i = p_175385_1_ - (this.width - this.bookImageWidth) / 2 - 36;
			int j = p_175385_2_ - 2 - 16 - 16;

			if (i >= 0 && j >= 0)
			{
				int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());

				if (i <= 116 && j < this.mc.fontRendererObj.FONT_HEIGHT * k + k)
				{
					int l = j / this.mc.fontRendererObj.FONT_HEIGHT;

					if (l >= 0 && l < this.field_175386_A.size())
					{
						ITextComponent itextcomponent = (ITextComponent)this.field_175386_A.get(l);
						int i1 = 0;

						for (ITextComponent itextcomponent1 : itextcomponent)
						{
							if (itextcomponent1 instanceof TextComponentString)
							{
								i1 += this.mc.fontRendererObj.getStringWidth(((TextComponentString)itextcomponent1).getText());

								if (i1 > i)
								{
									return itextcomponent1;
								}
							}
						}
					}

					return null;
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton
	{
		private final boolean field_146151_o;

		public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_)
		{
			super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
			this.field_146151_o = p_i46316_4_;
		}

		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			if (this.visible)
			{
				boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiBook.bookGuiTextures);
				int i = 0;
				int j = 192;

				if (flag)
				{
					i += 23;
				}

				if (!this.field_146151_o)
				{
					j += 13;
				}

				this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
			}
		}
	}

	@Override
	public FontRenderer getFontRenderer()
	{
		return fontRendererObj;
	}

	@Override
	public RenderItem getItemRenderer()
	{
		return itemRender;
	}
}