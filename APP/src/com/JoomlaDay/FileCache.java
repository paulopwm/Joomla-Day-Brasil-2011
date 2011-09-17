package com.JoomlaDay;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class FileCache {
    
    private static File cacheDir;
    private File fileCache;
    
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"Android/data/"+context.getString(R.string.package_name));
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        fileCache = new File(cacheDir, filename);
        return fileCache;
        
    }
    
    public boolean saveFile(String content){
    	try {
			FileWriter fw = new FileWriter(fileCache);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
			return true;
		} catch (Exception e) {
			Log.e("FILECACHE", e.toString());
		}
		return false;
    }
    
    public boolean verifyCache(String filename){
    	// create a java calendar instance
    	Calendar calendar = Calendar.getInstance();

    	// get a java.util.Date from the calendar instance.
    	// this date will represent the current instant, or "now".
    	Date now = calendar.getTime();
    	
    	File file = new File(filename);
    	Log.i("FILECACHE - LAST", (file.lastModified() + 5*60*1000)+"");
    	Log.i("FILECACHE - NOW", now.getTime()+"");
    	if (file.length() > 0){
	    	if (now.getTime() <= (file.lastModified() + 5*60*1000)){
	    		return true;
	    	}
    	}
    	return false;
    }
    
    public String readFile(String filePath) throws java.io.IOException{
        byte[] buffer = new byte[(int) new File(filePath).length()];
        BufferedInputStream f = null;
        try {
            f = new BufferedInputStream(new FileInputStream(filePath));
            f.read(buffer);
        } finally {
            if (f != null) try { f.close(); } catch (IOException ignored) { }
        }
        return new String(buffer);
    }
    
    public static void clear(){
        File[] files = cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }

}