package org.zaval.lw.rs;

import org.zaval.lw.event.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.data.*;
import org.zaval.misc.*;
import java.util.*;
import javax.microedition.lcdui.*;

public class LwRsLoaderImpl
implements LwRsLoader
{
   private Vector managers = new Vector();

   public void loadResources (Hashtable r)
   {
     Object o = new LwEventManager();
     managers.addElement (o);
     r.put("event", o);
     o = new LwPaintManImpl();
     managers.addElement (o);
     r.put("paint", o);
     o = new LwFocusManager();
     managers.addElement (o);
     r.put("focus", o);

     r.put("ch.img","img/checkbox.png");
     r.put("tree.img","img/tree.png");
     r.put("br.raised2",new LwBorder(1));
     r.put("br.sunken2",new LwBorder(6));
     r.put("br.raised",new LwBorder(1));
     r.put("br.sunken",new LwBorder(2));
     r.put("br.etched",new LwBorder(3));
     r.put("br.plain",new LwBorder(4));
     r.put("br.dot", new LwBorder(5));
     r.put("button.on",r.get("br.plain"));
     r.put("button.off",r.get("br.etched"));
     r.put("layout.raster",new LwRasterLayout());
     r.put("check.off",new LwImgSetRender((String)r.get("ch.img"),0,0,12,12,3));
     r.put("check.on",new LwImgSetRender((String)r.get("ch.img"),12,0,12,12,3));
     r.put("check.dison",new LwImgSetRender((String)r.get("ch.img"),36,0,12,12,3));
     r.put("check.disoff",new LwImgSetRender((String)r.get("ch.img"),24,0,12,12,3));
     r.put("radio.off",new LwImgSetRender((String)r.get("ch.img"),0,12,12,12,3));
     r.put("radio.on",new LwImgSetRender((String)r.get("ch.img"),12,12,12,12,3));
     r.put("radio.dison",new LwImgSetRender((String)r.get("ch.img"),36,12,12,12,3));
     r.put("radio.disoff",new LwImgSetRender((String)r.get("ch.img"),24,12,12,12,3));
     r.put("tree.least",new LwImgSetRender((String)r.get("tree.img"),0,0,16,16,3));
     r.put("tree.open",new LwImgSetRender((String)r.get("tree.img"),16,0,16,16,3));
     r.put("tree.close",new LwImgSetRender((String)r.get("tree.img"),32,0,16,16,3));
     r.put("toggle.off",new LwImgSetRender((String)r.get("tree.img"),48,0,9,9,3));
     r.put("toggle.on",new LwImgSetRender((String)r.get("tree.img"),48,9,9,9,3));
     r.put("bpan.br",new LwTitledBorder(3,16));
     r.put("acont.layout",new LwFlowLayout(16,16,1,2));
     r.put("def.font",Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
     r.put("def.bgcolor",new Color(182,182,170));
     r.put("def.fgcolor",Color.black);
     r.put("def.bfont",Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
     r.put("def.sfont",Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
     r.put("nb.br",new LwTitledBorder(1));
     r.put("stbar.br",r.get("br.sunken2"));
     r.put("pr.br",r.get("br.sunken2"));
     r.put("ln.font",Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
     r.put("sel1.color",Color.black);
     r.put("sel2.color",Color.darkGray);
   }

   public Vector getManagers () {
     return managers;
   }
}
