/*
 * Fire (Flexible Interface Rendering Engine) is a set of graphics widgets for creating GUIs for j2me applications. 
 * Copyright (C) 2006  Bluebird co (www.bluebird.gr)
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
/*
 * Created on Aug 25, 2006
 */
package gr.bluevibe.fire.displayables;

import gr.bluevibe.fire.components.FGauge;
import gr.bluevibe.fire.components.Panel;
import gr.bluevibe.fire.components.Popup;
import gr.bluevibe.fire.util.FireIO;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.sun.cldc.i18n.uclc.DefaultCaseConverter;

/**
 * FireScreen is the core of the Fire engine.
 * It is the only Displayable in an Fire based, application (not counting SplashScreen).
 * It is indented to be used as the Display singleton is used when developing a regular j2me midlet.
 *  
 * @author padeler
 *
 */
public final class FireScreen extends Canvas implements Runnable
{
	/**
	 * The step of the inner clock in miliseconds.
	 */
	public static final long CLOCK_STEP = 100;
	/**
	 * The name of the theme key, used by FireIO.
	 */
	public static final String THEME_FILE="firetheme";
	/**
	 * The name of the backgroung key.
	 */
	public static final String THEME_BG="firebg";
	
	
	public static final int CENTRE=0;
	public static final int RIGHT=1;
	public static final int LEFT=2;
	public static final int TOP =3;	
	public static final int BOTTOM = 4;
	public static final int UP =TOP;	
	public static final int DOWN = BOTTOM;
	
	
	public static final int NORMAL=0;
	public static final int LANDSCAPELEFT=1;
	public static final int LANDSCAPERIGHT=2;
	
	public static final int SCROLL_COUNT=3;
	public static final int ANIMATION_COUNT=4;
	
	public static final int SCROLLBAR_WIDTH=5;
	public static final int SCROLLBAR_HEIGHT=16;
	
	public static final int ALERT_VERTICAL_OFFSET=30;
	public static final int ALERT_HORIZONTAL_OFFSET=5;	
	
	public static final String defaultLabel = "";
	public static final Font defaultLabelFont = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL);
	public static final Font defaultPopupFont = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
	public static final Font defaultTickerFont = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
	public static final int defaultTickerColor = 0xFF000000;
	
	public static final int defaultVpos = CENTRE;
	public static final int defaultHpos = CENTRE;
	public static final boolean defaultVertical = false;
	public static final boolean defaultTiled = false;
	
	private static FireScreen singleton=null;
	
	
	/**
	 * Used to create and retrieve the FireScreen singleton.
	 * @param display, if not null and its the first call of the method, a FireScreen instance for this display is created.
	 * @return the FireScreen singleton. 
	 */
	public static FireScreen getScreen(Display display)
	{
		if(display!=null && singleton==null)
		{
			singleton = new FireScreen(display);
		}
		return singleton;
	}

	/* Key mapping info. */
	public static int leftSoftKey = -6;
	public static int rightSoftKey = -7;
	
	private static final Object[][] keyMaps = {
			{"Nokia",new Integer(-6),new Integer(-7)},{"ricsson",new Integer(-6),new Integer(-7)},
			{"iemens",new Integer(-1),new Integer(-4)},{"otorola",new Integer(-21),new Integer(-22)},
			{"harp",new Integer(-21),new Integer(-22)},{"j2me",new Integer(-6),new Integer(-7)}
	};
	
	/* Default Theme Data */ 
	public static int selectedLinkColor;	
	public static int linkColor;
	public static Integer selectedLinkBgColor;
	public static int defaultLabelColor;
	public static int defaultColor;
	public static int defaultFilledRowColor,defaultSecondaryFilledRowColor,defaultBorderColor,defaultPointerColor,defaultSecondaryPointerColor;
	public static int defaultTooltipFgColor=0x00000000,defaultTooltipBgColor=0x00FFFFFF;
	
	public static int defaultScrollColor;	
	public static int defaultRulerColor1;
	public static int defaultRulerColor2;
	public static int defaultScrollRulerColor1;
	public static int defaultScrollRulerColor2;
	public static int defaultScrollRulerColor3;
	public static Image defaultGradImage;
	public static Image defaultBgImageSrc;
	public static Image defaultLogo;
	public static Image defaultBorder;

	public static int SCROLL_HEIGHT=50;
	public static int SCROLL_STEP = 10;

	

	private static int logoPossition=RIGHT;


	public static int topOffset;
	public static int bottomOffset;
	

	
	private Image bgImage=null;
	
	/** Theme params */
	private Font labelFont = defaultLabelFont;
	private int vpos = defaultVpos;
	private int hpos = defaultHpos;
	private boolean vertical = defaultVertical;
	private boolean tiled = defaultTiled;

	
	
	/** Support Vars */
	private Display display=null;
	private Panel panel=null;
	private Vector popups;
	private Image offscreen=null;
	private boolean forceRepaint=false, panelAnimation=false, userActionRepaint=false;
	private FGauge innerGauge=null;
	
	private boolean alive=false;
	
	private Image animationImage=null;
	private int animationDirection=UP;
	private int animationFrameCount=0;
	private int animationX,animationY;
	
	private boolean busyMode=false;
	private boolean busyModeRepaint=false;
	private Integer orientationKey=null;
	private int orientation=NORMAL;
	
	private boolean interactiveBusyMode=false;
	
	
	private void loadTheme()
	{
		defaultGradImage=null;
		defaultBgImageSrc=null;
		defaultLogo=null;
		defaultBorder=null;
		
		// first check record store for a custom theme.
		
		Image img = FireIO.getLocalImage(THEME_FILE);
				
		defaultLogo = Image.createImage(50,15);
		Graphics g = defaultLogo.getGraphics();
		g.drawImage(img,0,0,Graphics.TOP|Graphics.LEFT);


		int bgrad[]= new int[15];
		img.getRGB(bgrad,0,15,35,16,15,1);
		defaultBorder = Image.createRGBImage(bgrad,15,1,false);
		int []palete = new int[50];
		
		img.getRGB(palete,0,50,0,16,50,1);
		int tmp = palete[12];
		System.out.println("12 : "+Integer.toHexString(tmp));
		
		if((tmp&0x00FFFFFF)==0x00FFFFFF) // show grad
		{
			bgrad= new int[50];
			img.getRGB(bgrad,0,50,0,15,50,1);
			defaultGradImage = Image.createRGBImage(bgrad,50,1,true);
		}
		else
		{
			System.out.println("Grad background is not displayed.");
			defaultGradImage=null;
		}
	
		defaultColor = palete[0];
//		System.out.println("0 : "+Integer.toHexString(palete[0]));
		
		defaultLabelColor = palete[1];
//		System.out.println("1 : "+Integer.toHexString(palete[1]));

		defaultScrollColor = palete[2];
//		System.out.println("2 : "+Integer.toHexString(palete[2]));

//		System.out.println("3 : "+Integer.toHexString(palete[3]));
		linkColor = palete[3];
//		System.out.println("4 : "+Integer.toHexString(palete[4]));
		selectedLinkColor = palete[4];
		
//		System.out.println("5 : "+Integer.toHexString(palete[5]));
		tmp = palete[5];
		if((tmp&0xFF000000)!=0xFF000000) // dont show bg color
		{
			selectedLinkBgColor = null;
		}
		else
		{
			selectedLinkBgColor = new Integer(tmp);
		}

		
		defaultRulerColor1 = palete[6];
//		System.out.println("6 : "+Integer.toHexString(palete[6]));

		defaultRulerColor2 = palete[7];
//		System.out.println("7 : "+Integer.toHexString(palete[7]));

		defaultScrollRulerColor1= palete[8];
//		System.out.println("8 : "+Integer.toHexString(palete[8]));

		defaultScrollRulerColor2 = palete[9];
//		System.out.println("9 : "+Integer.toHexString(palete[9]));
		defaultScrollRulerColor3 = palete[10];
//		System.out.println("10 : "+Integer.toHexString(palete[10]));
		
		defaultFilledRowColor = palete[13];
		
		defaultSecondaryFilledRowColor = palete[14];
		
		defaultBorderColor = palete[15];
		defaultPointerColor = palete[16];
		defaultSecondaryPointerColor = palete[17];
		defaultTooltipFgColor = palete[18];
		defaultTooltipBgColor = palete[19];
		
		
		tmp = palete[11];
		if((tmp&0x00FFFFFF)==0x00FFFFFF) // show bg image
		{
			img = FireIO.getLocalImage(THEME_BG);
			defaultBgImageSrc = img;
		}
		else
		{
			defaultBgImageSrc=null;
		}
		
		String platform = System.getProperty("microedition.platform");
		for(int i =0 ;i< keyMaps.length;++i)
		{
			String manufacturer = (String)keyMaps[i][0];
			
			if(platform.indexOf(manufacturer)!=-1)
			{
				if(i==1) // ta sony ericsson exoun enalaktika keys sta p800/p900/p908/p802
				{
					if(platform.indexOf("P900")!=-1 || platform.indexOf("P908")!=-1)
					{
						leftSoftKey = ((Integer)keyMaps[i][2]).intValue();
						rightSoftKey = ((Integer)keyMaps[i][1]).intValue();
					}
					else
					{
						leftSoftKey = ((Integer)keyMaps[i][1]).intValue();
						rightSoftKey = ((Integer)keyMaps[i][2]).intValue();
					}
				}
				else
				{
					leftSoftKey = ((Integer)keyMaps[i][1]).intValue();
					rightSoftKey = ((Integer)keyMaps[i][2]).intValue();
				}
				break;
			}
		}
	}
	
	/**
	 * Creates all the decoration of a canvas based on the default theme values and its parameters.
	 * @param width
	 * @param height
	 * @param color
	 * @param gradImage
	 * @param bg
	 * @param hpos
	 * @param vpos
	 * @param vertical
	 * @param tiled
	 * @return An Image with the decorations (theme) of the FireScreen
	 */
	private Image prepareBgImage(int width,int height)
	{
		int color= defaultColor;
		Image gradImage = defaultGradImage;
		Image bg = defaultBgImageSrc;
		int hpos = defaultHpos;
		int vpos = defaultVpos;
		boolean vertical = defaultVertical;
		boolean tiled =defaultTiled;
		
		bottomOffset = defaultLabelFont.getHeight()+2;
				
		if(defaultLogo.getHeight()+2> bottomOffset)
		{
			topOffset= defaultLogo.getHeight()+2;
		}
		else
		{
			topOffset = bottomOffset;
		}
		
		System.out.println("BottomOffset: "+bottomOffset );
		System.out.println("TopOffset: "+topOffset);
		
		Image img = Image.createImage(width,height);
		Graphics g = img.getGraphics();
		if(gradImage==null)
		{
			g.setColor(color);
			g.fillRect(0,0,width,height);
		}
		else
		{
			int srcLen = gradImage.getWidth() * gradImage.getHeight();
			int srcColorMap[] = new int[srcLen];
			gradImage.getRGB(srcColorMap,0,gradImage.getWidth(),0,0,gradImage.getWidth(),gradImage.getHeight());
			
			int len;
			if(vertical) len = width;
			else len = height;
			
			int []colorMap = new int[len];
			for(int i=0;i<len;++i)
			{
				colorMap[i] = srcColorMap[(i*srcLen)/len];
			}
			srcColorMap=null;
			
			if(vertical)
			{
				int i;
				for(i=0;i<width;++i)
				{
					g.setColor(colorMap[i]);
					g.drawLine(i,0,i,height);
				}
			}
			else
			{
				int i;
				for(i=topOffset;i<len-bottomOffset;++i)
				{
					g.setColor(colorMap[i-topOffset]);
					g.drawLine(0,i,width,i);
				}
			}
		}
		
		if(defaultBorder!=null)
		{
			// draw top border.
			int len = defaultBorder.getWidth() * defaultBorder.getHeight();
			int colorMap[] = new int[len];
			defaultBorder.getRGB(colorMap,0,defaultBorder.getWidth(),0,0,defaultBorder.getWidth(),defaultBorder.getHeight());
//			System.out.println("Rendering Border GRADIENT: "+len);
			int i;
			for(i=0;i<(topOffset-1) && i<len ;++i)
			{
				g.setColor(colorMap[i]&0x00FFFFFF);
				g.drawLine(0,i,width,i);
			}
			for(i=len;i<(topOffset-1);++i)
			{
				g.drawLine(0,i,width,i);				
			}
			g.setColor(defaultRulerColor2);
			g.drawLine(0,topOffset-2,width,topOffset-2);
			g.setColor(defaultRulerColor1);
			g.drawLine(0,topOffset-1,width,topOffset-1);
			
			// draw bottom border.
			int p = height-bottomOffset+2;
			for(i=0;i<bottomOffset && i<len;++i)
			{
				g.setColor(colorMap[i]);
				g.drawLine(0,p+i,width,p+i);
			}
			for(i=len;i<bottomOffset;++i)
			{
				g.drawLine(0,p+i,width,p+i);
			}
			
			
			g.setColor(defaultRulerColor2);
			g.drawLine(0,height-bottomOffset+2,width,height-bottomOffset+2);
			g.setColor(defaultRulerColor1);
			g.drawLine(0,height-bottomOffset+1,width,height-bottomOffset+1);
		}

		if(defaultLogo!=null)
		{
			switch (logoPossition)
			{
				case LEFT:
					g.drawImage(defaultLogo,0,0,Graphics.TOP|Graphics.LEFT);					
					break;
				case CENTRE:
					g.drawImage(defaultLogo,(width/2)-(defaultLogo.getWidth()/2),0,Graphics.TOP|Graphics.LEFT);					
					break;
				default: // default is RIGHT.
					g.drawImage(defaultLogo,width-defaultLogo.getWidth(),0,Graphics.TOP|Graphics.LEFT);
					break;
			}
		}
		
		// draw bgImage
		if(bg!=null)
		{
			int w = width;
			int h = height-topOffset-bottomOffset;
			
			int offx = bg.getWidth()/2,offy=bg.getHeight()/2;
			int x=0,y=0;
			if(hpos==CENTRE) x = w/2 - offx;
			else if(hpos==LEFT) x=0;
			else if(hpos==RIGHT) x=w-offx-offx;
			
			if(vpos==CENTRE) y = h/2-offy; 
			else if(vpos==TOP) y= 0;
			else if(vpos==BOTTOM) y=h-offy-offy;
			
			g.drawImage(bg,x,y+topOffset,Graphics.TOP|Graphics.LEFT);
		}
		
		
		// draw scroll bar area.		
		g.setColor(defaultScrollRulerColor1);
		g.drawLine(width-SCROLLBAR_WIDTH,topOffset,width-SCROLLBAR_WIDTH,height-bottomOffset);
		g.drawLine(width-SCROLLBAR_WIDTH,topOffset,width,topOffset);
		g.drawLine(width-SCROLLBAR_WIDTH,height-bottomOffset,width,height-bottomOffset);
		
		g.setColor(defaultScrollRulerColor2);
		g.drawLine(width-SCROLLBAR_WIDTH+1,topOffset+1,width-SCROLLBAR_WIDTH+1,height-bottomOffset-1);
		
		g.setColor(defaultScrollRulerColor3);
		g.fillRect(width-SCROLLBAR_WIDTH+2,topOffset+1,SCROLLBAR_WIDTH-2,height-topOffset-bottomOffset-1);
		
		return img;
	}
	
	
	private FireScreen(Display display)
	{
		this.display = display;
		popups = new Vector();
		bgImage = null;
		alive = true;
		loadTheme();
		bgImage=prepareBgImage(getWidth(),getHeight());
		
		innerGauge = new FGauge();
		innerGauge.setWidth(bottomOffset-1);
		innerGauge.setHeight(bottomOffset-1);
		
		display.callSerially(this);
	}
	

	protected void paint(Graphics sg)
	{
		int width = getWidth();
		int height = getHeight();
		
		if(offscreen==null || offscreen.getWidth()!=width || offscreen.getHeight()!=height)
		{// some implementations dont call correctly the sizeChanged() method so we cannot rely on it.
			offscreen = Image.createImage(width,height);
		}
		
		/* **************** Code to Handle animations and fancy stuff ********************** */
		if(animationImage!=null)
		{
			Graphics g = offscreen.getGraphics();
			int h,w,diff,py,px;
			switch (animationDirection)
			{
				case UP:
					h = animationImage.getHeight();
					w = animationImage.getWidth();
					diff = h/ANIMATION_COUNT;
					py = animationY+h-(diff*animationFrameCount);
					g.setClip(animationX,py,w,(diff*animationFrameCount));
					g.drawImage(animationImage,animationX,py,Graphics.LEFT|Graphics.TOP);
					break;
				case DOWN:
					h = animationImage.getHeight();
					w = animationImage.getWidth();
					diff = h/ANIMATION_COUNT;
					py = animationY-h+(diff*animationFrameCount);
					g.setClip(animationX,animationY,w,(diff*animationFrameCount));
					g.drawImage(animationImage,animationX,py,Graphics.LEFT|Graphics.TOP);
					break;
				case LEFT:
					h = animationImage.getHeight();
					w = animationImage.getWidth();
					diff = w/ANIMATION_COUNT;
					px = animationX-(diff*animationFrameCount);
					py= animationY;
					//sg.setClip(px,py,(diff*animationFrameCount),h);
					//sg.drawImage(animationImage,px,py,Graphics.LEFT|Graphics.TOP);
					g.drawRegion(animationImage,0,0,diff*animationFrameCount,h,Sprite.TRANS_NONE,px,py,Graphics.LEFT|Graphics.TOP);
					break;
				case RIGHT:
					h = animationImage.getHeight();
					w = animationImage.getWidth();
					diff = w/ANIMATION_COUNT;
					px = animationX;
					py= animationY;
					int sw = (diff*animationFrameCount);
					g.drawRegion(animationImage,w-sw,0,sw,h,Sprite.TRANS_NONE,px,py,Graphics.LEFT|Graphics.TOP);
					break;
				default:
					break;
			}
			finalPaint(offscreen,sg,width,height);
			return;
		}
		if(!forceRepaint && !userActionRepaint)
		{
			if(busyModeRepaint)
			{
				busyModeRepaint=false;
				width = super.getWidth();
				height = super.getHeight();
				int gw = innerGauge.getWidth();
				int gh = innerGauge.getHeight();
				switch (orientation)
				{
					case LANDSCAPELEFT:
						sg.translate(width-gw,height/2-gh/2);
						sg.setClip(0,0,gh,gw);
						break;
					case LANDSCAPERIGHT:
						sg.translate(0,height/2-gw/2);
						sg.setClip(0,0,gh,gw);
						break;
					default:
						sg.translate(width/2-gw/2,height-gh);
						sg.setClip(0,0,gw,gh);
						break;
				}
				innerGauge.paint(sg);

				return;
			}
			if(panelAnimation)
			{
				Graphics g = offscreen.getGraphics();
				panelAnimation=false;
				if(popups.size()>0)
				{
					Popup popup = (Popup)popups.lastElement();
					g.translate(popup.getPosX(),popup.getPosY());
					g.setClip(0,0,popup.getWidth(),popup.getHeight());
					popup.paint(g);
				}
				else if(panel!=null)// draw panel if any.
				{
					g.setClip(0,0,width,height);
					panel.paint(g);
				}
				finalPaint(offscreen,sg,width,height);
				return;
			}
		}
		
		/* ************************* Code to Handle usefull painting operations ************************ */
		if(bgImage==null)
		{
			bgImage=prepareBgImage(getWidth(),getHeight());
			if(panel!=null)
			{
				panel.validate();
			}
		}
		Graphics g = offscreen.getGraphics();
		g.translate(-g.getTranslateX(),-g.getTranslateY()); // reset.
		if(!forceRepaint) // in normal operation, only the top level panel is redrawn.
		{
			userActionRepaint=false;
			if(popups.size()>0)
			{
				Popup popup = (Popup)popups.lastElement();
				g.translate(popup.getPosX(),popup.getPosY());
				g.setClip(0,0,popup.getWidth(),popup.getHeight());
				popup.paint(g);
			}
			else if(panel!=null)// draw panel if any.
			{
				g.setClip(0,0,width,height);
				panel.paint(g);
			}
		}
		else // if a repaint is forced, everything is redrawn.
		{
			forceRepaint = false;
			if(panel!=null)// draw panel if any.
			{
				g.setClip(0,0,width,height);
				panel.paint(g);
			}
			for(int i =0;i<popups.size();++i) // redraw all popups.
			{
				Popup popup = (Popup)popups.elementAt(i);
				g.translate(-g.getTranslateX(),-g.getTranslateY()); // reset.
				g.translate(popup.getPosX(),popup.getPosY());
				g.setClip(0,0,popup.getWidth(),popup.getHeight());
				popup.paint(g);
			}
		}
		finalPaint(offscreen,sg,width,height);
	}
	
	private void finalPaint(Image src, Graphics dest,int width,int height)
	{
		if(orientation==NORMAL)
		{
			dest.drawImage(src,0,0,Graphics.TOP|Graphics.LEFT);
		}
		else if(orientation==LANDSCAPELEFT)
		{
			dest.drawRegion(src,0,0,width,height,Sprite.TRANS_ROT270,0,0,Graphics.TOP|Graphics.LEFT);
		}
		else if(orientation==LANDSCAPERIGHT)
		{
			dest.drawRegion(src,0,0,width,height,Sprite.TRANS_ROT90,0,0,Graphics.TOP|Graphics.LEFT);
		}
	}
	

	/**
	 * Sets Displayable c as the Current display.
	 * @param c a displayable
	 */
	public void setCurrent(Displayable c)
	{
		display.setCurrent(c);
	}
	
	/**
	 * Returns the current panel set on the FireScreen.
	 * @return 
	 */
	public Panel getCurrentPanel()
	{
		return panel;
	}
	/**
	 * Set a panel to the FireScreen. 
	 * @param p 
	 */
	public void setCurrent(Panel p)
	{
		setCurrent(p,LEFT);
	}
	
	/**
	 * Shows the panel p on the screen with the supplied animation direction.
	 * @param p 
	 * @param animDirection 
	 */
	public void setCurrent(Panel p,int animDirection)
	{
		popups.removeAllElements();
		p.setWidth(getWidth());
		p.setHeight(getHeight());
		p.validate();
		
		
		p.resetPointer();
		
		Displayable dsp = display.getCurrent();
		if(dsp!=this)
		{
			display.setCurrent(this);
			panel=null;
		}

		if(panel==null) // den exei animation.
		{
			panel = p;
			repaint();
		}
		else
		{ // kanoume ena animation gia tin pozeria.
			int w = getWidth();
			int h = getHeight();
			Image img = Image.createImage(w,h);
			Graphics g = img.getGraphics();
			p.paint(g);
			g.setColor(0x00000000);
			g.translate(-g.getTranslateX(),-g.getTranslateY());
			g.setClip(0,0,w,h);
			g.drawRect(0,0,w,h);
			
			animationDirection=animDirection;
			animationFrameCount=0;
			if(animDirection==LEFT)
			{
				animationX = getWidth();
			}
			else if(animDirection==RIGHT)
			{
				animationX=0;
			}
			animationY = 0;
			animationImage= img;
			panel=p;
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		if(alive)
		{
			try{
				try{Thread.sleep(CLOCK_STEP);} catch (InterruptedException e)	{	}
				Displayable disp;
				disp = display.getCurrent();
				if(disp!=null && disp instanceof FireScreen)
				{ // stin othonh exoume ena panel (kai oxi textbox )
					// check for animation
					if(animationImage!=null)
					{
						animationFrameCount++;
						if(animationFrameCount>ANIMATION_COUNT)
						{
							animationImage=null;
							forceRepaint=true;
						}
					}
					
					if(popups.size()>0)
					{
						Panel p= (Panel)popups.lastElement();
						if(p.isAnimated() && p.clock())
						{
							panelAnimation=true;
						}
					}
					else if(panel!=null && panel.isAnimated())
					{
						boolean repaint = panel.clock();
						if(repaint)
						{
							panelAnimation=true;
						}
					}
					if(busyMode)
					{
						busyModeRepaint=true;
						innerGauge.clock();
					}
					
					if(forceRepaint || animationImage!=null|| busyModeRepaint || panelAnimation)
					{
						repaint();
					}
				}
				else if(disp!=null && disp instanceof SplashScreen) //nice hack for the SplashScreen animation. 
				{
					((SplashScreen)disp).clock();
					((SplashScreen)disp).repaint();
				}
			}catch(Throwable e)
			{
				System.out.println("Error: "+e.getClass().getName()+". Msg: "+e.getMessage());
			}
			display.callSerially(this);
		}
	}

	public static int getTopOffset()
	{
		return topOffset;
	}


	public static int getBottomOffset()
	{
		return bottomOffset;
	}


	public Font getLabelFont()
	{
		return labelFont;
	}
	

	public void setLabelFont(Font labelFont)
	{
		this.labelFont = labelFont;
	}

	public boolean isTiled()
	{
		return tiled;
	}


	public void setTiled(boolean tiled)
	{
		if(this.tiled!=tiled)
		{
			bgImage=null;
			this.tiled = tiled;
		}
	}


	public boolean isVertical()
	{
		return vertical;
	}


	public void setVertical(boolean vertical)
	{
		bgImage=null;
		this.vertical = vertical;
	}


	public int getVpos()
	{
		return vpos;
	}


	public void setVpos(int vpos)
	{
		if(this.vpos!=vpos)
		{
			bgImage=null;
			this.vpos = vpos;
		}
	}
	
	/**
	 * This method is called by the vm every time the with and the height of the screen change. The parameters are the real width and height of the screen.
	 * @see javax.microedition.lcdui.Displayable#sizeChanged(int, int)
	 */
	protected void sizeChanged(int ww, int hh)
	{ // force recalculation of backgrounds.
		int w,h;
		
		if(orientation==NORMAL) // keep in mind the screen orientation.
		{
			w=ww;
			h=hh;
		}
		else
		{
			w = hh;
			h =ww;
		}
				
		SCROLL_HEIGHT = h/2;
		SCROLL_STEP = SCROLL_HEIGHT/SCROLL_COUNT;
		bgImage=prepareBgImage(w,h);
		offscreen = null;
		
		if(panel!=null)
		{
			panel.setWidth(w);
			panel.setHeight(h);
			panel.validate();
		}
		for(int i =0 ;i< popups.size();++i)
		{
			Panel popup = (Panel)popups.elementAt(i);
			popup.setWidth(w-SCROLLBAR_WIDTH); // den tha fenete oraio, alla toulaxiston tha mini to functionallity tou popup.
			popup.validate();
		}
		try{
			System.gc(); // now its a very good time for garbage collection
		}catch(Throwable e){
			System.out.println("Failed to call garbage collector."+e.getMessage());
			e.printStackTrace();
		}
	}

	
	public static int getLeftSoftKey()
	{
		return leftSoftKey;
	}

	public static void setLeftSoftKey(int leftSoftKey)
	{
		FireScreen.leftSoftKey = leftSoftKey;
	}

	public static int getRightSoftKey()
	{
		return rightSoftKey;
	}

	public static void setRightSoftKey(int rightSoftKey)
	{
		FireScreen.rightSoftKey = rightSoftKey;
	}

	public void setContainerCurrent()
	{
		if(panel!=null)
		{
			setCurrent(panel);
		}				
	}

	public Image getBgImageSrc()
	{
		return defaultBgImageSrc;
	}


	public void setBgImageSrc(Image bgImageSrc)
	{
		bgImage=null;
		defaultBgImageSrc = bgImageSrc;
	}


	public int getColor()
	{
		return defaultColor;
	}

	public void setColor(int color)
	{
		if(defaultColor!=color)
		{
			bgImage=null;
			defaultColor = color;
		}
	}


	public Image getGradImage()
	{
		return defaultGradImage;
	}


	public void setGradImage(Image gradImage)
	{
		bgImage=null;
		defaultGradImage = gradImage;
	}


	public int getHpos()
	{
		return hpos;
	}


	public void setHpos(int hpos)
	{
		if(this.hpos!=hpos)
		{
			bgImage=null;
			this.hpos = hpos;
		}
	}


	public Image getBgImage()
	{
		return bgImage;
	}
	
	protected void pointerReleased(int x, int y)
	{
		if(animationImage!=null) return; // ignore events while in animation.
		
		if(orientation!=NORMAL) 
		{
			int tmp = y;
			if(orientation==LANDSCAPERIGHT)
			{
				y= getHeight()-x;
				x=tmp;
			}
			else
			{
				y= x;
				x=getWidth()-tmp;				
			}
		}
		if(y<getHeight()-bottomOffset && busyMode && !interactiveBusyMode) return; // only the cancel command is allowed 
		
		if(popups.size()>0)
		{
			Popup p = (Popup)popups.lastElement();
			if(p.pointerEvent(x-p.getPosX(),y-p.getPosY())) // XXX Thelei mono to top level popup na ginete repaint gia na glitonoume CPU TIme 
			{
				userActionRepaint=true;
				repaint();
			}
		}
		else if(panel!=null)
		{
			if(panel.pointerEvent(x,y))
			{
				userActionRepaint=true;
				repaint();
			}
		}		
	}
	
	protected void keyReleased(int k)
	{
		if(animationImage!=null) return; // ignore events while in animation.
		
		if(orientationKey!=null && k==orientationKey.intValue())
		{ // change the orientation of the screen
			if(orientation==NORMAL)
			{
				orientation=LANDSCAPELEFT;
			}
			else if(orientation==LANDSCAPELEFT)
			{
				orientation = LANDSCAPERIGHT;
			}
			else if(orientation==LANDSCAPERIGHT)
			{
				orientation=NORMAL;
			}
			sizeChanged(super.getWidth(),super.getHeight());
			repaint();
			return;
		}
		
		int kkey,key;
		boolean repaint;
		if(k==FireScreen.leftSoftKey) key = Canvas.GAME_A;
		else if(k==FireScreen.rightSoftKey) key = Canvas.GAME_B;
		else
		{
			kkey = getGameAction(k);
			switch(orientation)
			{
				case LANDSCAPELEFT:// translate joystick for left handed land scape.
					switch(kkey)
					{
						case Canvas.UP:
							key=Canvas.RIGHT;break;
						case Canvas.DOWN:
							key=Canvas.LEFT;break;
						case Canvas.LEFT:
							key=Canvas.UP;break;
						case Canvas.RIGHT:
							key=Canvas.DOWN;break;
						default:
							if(k==Canvas.KEY_NUM9)
								key=Canvas.GAME_A;
							else if(k==Canvas.KEY_NUM3)
								key=Canvas.GAME_B;
							else
								key=kkey;
					}
					break;
				case LANDSCAPERIGHT:// translate joystick for right handed land scape.
					switch(kkey)
					{
						case Canvas.UP:
							key=Canvas.LEFT;break;
						case Canvas.DOWN:
							key=Canvas.RIGHT;break;
						case Canvas.RIGHT:
							key=Canvas.UP;break;
						case Canvas.LEFT:
							key=Canvas.DOWN;break;
						default:
							if(k==Canvas.KEY_NUM1)
								key=Canvas.GAME_A;
							else if(k==Canvas.KEY_NUM7)
								key=Canvas.GAME_B;
							else 
								key=kkey;
					}
					break;
				default: // normal orientation
					switch(k)
					{
						case Canvas.KEY_NUM1:
							key=Canvas.GAME_A;break;
						case Canvas.KEY_NUM3:
							key=Canvas.GAME_B;break;
						default:
							key=kkey;
					}
			}
		}
		
		if((busyMode && !interactiveBusyMode)  && key!=Canvas.GAME_A) return; // only cancel event is allowed.
		
		if(popups.size()>0)
		{
			Panel p = (Panel)popups.lastElement();
			repaint = p.keyEvent(key);
			if(repaint)
			{
				userActionRepaint=true;
				repaint();
			}
 		}
		else if(panel!=null)
		{
			repaint = panel.keyEvent(key);
			if(repaint)
			{
				userActionRepaint=true;
				repaint();
			}
		}
	}

	/**
	 * Shows a popup panel on the screen. The popups can stuck on each other.
	 * @param popup
	 */
	public void showPopup(Popup popup)
	{
		popup.validate();
		popup.resetPointer();
		Image img = Image.createImage(popup.getWidth(),popup.getHeight());
		popup.paint(img.getGraphics());
		

		animationFrameCount=0;
		animationX=popup.getPosX();
		animationY=popup.getPosY();
		
		if(animationY<0)
		{
			animationDirection = DOWN;
		}
		else
		{
			animationDirection = UP;
		}
		
		animationImage=img;
		popups.addElement(popup);
	}

	/**
	 * Closes the top-level popup, shown on the FireScreen
	 * @return
	 */
	public boolean closePopup()
	{
		if(popups.size()>0)
		{ // remove top-level popup
			popups.removeElementAt(popups.size()-1);
			forceRepaint =true;
			repaint();
			return true;
		}
		return false;
	}


	public boolean isBusyMode()
	{
		return busyMode;
	}


	/**
	 * When a FireScreen instance is in busy mode, it displayes a "busy indicator" 
	 * gauge on the bottom of the screen.
	 * If the screen instance is not in interactiveBusyMode, it will not allow user input until the busy mode is set to false.
	 *  
	 * @param busyMode
	 */
	public void setBusyMode(boolean busyMode)
	{
		this.busyMode = busyMode;
		if(!interactiveBusyMode)
		{
			if(!busyMode)  
			{// repaint the screen so that the shaded offscreen is 			
				busyModeRepaint=false;
				forceRepaint=true;
			}
		}
	}

	/**
	 * Destroys the FireScreen instance. This method should be called on clean-up 
	 * in order to stop the animation thread inside the FireScreen Singleton. 
	 *
	 */
	public void destroy()
	{
		if(alive)
		{
			try{
				alive=false;
			}catch(Throwable e){}
		}
		singleton=null;
	}
	
	public void reloadTheme()
	{
		loadTheme();
		bgImage=prepareBgImage(getWidth(),getHeight());
		offscreen=Image.createImage(getWidth(),getHeight());
		
		forceRepaint=true;
	}
	
	/**
	 * Sets the possition of the logo (found in the theme file) on the top border of a panel. 
	 * Valid options are FireScreen.RIGHT, FireScreen.CENTER, FireScreen.LEFT.
	 * Default value is RIGHT. All values different than the three mentioned will be ignored. 
	 * @param pos
	 */
	public static void setLogoPossition(int pos)
	{
		if(pos==CENTRE || pos==LEFT || pos==RIGHT)
			logoPossition = pos;
	}
	
	public static int getLogoPossition()
	{
		return logoPossition;
	}


	public int getOrientation()
	{
		return orientation;
	}


	/**
	 * Sets the orientation of the screen. 
	 * Possible values: NORMAL, LANDSCAPELEFT, LANDSCAPERIGHT
	 * Default value is NORMAL. All values different than the three mentionted will be ignored.
	 * @param orientation
	 */
	public void setOrientation(int o)
	{
		if(o==NORMAL || o==LANDSCAPELEFT || o==LANDSCAPERIGHT)
		{
			orientation = o;
			sizeChanged(super.getWidth(),super.getHeight());
			repaint();
		}
	}
	
	/**
	 * Sets the keycode (@see Canvas) that changes the orientation of the screen. 
	 * If the parameter is null, then no shortcut is set. 
	 * Repeated presses to the "key" will circle through the orientation modes.
	 * 
	 * Note that the orientationKey event is not sent to the panel(s) inside the FireScreen.
	 * 
	 * @param key
	 */
	public void setOrientationChangeKey(Integer key)
	{
		this.orientationKey = key;
	}
	
	/**
	 * Returns the width of this FireScreen. If the screen is in landscape mode, it will return the real height of the screen.
	 * @see javax.microedition.lcdui.Displayable#getWidth()
	 */
	public int getWidth()
	{
		if(orientation==NORMAL)
		{
			return super.getWidth();
		}
		return super.getHeight();
	}
	
	/**
	 * Returns the height of this FireScreen. If the screen is in landscape mode, it will return the real width of the screen.
	 * @see javax.microedition.lcdui.Displayable#getHeight()
	 */
	public int getHeight()
	{
		if(orientation==NORMAL)
		{
			return super.getHeight();
		}
		return super.getWidth();
	}
	
	
	
	/**
	 * Return left softkey shortcut, depending on the screen orientation (left/right landscare or normal) 
	 * @return
	 */
	public String getLeftSoftKeyShortcut()
	{
		switch (orientation)
		{
			case LANDSCAPELEFT:
				return "[9]";
			case LANDSCAPERIGHT:
				return "[1]";
			default:
				return "[1]";
		}
	}
	
	/**
	 * Returns right softkey shortcut, depending on the screen orientation (left/right landscare or normal)
	 * @return 
	 */
	public String getRightSoftKeyShortcut()
	{
		switch (orientation)
		{
			case LANDSCAPELEFT:
				return "[3]";
			case LANDSCAPERIGHT:
				return "[7]";
			default:
				return "[3]";
		}
	}


	/**
	 * If this FireScreen instance is in interactive busy mode, it will allow user actions when busyMode==true.
	 * (see the setBusyMode() method)	 
	 * @return
	 */
	public boolean isInteractiveBusyMode()
	{
		return interactiveBusyMode;
	}

	/**
	 * If this FireScreen instance is in interactive busy mode, it will allow user actions when busyMode==true.
	 * (see the setBusyMode() method)	 
	 * @param
	 */
	public void setInteractiveBusyMode(boolean interactiveBusyMode)
	{
		this.interactiveBusyMode = interactiveBusyMode;
	}
	
	/**
	 * Sets the gauge that is shown when the Screen is set to busy mode.
	 * @param g
	 * @throws NullPointerException if g is null.
	 */
	public void setGauge(FGauge g)
	{
		if(g!=null) innerGauge = g;
		else throw new NullPointerException("Gauge cannot be null");
	}
	
	public FGauge getGauge()
	{
		return innerGauge;
	}
}