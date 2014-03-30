package com.java4ever.apime.demo;

import javax.microedition.lcdui.*;

import com.java4ever.apime.ui.*;
import com.java4ever.apime.ui.event.*;

public abstract class ApimeDemo extends Application
{
	private long timer;
	private IButton exit;
	private ILabel input;
	private int input_type=-1;
	private Image LOWERCASE,UPPERCASE,NUMBER;

	/*************************************************************************/
	/*************************************************************************/

	public ApimeDemo()
	{
		super(20,"/logo.png");
		timer=System.currentTimeMillis();
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void initCode()
	{
		try
		{
			// Exit.
			exit=new IButton(loadImage("/exit_n.png"));
			exit.setImage(IButton.ROLLOVER,loadImage("/exit_r.png"));
			exit.setToolTip("Exit");
			exit.setActionListener(new ActionListener()
				{
					public void actionPerformed(Object object,Object data)
					{
						exit();
					}
				}
			);
			// Input.
			LOWERCASE=loadImage("/lowercase.png");
			UPPERCASE=loadImage("/uppercase.png");
			NUMBER=loadImage("/number.png");
			input=new ILabel(LOWERCASE);
			//
			Thread.sleep(Math.max(0,3000-(System.currentTimeMillis()-timer)));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected void updateCode(int delay)
	{
		if (KeyboardManager.instance.getInput()!=input_type)
		{
			input_type=KeyboardManager.instance.getInput();
			if (input_type==KeyboardManager.INPUT_LOWERCASE) input.setImage(LOWERCASE);
				else if (input_type==KeyboardManager.INPUT_UPPERCASE) input.setImage(UPPERCASE);
				else if (input_type==KeyboardManager.INPUT_NUMBER) input.setImage(NUMBER);
		}
	}

	protected void destroyCode()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	public IButton getExitButton()
	{
		return exit;
	}

	public ILabel getInputLabel()
	{
		return input;
	}

	public static Image loadImage(String image)
	{
		try
		{
			return Image.createImage(image);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}