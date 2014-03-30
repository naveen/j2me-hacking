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
 * Created on May 17, 2006
 */
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.util.FString;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class Row extends Component
{
	private String id;
	
	private int imageHpos=FireScreen.RIGHT;
	private int imageVpos=FireScreen.CENTRE;
	
	private boolean overlay=false;
	private int textHpos=FireScreen.LEFT;
	private int textVpos=FireScreen.CENTRE;
	private int alignment=-1;
	
	private int color=0x00000000;
	private Integer bgColor=null;

	private boolean border=false;
	private boolean editable=false;
	
	private Integer filled=null;
	private int editableColor=0xFFFFFFFF;
	
	
	private int textBoxConstrains=TextField.ANY;
	private int textBoxSize=160;
	
	protected Image image;
	protected Image selectedImage;
	protected FString text = new FString();
	protected FString label =null;
	
	private int labelVpos=FireScreen.CENTRE;
	private Integer forcedTextWidth=null;
	
	public Row()
	{
	}
	
	public Row(String txt,Image img)
	{
		setText(txt);
		setImage(img);
		setTextHpos(FireScreen.LEFT);
		setImageHpos(FireScreen.RIGHT);
	}
	
	public Row(Image img)
	{
		setImage(img);
		setImageHpos(FireScreen.LEFT);
	}
	
	public Row(String txt)
	{
		setText(txt);
		setTextHpos(FireScreen.LEFT);
	}
	
	public Row(Image img,String txt)
	{
		setText(txt);
		setImage(img);
		setTextHpos(FireScreen.RIGHT);
		setImageHpos(FireScreen.LEFT);
	}
	
	public void paint(Graphics g)
	{
		int imWidth =0;

		int width = getWidth();
		int  height = getHeight();
		
		int alignOffset=0;
		


		if(filled!=null)
		{
			g.setColor(filled.intValue());
			g.fillRect(0,0,width,height);
		}
		
		if(image!=null)
		{
			imWidth = image.getWidth();
		}
		int internalWidth = imWidth+text.getWidth()+6;
		int labelOffset=0;
		if(label!=null) labelOffset =label.getWidth();
		
		width = width - labelOffset;
	
		
		
		if(alignment!=-1 && (internalWidth)<width)
		{
			if(alignment==FireScreen.LEFT)
			{
				alignOffset=0;
			}
			if(alignment==FireScreen.CENTRE)
			{
				alignOffset=(width-internalWidth)/2;
			}
			if(alignment==FireScreen.RIGHT)
			{
				alignOffset=(width-internalWidth);
			}
		}
		// draw Image
		if(image!=null)
		{
			int offx = imWidth/2,offy=image.getHeight()/2;
			int x=0,y=0;
			if(imageHpos==FireScreen.CENTRE) x = internalWidth/2 - offx;  
			else if(imageHpos==FireScreen.LEFT) x=0;
			else if(imageHpos==FireScreen.RIGHT) x=internalWidth-offx-offx;
			
			if(imageVpos==FireScreen.CENTRE) y = height/2-offy; 
			else if(imageVpos==FireScreen.TOP) y= 0;
			else if(imageVpos==FireScreen.BOTTOM) y=height-offy-offy;
			if(!isSelected() || !isTraversable()) 
			{
				g.drawImage(image,labelOffset+alignOffset+x,y,Graphics.TOP|Graphics.LEFT);
			}
			else 
			{
				if(selectedImage!=null)
				{
					g.drawImage(selectedImage,labelOffset+alignOffset+x,y,Graphics.TOP|Graphics.LEFT);
				}
				else
				{
					g.drawImage(image,labelOffset+alignOffset+x,y,Graphics.TOP|Graphics.LEFT);
				}
			}
		}
		 // draw text.
		int offx = (text.getWidth()/2)+3,offy=text.getHeight()/2;
		int x=0,y=0;
		if(textHpos==FireScreen.CENTRE) x = internalWidth/2 - offx;
		else if(textHpos==FireScreen.LEFT) x=0;
		else if(textHpos==FireScreen.RIGHT) x=internalWidth-offx-offx;
		
		if(textVpos==FireScreen.CENTRE) y = height/2-offy; 
		else if(textVpos==FireScreen.TOP) y= 0;
		else if(textVpos==FireScreen.BOTTOM) y=height-offy-offy;
		g.setFont(text.getFont());
		Vector lines = text.getFormatedText();
		int rowHeight = text.getRowHeight();

		int txtCol;
		Integer bgCol = null;
		if(isSelected() && isTraversable())
		{
			bgCol = FireScreen.selectedLinkBgColor;
			txtCol = FireScreen.selectedLinkColor;
		}
		else  
		{
			bgCol = bgColor;
			txtCol = color;
		}
		
		if(editable)
		{
			int fw = text.getWidth()+3;
			if(fw==0)
			{
				fw = width;
			}
			g.setColor(editableColor);
			int ey = (height - text.getHeight()-2)/2;
			if(ey<0) ey=0;
			g.fillRect(labelOffset+alignOffset+x+1,ey+1,fw-2,text.getHeight());		
		}


		for(int i =0 ;i<lines.size();++i)
		{
			String tmp = (String)lines.elementAt(i);
			String str="";
			if(textBoxConstrains==TextField.PASSWORD)
			{
				// just draw a string of starts
				for(int j=0;j<tmp.length();++j)
				{
					str+="*";
				}
			}
			else
			{
				str = tmp;
			}

			if(bgCol!=null)
			{
				g.setColor(bgCol.intValue());
				g.fillRect(labelOffset+alignOffset+x+2,y+1+(i*(rowHeight+FString.LINE_DISTANCE)),text.getRowWidth(str),rowHeight-1);
			}
			g.setColor(txtCol);
			g.drawString(str,labelOffset+alignOffset+x+3,y+(i*(rowHeight+FString.LINE_DISTANCE))+1,Graphics.TOP|Graphics.LEFT);
		}
		
		if(editable)
		{
			int fw = text.getWidth()+3;
			if(fw==0)
			{
				fw = width;
			}
			int ey = (height - text.getHeight()-2)/2;
			if(ey<0) ey=0;
			if(isSelected())
			{			
				if(bgCol!=null)
					g.setColor(0x00000000);//bgCol.intValue());
				g.drawRect(labelOffset+alignOffset+x-1,ey-1,fw+2,text.getHeight()+4);
			}
			g.setColor(0x00000000);
			g.drawRect(labelOffset+alignOffset+x+1,ey+1,fw-2,text.getHeight());
		}
		
		if(label!=null)
		{
			 // draw label
			offx = label.getWidth()/2;
			offy=label.getHeight()/2;
			x=0;
			
			if(labelVpos==FireScreen.CENTRE) y = height/2-offy; 
			else if(labelVpos==FireScreen.TOP) y= 0;
			else if(labelVpos==FireScreen.BOTTOM) y=height-offy-offy;
			g.setFont(label.getFont());
			lines = label.getFormatedText();
			rowHeight = label.getRowHeight();
			
			Integer labelBgCol = bgColor;
			int labelTxtCol = color;
			for(int i =0 ;i<lines.size();++i)
			{
				String str = (String)lines.elementAt(i);
				
				if(labelBgCol!=null)
				{
					g.setColor(labelBgCol.intValue());
					g.fillRect(x+1,y+1+(i*(rowHeight+FString.LINE_DISTANCE)),label.getRowWidth(str),rowHeight);
				}
				g.setColor(labelTxtCol);
				g.drawString(str,x+3,y+2+(i*(rowHeight+FString.LINE_DISTANCE)),Graphics.TOP|Graphics.LEFT);
			}
		}
		if(border)
		{
			g.setColor(FireScreen.defaultBorderColor);
			g.drawRect(0,0,width-1,height-1);
		}
	}
	
	private void showTextBox()
	{
		RowTextBox b = new RowTextBox(this);
		setCurrent(b);
	}
	
	void textUpdate(String text)
	{
		setText(text);
		fireValidateEvent();
		setContainerCurrent();
		generateEvent();
	}
	
	public boolean pointerEvent(int x, int y)
	{
		return keyEvent(Canvas.FIRE);
	}
	
	public boolean keyEvent(int key)
	{	
		if(key==Canvas.FIRE)
		{
			if(isEditable())
			{
				showTextBox();
				return false;
			}
			else
			{
				return generateEvent();
			}
		}
		else if(key==Canvas.UP || key==Canvas.DOWN)
		{
			setSelected(!isSelected());
			return isSelected();
		}
		return false; // any other key exits focus
	}


	public Image getImage()
	{
		return image;
	}


	public void setImage(Image image)
	{
		this.image = image;
	}


	public int getImageHpos()
	{
		return imageHpos;
	}


	public void setImageHpos(int imageHpos)
	{
		this.imageHpos = imageHpos;
	}


	public int getImageVpos()
	{
		return imageVpos;
	}


	public void setImageVpos(int imageVpos)
	{
		this.imageVpos = imageVpos;
	}


	public boolean isOverlay()
	{
		return overlay;
	}


	public void setOverlay(boolean overlay)
	{
		this.overlay = overlay;
	}


	public Image getSelectedImage()
	{
		return selectedImage;
	}


	public void setSelectedImage(Image selectedImage)
	{
		this.selectedImage = selectedImage;
	}

	public void setText(String text)
	{
		if(text!=null && textBoxSize<text.length()) 
		{
			textBoxSize = text.length();
		}
		
		this.text.setText(text);
	}
	
	public String getText()
	{
		return this.text.getText();
	}
	
	public Font getFont()
	{
		return this.text.getFont();
	}
	public void setFont(Font f)
	{
		this.text.setFont(f);
	}
	

	public int getTextHpos()
	{
		return textHpos;
	}


	public void setTextHpos(int textHpos)
	{
		this.textHpos = textHpos;
	}


	public int getTextVpos()
	{
		return textVpos;
	}

	public void setTextVpos(int textVpos)
	{
		this.textVpos = textVpos;
	}
	
	public void validate()
	{
		int minW=0;

		// kitame to image.
		int textWidth=0;
		int imWidth = 0;
		int imHeight = 0;
		
		int width = getWidth()-FireScreen.SCROLLBAR_WIDTH;
		if(label!=null)
		{
			width -=label.getWidth();
			minW += label.getWidth();
		}

		if(image!=null)
		{
			imWidth = image.getWidth();
			imHeight = image.getHeight();
			minW += image.getWidth();
			if(overlay==false)
			{
				if(imageHpos==FireScreen.CENTRE)
				{
					textWidth = width/2 - imWidth/2;
				}
				else
				{
					textWidth = width - imWidth;
				}
				if(textWidth<=0) // den mporoume na zografisoume to keimeno dipla stin eikona 
				{ // opote pame anagastika se overlay.
					setOverlay(true);
				}
			}
			if(overlay)
			{
				textWidth = width;
			}
		}
		else
		{
			textWidth=width;
		}
		
		if(forcedTextWidth!=null) 
		{
			int ftw = forcedTextWidth.intValue();
			if(ftw>textWidth) // to textWidth einai to megisto pou xoraei, 
			{							// an to forced einai megalitero tha vgenei ekso apo tin othoni
				ftw = textWidth;
			}
			text.format(ftw,true);
		}
		else
		{
			text.format(textWidth,false);
		}
		minW +=text.getWidth()+6;
		int textHeight = text.getHeight()+2;
		
		int myHeight = 0;
		if(textHeight>imHeight) myHeight=textHeight;
		else myHeight=imHeight;
		if(label!=null && myHeight <label.getHeight())
		{
			myHeight = label.getHeight();
		}
		if(editable)
		{
			myHeight+=4;
		}
		setHeight(myHeight);
		
		if(getMinHeight()>getHeight())
		{
			setHeight(getMinHeight());
		}
		setMinWidth(minW);
	}

	public Integer getBgColor()
	{
		return bgColor;
	}

	public void setBgColor(Integer bgColor)
	{
		this.bgColor = bgColor;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public int getAlignment()
	{
		return alignment;
	}

	public void setAlignment(int alignment)
	{
		this.alignment = alignment;
	}

	public boolean isBorder()
	{
		return border;
	}

	public void setBorder(boolean border)
	{
		this.border = border;
	}

	public Integer getFilled()
	{
		return filled;
	}

	public void setFilled(Integer filled)
	{
		this.filled = filled;
	}

	public boolean isEditable()
	{
		return editable;
	}

	public void setEditable(boolean editable)
	{
		this.editable = editable;
		if(editable)
		{
			setMinHeight(text.getFont().getHeight()+2);
		}
	}

	public int getTextBoxConstrains()
	{
		return textBoxConstrains;
	}

	public void setTextBoxConstrains(int textBoxConstrains)
	{
		this.textBoxConstrains = textBoxConstrains;
	}

	public int getTextBoxSize()
	{
		return textBoxSize;
	}

	public boolean isTraversable()
	{
		if(!editable)
		{ 
			return super.isTraversable();
		}
		// an to row einai editable einia panta traversable.
		return true;
	}


	public void setTextBoxSize(int textBoxSize)
	{
		this.textBoxSize = textBoxSize;
	}

	public String getId()
	{
		return id;
	}
	
	public void setLabel(String labelStr,Font labelFont,Integer width,int labelVpos)
	{
		label = new FString();
		label.setText(labelStr);
		if(labelFont!=null)
		{
			label.setFont(labelFont);
		}
		if(width!=null)
		{
			label.format(width.intValue(),true);			
		}
		else
		{ // calculate width.
			int w = label.getFont().stringWidth(labelStr);
			label.format(w,true);
			label.setWidth(label.getWidth()+4); 
		}
		this.labelVpos = labelVpos;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Integer getForcedTextWidth()
	{
		return forcedTextWidth;
	}

	public void setForcedTextWidth(Integer forcedTextWidth)
	{
		this.forcedTextWidth = forcedTextWidth;
	}
}