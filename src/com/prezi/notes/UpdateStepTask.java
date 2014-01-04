package com.prezi.notes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.AsyncTask;

public class UpdateStepTask extends AsyncTask<String, String, String> {
    private NoteView noteView;
	
    public UpdateStepTask(NoteView noteView) {
        this.noteView = noteView;
    }
    
	protected String doInBackground(String... params) {
    	try {
    		Socket socket = new Socket("intra.prezi.com", 6000);
    		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	
	    	String received;
	    	while ((received = in.readLine()) != null) {
	    	    publishProgress(received);
	    	}
    	} catch (Exception e) {
    		publishProgress("error");
    	}
    	return "over";
    }

    protected void onProgressUpdate(String... progress) {
    	String[] splitted = progress[0].split("@@@");
    	noteView.currentNote = splitted[0];
    	noteView.setBackground(splitted[1]);
    }

    protected void onPostExecute(String result) {
        
    }
}