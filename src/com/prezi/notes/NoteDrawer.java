package com.prezi.notes;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;

public class NoteDrawer implements SurfaceHolder.Callback {
    private final NoteView mNoteView;
    private SurfaceHolder mHolder;

    public NoteDrawer(Context context) {
        mNoteView = new NoteView(context);
        mNoteView.setListener(new NoteView.ChangeListener() {
            @Override
            public void onChange() {
                draw(mNoteView);
            }
        });
        mNoteView.setForceStart(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        mNoteView.measure(measuredWidth, measuredHeight);
        mNoteView.layout(0, 0, mNoteView.getMeasuredWidth(), mNoteView.getMeasuredHeight());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        mNoteView.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mNoteView.stop();
        mHolder = null;
    }

    private void draw(View view) {
        Canvas canvas;
        try {
            canvas = mHolder.lockCanvas();
        } catch (Exception e) {
            return;
        }
        if (canvas != null) {
            view.draw(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
}
