package com.prezi.notes;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.prezi.notes.R;

public class NoteView extends FrameLayout {
    public interface ChangeListener {
        public void onChange();
    }

    // About 24 FPS.
    private static final long DELAY_MILLIS = 300;

    private final TextView mNoteTextView;

    private boolean mStarted;
    private boolean mForceStart;
    private boolean mVisible;
    private boolean mRunning;

    private ChangeListener mChangeListener;

    public NoteView(Context context) {
        this(context, null, 0);
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy); 
    }

    public NoteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        LayoutInflater.from(context).inflate(R.layout.card_notes, this);

        mNoteTextView = (TextView) findViewById(R.id.noteText);
    }

    public void setListener(ChangeListener listener) {
        mChangeListener = listener;
    }

    public void setForceStart(boolean forceStart) {
        mForceStart = forceStart;
        updateRunning();
    }

    public void start() {
        mStarted = true;
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = (visibility == VISIBLE);
        updateRunning();
    }

    private final Handler mHandler = new Handler();

    private final Runnable mUpdateTextRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                updateText();
                mHandler.postDelayed(mUpdateTextRunnable, DELAY_MILLIS);
            }
        }
    };

    private void updateRunning() {
        boolean running = (mVisible || mForceStart) && mStarted;
        if (running != mRunning) {
            if (running) {
                mHandler.post(mUpdateTextRunnable);
            } else {
                mHandler.removeCallbacks(mUpdateTextRunnable);
            }
            mRunning = running;
        }
    }

    private String[] notes = {"hello", "cica", "maki", "masik maki"}; 
    
    private String getPathStep() {
    	String response = "para";
    	try {
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpget = new HttpGet("http://oam2.us.prezi.com/~tothmate/step");
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        response = client.execute(httpget, responseHandler);
	        response = notes[Integer.parseInt(response)];
    	} catch (Exception e) {
    		response = e.toString();
    	}
        return response;
    }
    
    private void updateText() {
       mNoteTextView.setText(getPathStep());
        if (mChangeListener != null) {
            mChangeListener.onChange();
        }
    }
}
