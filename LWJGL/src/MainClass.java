import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.Sys;
import java.awt.event.*;
import java.awt.image.BufferedImage;
  
public class MainClass extends JFrame {
	//float number;
	/** Maximum data buffers we will need. */
	  public static final int NUM_BUFFERS = 3;
	 
	  /** Maximum emissions we will need. */
	  public static final int NUM_SOURCES = 3;
	 
	  /** Index of battle sound */
	  public static final int OCEAN = 0;
	 
	  /** Index of gun 1 sound */
	  public static final int THUNDER = 1;
	 
	  /** Index of gun 2 sound */
	  public static final int WATERDROP = 2;
  /** Buffers hold sound data. */
  IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
  
  /** Sources are points emitting sound. */
  IntBuffer source = BufferUtils.createIntBuffer(NUM_SOURCES);
  
  /** Position of the source sound. */
  FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3*NUM_BUFFERS);
  
  /** Velocity of the source sound. */
  FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3*NUM_BUFFERS);
  
  /** Position of the listener. */
  FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
  
  /** Velocity of the listener. */
  FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
  
  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
  FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f }).rewind();
  /**
  * boolean LoadALData()
  *
  *  This function will load our sample data from the disk using the Alut
  *  utility and send the data into OpenAL as a buffer. A source is then
  *  also created to play that buffer.
  */
  int loadALData() {
    // Load wav data into a buffer.
    AL10.alGenBuffers(buffer);
  
    if(AL10.alGetError() != AL10.AL_NO_ERROR)
      return AL10.AL_FALSE;
 
    WaveData waveFile = WaveData.create("ocean.wav"); 
    AL10.alBufferData(buffer.get(OCEAN), waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
    
    waveFile = WaveData.create("thunder.wav"); 
    AL10.alBufferData(buffer.get(THUNDER), waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
    
    waveFile = WaveData.create("waterdrop.wav"); 
    AL10.alBufferData(buffer.get(WATERDROP), waveFile.format, waveFile.data, waveFile.samplerate);
    waveFile.dispose();
  
    // Bind the buffer with the source.
    AL10.alGenSources(source);
  
    if (AL10.alGetError() != AL10.AL_NO_ERROR)
      return AL10.AL_FALSE;
    
    sourcePos.put(0, (float) 25.0);
    sourcePos.put(1, (float) 25.0);
    sourcePos.put(2, (float) 0.0);
    AL10.alSourcei(source.get(OCEAN), AL10.AL_BUFFER,   buffer.get(OCEAN) );
    AL10.alSourcef(source.get(OCEAN), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(OCEAN), AL10.AL_GAIN,     2.0f          );
    AL10.alSource (source.get(OCEAN), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(OCEAN*3));
    AL10.alSource (source.get(OCEAN), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(OCEAN*3)     );
    AL10.alSourcei(source.get(OCEAN), AL10.AL_LOOPING,  AL10.AL_TRUE  );
    
    sourcePos.put(3, (float) 185.0);
    sourcePos.put(4, (float) 185.0);
    sourcePos.put(5, (float) 0.0);
    AL10.alSourcei(source.get(THUNDER), AL10.AL_BUFFER,   buffer.get(THUNDER) );
    AL10.alSourcef(source.get(THUNDER), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(THUNDER), AL10.AL_GAIN,     2.0f          );
    AL10.alSource (source.get(THUNDER), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(THUNDER*3)     );
    AL10.alSource (source.get(THUNDER), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(THUNDER*3)     );
    AL10.alSourcei(source.get(THUNDER), AL10.AL_LOOPING,  AL10.AL_TRUE  );
    
    sourcePos.put(6, (float) 75.0);
    sourcePos.put(7, (float) 75.0);
    sourcePos.put(8, (float) 0.0);
    AL10.alSourcei(source.get(WATERDROP), AL10.AL_BUFFER,   buffer.get(WATERDROP) );
    AL10.alSourcef(source.get(WATERDROP), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(WATERDROP), AL10.AL_GAIN,     2.0f          );
    AL10.alSource (source.get(WATERDROP), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(WATERDROP*3)     );
    AL10.alSource (source.get(WATERDROP), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(WATERDROP*3)     );
    AL10.alSourcei(source.get(WATERDROP), AL10.AL_LOOPING,  AL10.AL_TRUE  );
  
    // Do another error check and return.
    if (AL10.alGetError() == AL10.AL_NO_ERROR)
      return AL10.AL_TRUE;
  
    return AL10.AL_FALSE;
  }
  
  /**
   * void setListenerValues()
   *
   *  We already defined certain values for the Listener, but we need
   *  to tell OpenAL to use that data. This function does just that.
   */
  void setListenerValues() {
    AL10.alListener(AL10.AL_POSITION,    listenerPos);
    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
  }
  
  /**
   * void killALData()
   *
   *  We have allocated memory for our buffers and sources which needs
   *  to be returned to the system. This function frees that memory.
   */
  void killALData() {
    AL10.alDeleteSources(source);
    AL10.alDeleteBuffers(buffer);
  }
  
  public class FrameMouseListener extends MouseAdapter{
	  public void mouseReleased(MouseEvent e){ 
		 // AL10.alSourcef(source.get(0), AL10.AL_GAIN,     0.0f          );
		  System.out.println("Mouse Released");
	  }
  }
  
  public class FrameMouseMotionListener extends MouseMotionAdapter{
	 /* public void mousePressed(MouseEvent e) {
	        float x = e.getX();
	        float y = e.getY();
	        System.out.println(x + "pressed" + y);
	    }
	    */
	  public void mouseDragged(MouseEvent e)
		 {
		     //AL10.alSourcef(source.get(0), AL10.AL_GAIN,     5.0f          );
			 float x = e.getX();
			 float y = e.getY();
			 listenerPos.put(0, x);
			 listenerPos.put(1, y);
			 listenerPos.put(2,0);  
			 System.out.println(listenerPos.get(0) +","+listenerPos.get(1));
			 System.out.println(sourcePos.get(0) +","+sourcePos.get(1));
		
			 AL10.alListener(AL10.AL_POSITION,    listenerPos);
		 }
  }
  
  public void paint(Graphics g) {
	  BufferedImage mapImage = null;
	  try {
	      mapImage = ImageIO.read(new File("UFCampusMap.jpg"));
	  } catch (IOException e) {
	  }
	  
	  BufferedImage img = null;
	  try {
	      img = ImageIO.read(new File("icon.png"));
	  } catch (IOException e) {
	  }
	  
	  BufferedImage ocean = null;
	  try {
	      ocean = ImageIO.read(new File("ocean.jpeg"));
	  } catch (IOException e) {
	  }
	  
	  BufferedImage thunder = null;
	  try {
	      thunder = ImageIO.read(new File("thunder.jpeg"));
	  } catch (IOException e) {
	  }
	  
	  BufferedImage drop = null;
	  try {
	      drop = ImageIO.read(new File("drop.jpg"));
	  } catch (IOException e) {
	  }
	  
	  g.drawImage(mapImage,0,0, this);
     // g.drawImage(img, 140, 135, 20, 20, this);
      g.drawImage(ocean, 25, 25, 20, 20, this);
      g.drawImage(thunder, 185, 185, 20, 20, this);
      g.drawImage(drop, 75, 75, 20, 20, this);
     
      
   }
  
  public static void main(String[] args) {
	  
	  //JFrame frame = new JFrame("MyFrame");
	  MainClass frame = new MainClass();
	  frame.setSize(300, 300);
	  frame.setLocationRelativeTo(null);
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  frame.setVisible(true); // Display the frame
	  FrameMouseMotionListener listener = frame.new FrameMouseMotionListener();
	  FrameMouseListener mouseListener = frame.new FrameMouseListener();
	  frame.addMouseMotionListener(listener);
	  frame.addMouseListener(mouseListener);
	  frame.execute();
  }
 
  public void execute() {
	  
    // Initialize OpenAL and clear the error bit.
    try{
      AL.create();
    } catch (LWJGLException le) {
      le.printStackTrace();
      return;
    }
 
    AL10.alGetError();
  
    // Load the wav data.
    if(loadALData() == AL10.AL_FALSE) {
      System.out.println("Error loading data.");
      return;
    }
 
    setListenerValues();
 
    AL10.alSourcePlay(source.get(OCEAN));
    AL10.alSourcePlay(source.get(THUNDER));
    AL10.alSourcePlay(source.get(WATERDROP));
 
    // Loop.
    long time = Sys.getTime();
    long elapse = 0;
 
    System.out.println("Press ENTER to exit");
 
    //while (!kbhit()) {
    
    	// AL10.alSourcePlay(source.get(0));
   // }
      
  }
  /**
   *  Check for keyboard hit
   */ 
  private boolean kbhit() {
    try {
      return (System.in.available() != 0);
    } catch (IOException ioe) {
    }
    return false;
  }
}
