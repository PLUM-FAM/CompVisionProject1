/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;
import java.util.Scanner;

class IMP implements MouseListener
{
   JFrame frame;
   JPanel mp;
   JButton start;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   //Instance Fields you will be using below

   //This will be your height and width of your 2d array
   int height=0, width=0;

   //your 2D array of pixels
      int picture[][];

      /* 
      * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
      * menu is how you will open an image to manipulate. 
      */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      
      frame.addWindowListener(new WindowAdapter()
      {
         @Override
         public void windowClosing(WindowEvent ev){quit();}
      });

      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){ handleOpen(); }
      });

      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){ reset(); }
      });  

      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){ quit(); }
      });

      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(false);

      start.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){drawHistograms(); }
      });

      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }

   /* 
      * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
      * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
      */

   private JMenu getFunctions()
   {
      JMenu fun = new JMenu("Functions");
      
      JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
      JMenuItem secondItem = new JMenuItem("Rotate Clockwise");
      JMenuItem thirdItem = new JMenuItem("Grayscale");
      JMenuItem fourthItem = new JMenuItem("Blur");
      JMenuItem fifthItem = new JMenuItem("Edge Detection");
      JMenuItem sixthItem = new JMenuItem("Color Detection"); 
      JMenuItem seventhItem = new JMenuItem("Draw Histograms"); 

      
      firstItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){fun1();}
      });
      
      secondItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){rotateImage();}
      });

      thirdItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){grayscale();}
      });

      fourthItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){blur();}
      });

      fifthItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){edgeDetection();}
      });
      sixthItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){colorDetection();}
      });
      seventhItem.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent evt){drawHistograms();}
      });

      
      fun.add(firstItem);
      fun.add(secondItem);
      fun.add(thirdItem);
      fun.add(fourthItem);
      fun.add(fifthItem);
      fun.add(sixthItem);
      fun.add(seventhItem);
      
      return fun;   

   }

   /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
   private void handleOpen()
   {  
      img = new ImageIcon();
      JFileChooser chooser = new JFileChooser();
      Preferences pref = Preferences.userNodeForPackage(IMP.class);
      String path = pref.get("DEFAULT_PATH", "");

      chooser.setCurrentDirectory(new File(path));
      int option = chooser.showOpenDialog(frame);
      
      if(option == JFileChooser.APPROVE_OPTION) 
      {
         pic = chooser.getSelectedFile();
         pref.put("DEFAULT_PATH", pic.getAbsolutePath());
         img = new ImageIcon(pic.getPath());
      }
      width = img.getIconWidth();
      height = img.getIconHeight(); 
      
      JLabel label = new JLabel(img);
      label.addMouseListener(this);
      pixels = new int[width*height];
      
      results = new int[width*height];

            
      Image image = img.getImage();
         
      PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
      try
      {
         pg.grabPixels();
      }
      catch(InterruptedException e)
      {
         System.err.println("Interrupted waiting for pixels");
         return;
      }

      for(int i = 0; i<width*height; i++)
         results[i] = pixels[i]; 

      turnTwoDimensional();
      mp.removeAll();
      mp.add(label);
      
      mp.revalidate();
   }

   /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
   private void turnTwoDimensional()
   {
      picture = new int[height][width];
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
            picture[i][j] = pixels[i*width+j];
      
      
   }
   /*
   *  This method takes the picture back to the original picture
   */
   private void reset()
   {
      for(int i = 0; i<width*height; i++)
            pixels[i] = results[i]; 

      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
      mp.removeAll();
      mp.add(label2);
      
      mp.revalidate();
      
      
   }

   /*
   * This method is called to redraw the screen with the new image. 
   */
   private void resetPicture()
   {
      for(int i=0; i<height; i++)
         for(int j=0; j<width; j++)
            pixels[i*width+j] = picture[i][j];

      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
         mp.removeAll();
         mp.add(label2);
         mp.revalidate(); 

   }
      /*
      * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
      */
   private int [] getPixelArray(int pixel)
   {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
   }
      /*
      * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
      */
   private int getPixels(int rgb[])
   {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
         return rgba;
   }

   public void getValue()
   {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
   }

   /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
      * Example function that just removes all red values from the picture. 
      * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
      * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
      * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
      * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
      * integer value so you can give it back to the program and display the new picture. 
      */
   private void fun1()
   {
      
      for(int i=0; i<height; i++)
      {
         for(int j=0; j<width; j++)
         {   
            int rgbArray[] = new int[4];
         
            //get three ints for R, G and B
            rgbArray = getPixelArray(picture[i][j]);
         
         
            rgbArray[1] = 0;
            //take three ints for R, G, B and put them back into a single int
            picture[i][j] = getPixels(rgbArray);
         } 
      }
      resetPicture();
   }



   // rotate the image ***CLOCKWISE***
   private void rotateImage()
   {
      System.out.println("rotateImage called");
      
      int destArray[][] = new int[600][600];
  
      for(int h=0; h<height; ++h)
      {
    	  for(int w=0; w<width; ++w)
          {           
    		  //destArray[w][h]= picture[height-h-1][w];
              destArray[w][h]= picture[height-h-1][width-w-1];
                    
          } 
      }
      
      picture = destArray;

      resetPicture();
   }


   private void grayscale()
   {
      System.out.println("Grayscale called");
      for(int i=0; i<height; i++)
      {
         for(int j=0; j<width; j++)
         {   
            int rgbArray[] = new int[4];
         
            //get three ints for R, G and B
            rgbArray = getPixelArray(picture[i][j]);
            
            double grayR = 0;
            double grayB = 0;
            double grayG = 0;
            int gray = 0;

            grayR = (double)rgbArray[1] * 0.299;
            grayB = (double)rgbArray[2] * 0.587;
            grayG = (double)rgbArray[3] * 0.114;

            gray = ((int)grayR + (int)grayB + (int)grayG);
         
            rgbArray[1] = gray;
            rgbArray[2] = gray;
            rgbArray[3] = gray;

            //take three ints for R, G, B and put them back into a single int
            picture[i][j] = getPixels(rgbArray);
         } 
      }
      resetPicture();
   }

   private void blur()
   {
	   int tempArray[][] = new int[height][width];
      grayscale();
      
      //looping through each pixel in the picture with an exclusion of a 1 pixel border on all sides.
      for(int i=1; i<height-1; i++)
      {
         for(int j=1; j<width-1; j++)
         {  
            int rgbArray[] = new int[4];
         
            //get three ints for R, G and B
            // rgbArray = getPixelArray(picture[i][j]);
            
            //top row of the 3x3
            int tl = getPixelArray(picture[i-1][j-1])[1];
            int tm = getPixelArray(picture[i-1][j])[1];
            int tr = getPixelArray(picture[i-1][j+1])[1];
            
            //middle row of 3x3 excluding the current pixel we are on
            int ml = getPixelArray(picture[i][j-1])[1];
            int mr = getPixelArray(picture[i][j+1])[1];
            
            //bottom row of the 3x3
            int bl = getPixelArray(picture[i+1][j-1])[1];
            int bm = getPixelArray(picture[i+1][j])[1];
            int br = getPixelArray(picture[i+1][j+1])[1];
            
            
            int average = (tl+tm+tr+ml+mr+bl+bm+br)/8;
            
            rgbArray[0] = 255;
            rgbArray[1] = average;
            rgbArray[2] = average;
            rgbArray[3] = average;
            
            //put into temp array to not alter original image.
            //take three ints for R, G, B and put them back into a single int
            tempArray[i][j] = getPixels(rgbArray);
          } 
      }
      //set picture array to what tempArray is so the image can be re-drawn.
      picture= tempArray;
      resetPicture();
   }

   private void edgeDetection()
   {
      grayscale();
      int[][] tempArray = new int[height][width];
      for(int i=1; i<height-1; i++)
      {
         for(int j=1; j<width-1; j++)
         {   
            int rgbArray[] = new int[4];

            int tl = getPixelArray(picture[i-1][j-1])[1];
            int tm = getPixelArray(picture[i-1][j])[1];
            int tr = getPixelArray(picture[i-1][j+1])[1];
            
            int ml = getPixelArray(picture[i][j-1])[1];
            int m = getPixelArray(picture[i][j])[1];
            int mr = getPixelArray(picture[i][j+1])[1];
            
            int bl = getPixelArray(picture[i+1][j-1])[1];
            int bm = getPixelArray(picture[i+1][j])[1];
            int br = getPixelArray(picture[i+1][j+1])[1];

            int[][] mask = {
               { -1, -1, -1 },
               { -1,  8, -1 },
               { -1, -1, -1 }
            };

            int[][] neighborhood = {
               { tl, tm, tr },
               { ml,  m, mr },
               { bl, bm, br },
            };

            int surround = 0;
            for(int x = 0; x < 3; x++)
            {
               for(int y = 0; y < 3; y++)
               {
                  neighborhood[x][y] *= mask[x][y];
                  surround += neighborhood[x][y];
               }
            }
            // System.out.println(surround);
            if(surround >= 100)
            {
               // System.out.println("Change to white");
               rgbArray[1] = 255;
               rgbArray[2] = 255;
               rgbArray[3] = 255;
            }
            else if(surround < 100)
            {
               // System.out.println("change to black");
               rgbArray[1] = 0;
               rgbArray[2] = 0;
               rgbArray[3] = 0;
            }
            tempArray[i][j] = getPixels(rgbArray);
         }
      }
      picture = tempArray;
      resetPicture();	 
   }

   
   //sixthItem
   private void colorDetection()
   {
	   Scanner reader = new Scanner(System.in); 
	   
	   System.out.println("Enter R MIN Value: ");
	   int rMin = reader.nextInt();
	   System.out.println("Enter R MAX Value: ");
	   int rMax = reader.nextInt();
	   
	   System.out.println("Enter G MIN Value: ");
	   int gMin = reader.nextInt();
	   System.out.println("Enter G MAX Value: ");
	   int gMax = reader.nextInt();
	   
	   System.out.println("Enter B MIN Value: ");
	   int bMin = reader.nextInt();
	   System.out.println("Enter B MAX Value: ");
	   int bMax = reader.nextInt();
	   
	   
	   for(int i=0; i<height; i++)
	   {
	      for(int j=0; j<width; j++)
	      { 
	    	  Boolean match = false;
	    	  int rgbArray[] = new int[4];
	          
	          //get three ints for R, G and B
	          rgbArray = getPixelArray(picture[i][j]);
	          
	          //if in red threshold
	          if(rgbArray[1] >= rMin && rgbArray[1] <= rMax)
	          {
	        	//and in green threshold
	        	if(rgbArray[2] >= gMin && rgbArray[2] <= gMax)
	        	{
	        		//and in the blue threshold
	        		if(rgbArray[3] >= bMin && rgbArray[3] <= bMax)
	        		{
	        			match = true;
	        			//color white
	        			rgbArray[1] =255;
	        			rgbArray[2] =255;
	        			rgbArray[3] =255;
	        		}
	        	}
	          }
	          //color black if not a match
	          if(!match)
	          {
	        	rgbArray[1] =0;
      			rgbArray[2] =0;
      			rgbArray[3] =0;
	        	  
	          }
	         	          //take three ints for R, G, B and put them back into a single int
	          picture[i][j] = getPixels(rgbArray);
	      }
	   }
	   resetPicture();
	   reader.close();
   }
   
   //seventhItem
   private void drawHistograms()
   {
	   //total pixels
	   int totalPixels = width*height;
	   
	   //frequency counters for each color & 0-255 value
	   int[] redFreq = new int[256];
	   int[] greenFreq = new int[256];
	   int[] blueFreq = new int[256];
	   
	   //Gathering/calculating histogram data (i.e. frequencies)
	   for(int h=0; h<height; ++h)
	      {
	    	  for(int w=0; w<width; ++w)
	          {           
	    		  int rgbArray[] = new int[4];
	    	         
	              //get three ints for R, G and B
	              rgbArray = getPixelArray(picture[h][w]);
	              
	              //current pixel RGB values
	              int r = rgbArray[1];
	              int g = rgbArray[2];
	              int b = rgbArray[3];
	              
	              //increasing corresponding frequency values by 1. 
	              redFreq[r]++;
	              greenFreq[g]++;
	              blueFreq[b]++;
	              
	          } 
	      }
	   
	   
	   //adjusting frequencies by dividing by 5 (as suggested by hunter in class)
	   for(int i =0; i< 255; i++)
	   {
		   redFreq[i] = redFreq[i]/5;
		   greenFreq[i] = greenFreq[i]/5;
		   blueFreq[i] = blueFreq[i]/5;  
	   }
	   
	   //printing/displaying histogram data to frames/panels    
	   JFrame redFrame = new JFrame("Red");
	   redFrame.setSize(305, 600);
	   redFrame.setLocation(800, 0);
	   JFrame greenFrame = new JFrame("Green");
	   greenFrame.setSize(305, 600);
	   greenFrame.setLocation(1150, 0);
	   JFrame blueFrame = new JFrame("blue");
	   blueFrame.setSize(305, 600);
	   blueFrame.setLocation(1450, 0);
	   
	   //Then pass those arrays to MyPanel constructor (frequency arrays)
	   MyPanel redPanel = new MyPanel(redFreq); 
	   MyPanel greenPanel = new MyPanel(greenFreq);
	   MyPanel bluePanel = new MyPanel(blueFreq);
	   
	   redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
	   redFrame.setVisible(true);
	   greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
	   greenFrame.setVisible(true);
	   blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
	   blueFrame.setVisible(true);
	   start.setEnabled(true);
	   
   }
   
   
   
   private void quit()
   {  
      System.exit(0);
   }

   @Override
   public void mouseEntered(MouseEvent m){}
      
   @Override
   public void mouseExited(MouseEvent m){}
     
   @Override
   public void mouseClicked(MouseEvent m)
   {
      colorX = m.getX();
      colorY = m.getY();
      System.out.println(colorX + "  " + colorY);
      getValue();
      start.setEnabled(true);
   }
      
   @Override
   public void mousePressed(MouseEvent m){}
     
   @Override
   public void mouseReleased(MouseEvent m){}

   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }