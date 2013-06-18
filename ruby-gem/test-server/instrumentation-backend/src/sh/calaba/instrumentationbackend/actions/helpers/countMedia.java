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
		String message = "";
		String path = args[0];
		filter = args[1];
		int numberOfImages = 0;
        
		if (path.equals("**ALL_MEDIA**")) {
			File dir = new File(Environment.getExternalStorageDirectory() + "");
			numberOfImages = countMedia(dir.listFiles(), filter);
			message += numberOfImages;
            result.setSuccess(true);
		} else {
			File dir = new File(Environment.getExternalStorageDirectory() + "/"
                                + path);
			File[] files = dir.listFiles();
			if (files != null) {
				numberOfImages = countFiles(files, filter);
				message += numberOfImages;
				result.setSuccess(true);
			} else {
				message = "Directory doesn't exist!";
				result.setSuccess(false);
			}
		}
		result.setMessage(message);
		return result;
	}
    
	/**
	 * counts the files in the array that correspond to the filter
	 *
	 * @param files
	 * @param filter
	 * @return the number of files counted
	 */
	private int countFiles(File[] files, String filter) {
		int result = 0;
		if (files != null) {
			for (File fi : files) {
				if (checkFilter(fi, filter)) {
					result++;
				}
			}
		}
		return result;
	}
	
	/**
	 * recursive call that counts the media files in the input
	 * and the input's sub-directories that correspond to the filter
	 *
	 * @param files
	 * @param filter
	 * @return
	 */
	private int countMedia(File[] files, String filter) {
		int result = 0;
        if(files != null){
            for (File fi : files) {
                if (fi.isDirectory() && !fi.getName().equals(".thumbnails")) {
                    result += countMedia(fi.listFiles(), filter);
                } else if (checkFilter(fi, filter)) {
                    result++;
                }
            }
        }
		return result;
	}
    
	/**
	 * check whether the inputed filter counts the inputed file
	 *
	 * @param file
	 * @param filter
	 * @return true if counted
	 */
	private boolean checkFilter(File file, String filter) {
		boolean result = false;
		if (!filter.equals("video")) {
			result = filterForPhotos(file.getName());
		}
		if (!filter.equals("photo") && !result) {
			result = filterForVideos(file.getName());
		}
		return result;
	}
    
	/**
	 * filter function for photo file extensions
	 *
	 * @param dirName
	 * @return true if has a photo extension
	 */
	private boolean filterForPhotos(String dirName) {
		return (dirName.endsWith(".png") || dirName.endsWith(".jpg")
				|| dirName.endsWith(".jpeg") || dirName.endsWith(".gif")
				|| dirName.endsWith(".bmp") || dirName.endsWith(".webp"));
	}
    
	/**
	 * filter function for video file extensions
	 *
	 * @param dirName
	 * @return true if has a video extension
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

