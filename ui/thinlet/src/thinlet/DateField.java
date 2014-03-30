package thinlet;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;




public class DateField extends Widget {
	
	private static final Layout layout = new InlineLayout(3, 6, 3, 16);
	{ setLayout(layout); }
	static { Locale.setDefault(new Locale("HU", "hu")); }

	private DateChoice choice;
	
	public DateField() {
		add(new Text(DateFormat.getDateInstance().format(new Date())));
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			if (choice == null) choice = new DateChoice();
			choice.setDate(System.currentTimeMillis());
			choice.popup(this, 0, getHeight() + 1);
			repaint();
			break;
		case MouseEvent.MOUSE_ENTERED:
		case MouseEvent.MOUSE_EXITED:
			if ((choice == null) || (choice.getParent() == null))
				repaint();
			break;
		case PopupMenu.POPUP_CLOSED: 
			repaint();
		}
	}
	
	private static final Color light = new Color(0xACB9C8),
		dark = new Color(0x93A1B1), border = new Color(0x838F9E);
	
	protected void paint(Graphics g) {
		if ((choice != null) && (choice.getParent() != null)) g.setColor(dark);
			else if (isMouseInside()) g.setColor(light);
			else ((Graphics2D) g).setPaint(new GradientPaint(0, 1, light, 0, getHeight() - 2, dark));
		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
		g.setColor(border);
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
		g.setColor(Color.darkGray);
		int tx = getWidth() - 12, ty = (getHeight() - 8) / 2;
		g.fillPolygon(new int [] { tx, tx + 4, tx + 8 }, new int [] { ty, ty + 8, ty }, 3);
		g.setColor(Color.black);
	}
	
	private class DateChoice extends Widget {
		
		private Text tyear, tmonth;
		
		private void setDate(long millis) {
			
			DateFormatSymbols symbols = new DateFormatSymbols(); 
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millis);
			int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH),
				date = calendar.get(Calendar.DATE), dayofweek = calendar.get(Calendar.DAY_OF_WEEK),
				dayofmonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 
			calendar.set(Calendar.DATE, 1); calendar.add(Calendar.DATE, -1);
			int lastday = calendar.get(Calendar.DATE);

			removeAll();



			int firstday = calendar.getFirstDayOfWeek();
			String[] weekdays = symbols.getShortWeekdays();
			for (int i = 0; i < 7; i++)
				add(new Text(weekdays[(firstday + i - 1) % 7 + 1]));
			
			int os = (dayofweek - date % 7 + 8 - firstday) % 7;
			for (int i = 0; i < os; i++) 
				add(new Text(String.valueOf(lastday - os + i + 1)));
			for (int i = 1; i <= dayofmonth; i++)
				add(new Text(String.valueOf(i)));
			for (int i = 0; i < 7 - (os + dayofmonth) % 7; i++)
				add(new Text(String.valueOf(i + 1)));
		}
		
		public Metrics getPreferredSize(int preferredWidth) {
			int w = 0, h = 0, n = 0;
			for (Widget widget = getChild(); widget != null; widget = widget.getNext()) {
				Metrics metrics = widget.getPreferredSize(preferredWidth);
				w = Math.max(w, metrics.getWidth()); h = Math.max(h, metrics.getHeight()); n++;
			}
			return new Metrics(12 + 4 * 6 + w * 7, 12 + 4 * 6 + h * 7);
		}
		
		protected void doLayout() {
			int w = 0, h = 0, i = 0;
			for (Widget widget = getChild(); widget != null; widget = widget.getNext()) {
				Metrics metrics = widget.getPreferredSize(-1);
				w = Math.max(w, metrics.getWidth()); h = Math.max(h, metrics.getHeight());
			}
			for (Widget widget = getChild(); widget != null; widget = widget.getNext()) {
				widget.setBounds(2 + (i % 7) * (w + 4), 2 + (i / 7) * (h + 4), w, h); i++;
			}
		}
		
		protected void paint(Graphics g) {
			g.setColor(border);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			g.setColor(light);
			g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			g.setColor(Color.black);
		}
		
		public void remove() {
			super.remove();
			DateField.this.repaint();
		}
	}
}
