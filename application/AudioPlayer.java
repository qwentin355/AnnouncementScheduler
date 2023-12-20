package application;
//Java program to play an Audio 
//file using Clip Object 
import java.io.File; 
import java.io.IOException; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 


public class AudioPlayer {

	// to store current position 
    Long currentFrame; 
    Clip clip; 
      
    // current status of clip 
    String status; 
      
    AudioInputStream audioInputStream; 
    static String filePath; 
	
 public AudioPlayer(File source) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
	// create AudioInputStream object 
     audioInputStream =  
             AudioSystem.getAudioInputStream(source.getAbsoluteFile()); 
       
     // create clip reference 
     clip = AudioSystem.getClip(); 
       
     // open audioInputStream to the clip 
     clip.open(audioInputStream); 
       
     clip.start(); 
 }
}
