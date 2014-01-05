package com.prezi.notes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap img = null;
        try {
        	byte[] byteArray;
        	if (NoteView.imageCache.containsKey(url)) {
        		byteArray = NoteView.imageCache.get(url);
        	} else {
	            InputStream in = new java.net.URL(url).openStream();
	            
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            int next = in.read();
	            while (next > -1 && !isCancelled()) {
	                bos.write(next);
	                next = in.read();
	            }
	            if (isCancelled()) {
	            	in.close();
	            	return null;
	            }
	            bos.flush();
	            byteArray = bos.toByteArray();
	            NoteView.imageCache.put(url, byteArray);
        	}
            
            img = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {}
        return img;
    }

    protected void onPostExecute(Bitmap result) {
    	bmImage.setImageBitmap(result);
    }
}
