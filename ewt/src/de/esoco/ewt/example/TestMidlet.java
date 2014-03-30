//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// microEWT-Examples source file
// Copyright (c) 2007 Elmar Sonnenschein / esoco GmbH
// Last Change: 29.07.2007 by eso
//
// microEWT-Examples is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published
// by the Free Software Foundation; either version 2 of the License,
// or (at your option) any later version.
//
// microEWT-Examples is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with microEWT-Examples; if not, write to the Free Software Foundation, Inc.,
// 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA or use the
// contact information on the FSF website http://www.fsf.org
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package de.esoco.ewt.example;

import de.esoco.ewt.app.EWTMidlet;
import de.esoco.ewt.build.ContainerBuilder;
import de.esoco.ewt.component.Button;
import de.esoco.ewt.component.CheckBox;
import de.esoco.ewt.component.ComboBox;
import de.esoco.ewt.component.List;
import de.esoco.ewt.component.RadioButton;
import de.esoco.ewt.component.ScrollPanel;
import de.esoco.ewt.component.TextField;
import de.esoco.ewt.component.View;
import de.esoco.ewt.component.menu.Menu;
import de.esoco.ewt.component.menu.MenuBar;
import de.esoco.ewt.component.menu.MenuItem;
import de.esoco.ewt.dialog.MessageBox;
import de.esoco.ewt.dialog.MessageBox.ResultHandler;
import de.esoco.ewt.event.EWTEvent;
import de.esoco.ewt.event.EWTEventHandler;
import de.esoco.ewt.event.EventType;
import de.esoco.ewt.event.PaintHandler;
import de.esoco.ewt.geometry.Size;
import de.esoco.ewt.graphics.Color;
import de.esoco.ewt.graphics.Font;
import de.esoco.ewt.graphics.FontMetrics;
import de.esoco.ewt.graphics.GraphicsContext;
import de.esoco.ewt.graphics.Image;
import de.esoco.ewt.impl.j2me.CanvasView;
import de.esoco.ewt.layout.EdgeLayout;
import de.esoco.ewt.layout.FillLayout;
import de.esoco.ewt.layout.GridLayout;
import de.esoco.ewt.style.AlignedPosition;
import de.esoco.ewt.style.StyleData;
import de.esoco.ewt.style.StyleFlag;

import java.io.IOException;

import java.util.Random;

import javax.microedition.lcdui.Command;


/********************************************************************
 * A test MIDlet for J2ME-Lib. Currently this class serves just as a testbed for
 * microEWT features and should not be used as an example of typical microEWT
 * programming.
 *
 * @author eso
 */
public class TestMidlet extends EWTMidlet implements EWTEventHandler
{
	//~ Static fields/initializers ---------------------------------------------

	private static final String VIEW_TITLE = "microEWT Test";

	//~ Instance fields --------------------------------------------------------

	private View     rView;
	private Button   aMaxButton;
	private Button   aCloseButton;
	private Button   aOkButton;
	private Button   aCancelButton;
	private ComboBox aComboBox;

	//~ Constructors -----------------------------------------------------------

	/***************************************
	 * Constructor.
	 */
	public TestMidlet()
	{
	}

	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * Event handling for the test MIDlet components.
	 *
	 * @see EWTEventHandler#handleEvent(EWTEvent)
	 */
	public void handleEvent(EWTEvent rEvent)
	{
		Object rSource = rEvent.getSource();

		if (rSource == aCloseButton)
		{
			queryQuitApp();
		}
		else if (rSource == aMaxButton)
		{
			rView.setMaximized(!rView.isMaximized());
		}
		else if (rSource == aOkButton)
		{
			MessageBox.showConfirmation(rView,
										"Confirm",
										"A very long long message written here for a test of the message box!",
										MessageBox.ICON_INFORMATION,
				new ResultHandler()
				{
					public void handleResult(int nButton)
					{
						System.out.println("BUTTON: " +
										   nButton);
					}
				});
		}
		else if (rSource == aComboBox)
		{
			int nBaseColor = parseBaseColor(aComboBox.getText());

			if (nBaseColor >= 0)
			{
				String sHexColor = Color.toHexString(nBaseColor);

				aComboBox.setText(sHexColor);
				((CanvasView) rView).getSkin().setColors(nBaseColor);
				rView.repaint();
			}
		}
		else
		{
			System.out.println("EVENT: " + rEvent.getType() +
							   " - " + rSource);
		}
	}

	/***************************************
	 * Overridden to create a special test menu for this test MIDlet.
	 *
	 * @return the new menu
	 */
	protected MenuBar createApplicationMenu()
	{
		MenuBar		   aMenuBar = new MenuBar();
		Menu		   aMenu    = new Menu("Main");
		final MenuItem aExitMI  = new MenuItem("Exit", Command.EXIT, 1);

		aMenuBar.addChildItem(aMenu);

		aMenu.addChildItem(aExitMI);
		aMenu.addChildItem(new MenuItem("Action", Command.ITEM, 2));

		for (int i = 1; i < 5; i++)
		{
			aMenu.addChildItem(new MenuItem("Test " + i,
											Command.SCREEN,
											i + 10));
		}

		aMenuBar.setActionListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					if (rEvent.getSource() == aExitMI)
					{
						queryQuitApp();
					}
					System.out.println("MENU: " + rEvent.getSource());
				}
			});

		return aMenuBar;
	}

	/***************************************
	 * Shows the test view.
	 *
	 * @see EWTMidlet#initApplicationView(View)
	 */
	protected void initApplicationView(final View rView)
	{
		this.rView = rView;

		ContainerBuilder aBuilder    = new ContainerBuilder(rView);
		EdgeLayout		 aEdgeLayout = new EdgeLayout(2);

		rView.setTitle(VIEW_TITLE);
		rView.setLayout(aEdgeLayout);

		aBuilder = aBuilder.addPanel(AlignedPosition.TOP, aEdgeLayout);

		aCloseButton = aBuilder.addButton(AlignedPosition.LEFT, "X", null);
		aMaxButton   = aBuilder.addButton(AlignedPosition.RIGHT, "M", null);

		aBuilder.addLabel(AlignedPosition.CENTER, VIEW_TITLE, null);

		aBuilder = aBuilder.getParent();
		aBuilder = aBuilder.addPanel(AlignedPosition.BOTTOM, new FillLayout(5));

		aOkButton     =
			aBuilder.addButton(StyleData.DEFAULT, "OK", "#/img/ok.png");
		aCancelButton =
			aBuilder.addButton(StyleData.DEFAULT, "Cancel", "#/img/cancel.png");

		aCancelButton.setEnabled(false);

		aCloseButton.addEventListener(this, EventType.ACTION);
		aMaxButton.addEventListener(this, EventType.ACTION);
		aOkButton.addEventListener(this, EventType.ACTION);
		aCancelButton.addEventListener(this, EventType.ACTION);

		aBuilder = aBuilder.getParent();
		aBuilder =
			aBuilder.addPanel(AlignedPosition.CENTER,
							  new GridLayout(2, true, 5, 5));
		aBuilder = aBuilder.addPanel(StyleData.DEFAULT, aEdgeLayout);

		final List aList = aBuilder.addList(AlignedPosition.CENTER);

		for (int i = 1; i <= 20; i++)
		{
			aList.add("List Entry " + i);
		}
		aList.addEventListener(this, EventType.ACTION);
		aList.addEventListener(this, EventType.SELECTION);

		final TextField aTextField =
			aBuilder.addTextField(AlignedPosition.BOTTOM, "");
		Button		    aAddButton =
			aBuilder.addButton(AlignedPosition.BOTTOM_RIGHT, " + ", null);

		aAddButton.addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					String sText = aTextField.getText();

					if (sText.length() > 0)
					{
						aList.add(sText);
						aList.repaint();
					}
				}
			}, EventType.ACTION);

		StyleData aScrollStyle =
			AlignedPosition.CENTER.setFlags(StyleFlag.ETCHED_OUT,
											StyleFlag.DRAGGING);

		aBuilder = aBuilder.getParent();
		aBuilder = aBuilder.addPanel(StyleData.DEFAULT, aEdgeLayout);
		aBuilder = aBuilder.addScrollPanel(aScrollStyle);

		aBuilder.setLayout(new FillLayout());

		ScrollPanel rScrollPanel = (ScrollPanel) aBuilder.getContainer();

		rScrollPanel.getHorizontalScrollBar().setUnitIncrement(5);
		rScrollPanel.getVerticalScrollBar().setUnitIncrement(5);

		aBuilder.addCanvas(StyleData.DEFAULT).setPaintHandler(new TestPaintHandler());

		aBuilder = aBuilder.getParent();
		aBuilder =
			aBuilder.addPanel(AlignedPosition.BOTTOM.setFlags(StyleFlag.ETCHED_OUT),
							  new GridLayout(1));

		for (int i = 1; i <= 3; i++)
		{
			CheckBox cb =
				aBuilder.addCheckBox(StyleData.DEFAULT, "Checkbox " + i, null);

			cb.addEventListener(this, EventType.ACTION);

			if (i == 3)
			{
				cb.setEnabled(false);
			}
		}

		for (int i = 1; i <= 3; i++)
		{
			RadioButton rb =
				aBuilder.addRadioButton(StyleData.DEFAULT, "Radio " + i, null);

			rb.addEventListener(this, EventType.ACTION);

			if (i == 3)
			{
				rb.setEnabled(false);
			}
		}

		aComboBox = aBuilder.addComboBox(StyleData.DEFAULT, "Color:");
//			aBuilder.addComboBox(StyleData.DEFAULT.setFlags(StyleFlag.READ_ONLY), "Color:");

		aComboBox.add("Random");
		aComboBox.add("Yellow");
		aComboBox.add("Red");
		aComboBox.add("Green");
		aComboBox.add("Blue");
		aComboBox.addEventListener(this, EventType.ACTION);
	}

	/***************************************
	 * Determines the base color for the view skin from the given text string.
	 *
	 * @param  sText The color text string
	 *
	 * @return The resulting base color or -1 if undefined
	 */
	private int parseBaseColor(String sText)
	{
		int nColor = -1;

		if (sText.equalsIgnoreCase("random"))
		{
			nColor = new Random().nextInt(0x01000000);
		}
		else
		{
			if (sText.equalsIgnoreCase("yellow"))
			{
				nColor = 0xF0E040;
			}
			else if (sText.equalsIgnoreCase("red"))
			{
				nColor = 0xF06060;
			}
			else if (sText.equalsIgnoreCase("green"))
			{
				nColor = 0x80F040;
			}
			else if (sText.equalsIgnoreCase("blue"))
			{
				nColor = 0x4080F0;
			}
			else
			{
				try
				{
					nColor = Integer.parseInt(sText, 16);
				}
				catch (NumberFormatException e)
				{
					MessageBox.showNotification(rView,
												"Invalid Input",
												"Color invalid: '" + sText +
												"' must be a hexadecimal value",
												MessageBox.ICON_ERROR);
				}
			}
		}

		return nColor;
	}

	//~ Inner Classes ----------------------------------------------------------

	/********************************************************************
	 * A paint handler that paints a test pattern.
	 */
	static class TestPaintHandler implements PaintHandler
	{
		//~ Instance fields ----------------------------------------------------

		private Size  aPreferredSize = new Size(200, 200);
		private Image aImage		 = new Image("/img/fill.png");
		private Font  aFont;

		//~ Constructors -------------------------------------------------------

		/***************************************
		 * Creates a new instance.
		 */
		public TestPaintHandler()
		{
			try
			{
				aFont = Font.getFont("Vera-Shadow", Font.STYLE_PLAIN, 12);
			}
			catch (IOException e)
			{
				System.out.println("ERR: " + e.getMessage());
				e.printStackTrace();
			}
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * Returns the preferred size of the painted area.
		 *
		 * @return The preferred size
		 */
		public Size getPreferredSize()
		{
			return aPreferredSize;
		}

		/***************************************
		 * @see PaintHandler#paint(GraphicsContext, int, int, int, int)
		 */
		public void paint(GraphicsContext rGraphics, int x, int y, int w, int h)
		{
			rGraphics.tileImage(aImage, x, y, w, h);
			rGraphics.setFont(aFont);

			FontMetrics fm		    = rGraphics.getFontMetrics();
			int		    nLineHeight = fm.getHeight();

			rGraphics.drawString("A bitmap Font", x, y);
			y += nLineHeight;
			rGraphics.drawString("with Shadow", x, y);
			y += nLineHeight;
			rGraphics.drawString("and Antialiasing", x, y);
		}
	}
}
