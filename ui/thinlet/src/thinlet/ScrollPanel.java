package thinlet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScrollPanel extends Widget {
	
	private Widget headerport, header;
	private Widget viewport, content;
	private Widget hscroll, left, hknob, right;
	private Widget vscroll, up, vknob, down;
	
	public ScrollPanel() {
		super.add(viewport = new Widget());
		viewport.add(content = new Widget());
	}
	
	protected Widget getHeader() {
		if (header == null) {
			super.add(headerport = new Widget());
			headerport.add(header = new Widget());
		}
		return header;
	}
	
	protected Widget getContent() {
		return content;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(128, 96);
	}
	
	protected void doLayout() {
		int headerheight = 0;
		if (header != null) {
			headerport.setBounds(0, 0, getWidth() - 16, 16);
			header.setBounds(0, 0, getWidth() - 16, 16);
			int x = 0;
			for (Widget widget = header.getChild(); widget != null; widget = widget.getNext()) {
				widget.setBounds(x, 0, 80, 16); x += 80;
			}
			headerheight = 16;
		}
		
		Metrics metrics = layoutContent(getWidth() - 16);
		int contentwidth = metrics.getWidth(), contentheight = metrics.getHeight();
		
		boolean hneed = contentwidth > getWidth(); 
		boolean vneed = contentheight > getHeight() - (hneed ? 16 : 0) - headerheight; 
		if (vneed) hneed = hneed || (vneed && (contentwidth > getWidth() - 16));

		viewport.setBounds(0, headerheight, getWidth() - (vneed ? 16 : 0),
			getHeight() - headerheight - (hneed ? 16 : 0));
		content.setBounds(0, 0, contentwidth, contentheight);
		
		if (hneed) {
			if (hscroll == null) {
				super.add(hscroll = new Widget());
				hscroll.add(left = new Widget());
				hscroll.add(hknob = new Widget());
				hscroll.add(right = new Widget());
			}
			hscroll.setBounds(0, getHeight() - 16, getWidth() - (vneed ? 16 : 0), 16);
			left.setBounds(0, 0, 16, 16);
			hknob.setBounds(24, 0, 16, 16); sizeKnob(true);
			right.setBounds(hscroll.getWidth() - 16, 0, 16, 16);
		}
		else if (hscroll != null) {
			hscroll.remove(); hscroll = null;
		}
		
		if (vneed) {
			if (vscroll == null) {
				super.add(vscroll = new Widget());
				vscroll.add(up = new Widget());
				vscroll.add(vknob = new Widget());
				vscroll.add(down = new Widget());
			}
			vscroll.setBounds(getWidth() - 16, headerheight, 16,
				getHeight() - headerheight - (hneed ? 16 : 0));
			up.setBounds(0, 0, 16, 16);
			vknob.setBounds(0, 24, 16, 16); sizeKnob(false);
			down.setBounds(0, vscroll.getHeight() - 16, 16, 16);
		}
		else if (vscroll != null) {
			vscroll.remove(); vscroll = null;
		}
	}
	
	protected Metrics layoutContent(int preferredWidth) {
		return content.getPreferredSize(preferredWidth);
	}
	
	private void sizeKnob(boolean horizontal) {
		if (horizontal) {
			int t = hscroll.getWidth() - 32, v = viewport.getWidth(), c = content.getWidth(),
				k = Math.max(t * v / c, 10);
			hknob.setBounds(16 + content.getX() * (t - k) / (v - c), 0, k, 16);
			hscroll.repaint(); 
		} else {
			int t = vscroll.getHeight() - 32, v = viewport.getHeight(), c = content.getHeight(),
				k = Math.max(t * v / c, 10);
			vknob.setBounds(0, 16 + content.getY() * (t - k) / (v - c), 16, k);
			vscroll.repaint(); 
		}
	}
	
	private boolean offset(int dx, int dy) {
		if (dx > 0) { 
			int d = content.getWidth() - viewport.getWidth() + content.getX();
			translate(dx = Math.min(dx, d), 0);
			return d > dx;
		}
		if (dx < 0) { 
			int d = content.getX();
			translate(dx = Math.max(dx, d), 0);
			return d < dx;
		}
		if (dy > 0) { 
			int d = content.getHeight() - viewport.getHeight() + content.getY();
			translate(0, dy = Math.min(dy, d));
			return d > dy;
		}
		if (dy < 0) { 
			int d = content.getY();
			translate(0, dy = Math.max(dy, d));
			return d < dy;
		}
		return false;
	}
	
	private void translate(int dx, int dy) {
		if ((dx == 0) && (dy == 0)) return;
		content.setBounds(content.getX() - dx, content.getY() - dy,
			content.getWidth(), content.getHeight());
		sizeKnob(dx != 0);
		repaint(); 
	}
	
	private Timer timer;
	
	protected void process(AWTEvent e, Widget widget) {
		if ((widget == hscroll) || (widget == vscroll)) {
			if (false) System.out.println();
		}
		else if ((widget == left) || (widget == right) || (widget == up) || (widget == down)) {
			switch (e.getID()) {
				case MouseEvent.MOUSE_ENTERED:
				case MouseEvent.MOUSE_EXITED:
					widget.repaint(); break;
				case MouseEvent.MOUSE_RELEASED:
					if (timer != null) timer.stop();
					widget.repaint(); break;
				case MouseEvent.MOUSE_PRESSED:
					Point offset = new Point();
					if (widget == left) offset.x = -12;
						else if (widget == right) offset.x = 12;
						else if (widget == up) offset.y = -12;
						else if (widget == down) offset.y = 12;
					if (offset(offset.x, offset.y)) timer = startTimer(300, 60, offset);
					widget.repaint();
			}
		}
		else if ((widget == hknob) || (widget == vknob)) {
			switch (e.getID()) {
				case MouseEvent.MOUSE_ENTERED:
				case MouseEvent.MOUSE_EXITED:
				case MouseEvent.MOUSE_PRESSED:
				case MouseEvent.MOUSE_RELEASED:
					widget.repaint();
			}
		}
	}
	
	protected boolean timerEvent(Object parameter) {
		Point offset = (Point) parameter;
		return offset(offset.x, offset.y);
	}
	
	private static final Color light = new Color(0xededed),
		dark = new Color(0xd0d0d0), border = new Color(0xc0c0c0);
	
	protected void paint(Graphics g, Widget widget) {
		if ((widget == hscroll) || (widget == vscroll)) {
			g.setColor(light);
			g.fillRect(0, 0, widget.getWidth(), widget.getHeight());
		}
		else if ((widget == left) || (widget == right) || (widget == up) || (widget == down)) {
			if (widget.isMouseInside() || widget.isPressedInside()) {
				g.setColor(widget.isPressedInside() ? dark : light);
				g.fillRect(1, 1, 14, 14);
				g.setColor(border);
				g.drawRoundRect(0, 0, 15, 15, 2, 2);
			}
			
			g.setColor(Color.gray);
			if (widget == left) g.fillPolygon(new int [] { 12, 4, 12 }, new int [] { 4, 8, 12 }, 3);
				else if (widget == right) g.fillPolygon(new int [] { 4, 12, 4 }, new int [] { 4, 8, 12 }, 3);
				else if (widget == up) g.fillPolygon(new int [] { 4, 8, 12 }, new int [] { 12, 4, 12 }, 3);
				else if (widget == down) g.fillPolygon(new int [] { 4, 8, 12 }, new int [] { 4, 12, 4 }, 3);
		}
		else if ((widget == hknob) || (widget == vknob)) {
			g.setColor(widget.isPressedInside() ? dark : light);
			g.fillRect(1, 1, widget.getWidth() - 2, widget.getHeight() - 2);
			g.setColor(border);
			g.drawRoundRect(0, 0, widget.getWidth() - 1, widget.getHeight() - 1, 2, 2);
		}
	}
}
