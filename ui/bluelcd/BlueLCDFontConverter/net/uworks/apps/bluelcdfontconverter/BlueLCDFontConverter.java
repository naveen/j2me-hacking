package net.uworks.apps.bluelcdfontconverter;

import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import org.apache.commons.cli.*;

/**
 * <p>Title: BlueLCD Font Converter</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: uWorks</p>
 *
 * @author Josep del Rio
 * @version 1.0
 */
public class BlueLCDFontConverter {
    private static Color foregroundColor = new Color(0xffffff);
    private static Color backgroundColor = new Color(0x000000);

    public static void main(String[] args) {
        try {
        Options opt = new Options();
        opt.addOption("h", false, "Print help for this application");
        opt.addOption("i", "input", true, "The font file to use (mandatory)");
        opt.addOption("o", "output", true, "The image file to write to (will use PNG format, mandatory)");
        opt.addOption("c", "cols", true, "The number of columns per character (must be less or equal than 8, 8 is the default value if not specified)");
        opt.addOption("r", "rows", true, "The number of rows per character (will be calculated automatically if not specified)");
        opt.addOption("b", "background", true, "The background color (in hex format, default is 0x000000)");
        opt.addOption("f", "foreground", true, "The foreground color (in hex format, default is 0xFFFFFF)");
        opt.addOption("s", "scale", true, "The scaling factor (1 as default, can be 2, 3, 4, 6 or 8)");

        BasicParser parser = new BasicParser();
        CommandLine cl = parser.parse(opt, args);

        if ( cl.hasOption('h') ) {
            HelpFormatter f = new HelpFormatter();
            f.printHelp("BlueLCD Font Converter", opt);
            System.exit(0);
        }

        // Check the mandatory options
        if (!cl.hasOption('i')) {
            System.err.println("You must specify a font file to use (you can get help on the possible options using the -h parameter)");
            System.exit(1);
        }

        if (!cl.hasOption('o')) {
            System.err.println("You must specify a destination file to write the resulting image (you can get help on the possible options using the -h parameter)");
            System.exit(1);
        }

        File targetFontFile = new File(cl.getOptionValue('i'));
        File targetImageFile = new File(cl.getOptionValue('o'));
        if (!targetFontFile.exists()) {
            System.err.println("File '" + targetFontFile.getAbsolutePath() + "' doesn't exists.");
            System.exit(1);
        }
        if (!targetImageFile.getName().toLowerCase().endsWith(".png")) {
            System.err.println("Destination file must end with '.png'.");
            System.exit(1);
        }


        // Read other parameters
        if (cl.hasOption('f')) {
            foregroundColor = new Color(Integer.decode(cl.getOptionValue('f')).intValue());
        }
        if (cl.hasOption('b')) {
            backgroundColor = new Color(Integer.decode(cl.getOptionValue('b')).intValue());
        }

        int scale = 1;
        if (cl.hasOption('s')) {
            scale = Integer.parseInt(cl.getOptionValue('s'));
        }

        boolean processed = false;
            try {
                byte[] targetBytes = new byte[(int) targetFontFile.length()];
                FileInputStream inFile = new FileInputStream(targetFontFile);
                DataInputStream inDataFile = new DataInputStream(inFile);
                inDataFile.readFully(targetBytes);
                // Let's calculate the ammount of bytes per character
                int bytesPerCharacter;
                int numberColumns;

                if (cl.hasOption('r')) {
                    bytesPerCharacter = Integer.parseInt(cl.getOptionValue('r'));
                } else {
                    bytesPerCharacter = (int) targetFontFile.length() / 256;
                }
                if (cl.hasOption('c')) {
                    numberColumns = Integer.parseInt(cl.getOptionValue('c'));
                } else {
                    numberColumns = 8;
                }

                int imageOffset = 2; // Offset for scaling images properly

                BufferedImage fontImage = new BufferedImage(numberColumns * 16 * scale,
                        bytesPerCharacter * 16 * scale, BufferedImage.TYPE_INT_ARGB);
                Graphics2D targetGraphics = fontImage.createGraphics();
                BufferedImage currentCharImage = new BufferedImage((numberColumns + imageOffset) * scale, (bytesPerCharacter + imageOffset) * scale, BufferedImage.TYPE_INT_ARGB);
                Graphics2D currentCharImageGraphics = currentCharImage.createGraphics();
                int[] primaryBuffer = new int[(numberColumns + imageOffset) * scale * (bytesPerCharacter + imageOffset) * scale];
                int[] secondaryBuffer = new int[(numberColumns + imageOffset) * scale * (bytesPerCharacter + imageOffset) * scale];

                for (int c=0; c<16; c++) {
                    for (int d=0; d<16; d++) {
                        currentCharImageGraphics.setColor(backgroundColor);
                        currentCharImageGraphics.fillRect(0,0,currentCharImage.getWidth(), currentCharImage.getHeight());
                        currentCharImageGraphics.setColor(foregroundColor);
                        for (int f=0; f<bytesPerCharacter; f++) {
                            int targetByte = unsignedByteToInt(targetBytes[(c *
                                    16 + d) * bytesPerCharacter + f]);
//                            int currentPos = numberColumns - 1;
                            int currentPos = numberColumns - 1 + (imageOffset / 2);
                            for (int e = 1; e < (1 << numberColumns); e *= 2) {
                                if ((targetByte & e) > 0) {
                                    currentCharImageGraphics.fillRect(currentPos, f + (imageOffset / 2), 1,
                                            1);
                                }
                                currentPos--;
                            }
                        }
                        switch (scale) {
                          case 2:
                              currentCharImage.getRGB(0, 0, numberColumns + imageOffset, bytesPerCharacter + imageOffset, primaryBuffer, 0, numberColumns + imageOffset);
                              getScaled2x(primaryBuffer, secondaryBuffer, numberColumns + imageOffset, bytesPerCharacter + imageOffset);
                              currentCharImage.setRGB(0, 0, currentCharImage.getWidth(), currentCharImage.getHeight(), secondaryBuffer, 0, currentCharImage.getWidth());
                              break;
                          case 3:
                              currentCharImage.getRGB(0, 0, numberColumns + imageOffset, bytesPerCharacter + imageOffset, primaryBuffer, 0, numberColumns + imageOffset);
                              getScaled3x(primaryBuffer, secondaryBuffer, (numberColumns + imageOffset), (bytesPerCharacter + imageOffset));
                              currentCharImage.setRGB(0, 0, currentCharImage.getWidth(), currentCharImage.getHeight(), secondaryBuffer, 0, currentCharImage.getWidth());
                              break;
                          case 4:
                              currentCharImage.getRGB(0, 0, numberColumns + imageOffset, bytesPerCharacter + imageOffset, primaryBuffer, 0, numberColumns + imageOffset);
                              getScaled2x(primaryBuffer, secondaryBuffer, (numberColumns + imageOffset), (bytesPerCharacter + imageOffset));
                              getScaled2x(secondaryBuffer, primaryBuffer, (numberColumns + imageOffset)*2, (bytesPerCharacter + imageOffset)*2);
                              currentCharImage.setRGB(0, 0, currentCharImage.getWidth(), currentCharImage.getHeight(), primaryBuffer, 0, currentCharImage.getWidth());
                              break;
                          case 6:
                              currentCharImage.getRGB(0, 0, numberColumns + imageOffset, bytesPerCharacter + imageOffset, primaryBuffer, 0, numberColumns + imageOffset);
                              getScaled3x(primaryBuffer, secondaryBuffer, (numberColumns + imageOffset), (bytesPerCharacter + imageOffset));
                              getScaled2x(secondaryBuffer, primaryBuffer, (numberColumns + imageOffset)*3, (bytesPerCharacter + imageOffset)*3);
                              currentCharImage.setRGB(0, 0, currentCharImage.getWidth(), currentCharImage.getHeight(), primaryBuffer, 0, currentCharImage.getWidth());
                              break;
                          case 8:
                              currentCharImage.getRGB(0, 0, numberColumns + imageOffset, bytesPerCharacter + imageOffset, primaryBuffer, 0, numberColumns + imageOffset);
                              getScaled2x(primaryBuffer, secondaryBuffer, numberColumns + imageOffset, bytesPerCharacter + imageOffset);
                              getScaled2x(secondaryBuffer, primaryBuffer, (numberColumns + imageOffset)*2, (bytesPerCharacter + imageOffset)*2);
                              getScaled2x(primaryBuffer, secondaryBuffer, (numberColumns + imageOffset)*4, (bytesPerCharacter + imageOffset)*4);
                              currentCharImage.setRGB(0, 0, currentCharImage.getWidth(), currentCharImage.getHeight(), secondaryBuffer, 0, currentCharImage.getWidth());
                              break;
                        }
                        int targetWidth = numberColumns * scale;
                        int targetHeight = bytesPerCharacter * scale;
//                        System.err.println("" + (imageOffset / 2));
                        targetGraphics.drawImage(currentCharImage, numberColumns * c * scale, bytesPerCharacter * d * scale, numberColumns * c * scale + targetWidth, bytesPerCharacter * d * scale + targetHeight, (imageOffset / 2)*scale,(imageOffset / 2)*scale,(imageOffset / 2)*scale + targetWidth, (imageOffset / 2)*scale + targetHeight, null);
                        //targetGraphics.drawImage(currentCharImage, null, numberColumns * c * scale, bytesPerCharacter * d * scale);
                    }
                }

                ImageIO.write(fontImage, "png", targetImageFile);
                processed = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        if (!processed) {
            System.err.println("The provided font file is not compatible with this program.");
            System.exit(0);
        } else {
            System.out.println("'" + targetImageFile.getAbsolutePath() + "' has been created successfully.");
            System.exit(0);
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static  int[] getScaled2x(int[] pix, int[] pixel, int _width, int _height) {
      int i1, i2;
      int srcofs;
      int dstofs;
      int dx, dy;
      int x, y, e;

      // Scale2x algorithm (based on AdvanceMAME Scale2x)

      dx = _width;
      dy = _height;

      int finalRowOffset = _width * (_height - 1);
      int destinationFinalRowOffset = ((_width * _height) << 2) - (_width << 2);
      for (int c=0; c<dx; c++) {
        i1 = (c << 1);
        i2 = i1 + (_width << 1);
        e = pix[c];
        pixel[i1] = e;
        pixel[i1 + 1] = e;
        pixel[i2] = e;
        pixel[i2 + 1] = e;

        i1 += destinationFinalRowOffset;
        i2 += destinationFinalRowOffset;
        e = pix[c+finalRowOffset];
        pixel[i1] = e;
        pixel[i1 + 1] = e;
        pixel[i2] = e;
        pixel[i2 + 1] = e;
      }

      srcofs = dx + 1;
      dstofs = (_width << 2);

      for (y = 1; y < dy - 1; y++) {
        i1 = dstofs;
        i2 = i1 + (_width << 1);

        pixel[i1] = pix[srcofs - 1];
        pixel[i1 + 1] = pix[srcofs - 1];
        pixel[i2] = pix[srcofs - 1];
        pixel[i2 + 1] = pix[srcofs - 1];

        for (x = 1; x < dx - 1; x++) {
          int E0, E1, E2, E3;
          int A, B, C, D, E, F, G, H, I;

          A = pix[srcofs - dx - 1];
          B = pix[srcofs - dx];
          C = pix[srcofs - dx + 1];
          D = pix[srcofs - 1];
          E = pix[srcofs];
          F = pix[srcofs + 1];
          G = pix[srcofs + dx - 1];
          H = pix[srcofs + dx];
          I = pix[srcofs + dx + 1];

          //
          //	ABC
          //	DEF
          //	GHI

          //	E0E1
          //	E2E3
          //

          E0 = D == B && B != F && D != H ? D : E;
          E1 = B == F && B != D && F != H ? F : E;
          E2 = D == H && D != B && H != F ? D : E;
          E3 = H == F && D != H && B != F ? F : E;

          i1 = dstofs + (x << 1);
          i2 = i1 + (_width << 1);

          pixel[i1] = E0;
          pixel[i1 + 1] = E1;
          pixel[i2] = E2;
          pixel[i2 + 1] = E3;

          srcofs++;
        }

        // Row last pixel (just scale standard)
        i1 = dstofs + (_width << 1) - 2;
        i2 = i1 + (_width << 1);

        pixel[i1] = pix[srcofs];
        pixel[i1 + 1] = pix[srcofs];
        pixel[i2] = pix[srcofs];
        pixel[i2 + 1] = pix[srcofs];

        dstofs += (_width << 2);


        srcofs += 2;
      }

      return pixel;
    }

    public static int[] getScaled3x(int[] pix, int[] pixel, int _width, int _height) {
      int p1, p2, i1, i2, i0;
      int srcofs;
      int dstofs;
      int dx, dy;
      int x, y, e;

      // Scale3x algorithm as seen as http://scale2x.sourceforge.net

      dx = _width;
      dy = _height;

      int finalRowOffset = _width * (_height - 1);
      int destinationFinalRowOffset = ((_width * (_height - 1)) * 9);
      for (int c=0; c<dx; c++) {
        i1 = (c << 1) + c + (_width << 1) + _width + 1;
        i0 = i1 - (_width << 1) - _width;
        i2 = i1 + (_width << 1) + _width;
        e = pix[c];

        pixel[i0 - 1] = e;
        pixel[i0] = e;
        pixel[i0 + 1] = e;
        pixel[i1 - 1] = e;
        pixel[i1] = e;
        pixel[i1 + 1] = e;
        pixel[i2 - 1] = e;
        pixel[i2] = e;
        pixel[i2 + 1] = e;

        i0 += destinationFinalRowOffset;
        i1 += destinationFinalRowOffset;
        i2 += destinationFinalRowOffset;
        e = pix[c+finalRowOffset];
        pixel[i0 - 1] = e;
        pixel[i0] = e;
        pixel[i0 + 1] = e;
        pixel[i1 - 1] = e;
        pixel[i1] = e;
        pixel[i1 + 1] = e;
        pixel[i2 - 1] = e;
        pixel[i2] = e;
        pixel[i2 + 1] = e;
      }



      srcofs = dx + 1;
      dstofs = _width * 12 + 1;

      for (y = 1; y < dy - 1; y++) {
        i1 = dstofs;
        i0 = i1 - _width - (_width << 1);
        i2 = i1 + _width + (_width << 1);

        e = pix[srcofs - 1];

        pixel[i0 - 1] = e;
        pixel[i0] = e;
        pixel[i0 + 1] = e;
        pixel[i1 - 1] = e;
        pixel[i1] = e;
        pixel[i1 + 1] = e;
        pixel[i2 - 1] = e;
        pixel[i2] = e;
        pixel[i2 + 1] = e;

        for (x = 1; x < dx - 1; x++) {
          int E0, E1, E2, E3, E4, E5, E6, E7, E8;
          int A, B, C, D, E, F, G, H, I;

          A = pix[srcofs - dx - 1];
          B = pix[srcofs - dx];
          C = pix[srcofs - dx + 1];
          D = pix[srcofs - 1];
          E = pix[srcofs];
          F = pix[srcofs + 1];
          G = pix[srcofs + dx - 1];
          H = pix[srcofs + dx];
          I = pix[srcofs + dx + 1];

          //A = peek(x-1,y-1,pix,dx,dy);
          //B = peek(x,y-1,pix,dx,dy);
          //C = peek(x+1,y-1,pix,dx,dy);
          //D = peek(x-1,y,pix,dx,dy);
          //E = peek(x,y,pix,dx,dy);
          //F = peek(x+1,y,pix,dx,dy);
          //G = peek(x-1,y+1,pix,dx,dy);
          //H = peek(x,y+1,pix,dx,dy);
          //I = peek(x+1,y+1,pix,dx,dy);

          //
          //	ABC
          //	DEF
          //	GHI

          //     E0E1E2
          //     E3E4E5
          //     E6E7E8

          if (B != H && D != F) {
                  E0 = D == B ? D : E;
                  E1 = (D == B && E != C) || (B == F && E != A) ? B : E;
                  E2 = B == F ? F : E;
                  E3 = (D == B && E != G) || (D == B && E != A) ? D : E;
                  E4 = E;
                  E5 = (B == F && E != I) || (H == F && E != C) ? F : E;
                  E6 = D == H ? D : E;
                  E7 = (D == H && E != I) || (H == F && E != G) ? H : E;
                  E8 = H == F ? F : E;
          } else {
                  E0 = E;
                  E1 = E;
                  E2 = E;
                  E3 = E;
                  E4 = E;
                  E5 = E;
                  E6 = E;
                  E7 = E;
                  E8 = E;
          }

          i1 = dstofs + x + (x << 1);
          i0 = i1 - _width - (_width << 1);
          i2 = i1 + _width + (_width << 1);

          pixel[i0 - 1] = E0;
          pixel[i0] = E1;
          pixel[i0 + 1] = E2;
          pixel[i1 - 1] = E3;
          pixel[i1] = E4;
          pixel[i1 + 1] = E5;
          pixel[i2 - 1] = E6;
          pixel[i2] = E7;
          pixel[i2 + 1] = E8;

          srcofs++;
        }

        i1 = dstofs + 3*(dx - 1);
        i0 = i1 - _width - (_width << 1);
        i2 = i1 + _width + (_width << 1);

        e = pix[srcofs];

        pixel[i0 - 1] = e;
        pixel[i0] = e;
        pixel[i0 + 1] = e;
        pixel[i1 - 1] = e;
        pixel[i1] = e;
        pixel[i1 + 1] = e;
        pixel[i2 - 1] = e;
        pixel[i2] = e;
        pixel[i2 + 1] = e;


        srcofs += 2;
        dstofs += (_width << 3) + _width;
      }

      return pixel;
    }

}
