package thinlet;

import java.awt.*;
import java.awt.image.*;

public class RadioButton extends CheckBox {
	
	private static final Layout layout = new InlineLayout(0, 16, 0, 0);
	{ setLayout(layout); }
	
	public RadioButton() {
	}
	
	protected void paint(Graphics g) {
		int index = true ? ((isMouseInside() && isPressedInside()) ? (isSelected() ? 3 : 2) : (isSelected() ? 1 : 0)) : (isSelected() ? 5 : 4);
		if (radios[index] == null) radios[index] = createOval(radiocolors[index], 0xffffffff);
		g.drawImage(radios[index], 0, (getHeight() - 12) / 2, null);
		g.setColor(Color.black);
	}

	
	private static final Image[] radios = new Image[6];
	private static final int[][] radiocolors = {
		{ 0xffe6e6e6, 0xffe6e6e6, 0xffc0c0c0, 0xffe6e6e6, 0xffe6e6e6, 0xff909090 }, 
		{ 0xff7cbff8, 0xff77acd3, 0xff356ca6, 0xffe6e6e6, 0xffe6e6e6, 0xff909090 }, 
		{ 0xffb9b9b9, 0xffb9b9b9, 0xff909090, 0xffb9b9b9, 0xffb9b9b9, 0xff909090 }, 
		{ 0xff7cbff8, 0xff5895cf, 0xff356ca6, 0xffb9b9b9, 0xffb9b9b9, 0xff909090 }, 
		{ 0xffffffff, 0xffffffff, 0xffc0c0c0, 0xffffffff, 0xffffffff, 0xffb0b0b0 }, 
		{ 0xffd0d0d0, 0xffd0d0d0, 0xffc0c0c0, 0xffffffff, 0xffffffff, 0xffb0b0b0 }}; 
	
	private Image createOval(int[] colors, int bg) {
		int size = colors.length * 2;
		int[] pix = new int[size * size];
		for (int x = 0, h = size / 2; x < h; x++) {
			for (int y = x; y < h; y++) {
				double r = Math.sqrt(x * x + y * y); int ri = (int) r;
				int ci = (ri < colors.length) ? colors[ri] : bg;
				if (ri != r) {
					int cn = (ri + 1 < colors.length) ? colors[ri + 1] : bg;
					if (ci != cn) {
						int pn = (int)	 ((r - ri) * 100), pi = 100 - pn;
						int a1 = (ci >> 24) & 0xff, r1 = (ci >> 16) & 0xff, g1 = (ci >> 8) & 0xff, b1 = ci & 0xff,
							a2 = (cn >> 24) & 0xff, r2 = (cn >> 16) & 0xff, g2 = (cn >> 8) & 0xff, b2 = cn & 0xff,
							aa = (a1 * pi + a2 * pn) / 100, ra = (r1 * pi + r2 * pn) / 100,
							ga = (g1 * pi + g2 * pn) / 100, ba = (b1 * pi + b2 * pn) / 100;
						ci = (aa << 24) | (ra << 16) | (ga << 8) | ba;
					}
				}
				int a = h + x, b = h + y, c = h - 1 - x, d = h - 1 - y;
				pix[a + size * b] = pix[a + size * d] =
					pix[b + size * a] = pix[b + size * c] =
					pix[c + size * b] = pix[c + size * d] =
					pix[d + size * a] = pix[d + size * c] = ci;
			}
		}
		return getDesktop().getComponent().createImage(
			new MemoryImageSource(size, size, pix, 0, size));
	}
}
