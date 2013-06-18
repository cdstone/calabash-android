package sh.calaba.instrumentationbackend.actions.helpers;

import com.jayway.android.robotium.solo.Solo;

import java.io.File;

import android.os.Environment;
import android.view.Display;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 counts the media files (photos + videos) in the specified folder
 **/
public class countMedia implements Action {
    
    // arg[0] = the folder
    // arg[1] = the specified filter
    @Override
    public Result execute(String... args) {
        Result result = new Result();
        String filter = "";
        String path = args[0];
        filter = args[1];
        
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + path);
        System.out.println(dir);
        
        File[] files = dir.listFiles();
        int numberOfImages = 0;
        if (files != null) {
            // count photo files since not filtered against them
			if(!filter.equals("video")){
				for(int i = 0; i < files.length; i++){
					if(filterForPhotos(files[i].getName())){
						numberOfImages++;
					}
				}
			}
            // count video files since not filtered against them
			if(!filter.equals("photo")){
				for(int i = 0; i < files.length; i++){
					if(filterForVideos(files[i].getName())){
						numberOfImages++;
					}
				}
			}
            String message = "" + numberOfImages;
            result.setMessage(message);
            result.setSuccess(true);
		}
        else{
            String message = "Directory doesn't exist!";
            result.setMessage(message);
            result.setSuccess(false);
        }
        return result;
    }
    
    /**
	 * filter function for photo file extensions
	 * @param dirName - name of inputted file
	 * @return true if file name has a photo extension
	 */
	private boolean filterForPhotos(String dirName) {
		return (dirName.endsWith(".png") || dirName.endsWith(".jpg")
				|| dirName.endsWith(".jpeg") || dirName.endsWith(".gif")
				|| dirName.endsWith(".bmp") || dirName.endsWith(".webp"));
	}
    
    /**
     * filter function for video file extensions
     * @param dirName - name of inputted file
     * @return true if file name has a video extension
     */
	private boolean filterForVideos(String dirName) {
		return (dirName.endsWith(".3gp") || dirName.endsWith(".mp4")
				|| dirName.endsWith(".ts") || dirName.endsWith(".webm")
				|| dirName.endsWith(".mkv") || dirName.endsWith(".m4v"));
	}
    
    @Override
    public String key() {
        return "count_media";
    }
}

