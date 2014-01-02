package com.prezi.notes;

import android.content.Context;
import android.os.Handler;
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
    private static final long DELAY_MILLIS = 41;

    private final TextView mNoteTextView;

    private boolean mStarted;
    private boolean mForceStart;
    private boolean mVisible;
    private boolean mRunning;

    private ChangeListener mChangeListener;

    public NoteView(Context context) {
        this(context, null, 0);
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

    private void updateText() {
       mNoteTextView.setText("hello cicus");
        if (mChangeListener != null) {
            mChangeListener.onChange();
        }
    }
}
