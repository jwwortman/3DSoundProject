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
	// float number;

	float x;
	float y;
	float firstX;
	float firstY;

	/** Maximum data buffers we will need. */
	public static final int NUM_BUFFERS = 7;

	/** Maximum emissions we will need. */
	public static final int NUM_SOURCES = 7;

	/** Index of Bell Tower sound */
	public static final int BELLTOWER = 0;

	/** Index of musicBuilding 1 sound */
	public static final int MUSIC = 1;

	/** Index of traffic 2 sound */
	public static final int TRAFFIC = 2;

	/** Index of Footsteps 2 sound */
	public static final int FOOTSTEPS = 3;

	/** Index of bird sound */
	public static final int BIRDS = 4;

	/** Index for Stadium */
	public static final int STADIUM = 5;

	/** Index for bicycle sound */
	public static final int BICYCLE = 6;

	/** Buffers hold sound data. */
	IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);

	/** Sources are points emitting sound. */
	IntBuffer source = BufferUtils.createIntBuffer(NUM_SOURCES);

	/** Position of the source sound. */
	FloatBuffer sourcePos = (FloatBuffer) BufferUtils
			.createFloatBuffer(3 * NUM_BUFFERS);

	/** Velocity of the source sound. */
	FloatBuffer sourceVel = (FloatBuffer) BufferUtils
			.createFloatBuffer(3 * NUM_BUFFERS);

	/** Position of the listener. */
	FloatBuffer listenerPos = (FloatBuffer) BufferUtils.createFloatBuffer(3)
			.put(new float[] { 581.0f, 95.0f, 0.0f }).rewind();

	/** Velocity of the listener. */
	FloatBuffer listenerVel = (FloatBuffer) BufferUtils.createFloatBuffer(3)
			.put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/**
	 * Orientation of the listener. (first 3 elements are "at", second 3 are
	 * "up")
	 */
	FloatBuffer listenerOri = (FloatBuffer) BufferUtils.createFloatBuffer(6)
			.put(new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f }).rewind();

	/**
	 * boolean LoadALData()
	 * 
	 * This function will load our sample data from the disk using the Alut
	 * utility and send the data into OpenAL as a buffer. A source is then also
	 * created to play that buffer.
	 */
	int loadALData() {
		// Load wav data into a buffer.
		AL10.alGenBuffers(buffer);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;
		WaveData waveFile = WaveData.create("BellTower.wav");
		AL10.alBufferData(buffer.get(BELLTOWER), waveFile.format,
				waveFile.data, waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create("Violin.wav");
		AL10.alBufferData(buffer.get(MUSIC), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create("Traffic.wav");
		AL10.alBufferData(buffer.get(TRAFFIC), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create("Footsteps.wav");
		AL10.alBufferData(buffer.get(FOOTSTEPS), waveFile.format,
				waveFile.data, waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create("birds.wav");
		AL10.alBufferData(buffer.get(BIRDS), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create("stadium.wav");
		AL10.alBufferData(buffer.get(STADIUM), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create("birds.wav");
		AL10.alBufferData(buffer.get(BICYCLE), waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();

		// Bind the buffer with the source.
		AL10.alGenSources(source);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		sourcePos.put(0, (float) 863.0);
		sourcePos.put(1, (float) 347.0);
		sourcePos.put(2, (float) 0.0);
		AL10.alSourcei(source.get(BELLTOWER), AL10.AL_BUFFER,
				buffer.get(BELLTOWER));
		AL10.alSourcef(source.get(BELLTOWER), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(BELLTOWER), AL10.AL_GAIN, 2.0f);
		AL10.alSourcef(source.get(BELLTOWER), AL10.AL_REFERENCE_DISTANCE, 5.0f);
		// AL10.alSourcef (source.get(BELLTOWER), AL10.AL_ROLLOFF_FACTOR, 0.0f
		// );
		AL10.alSource(source.get(BELLTOWER), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(BELLTOWER * 3));
		AL10.alSource(source.get(BELLTOWER), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(BELLTOWER * 3));
		AL10.alSourcei(source.get(BELLTOWER), AL10.AL_LOOPING, AL10.AL_TRUE);

		sourcePos.put(3, (float) 866.0);
		sourcePos.put(4, (float) 423.0);
		sourcePos.put(5, (float) 0.0);
		AL10.alSourcei(source.get(MUSIC), AL10.AL_BUFFER, buffer.get(MUSIC));
		AL10.alSourcef(source.get(MUSIC), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(MUSIC), AL10.AL_GAIN, 1.0f);
		AL10.alSourcef(source.get(MUSIC), AL10.AL_REFERENCE_DISTANCE, 0.5f);
		AL10.alSource(source.get(MUSIC), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(MUSIC * 3));
		AL10.alSource(source.get(MUSIC), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(MUSIC * 3));
		AL10.alSourcei(source.get(MUSIC), AL10.AL_LOOPING, AL10.AL_TRUE);

		sourcePos.put(6, (float) 617.0);
		sourcePos.put(7, (float) 386.0);
		sourcePos.put(8, (float) 0.0);
		AL10.alSourcei(source.get(TRAFFIC), AL10.AL_BUFFER, buffer.get(TRAFFIC));
		AL10.alSourcef(source.get(TRAFFIC), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(TRAFFIC), AL10.AL_GAIN, 2.0f);
		AL10.alSourcef(source.get(TRAFFIC), AL10.AL_REFERENCE_DISTANCE, 0.5f);
		AL10.alSource(source.get(TRAFFIC), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(TRAFFIC * 3));
		AL10.alSource(source.get(TRAFFIC), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(TRAFFIC * 3));
		AL10.alSourcei(source.get(TRAFFIC), AL10.AL_LOOPING, AL10.AL_TRUE);

		sourcePos.put(12, (float) 985.0);
		sourcePos.put(13, (float) 429.0);
		sourcePos.put(14, (float) 0.0);
		AL10.alSourcei(source.get(BIRDS), AL10.AL_BUFFER, buffer.get(BIRDS));
		AL10.alSourcef(source.get(BIRDS), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(BIRDS), AL10.AL_GAIN, 2.0f);
		AL10.alSourcef(source.get(BIRDS), AL10.AL_REFERENCE_DISTANCE, 2.0f);
		AL10.alSource(source.get(BIRDS), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(BIRDS * 3));
		AL10.alSource(source.get(BIRDS), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(BIRDS * 3));
		AL10.alSourcei(source.get(BIRDS), AL10.AL_LOOPING, AL10.AL_TRUE);

		AL10.alSourcei(source.get(FOOTSTEPS), AL10.AL_BUFFER,
				buffer.get(FOOTSTEPS));
		AL10.alSourcef(source.get(FOOTSTEPS), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(FOOTSTEPS), AL10.AL_GAIN, 0.5f);
		AL10.alSource(source.get(FOOTSTEPS), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(FOOTSTEPS * 3));
		AL10.alSource(source.get(FOOTSTEPS), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(FOOTSTEPS * 3));
		AL10.alSourcei(source.get(FOOTSTEPS), AL10.AL_LOOPING, AL10.AL_TRUE);

		sourcePos.put(15, (float) 335.0);
		sourcePos.put(16, (float) 245.0);
		sourcePos.put(17, (float) 0.0);
		AL10.alSourcei(source.get(STADIUM), AL10.AL_BUFFER, buffer.get(STADIUM));
		AL10.alSourcef(source.get(STADIUM), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(STADIUM), AL10.AL_GAIN, 2.0f);
		AL10.alSourcef(source.get(STADIUM), AL10.AL_REFERENCE_DISTANCE, 4.0f);
		AL10.alSource(source.get(STADIUM), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(STADIUM * 3));
		AL10.alSource(source.get(STADIUM), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(STADIUM * 3));
		AL10.alSourcei(source.get(STADIUM), AL10.AL_LOOPING, AL10.AL_TRUE);

		sourcePos.put(18, (float) 811.0);
		sourcePos.put(19, (float) 190.0);
		sourcePos.put(20, (float) 0.0);
		AL10.alSourcei(source.get(BICYCLE), AL10.AL_BUFFER, buffer.get(BICYCLE));
		AL10.alSourcef(source.get(BICYCLE), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(BICYCLE), AL10.AL_GAIN, 2.0f);
		AL10.alSourcef(source.get(BICYCLE), AL10.AL_REFERENCE_DISTANCE, 2.0f);
		AL10.alSource(source.get(BICYCLE), AL10.AL_POSITION,
				(FloatBuffer) sourcePos.position(BICYCLE * 3));
		AL10.alSource(source.get(BICYCLE), AL10.AL_VELOCITY,
				(FloatBuffer) sourceVel.position(BICYCLE * 3));
		AL10.alSourcei(source.get(BICYCLE), AL10.AL_LOOPING, AL10.AL_TRUE);

		// Do another error check and return.
		if (AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;

		return AL10.AL_FALSE;
	}

	/**
	 * void setListenerValues()
	 * 
	 * We already defined certain values for the Listener, but we need to tell
	 * OpenAL to use that data. This function does just that.
	 */
	void setListenerValues() {
		AL10.alListener(AL10.AL_POSITION, listenerPos);
		AL10.alListener(AL10.AL_VELOCITY, listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

	/**
	 * void killALData()
	 * HELLO THIS IS A CHANGE
	 * We have allocated memory for our buffers and sources which needs to be
	 * returned to the system. This function frees that memory.
	 */
	void killALData() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public class FrameMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			firstX = e.getX();
			firstY = e.getY();
			System.out.println("Intial x:" + firstX + " ," + "initial y:"
					+ firstY);
		}

		public void mouseReleased(MouseEvent e) {
			AL10.alSourcef(source.get(FOOTSTEPS), AL10.AL_GAIN, 0.0f);
			System.out.println("Mouse Released " + e.getX() + " , " + e.getY());
		}
	}

	public class FrameMouseMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			x = e.getX();
			y = e.getY();

			AL10.alSourcef(source.get(FOOTSTEPS), AL10.AL_GAIN, 0.05f);
			sourcePos.put(9, x);
			sourcePos.put(10, y);
			sourcePos.put(11, (float) 0.0);
			AL10.alSource(source.get(FOOTSTEPS), AL10.AL_POSITION,
					(FloatBuffer) sourcePos.position(FOOTSTEPS * 3));
			listenerPos.put(0, x);
			listenerPos.put(1, y);
			listenerPos.put(2, 0);

			System.out.println(sourcePos.get(9) + "," + sourcePos.get(10));

			AL10.alListener(AL10.AL_POSITION, listenerPos);
			if (firstY < y) {
				listenerOri.put(0, 0);
				listenerOri.put(1, 0);
				listenerOri.put(2, 1);
				listenerOri.put(3, 0);
				listenerOri.put(4, 1);
				listenerOri.put(5, 0);
			} else {
				if (firstY > y) {
					listenerOri.put(0, 0);
					listenerOri.put(1, 0);
					listenerOri.put(2, -1);
					listenerOri.put(3, 0);
					listenerOri.put(4, 1);
					listenerOri.put(5, 0);
				}
			}

			setListenerValues();
			
			  for(int i = 0; i <= 5;i++){ System.out.print(listenerOri.get(i)+
			  " ,"); } System.out.println();
			 
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
		g.drawImage(mapImage, 0, 0, this);
	}

	public static void main(String[] args) {
		MainClass frame = new MainClass();
		frame.setSize(1200, 510);
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

		try {
			AL.create();
		} catch (LWJGLException le) {
			le.printStackTrace();
			return;
		}

		AL10.alGetError();

		// Load the wav data.
		if (loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
			return;
		}

		setListenerValues();

		AL10.alSourcePlay(source.get(BELLTOWER));
		AL10.alSourcePlay(source.get(MUSIC));
		AL10.alSourcePlay(source.get(TRAFFIC));
		AL10.alSourcePlay(source.get(FOOTSTEPS));
		AL10.alSourcePlay(source.get(BIRDS));
		AL10.alSourcePlay(source.get(STADIUM));
		AL10.alSourcePlay(source.get(BICYCLE));

	}

	/**
	 * Check for keyboard hit
	 */
	private boolean kbhit() {
		try {
			return (System.in.available() != 0);
		} catch (IOException ioe) {
		}
		return false;
	}
}
