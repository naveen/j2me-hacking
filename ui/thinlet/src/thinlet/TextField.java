package thinlet;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

public class TextField extends Widget {
	
	private static final Insets is = new Insets(2, 4, 2, 4);
	
	private String text;
	private int start, end, offset;
	private boolean editable = true;
	
	public TextField() {
		text = "text field";
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		FontMetrics fm = RootPane.getFontMetrics();
		return new Metrics(96, fm.getAscent() + fm.getDescent() + is.top + is.bottom,
			fm.getAscent() + is.top);
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_ENTERED:
			setCursor(Cursor.TEXT_CURSOR);
			break;
		case MouseEvent.MOUSE_EXITED:
			setCursor(Cursor.DEFAULT_CURSOR);
			break;
		case MouseEvent.MOUSE_PRESSED:
			int caret = getIndexAt(((MouseEvent) e).getX() - is.left + offset);
			select(caret, caret);
			requestFocus();
			break;
		case MouseEvent.MOUSE_DRAGGED:
			int index = getIndexAt(((MouseEvent) e).getX() - is.left + offset);
			select(start, index);
			break;
		case KeyEvent.KEY_PRESSED:
			KeyEvent ke = (KeyEvent) e;
			int code = ke.getKeyCode();
			char keychar = ke.getKeyChar();
			if (code == KeyEvent.VK_ENTER) {
				
			}
			else if (code == KeyEvent.VK_HOME) {
				select(ke.isShiftDown() ? start : 0, 0);
			}
			else if (code == KeyEvent.VK_END) {
				int n = text.length();
				select(ke.isShiftDown() ? start : n, n);
			}
			else if (code == KeyEvent.VK_LEFT) {
				int n = end;
				if (ke.isControlDown()) {
					while ((n > 0) && !Character.isLetterOrDigit(text.charAt(n - 1))) n--;
					while ((n > 0) && Character.isLetterOrDigit(text.charAt(n - 1))) n--;
				}
				else n = Math.max(0, end - 1);
				select(ke.isShiftDown() ? start : n, n);
			}
			else if (code == KeyEvent.VK_RIGHT) {
				int n = end, l = text.length();
				if (ke.isControlDown()) {
					while ((n < l - 1) && Character.isLetterOrDigit(text.charAt(n + 1))) n++;
					while ((n < l - 1) && !Character.isLetterOrDigit(text.charAt(n + 1))) n++; 
				}
				else n = Math.min(end + 1, l);
				select(ke.isShiftDown() ? start : n, n);
			}
			else if (editable && (code == KeyEvent.VK_BACK_SPACE)) {
				insert("", start, (start == end) ? Math.max(0, end - 1) : end);
			}
			else if (editable && (code == KeyEvent.VK_DELETE)) {
				insert("", start, (start == end) ? Math.min(end + 1, text.length()) : end);
			}
			else if (((code == KeyEvent.VK_A) || (code == 0xbf)) && ke.isControlDown()) { 
				select(0, text.length());
			}
			else if ((code == 0xdc) && ke.isControlDown()) { 
				int n = text.length();
				select(n, n);
			}
			else if ((ke.isControlDown() || ke.isMetaDown()) &&
					((code == KeyEvent.VK_X) || (code == KeyEvent.VK_C))) { 
				try {
					String subtext = text.substring(Math.min(start, end), Math.max(start, end));
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
						new StringSelection(subtext), null);
					if (code == KeyEvent.VK_X) insert("", start, end);
				} catch (Throwable exc) {}
			}
			else if (editable && (ke.isControlDown() || ke.isMetaDown()) &&
					(code == KeyEvent.VK_V)) { 
				try {
					String insert = (String) Toolkit.getDefaultToolkit().getSystemClipboard().
						getContents(this).getTransferData(DataFlavor.stringFlavor);
					
					insert(insert, start, end);
				} catch (Throwable exc) {}
			}
			else if (editable && (ke.getModifiers() != InputEvent.ALT_MASK) &&
					!((keychar <= 0x1f) ||
					((keychar >= 0x7f) && (keychar <= 0x9f)) ||
					(keychar >= 0xffff) || ke.isControlDown())) {
				insert(String.valueOf(keychar), start, end);
			}
			break;
		case FocusEvent.FOCUS_GAINED:
		case FocusEvent.FOCUS_LOST:
			repaint();
		}
	}
	
	private void select(int start, int end) {
		if ((this.start != start) || (this.end != end)) {
			this.start = start; this.end = end;
			checkOffset(); repaint();
		}
	}
	
	private void insert(String insert, int start, int end) {
		if ((this.start != start) || (this.end != end) || (insert != null)) {
			int min = Math.min(start, end);
			text = text.substring(0, min) + insert + text.substring(Math.max(start, end));
			this.start = this.end = min + insert.length();
			checkOffset(); repaint();
		}
	}
	
	private void checkOffset() {
		FontMetrics fm = RootPane.getFontMetrics();
		
		int textwidth = fm.stringWidth(text), width = getWidth() - is.left - is.right;
		if (textwidth <= width) { 
			offset = 0; 
		} else { 
			int caret = fm.stringWidth(text.substring(0, end));
			if (offset > caret) offset = caret;
			else if (offset < caret - width) offset = caret - width;
			offset = Math.max(0, Math.min(offset, textwidth - width)); 
		}
	}
	
	private int getIndexAt(int x) {
		FontMetrics fm = RootPane.getFontMetrics();
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int charwidth = fm.charWidth(chars[i]);
			if (x <= (charwidth / 2)) { return i; }
			x -= charwidth;
		}
		return chars.length;
	}
	
	protected void paint(Graphics g) {
		g.setColor(Color.lightGray);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.setColor(Color.white);
		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);

		FontMetrics fm = g.getFontMetrics();
		
		if (isFocused()) {
			int caret = fm.stringWidth(text.substring(0, end));
			if (start != end) {
				int istart = fm.stringWidth(text.substring(0, start));
				g.setColor(new Color(0xccccff));
				g.fillRect(is.left - offset + Math.min(istart, caret), is.top,
					Math.abs(caret - istart), getHeight() - is.top - is.bottom);
			}
			g.setColor(Color.red);
			g.fillRect(is.left - offset + caret, is.top, 1, getHeight() - is.top - is.bottom);
		}
		
		g.setColor(Color.black);
		g.drawString(text, is.left - offset, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
	}
}
