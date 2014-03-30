//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// microEWT-Examples source file
// Copyright (c) 2007 Elmar Sonnenschein / esoco GmbH
// Last Change: 26.07.2007 by eso
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
import de.esoco.ewt.component.Canvas;
import de.esoco.ewt.component.CheckBox;
import de.esoco.ewt.component.ChildView;
import de.esoco.ewt.component.Container;
import de.esoco.ewt.component.Label;
import de.esoco.ewt.component.List;
import de.esoco.ewt.component.ProgressBar;
import de.esoco.ewt.component.ScrollBar;
import de.esoco.ewt.component.TextField;
import de.esoco.ewt.component.View;
import de.esoco.ewt.component.menu.Menu;
import de.esoco.ewt.component.menu.MenuBar;
import de.esoco.ewt.component.menu.MenuItem;
import de.esoco.ewt.dialog.MessageBox;
import de.esoco.ewt.event.EWTEvent;
import de.esoco.ewt.event.EWTEventHandler;
import de.esoco.ewt.event.EventType;
import de.esoco.ewt.event.PaintHandler;
import de.esoco.ewt.geometry.Margins;
import de.esoco.ewt.geometry.Size;
import de.esoco.ewt.graphics.Color;
import de.esoco.ewt.graphics.GraphicsContext;
import de.esoco.ewt.impl.j2me.CanvasView;
import de.esoco.ewt.layout.EdgeLayout;
import de.esoco.ewt.layout.FillLayout;
import de.esoco.ewt.layout.FlowLayout;
import de.esoco.ewt.layout.GenericLayout;
import de.esoco.ewt.layout.GridLayout;
import de.esoco.ewt.style.AlignedPosition;
import de.esoco.ewt.style.Alignment;
import de.esoco.ewt.style.StyleData;
import de.esoco.ewt.style.StyleFlag;
import de.esoco.ewt.style.ViewStyle;

import java.util.Random;
import java.util.Vector;


/********************************************************************
 * This MIDlet shows the different microEWT user interface elements, similar to
 * the SwingSet demo application for Swing. It's main intention is to
 * demonstrate microEWT features, not to serve as an example of typical microEWT
 * programming. Especially it's intensive use of anonymous inner classes should
 * be avoided in production code, at least in memory-sensitive application
 * areas.
 *
 * @author eso
 */
public class EWTSet extends EWTMidlet implements EWTEventHandler
{
	//~ Instance fields --------------------------------------------------------

	private Button aButtonBT;
	private Button aTextBT;
	private Button aListBT;
	private Button aCanvasBT;
	private Button aValueBarBT;
	private Button aLayoutBT;

	private CheckBox aChildViewCB;
	private CheckBox aFullScreenCB;

	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * @see EWTEventHandler#handleEvent(EWTEvent)
	 */
	public void handleEvent(EWTEvent rEvent)
	{
		Button  rButton    = (Button) rEvent.getSource();
		boolean bChildView = aChildViewCB.isSelected();

		ContainerBuilder rBuilder =
			createView(rButton.getView(), rButton.getText(), bChildView);
		View			 rView    = (View) rBuilder.getContainer();

		GenericLayout rLayout =
			new FlowLayout(Alignment.FILL,
						   Alignment.FILL,
						   false,
						   Margins.ZERO_MARGINS,
						   3,
						   3,
						   false,
						   false);

		rBuilder = rBuilder.addPanel(AlignedPosition.CENTER, rLayout);

		if (rButton == aButtonBT)
		{
			initButtonView(rBuilder);
		}
		else if (rButton == aTextBT)
		{
			initTextView(rBuilder);
		}
		else if (rButton == aListBT)
		{
			initListView(rBuilder);
		}
		else if (rButton == aCanvasBT)
		{
			initCanvasView(rBuilder);
		}
		else if (rButton == aValueBarBT)
		{
			initBarView(rBuilder);
		}
		else if (rButton == aLayoutBT)
		{
			initLayoutView(rBuilder);
		}

		if (aFullScreenCB.isSelected())
		{
			rView.setMaximized(true);
		}

		rView.pack();
		getContext().displayViewCentered(rView);
	}

	/***************************************
	 * Overridden to display a simple message box because no resource is used
	 * for this application.
	 *
	 * @see EWTMidlet#about()
	 */
	protected void about()
	{
		MessageBox.showNotification(CanvasView.getCurrent(),
									"About",
									"microEWT demonstration MIDlet\n" +
									"(c) 2006 Elmar Sonnenschein / esoco GmbH\n" +
									"Released under the GNU General Public License\n\n",
									MessageBox.ICON_INFORMATION);
	}

	/***************************************
	 * @see EWTMidlet#initApplicationView(View)
	 */
	protected void initApplicationView(View rView)
	{
		ContainerBuilder aBuilder    = new ContainerBuilder(rView);
		GenericLayout    aViewLayout =
			new FlowLayout(Alignment.CENTER,
						   Alignment.CENTER,
						   false,
						   Margins.ZERO_MARGINS,
						   0,
						   0,
						   false,
						   false);
		StyleData		 rSD		 = StyleData.DEFAULT;

		rView.setTitle("EWTSet");
		rView.setLayout(aViewLayout);

		aBuilder =
			aBuilder.addPanel(StyleData.DEFAULT.setFlags(StyleFlag.ETCHED_OUT),
							  new FlowLayout(Alignment.CENTER,
											 Alignment.CENTER,
											 false,
											 new Margins(5),
											 3,
											 3,
											 true,
											 true));
		aBuilder.setActionListener(this);

		aButtonBT   = aBuilder.addButton(rSD, "Buttons", null);
		aTextBT     = aBuilder.addButton(rSD, "Text", null);
		aListBT     = aBuilder.addButton(rSD, "Lists", null);
		aCanvasBT   = aBuilder.addButton(rSD, "Canvas", null);
		aValueBarBT = aBuilder.addButton(rSD, "Bars", null);
		aLayoutBT   = aBuilder.addButton(rSD, "Layouts", null);

		aBuilder.setActionListener(null);
		aChildViewCB  = aBuilder.addCheckBox(rSD, "Child View", null);
		aFullScreenCB = aBuilder.addCheckBox(rSD, "Full Screen", null);
		aBuilder	  = aBuilder.getParent();

		aChildViewCB.setSelected(true);
	}

	/***************************************
	 * Creates an edge layout test panel with the given container builder.
	 *
	 * @param  cb The container builder
	 *
	 * @return A vector containing the buttons in the panel
	 */
	Vector createEdgeLayoutPanel(ContainerBuilder cb)
	{
		cb = cb.addPanel(StyleData.DEFAULT, new EdgeLayout(2));

		Vector aButtons = new Vector();

		aButtons.addElement(cb.addButton(AlignedPosition.TOP_LEFT, "TL", null));
		aButtons.addElement(cb.addButton(AlignedPosition.TOP,
										 "<--- TOP --->",
										 null));
		aButtons.addElement(cb.addButton(AlignedPosition.TOP_RIGHT,
										 "TR",
										 null));
		aButtons.addElement(cb.addButton(AlignedPosition.LEFT, "LEFT", null));
		aButtons.addElement(cb.addButton(AlignedPosition.CENTER,
										 "<--- CENTER --->",
										 null));
		aButtons.addElement(cb.addButton(AlignedPosition.RIGHT, "RIGHT", null));
		aButtons.addElement(cb.addButton(AlignedPosition.BOTTOM_LEFT,
										 "BL",
										 null));
		aButtons.addElement(cb.addButton(AlignedPosition.BOTTOM,
										 "<--- BOTTOM --->",
										 null));
		aButtons.addElement(cb.addButton(AlignedPosition.BOTTOM_RIGHT,
										 "BR",
										 null));

		cb = cb.getParent();

		return aButtons;
	}

	/***************************************
	 * Creates a fill layout test panel with the given container builder.
	 *
	 * @param  cb The container builder
	 *
	 * @return A vector containing the buttons in the panel
	 */
	Vector createFillLayoutPanel(ContainerBuilder cb)
	{
		Vector aButtons = new Vector();

		cb = cb.addPanel(StyleData.DEFAULT, new FillLayout(false, 2));

		for (int i = 1; i <= 4; i++)
		{
			aButtons.addElement(cb.addButton(StyleData.DEFAULT,
											 "Fill " + i,
											 null));
		}
		cb = cb.getParent();

		return aButtons;
	}

	/***************************************
	 * Creates an edge layout test panel with the given container builder.
	 *
	 * @param  cb The container builder
	 *
	 * @return A vector containing the buttons in the panel
	 */
	Vector createFlowLayoutPanel(ContainerBuilder cb)
	{
		final String[] aBtNames =
			new String[]
			{
				"Unequal", "Horizontal", "No Wrap", "Align: ", "Justify: "
			};
		final Button[] aButtons = new Button[aBtNames.length];
		FlowLayout     fl	    = new FlowLayout(Alignment.CENTER, 2);

		cb = cb.addPanel(StyleData.DEFAULT, fl);

		final Container rPanel = cb.getContainer();

		for (int i = 0; i < aBtNames.length; i++)
		{
			aButtons[i] = cb.addButton(StyleData.DEFAULT, aBtNames[i], null);
		}

		cb = cb.getParent();

		aButtons[3].setText(aBtNames[3] + fl.getAlignment());
		aButtons[4].setText(aBtNames[4] + fl.getJustification());

		aButtons[0].addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					FlowLayout fl = (FlowLayout) rPanel.getLayout();

					aButtons[0].setText(fl.isEqualSizes() ? "Unequal"
														  : "Equal");
					updateLayout(rPanel, aButtons);
				}
			}, EventType.ACTION);

		aButtons[1].addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					FlowLayout fl = (FlowLayout) rPanel.getLayout();

					aButtons[1].setText(fl.isHorizontal() ? "Vertical"
														  : "Horizontal");
					updateLayout(rPanel, aButtons);
				}
			}, EventType.ACTION);

		aButtons[2].addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					FlowLayout fl = (FlowLayout) rPanel.getLayout();

					aButtons[2].setText(fl.isWrapping() ? "No Wrap" : "Wrap");
					updateLayout(rPanel, aButtons);
				}
			}, EventType.ACTION);

		aButtons[3].addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					FlowLayout fl = (FlowLayout) rPanel.getLayout();

					aButtons[3].setText(aBtNames[3] +
										nextAlignment(fl.getAlignment()));
					updateLayout(rPanel, aButtons);
				}
			}, EventType.ACTION);

		aButtons[4].addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					FlowLayout fl = (FlowLayout) rPanel.getLayout();

					aButtons[4].setText(aBtNames[4] +
										nextAlignment(fl.getJustification()));
					updateLayout(rPanel, aButtons);
				}
			}, EventType.ACTION);

		Vector aButtonList = new Vector();

		for (int i = 0; i < aButtons.length; i++)
		{
			aButtonList.addElement(aButtons[i]);
		}

		return aButtonList;
	}

	/***************************************
	 * Creates a grid layout test panel with the given container builder.
	 *
	 * @param  cb The container builder
	 *
	 * @return A vector containing the buttons in the panel
	 */
	Vector createGridLayoutPanel(ContainerBuilder cb)
	{
		cb = cb.addPanel(StyleData.DEFAULT, new GridLayout(3, true, 2, 2));

		Vector aButtons = new Vector();

		for (int i = 1; i <= 9; i++)
		{
			aButtons.addElement(cb.addButton(StyleData.DEFAULT,
											 "[" + i + "]",
											 null));
		}
		cb = cb.getParent();

		return aButtons;
	}

	/***************************************
	 * Creates the layout test panel for a certain layout name.
	 *
	 * @param  rBuilder The container builder to create the panel with
	 * @param  sLayout  The layout name
	 *
	 * @return A vector containing the buttons in the panel
	 */
	Vector createLayoutPanel(ContainerBuilder rBuilder, String sLayout)
	{
		if (sLayout.equals("EdgeLayout"))
		{
			return createEdgeLayoutPanel(rBuilder);
		}
		else if (sLayout.equals("FlowLayout"))
		{
			return createFlowLayoutPanel(rBuilder);
		}
		else if (sLayout.equals("GridLayout"))
		{
			return createGridLayoutPanel(rBuilder);
		}
		else if (sLayout.equals("FillLayout"))
		{
			return createFillLayoutPanel(rBuilder);
		}

		return null;
	}

	/***************************************
	 * Creates a new view.
	 *
	 * @param  rView      The parent view
	 * @param  sTitle     The view title
	 * @param  bChildView TRUE for a child view, FALSE for a main view
	 *
	 * @return A container builder for the new view
	 */
	ContainerBuilder createView(View rView, String sTitle, boolean bChildView)
	{
		MenuBar    aMenuBar		  = createApplicationMenu();
		MenuItem   aCloseMenuItem = new MenuItem("Close");
		final View aView;

		((Menu) aMenuBar.getChildItem(0)).addChildItem(aCloseMenuItem);

		if (bChildView)
		{
			aView = getContext().createChildView(rView, ViewStyle.DEFAULT);
		}
		else
		{
			aView = getContext().createMainView(ViewStyle.DEFAULT);
		}

		aView.setTitle(sTitle);
		aView.setMenuBar(aMenuBar);
		aView.setLayout(new EdgeLayout(3));

		aCloseMenuItem.setActionListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					aView.setVisible(false);
				}
			});

		return new ContainerBuilder(aView);
	}

	/***************************************
	 * Initializes the value bar demonstration.
	 *
	 * @param rBuilder The builder to create the components with
	 */
	void initBarView(ContainerBuilder rBuilder)
	{
		rBuilder = rBuilder.addPanel(StyleData.DEFAULT, new EdgeLayout(3));

		final Label		  aLabel     =
			rBuilder.addLabel(AlignedPosition.CENTER,
							  "HScroll: 0\nVScroll: 0",
							  null);
		final ProgressBar aHProgress =
			rBuilder.addProgressBar(AlignedPosition.TOP, false);
		final ProgressBar aVProgress =
			rBuilder.addProgressBar(AlignedPosition.LEFT, true);
		final ScrollBar   aHScroll   =
			rBuilder.addScrollBar(AlignedPosition.BOTTOM, false);
		final ScrollBar   aVScroll   =
			rBuilder.addScrollBar(AlignedPosition.RIGHT, true);

		aLabel.setMinimumSize(80, 80);

		aHScroll.setMaximum(42);
		aHScroll.setVisibleAmount(7);
		aHScroll.setUnitIncrement(7);
		aVScroll.setMaximum(5);
		aVScroll.setVisibleAmount(1);

		aHProgress.setMaximum(100);
		aVProgress.setMaximum(50);

		rBuilder.addPanel(AlignedPosition.BOTTOM_LEFT.setFlags(StyleFlag.BEVEL_LOWERED),
						  EdgeLayout.NO_GAP_LAYOUT);
		rBuilder = rBuilder.getParent();
		rBuilder.addPanel(AlignedPosition.BOTTOM_RIGHT.setFlags(StyleFlag.BEVEL_RAISED),
						  EdgeLayout.NO_GAP_LAYOUT);

//		for (int i = 1; i <= 4; i++)
//		{
//			aList.add("Test " + i);
//		}

		EWTEventHandler aScrollHandler =
			new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					aLabel.setText("HScroll: " + aHScroll.getValue() +
								   "\nVScroll: " + aVScroll.getValue());
					aLabel.repaint();
				}
			};

		aHScroll.addEventListener(aScrollHandler, EventType.VALUE_CHANGED);
		aVScroll.addEventListener(aScrollHandler, EventType.VALUE_CHANGED);

		new Thread()
			{
				public void run()
				{
					try
					{
						while (!aVProgress.getView().isVisible())
						{
							// wait for view to be displayed
						}

						while (aVProgress.getView().isVisible())
						{
							Thread.sleep(100);
							updateProgressBar(aHProgress);
							updateProgressBar(aVProgress);
						}
					}
					catch (InterruptedException e)
					{
						System.out.println("ERR: " + e.getMessage());
						e.printStackTrace();
					}
				}

				void updateProgressBar(ProgressBar rBar)
				{
					int nValue = rBar.getValue() + 1;

					rBar.setValue(nValue > rBar.getMaximum() ? 0 : nValue);
					rBar.repaint();
				}
			}.start();
	}

	/***************************************
	 * Initializes the button demonstration.
	 *
	 * @param rBuilder The builder to create the components with
	 */
	void initButtonView(ContainerBuilder rBuilder)
	{
		StyleData  rBorderStyle =
			StyleData.DEFAULT.setFlags(StyleFlag.ETCHED_OUT);
		GridLayout aGridLayout  = new GridLayout(1);

		rBuilder = rBuilder.addPanel(rBorderStyle, aGridLayout);

		rBuilder.addRadioButton(null, "Radio 1", null);
		rBuilder.addRadioButton(null, "Radio 2", "#/img/error.png");
		rBuilder.addRadioButton(null, "Radio 3", "#/img/info.png");

		rBuilder = rBuilder.getParent();
		rBuilder = rBuilder.addPanel(rBorderStyle, aGridLayout);

		rBuilder.addCheckBox(null, "Check 1", null);
		rBuilder.addCheckBox(null, "Check 2", "#/img/warning.png");
		rBuilder.addCheckBox(null, "Check 2", "#/img/question.png");
	}

	/***************************************
	 * Initializes the canvas demonstration.
	 *
	 * @param rBuilder The builder to create the components with
	 */
	void initCanvasView(ContainerBuilder rBuilder)
	{
		final Canvas rCanvas = rBuilder.addCanvas(StyleData.DEFAULT);

		rCanvas.setPaintHandler(new PaintHandler()
			{
				Random aRandom = new Random();
				int    x1	   = 20;
				int    y1	   = 10;
				int    x2	   = 10;
				int    y2	   = 20;
				int    dx1     = 0;
				int    dy1     = 0;
				int    dx2     = 0;
				int    dy2     = 0;
				int    nCount  = 0;

				public Size getPreferredSize()
				{
					return new Size(100, 100);
				}

				public void paint(GraphicsContext rGraphics,
								  int			  x,
								  int			  y,
								  int			  w,
								  int			  h)
				{
					rGraphics.setColor(Color.YELLOW);
					rGraphics.fillRectangle(x, y, w, h);
					rGraphics.draw3DRect(Color.WHITE,
										 Color.BLACK,
										 2,
										 StyleFlag.BEVEL_LOWERED,
										 x,
										 y,
										 w,
										 h);
					rGraphics.setColor(Color.RED);
					rGraphics.drawLine(x1, y1, x2, y2);

					if (nCount++ % 10 == 0)
					{
						dx1 = aRandom.nextInt(5) - 2;
						dy1 = aRandom.nextInt(5) - 2;
						dx2 = aRandom.nextInt(5) - 2;
						dy2 = aRandom.nextInt(5) - 2;
					}

					x1 += dx1;
					y1 += dy1;
					x2 += dx2;
					y2 += dy2;
					x1 = x1 < 2 ? 2 : (x1 > 97 ? 97 : x1);
					y1 = y1 < 2 ? 2 : (y1 > 97 ? 97 : y1);
					x2 = x2 < 2 ? 2 : (x2 > 97 ? 97 : x2);
					y2 = y2 < 2 ? 2 : (y2 > 97 ? 97 : y2);
				}
			});

		new Thread()
			{
				public void run()
				{
					try
					{
						while (rCanvas.getView().isVisible())
						{
							Thread.sleep(50);
							rCanvas.repaint();
						}
					}
					catch (InterruptedException e)
					{
						System.out.println("ERR: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}.start();
	}

	/***************************************
	 * Initializes a layout test view.
	 *
	 * @param rBuilder The builder to create the components in
	 * @param sLayout  The name of the layout to create the test view for
	 */
	void initLayoutTestView(ContainerBuilder rBuilder, String sLayout)
	{
		EWTEventHandler aHandler =
			new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					Button b = (Button) rEvent.getSource();

					b.setVisible(false);
					b.getParent().repaint();
				}
			};

		final Vector aButtons = createLayoutPanel(rBuilder, sLayout);

		if (!sLayout.equals("FlowLayout"))
		{
			for (int i = 0; i < aButtons.size(); i++)
			{
				((Button) aButtons.elementAt(i)).addEventListener(aHandler,
																  EventType.ACTION);
			}

			rBuilder.addButton(AlignedPosition.TOP, "Reset", null)
					.addEventListener(new EWTEventHandler()
				{
					public void handleEvent(EWTEvent rEvent)
					{
						Button rButton = null;

						for (int i = 0; i < aButtons.size(); i++)
						{
							rButton = (Button) aButtons.elementAt(i);
							rButton.setVisible(true);
						}
						rButton.getView().pack();
					}
				}, EventType.ACTION);

			rBuilder.addButton(AlignedPosition.BOTTOM, "Pack", null)
					.addEventListener(new EWTEventHandler()
				{
					public void handleEvent(EWTEvent rEvent)
					{
						((Button) rEvent.getSource()).getView().pack();
					}
				}, EventType.ACTION);
		}
	}

	/***************************************
	 * Initializes the layout test view.
	 *
	 * @param rBuilder The builder to create the components in
	 */
	void initLayoutView(ContainerBuilder rBuilder)
	{
		EWTEventHandler aHandler =
			new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					Button rButton = (Button) rEvent.getSource();

					ContainerBuilder cb =
						createView(rButton.getView(),
								   rButton.getText(),
								   rButton.getView() instanceof ChildView);

					View rView = (View) cb.getContainer();

					initLayoutTestView(cb, rButton.getText());
					rView.pack();
					getContext().displayViewCentered(rView);
				}
			};

		EventType rAction = EventType.ACTION;

		rBuilder.addButton(null, "FillLayout", null).addEventListener(aHandler,
																	  rAction);
		rBuilder.addButton(null, "GridLayout", null).addEventListener(aHandler,
																	  rAction);
		rBuilder.addButton(null, "FlowLayout", null).addEventListener(aHandler,
																	  rAction);
		rBuilder.addButton(null, "EdgeLayout", null).addEventListener(aHandler,
																	  rAction);
	}

	/***************************************
	 * Initializes the list demonstration.
	 *
	 * @param rBuilder The builder to create the components with
	 */
	void initListView(ContainerBuilder rBuilder)
	{
		final List aList1  = rBuilder.addList(StyleData.DEFAULT);
		Button     aButton;

		for (int i = 1; i <= 5; i++)
		{
			aList1.add("Entry #" + i);
		}

		rBuilder =
			rBuilder.addPanel(StyleData.DEFAULT,
							  new FlowLayout(Alignment.FILL, 3));
		aButton  = rBuilder.addButton(StyleData.DEFAULT, "^", null);

		final TextField aTextField =
			rBuilder.addTextField(StyleData.DEFAULT, "New Entry");

		aButton.addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					aList1.add(aTextField.getText());
					aList1.repaint();
				}
			}, EventType.ACTION);

		aButton = rBuilder.addButton(StyleData.DEFAULT, "v", null);

		rBuilder = rBuilder.getParent();
		rBuilder.addLabel(StyleData.DEFAULT, "Multiselection:", null);

		final List aList2 =
			rBuilder.addList(StyleData.DEFAULT.setFlags(StyleFlag.MULTISELECT));

		for (int i = 1; i <= 5; i++)
		{
			aList2.add("Entry #" + i);
		}
		aButton.addEventListener(new EWTEventHandler()
			{
				public void handleEvent(EWTEvent rEvent)
				{
					aList2.add(aTextField.getText());
					aList2.repaint();
				}
			}, EventType.ACTION);
	}

	/***************************************
	 * Initializes the text component demonstration.
	 *
	 * @param rBuilder The builder to create the components with
	 */
	void initTextView(ContainerBuilder rBuilder)
	{
		GenericLayout aLayout = new GridLayout(2);

		rBuilder = rBuilder.addPanel(StyleData.DEFAULT, aLayout);
		rBuilder.addLabel(StyleData.DEFAULT, "Text: ", null);
		rBuilder.addTextField(StyleData.DEFAULT, "");

		rBuilder = rBuilder.getParent();
		rBuilder = rBuilder.addPanel(StyleData.DEFAULT, aLayout);

		rBuilder.addLabel(StyleData.DEFAULT, "Password: ", null);
		rBuilder.addTextField(StyleData.DEFAULT.setFlags(StyleFlag.PASSWORD),
							  "");
	}

	/***************************************
	 * Returns the next alignment of another alignment instance.
	 *
	 * @param  rAlignment The alignment to return the next instance for
	 *
	 * @return The next alignment
	 */
	Alignment nextAlignment(Alignment rAlignment)
	{
		switch (rAlignment.getCharacter())
		{
			case 'B':
				return Alignment.END;

			case 'E':
				return Alignment.CENTER;

			case 'C':
				return Alignment.FILL;

			default:
				return Alignment.BEGIN;
		}
	}

	/***************************************
	 * Helper method to update the layout in a layout test view.
	 *
	 * @param rPanel   The panel containing the layout
	 * @param rButtons The buttons to create the new layout for
	 */
	void updateLayout(final Container rPanel, final Button[] rButtons)
	{
		String sAlignText   = rButtons[3].getText();
		String sJustifyText = rButtons[4].getText();

		sAlignText   = sAlignText.substring(sAlignText.indexOf(':') + 2);
		sJustifyText = sJustifyText.substring(sJustifyText.indexOf(':') + 2);

		Alignment rAlign   = Alignment.valueOf(sAlignText.charAt(0));
		Alignment rJustify = Alignment.valueOf(sJustifyText.charAt(0));

		boolean bEqualSize  = rButtons[0].getText().equals("Equal");
		boolean bHorizontal = rButtons[1].getText().equals("Horizontal");
		boolean bWrap	    = rButtons[2].getText().equals("Wrap");

		rPanel.setLayout(new FlowLayout(rAlign,
										rJustify,
										bHorizontal,
										Margins.ZERO_MARGINS,
										2,
										2,
										bEqualSize,
										bWrap));

		View rView = rPanel.getView();

		rView.pack();
		rView.getContext().displayViewCentered(rView);
	}
}
