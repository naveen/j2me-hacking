package org.zaval.lw;

import javax.microedition.lcdui.*;

public class LwInputController
{
   public static final int NUM_MODE      = 0;
   public static final int LOW_CHAR_MODE = 1;
   public static final int UP_CHAR_MODE  = 2;

   private int     prevKeyCode, times, mode;
   private long    time, maxDt;
   private boolean isInsert;

   static LwInputController controller;


   public LwInputController(long dt) {
     maxDt = dt;
   }

   public boolean getInsertMode() {
     return isInsert;
   }

   public int translate(int keyCode, int gameAction)
   {
     long    ntime = System.currentTimeMillis();
     boolean b     = (prevKeyCode == keyCode) && ((ntime - time) < maxDt);

     time        = ntime;
     prevKeyCode = keyCode;

     if (keyCode == Canvas.KEY_POUND)
     {
       mode = (mode + 1)%3;
       return -1;
     }

     if (keyCode == Canvas.KEY_STAR)
     {
       isInsert = !isInsert;
       return -1;
     }

     //!!!
     if (gameAction > 0)
     {
       switch (gameAction)
       {
         case Canvas.UP     : keyCode = LwToolkit.VK_UP;    break;
         case Canvas.DOWN   : keyCode = LwToolkit.VK_DOWN;  break;
         case Canvas.LEFT   : keyCode = LwToolkit.VK_LEFT;  break;
         case Canvas.RIGHT  : keyCode = LwToolkit.VK_RIGHT; break;
         case Canvas.FIRE   : keyCode = LwToolkit.VK_ENTER; break;
       }
     }

     int index = toIndex(keyCode);
     if (index < 0) return keyCode;

     int i1 = MODE_OFFS[index][mode];
     int i2 = MODE_OFFS[index][mode + 1];
     times = b?(times + 1)%(i2 - i1):0;
     return CHAR_MAP[index][i1 + times];
   }

   public int getMode ()  {
     return mode;
   }

   public static LwInputController getInputController() {
     return controller;
   }

   protected int toIndex (int keyCode)
   {
     switch (keyCode)
     {
       case Canvas.KEY_NUM0  : return 0;
       case Canvas.KEY_NUM1  : return 1;
       case Canvas.KEY_NUM2  : return 2;
       case Canvas.KEY_NUM3  : return 3;
       case Canvas.KEY_NUM4  : return 4;
       case Canvas.KEY_NUM5  : return 5;
       case Canvas.KEY_NUM6  : return 6;
       case Canvas.KEY_NUM7  : return 7;
       case Canvas.KEY_NUM8  : return 8;
       case Canvas.KEY_NUM9  : return 9;
       case Canvas.KEY_STAR  : return 10;
       case Canvas.KEY_POUND : return 11;
     }
     return -1;
   }

   private static char[][] CHAR_MAP = {
   					{ '0',   ' ',                ' '                },
                                        { '1',   '1',                '1'                },
                                        { '2',  'a', 'b', 'c',       'A', 'B', 'C'      },
                                        { '3',  'd', 'e', 'f',       'D', 'E', 'F'      },
                                        { '4',  'g', 'h', 'i',       'G', 'H', 'I'      },
                                        { '5',  'j', 'k', 'l',       'J', 'K', 'L'      },
                                        { '6',  'm', 'n', 'o',       'M', 'N', 'O'      },
                                        { '7',  'p', 'q', 'r', 's',  'P', 'Q', 'R', 'S' },
                                        { '8',  't', 'u', 'v',       'T', 'U', 'V'      },
                                        { '9',  'w', 'x', 'y', 'z',  'W', 'X', 'Y', 'Z' },
                                        { '*',  '.',                 '.'                },
                                        { '#',  '+', '-',            '+', '-'           }
                                      };

  private static int[][] MODE_OFFS =  {
                                        { 0, 1, 2, 3 },
                                        { 0, 1, 2, 3 },
                                        { 0, 1, 4, 7 },
                                        { 0, 1, 4, 7 },
                                        { 0, 1, 4, 7 },
                                        { 0, 1, 4, 7 },
                                        { 0, 1, 4, 7 },
                                        { 0, 1, 5, 9 },
                                        { 0, 1, 4, 7 },
                                        { 0, 1, 5, 9 },
                                        { 0, 1, 2, 3 },
                                        { 0, 1, 3, 5 },
                                      };
}



