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
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.util.CommandListener;
import gr.bluevibe.fire.util.ComponentListener;
import gr.bluevibe.fire.util.Lang;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Panel extends Component implements ComponentListener
{

	
	/* Components */
	private FTicker ticker;
	protected Vector rows = new Vector();
	
	private Command cancelCmd=null;
	
	private Command leftCmd,rightCmd;
	private Popup leftPopup,rightPopup;
	private CommandListener commandListener;
	
	
	private String label = FireScreen.defaultLabel;
	private String labelStr = FireScreen.defaultLabel;

	
	/* Support vars */
	
	protected FireScreen control=null;
	
	protected int internalHeight=0;
	protected int verticalOffset=0;
	protected int pointerPos=-1;
	protected int viewportHeight=0;
	
	protected int scrollY;

	protected boolean animateUp=false;
	protected boolean animateDown = false;
	private int scrollCount = 0;
	
	private boolean alertMode=false;
	private Row alertText = null;
	private boolean animatedChildren=false;
	
	
	public Panel()
	{
		control = FireScreen.getScreen(null); // get Singleton FireScreen.
		setWidth(control.getWidth());
		setHeight(control.getHeight());
	}
	
	public void paint(Graphics g)
	{
		// zogragizo ta giro giro.
		Image bgImage = control.getBgImage();
		if(bgImage!=null)
		{
			g.drawImage(bgImage,0,0,Graphics.TOP|Graphics.LEFT);
		}

		int tickerClip = 0;
		if(ticker!=null)
		{ 
			tickerClip = FireScreen.defaultTickerFont.getHeight();
		}
		int topOffset = FireScreen.getTopOffset();
		int bottomOffset = FireScreen.getBottomOffset();
		viewportHeight = (getHeight()-bottomOffset-topOffset-tickerClip);
		int ypos=topOffset;
		int htmp=0;
		Component row;
		int width = getWidth();
		int height = getHeight();
		int i;

		if(ticker!=null)
		{
			htmp = ticker.getHeight();
			int trY = topOffset;
			int clipYa=0;
			int clipYb=htmp;
			g.translate(-g.getTranslateX(),-g.getTranslateY());
			g.translate(0,trY);
			g.setClip(0,clipYa,width-FireScreen.SCROLLBAR_WIDTH,clipYb);
			ticker.paint(g);
			ypos+=htmp;
		}

		for(i =0 ;i<rows.size() && ypos<(verticalOffset+height);++i)
		{
			row = (Component)rows.elementAt(i);
			htmp = row.getHeight();
			if(ypos+htmp>verticalOffset)
			{ // arxizoume na zografizoume apo auto edo to row.
				
				int trY = ypos-verticalOffset;
				int clipYa=0;

				if(trY<(topOffset+tickerClip))
				{
					clipYa = topOffset+tickerClip - trY;
				}
				int clipYb=htmp-clipYa;
				
				if(trY+htmp>viewportHeight)
				{
					clipYb = clipYb - ((trY+htmp)-(height-bottomOffset)); 
				}
				
				g.translate(-g.getTranslateX(),-g.getTranslateY());

				g.translate(0,trY);
				g.setClip(0,clipYa,width-FireScreen.SCROLLBAR_WIDTH,clipYb);
				row.paint(g);
			}
			ypos +=htmp;
		}

		/* Zografizoume ta ypolipa komatia tou interface. */
		g.translate(-g.getTranslateX(),-g.getTranslateY());
		g.setClip(0,0,width,height);
		
		
		if(internalHeight>viewportHeight)
		{
			if(animateDown || animateUp)
			{
				scrollY = (((verticalOffset+viewportHeight/2) * viewportHeight)/internalHeight) + topOffset;
			}
			
			if(scrollY<topOffset) scrollY=topOffset;
			if(scrollY>(getHeight()-bottomOffset-FireScreen.SCROLLBAR_HEIGHT)) scrollY = height-bottomOffset-FireScreen.SCROLLBAR_HEIGHT;
			
			g.setColor(FireScreen.defaultScrollColor);
			g.fillRect(width-FireScreen.SCROLLBAR_WIDTH+3,scrollY,FireScreen.SCROLLBAR_WIDTH-3,FireScreen.SCROLLBAR_HEIGHT);
		}

		Font labelFont = control.getLabelFont();
		
		g.setFont(labelFont);
		g.setColor(FireScreen.defaultLabelColor);
		int lhpos = height-(((bottomOffset-2)/2)+(labelFont.getHeight()/2));

		if(control.isBusyMode() && cancelCmd!=null)
		{ // draw the cancel command
			String str = control.getLeftSoftKeyShortcut()+cancelCmd.getLabel();
			g.drawString(str,2,lhpos,Graphics.TOP|Graphics.LEFT);			
		}
		else
		{
			if(leftCmd!=null)
			{
				String str = control.getLeftSoftKeyShortcut()+leftCmd.getLabel();
				g.drawString(str,2,lhpos,Graphics.TOP|Graphics.LEFT);
			}		
			if(rightCmd!=null)
			{
				String str = control.getRightSoftKeyShortcut()+rightCmd.getLabel();
				g.drawString(str,width-labelFont.stringWidth(str)-3,lhpos,Graphics.TOP|Graphics.LEFT);
			}
		}
		if(label!=null)
		{
			lhpos = (((topOffset-2)/2)-(labelFont.getHeight()/2));
			int loff = 4;
			if(FireScreen.getLogoPossition()==FireScreen.LEFT)
			{
				loff= width - labelFont.stringWidth(label)-4;
			}
			g.drawString(label,loff,lhpos,Graphics.TOP|Graphics.LEFT);
		}

		if(alertMode)
		{
			g.translate(FireScreen.ALERT_HORIZONTAL_OFFSET,topOffset+viewportHeight/2-alertText.getHeight()/2);
			g.setClip(0,0,alertText.getWidth(),alertText.getHeight());
			alertText.paint(g);
		}
	}

	public FTicker getTicker()
	{
		return ticker;
	}

	public void setTicker(FTicker ticker)
	{
		ticker.setWidth(getWidth()-FireScreen.SCROLLBAR_WIDTH);
		ticker.setHeight(FireScreen.defaultTickerFont.getHeight());
		ticker.validate();
		this.ticker = ticker;		
	}
	
	public synchronized int add(Component cmp)
	{
		if(cmp.isAnimated())
		{
			animatedChildren=true;
		}
		
		rows.addElement(cmp);
		int id = rows.size()-1;
		cmp.set_componentID(id);
		cmp.addValidateListener(this);
		return id;
	}
	
	public int countRows()
	{
		if(rows!=null) return rows.size();
		return 0;
	}
	
	public boolean remove(Component cmp)
	{
		pointerPos=-1;
		boolean found =rows.removeElement(cmp);
		if(found) internalHeight-=cmp.getHeight();
		return found;
	}
	public void remove(int pos)
	{
		pointerPos=-1;
		if(pos>=0 && pos<rows.size())
		{
			Component c = (Component)rows.elementAt(pos);
			internalHeight-=c.getHeight();
			rows.removeElementAt(pos);
		}
	}
	

	public boolean pointerEvent(int x, int y)
	{
		if(alertMode)
		{
			alertMode=false;
			alertText=null;
			return true;
		}
		
		int height = getHeight();
		int width = getWidth();
		
		int topOffset = FireScreen.getTopOffset();
		
		if(ticker!=null) topOffset+=ticker.getHeight();
		
		if(y>height-FireScreen.getBottomOffset())
		{ // event sto command bar (bottom border)
			if(leftCmd!=null)
			{
				if(x<=(FireScreen.defaultLabelFont.stringWidth(leftCmd.getLabel())+2))
				{
					return keyEvent(Canvas.GAME_A);
				}
			}
			if(rightCmd!=null)
			{
				if(x>=(width-FireScreen.defaultLabelFont.stringWidth(rightCmd.getLabel())-2))
				{
					return keyEvent(Canvas.GAME_B);
				}
			}
		}
		else if(y>topOffset && x<width-FireScreen.SCROLLBAR_WIDTH)
		{ // event se kapoio component tou Panel
			int i,tmpHeight=0;
			Component c=null;
			for(i=0;i<rows.size();++i)
			{
				c=(Component)rows.elementAt(i);
				tmpHeight+=c.getHeight();
				if((tmpHeight-verticalOffset+topOffset)>=y) break;
			}
			if(pointerPos==i) // fire event.
			{
				tmpHeight = tmpHeight-c.getHeight();
			
				c.pointerEvent(x,y+verticalOffset-tmpHeight-topOffset);
				return true;
			}
			if(i==rows.size())
			{ // no component in tap location.
				return true;
			}
			
			// selection event.
			if(pointerPos>=0)
			{ // deselect old selected component
				Component old = (Component)rows.elementAt(pointerPos);
				old.setSelected(false);
			}
			pointerPos=i;
			Component sel = (Component)rows.elementAt(pointerPos);
			sel.setSelected(true);
			sel.pointerEvent(x,y+verticalOffset-tmpHeight-topOffset);
			return true;
		}
		else if(internalHeight>viewportHeight)
		{ // scrollbar action.
			if(y>scrollY) 
			{
				return keyEvent(Canvas.RIGHT);

			}
			if(y<scrollY)
			{
				return keyEvent(Canvas.LEFT);
			}
		}
		return false;

	}
	
	private int getNextTraversableComponent(int pointer,boolean down)
	{
		int end;
		if(down)
		{
			end = rows.size();
			for(int i=(pointer+1);i<end;++i)
			{
				Component c = (Component)rows.elementAt(i);
				if(c.isTraversable()) return i;
			}
		}
		else
		{
			for(int i=(pointer-1);i>-1;--i)
			{
				Component c = (Component)rows.elementAt(i);
				if(c.isTraversable()) return i;
			}
		}
		return -1;
	}

	public boolean keyEvent(int key)
	{
		
		if(alertMode)
		{
			alertMode=false;
			alertText=null;
			return true;
		}

		//if(rows.size()==0) return false;
		boolean repaint=false;
		
		Component row=null;
		if((key==Canvas.UP || key==Canvas.DOWN) && pointerPos==-1)
		{
			int h =0;
			int i;
			for(i =0;i<rows.size() && h<verticalOffset;++i)
			{
				h += ((Component)rows.elementAt(i)).getHeight();
			}
			pointerPos = getNextTraversableComponent(i-1,true);
			if(pointerPos>-1)
			{
				row = ((Component)rows.elementAt(pointerPos));
				row.keyEvent(key);
				recalculateVecticalOffset();
			}
			else
			{
				if(key==Canvas.UP)
				{ 
					return keyEvent(Canvas.LEFT);

				}
				else if(key==Canvas.DOWN)
				{
					return keyEvent(Canvas.RIGHT);
				}
			}
			return true;
		}
		else if(pointerPos!=-1 && key!=Canvas.GAME_A && key!=Canvas.GAME_B) 
		{
			row = (Component)rows.elementAt(pointerPos);
			if(row.keyEvent(key)==true)
			{
				return true;
			}
		}

		int tmpPos;
		switch(key)
		{
			case Canvas.LEFT:
				if(!animateDown)
				{
					if(viewportHeight<internalHeight)
					{
						scrollCount=FireScreen.SCROLL_COUNT;
						animateUp=true;
					}
				}
				break;
			case Canvas.RIGHT:
				if(!animateUp)
				{
					if(viewportHeight<internalHeight)
					{
						scrollCount=FireScreen.SCROLL_COUNT;
						animateDown=true;
					}
				}
				break;
			case Canvas.UP:
				tmpPos = getNextTraversableComponent(pointerPos,false);
				if(tmpPos!=-1)
				{
					pointerPos=tmpPos;
					row = (Component)rows.elementAt(pointerPos);
					row.keyEvent(key);
					recalculateVecticalOffset();
					repaint=true;
				}
				else
				{ // the last selected component must remain selected, its more usable this way.
					((Component)rows.elementAt(pointerPos)).setSelected(true);
					return keyEvent(Canvas.LEFT); // scroll up
				}
				break;
			case Canvas.DOWN:
				tmpPos = getNextTraversableComponent(pointerPos,true);
				if(tmpPos!=-1)
				{
					pointerPos=tmpPos;
					row = (Component)rows.elementAt(pointerPos);
					row.keyEvent(key);
					recalculateVecticalOffset();
					repaint=true;
				}
				else
				{ 
					((Component)rows.elementAt(pointerPos)).setSelected(true);
					return keyEvent(Canvas.RIGHT); // scroll down
				}
				break;
			case Canvas.GAME_A:
				if(commandListener!=null && leftCmd!=null)
				{
					if(control.isBusyMode() && cancelCmd!=null) // in busy mode, the cancel command is shown.
					{
						commandListener.commandAction(cancelCmd,this);
						repaint=true;
					}
					else if(leftPopup==null)
					{
						
						commandListener.commandAction(leftCmd,this);
						repaint=true;
					}
					else
					{
						control.showPopup(leftPopup);
					}
				}
				break;
			case Canvas.GAME_B:
				if(!control.isBusyMode() && commandListener!=null && rightCmd!=null)
				{
					if(rightPopup==null)
					{
						commandListener.commandAction(rightCmd,this);
						repaint=true;
					}
					else
					{
						control.showPopup(rightPopup);
					}
				}
				break;
			case Canvas.FIRE:
				break;
		}
		return repaint;
	}
	
	private void recalculateVecticalOffset()
	{
		int i,tmpHeight=0;
		
		for(i=0;i<=pointerPos;++i)
		{
			tmpHeight+=((Component)rows.elementAt(i)).getHeight();
		}
		if(tmpHeight>viewportHeight)
		{
			int vo  = tmpHeight-viewportHeight;
			int diff =(verticalOffset - vo);
			if(diff<0 ||  diff>viewportHeight)
			{
				verticalOffset = vo; 
			}
		}
		else
		{
			verticalOffset=0;
		}
		scrollY =(viewportHeight*tmpHeight)/internalHeight ;
	}
	
	public void addCommand(Command c)
	{
		if(c.getCommandType()==Command.BACK)
		{
			leftCmd = c;
			leftPopup=null;
		}
		else if(c.getCommandType()==Command.CANCEL)
		{
			cancelCmd = c;
			leftPopup=null;
		}
		else
		{
			rightCmd = c;
			rightPopup=null;
		}
	}
	
	public void removeCommands()
	{
			leftCmd = null;
			leftPopup=null;
			rightCmd = null;
			rightPopup=null;
	}
	
	public void addCommand(Command popupCmd,Popup popup)
	{
		popup.setWidth(120);
		popup.setHeight(120);
		popup.validate();

		if(popupCmd.getCommandType()==Command.BACK)
		{
//			System.out.println("Setting popup to left softkey");
//			System.out.println("Width/Height: "+getWidth()+"/"+getHeight());

			leftCmd = popupCmd;
			leftPopup = popup;
			popup.setPosX(1);
			popup.setPosY(getHeight()-FireScreen.getBottomOffset()-popup.getHeight());
		}
		else
		{
//			System.out.println("Setting popup to right softkey");
//			System.out.println("Width/Height: "+getWidth()+"/"+getHeight());

			rightCmd = popupCmd;
			rightPopup=popup;
			popup.setPosX(getWidth()-FireScreen.SCROLLBAR_WIDTH-popup.getWidth()-1);
			popup.setPosY(getHeight()-FireScreen.getBottomOffset()-popup.getHeight());

		}
	}	
	
	public void setCommandListener(CommandListener l)
	{
		commandListener = l;
	}

	public String getLabel()
	{
		return labelStr;
	}

	public void setLabel(String label)
	{
		labelStr = label; // keep original string for later user (incase of a resize)
		int w = FireScreen.defaultLabelFont.stringWidth(label);
		int lw=10;
		if(FireScreen.defaultLogo!=null)
		{
			lw += FireScreen.defaultLogo.getWidth();
		}
		int max = (getWidth()-lw);
		if(w<max)
		{
			this.label = label;
			return;
		}
		// trim label to an acceptable size.
		char[] lch = label.toCharArray();
		max = max - FireScreen.defaultLabelFont.stringWidth("...");
		
		StringBuffer res = new StringBuffer();
		int sum = 0;
		for(int i =0;i<lch.length && sum<max;++i)
		{
			sum+=FireScreen.defaultLabelFont.charWidth(lch[i]);
			res.append(lch[i]);
		}
		
		this.label = res.toString()+"...";
	}


	/**
	 * Removes all components from this panel. Its does not remove the commands
	 *
	 */
	public void removeAll()
	{
		animateDown=false;
		animateUp = false;
		pointerPos=0;
		verticalOffset=0;
		internalHeight=0;
		scrollY=0;
		rows = new Vector();
	}
	

	/**
	 * Kitame an kapoio apo ta components pou fenonte ston panel einai animated.
	 * An einai kaloume tin clock() tou kathe enos animated component kai kanoume repaint
	 *
	 */
	public boolean clock()
	{
		boolean repaint=false;
		if(ticker!=null)
		{
			ticker.clock();
			repaint = true;
		}
		if(animateUp)
		{
			verticalOffset-=FireScreen.SCROLL_STEP;
			--scrollCount;
			if(verticalOffset<=0)
			{
				verticalOffset= 0;
				scrollCount=0;
				scrollY = control.getTopOffset();
			}
			if(scrollCount<=0)
			{
				animateUp=false;
			}
			return true;
		}
		if(animateDown)
		{
			verticalOffset+=FireScreen.SCROLL_STEP;
			--scrollCount;
			if(verticalOffset>internalHeight-viewportHeight)
			{
				verticalOffset= internalHeight-viewportHeight;
				scrollCount=0;
				scrollY =getHeight() - FireScreen.getBottomOffset()-FireScreen.SCROLLBAR_HEIGHT;
			}
			if(scrollCount<=0)
			{
				animateDown=false;
			}
			return true;
		}
		
		Component row;
		int ypos=0;
		int i,htmp;
		int height = getHeight();

		for(i =0 ;i<rows.size() && ypos<(verticalOffset+height);++i)
		{
			row = (Component)rows.elementAt(i);
			htmp = row.getHeight();
			if(ypos+htmp>verticalOffset)
			{ // arxizoume na zografizoume apo auto edo to row.
				if(row.isAnimated()){ row.clock();repaint=true;}
			}
			ypos +=htmp;
		}
		return repaint;
	}

	public void internalValidateEvent(Component c)
	{
		if(control!=null)
		{
			internalHeight-=c.getHeight();
			c.setWidth(getWidth()-FireScreen.SCROLLBAR_WIDTH);
			c.validate();
			internalHeight +=c.getHeight();
			control.repaint();
		}
	}
	
	public void setCurrent(Displayable d)
	{
		if(control!=null)
		{
			control.setCurrent(d);
		}
	}
	
	public void setContainerCurrent()
	{
		if(control!=null)
		{
			control.setContainerCurrent();
		}
	}
	
	/**
	 * 
	 * @see gr.bluevibe.fire.components.Component#validate()
	 */
	public void validate()
	{
		scrollY = FireScreen.getTopOffset();
		animatedChildren=false;
		// validate all components
		int w = getWidth();
		if(ticker!=null)
		{
			ticker.setWidth(w);
			ticker.validate();
		}
		internalHeight=0;
		if(leftPopup!=null)
		{
			leftPopup.setPosX(1);
			leftPopup.setPosY(getHeight()-FireScreen.getBottomOffset()-leftPopup.getHeight());
		}
		if(rightPopup!=null)
		{
			rightPopup.setPosX(getWidth()-FireScreen.SCROLLBAR_WIDTH-rightPopup.getWidth()-1);
			rightPopup.setPosY(getHeight()-FireScreen.getBottomOffset()-rightPopup.getHeight());
		}
		int wtmp = w-FireScreen.SCROLLBAR_WIDTH;
		for(int i =0 ;i <rows.size();++i)
		{
			Component c = (Component)rows.elementAt(i);
			c.setWidth(wtmp);
			c.validate();
			internalHeight +=c.getHeight();
			if(c.isAnimated())
			{
				animatedChildren=true;
			}
		}
		setLabel(labelStr); // recalculate label.
	}
	
	public boolean isAnimated()
	{
		if(ticker!=null || animatedChildren || animateDown || animateUp)
		{
			return true;
		}
		return false;
	}
	
	
	public void showAlert(String message,Image img)
	{
		alertText = new Row(img,message); // image on the left, text on the right of the alert.
		alertText.setAlignment(FireScreen.LEFT);
		alertText.setTextHpos(FireScreen.RIGHT);
		alertText.setTextVpos(FireScreen.CENTRE);
		alertText.setImageVpos(FireScreen.CENTRE);
		alertText.setImageHpos(FireScreen.LEFT);
		alertText.setBorder(true);
		alertText.setFilled(new Integer(control.getColor()));
		alertText.setColor(FireScreen.defaultTickerColor);
		alertText.setFont(FireScreen.defaultLabelFont);
		alertText.setWidth(getWidth()-FireScreen.SCROLLBAR_WIDTH-(2*FireScreen.ALERT_HORIZONTAL_OFFSET));
		alertText.setMinHeight((2*FireScreen.ALERT_VERTICAL_OFFSET));
		alertText.validate();
		alertMode=true;
		control.repaint();
	}
	
	
	public void resetPointer()
	{
		for(int i =0 ;i<rows.size();++i)
		{
			((Component)rows.elementAt(i)).setSelected(false);
		}
		pointerPos = getNextTraversableComponent(-1,true);
		if(pointerPos>-1)
		{
			Component row = ((Component)rows.elementAt(pointerPos));
			row.keyEvent(Canvas.DOWN);
		}
	}
}