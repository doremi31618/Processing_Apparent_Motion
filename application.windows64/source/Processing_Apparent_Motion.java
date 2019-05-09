import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import blinkstick.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Processing_Apparent_Motion extends PApplet {



//GuiColorPicker cp;
BlinkStick blinkStick;
    // import controlP5 library
ControlP5 controlP5; 
PWindow win;
int [] colors = {color(255,0,0), color(0,255,0), color(0,0,255)}; 
int mode = 2;
//1 = two pictures;
//2 = three pictures
public void settings()
{
  size(450,200);
}
public void setup() {
  
  
  blinkStick = BlinkStick.findFirst();  
    surface.setLocation(20,20);
    
  controlP5 = new ControlP5(this);
  controlP5.addButton("Color1",1,70,30,60,20);
  controlP5.addButton("Color2",1,70,60,60,20);
  controlP5.addButton("Color3",1,70,90,60,20);
  controlP5.addToggle("Mode Switch",false,170,10,20,20);
  
  controlP5.addSlider("Frame Rate",0,255,24,25,160,100,10);
}
  PImage img;
  float time;
  
public void draw() { 
  time++;
  background(0);  // background black
  if(mode == 1)
  {
    if(time%2 == 0)
    {
      fill(colors[0]);
    }
    else
    {
      fill(colors[1]);
    }
   
  }
  else if(mode == 2)
  {
    if(time % 3 == 0)
    {
      fill(colors[0]);
    }
    else if(time % 3 == 1)
    {
      fill(colors[1]);
    }
    else if(time % 3 == 2)
    {
      fill(colors[2]);
    }
  }
  rect(260,20,width/3,width/3);
  
  pushMatrix();
  fill(255);
  text("Color picker", 20, 20);
  fill(colors[0]);
  rect(25,30,20,20);
  
  fill(colors[1]);
  rect(25,60,20,20);
  
  
  
  fill(255);
  text("Frame Rate : " + frame, 25, 150);
  
  if(mode == 1)
  {
    text("switch 2 colors ", 170,60);
    controlP5.getController("Color3").hide();
  }
  else if(mode ==2)
  {
    controlP5.getController("Color3").show();
    text("switch 3 colors ",170, 60);
    fill(colors[2]);
    rect(25,90,20,20);
  }
  //text(frame + " times per second ",170, 80);
  popMatrix();
}
float frame = 24;
public void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {
      if(frame+12 >1)
        frame += 12;
    } else if (keyCode == DOWN) {
      if(frame-12 >1)
        frame -= 12;
    } 
  } 
  controlP5.getController("Frame Rate").setValue(frame);
  frameRate(frame);
}
public void controlEvent(ControlEvent theEvent) {
  
  if(theEvent.isController()) { 
    
    print("control event from : "+theEvent.getController().getName());
    println(", value : "+theEvent.getController().getValue());
    
    if(theEvent.getController().getName()=="bang1") {
      colors[0] = colors[0] + color(40,40,0);
      if(colors[0]>255) colors[0] = color(40,40,0);    
    }
    
    if(theEvent.getController().getName()=="Color1") {
      if(win == null) win = new PWindow();
      win.index = 0;
       win.firstColor = colors[0];
      //theEvent.getController().getColor()
    }
    
    if(theEvent.getController().getName()=="Color2") {
      if(win == null) win = new PWindow();
      win.index = 1;
       win.firstColor = colors[1];
    }
    if(theEvent.getController().getName()=="Color3") {
      if(win == null) win = new PWindow();
      win.index = 2;
      win.firstColor = colors[2];
    }
    
    if(theEvent.getController().getName()=="Mode Switch") {
      if(theEvent.getController().getValue()==1) mode = 1;
      else                                 mode = 2;
    }
    
    if(theEvent.getController().getName()=="Frame Rate") {
      frame = theEvent.getController().getValue();
      frameRate(frame);
    } 
    
  }  
}

class PWindow extends PApplet {
  GuiColorPicker _cp;
  public int index;
  public int firstColor;
  PWindow() {
    super();
    PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);
  }

  public void settings() {
    size(250, 250);
  }

  public void setup() {
    background(150);
    
    _cp = new GuiColorPicker( 10, 10, 200, 200, firstColor);
  }

  public void draw() {
    _cp.render();
  }
  public @Override void exit()
  {
    colors[index] = _cp.c;
    dispose();
    win = null;
  }
  
  public class GuiColorPicker 
  {
    public int x, y, w, h, c;
    PImage cpImage;
     
    public BlinkStick _blinkStick;
    public GuiColorPicker ( int x, int y, int w, int h, int c )
    {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.c = c;
      
      cpImage = new PImage( w, h );
      _blinkStick = BlinkStick.findFirst(); 
      init();
    }
    
    private void init ()
    {
      // draw color.
      int cw = w - 60;
      for( int i=0; i<cw; i++ ) 
      {
        float nColorPercent = i / (float)cw;
        float rad = (-360 * nColorPercent) * (PI / 180);
        int nR = (int)(cos(rad) * 127 + 128) << 16;
        int nG = (int)(cos(rad + 2 * PI / 3) * 127 + 128) << 8;
        int nB = (int)(Math.cos(rad + 4 * PI / 3) * 127 + 128);
        int nColor = nR | nG | nB;
        
        setGradient( i, 0, 1, h/2, 0xFFFFFF, nColor );
        setGradient( i, (h/2), 1, h/2, nColor, 0x000000 );
      }
      
      // draw black/white.
      drawRect( cw, 0,   30, h/2, 0xFFFFFF );
      drawRect( cw, h/2, 30, h/2, 0 );
      
      // draw grey scale.
      for( int j=0; j<h; j++ )
      {
        int g = 255 - (int)(j/(float)(h-1) * 255 );
        drawRect( w-30, j, 30, 1, color( g, g, g ) );
      }
    }
  
    private void setGradient(int x, int y, float w, float h, int c1, int c2 )
    {
      float deltaR = red(c2) - red(c1);
      float deltaG = green(c2) - green(c1);
      float deltaB = blue(c2) - blue(c1);
  
      for (int j = y; j<(y+h); j++)
      {
        int c = color( red(c1)+(j-y)*(deltaR/h), green(c1)+(j-y)*(deltaG/h), blue(c1)+(j-y)*(deltaB/h) );
        cpImage.set( x, j, c );
      }
    }
    
    private void drawRect( int rx, int ry, int rw, int rh, int rc )
    {
      for(int i=rx; i<rx+rw; i++) 
      {
        for(int j=ry; j<ry+rh; j++) 
        {
          cpImage.set( i, j, rc );
        }
      }
    }
    
    public void render ()
    {
      image( cpImage, x, y );
      
      if( mousePressed &&
          mouseX >= x && 
          mouseX < x + w &&
          mouseY >= y &&
          mouseY < y + h )
      {
        c = get( mouseX, mouseY );
        
        if (_blinkStick != null)
        {
          _blinkStick.setColor(c);
        }
      }
      fill( c );
      rect( x, y+h+10, 20, 20 );
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Processing_Apparent_Motion" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
