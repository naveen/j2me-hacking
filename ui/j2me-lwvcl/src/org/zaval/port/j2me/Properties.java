package org.zaval.port.j2me;

import java.util.*;
import java.io.*;

public class Properties
extends Hashtable
{
   public String getProperty(String name) {
     return (String)get(name);
   }

   public void load(InputStream is)
   throws IOException
   {
     StringBuffer       res    = new StringBuffer();
     InputStreamReader  reader = new InputStreamReader(is);
     char[]             buf    = new char[512];
     int                c      = 0;
     while ((c = reader.read(buf, 0, buf.length)) >= 0) res.append(buf, 0, c);

     c = res.length();
     for (int i = 0; i < c; i++)
     {
       char ch = res.charAt(i);
       int  k  = endOfLine(res, i, c);
       int  j  = lookup(res, i, k, '=');
       if (j > 0)
       {
         if ((j - i) > buf.length || (k - j - 1) > buf.length)
           throw new IllegalArgumentException ();

         res.getChars(i, j, buf, 0);
         String name = new String (buf, 0, j - i);
         res.getChars(j + 1, k, buf, 0);
         String value = new String (buf, 0, k - j - 1);
         put (name, value);
       }
       i = k + 1;
     }
   }

   private int endOfLine(StringBuffer buf, int start, int max)
   {
     while (start < max && buf.charAt(start) != '\n') start++;
     if (start < max) start--;
     return start;
   }

   private int lookup(StringBuffer buf, int start, int max, char ch) {
     while (start < max && buf.charAt(start) != ch) start++;
     return (start < max)?start:-1;
   }
}
