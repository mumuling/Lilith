package com.youloft.lilith.login;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.youloft.lilith.R;

/**
 * Created by gyh on 2017/7/14.
 */

public class MediaPlayerHelper implements SurfaceHolder.Callback {

    private static MediaPlayer mMediaPlayer;
    private static SurfaceView mSurfaceView;
    private static SurfaceHolder mHolder;
    public static MediaPlayerHelper initMediaPlayerHelper(Activity content, SurfaceView surfaceView){
        MediaPlayerHelper mediaPlayerHelper = new MediaPlayerHelper(content, surfaceView);
        return mediaPlayerHelper;
    }
    public MediaPlayerHelper(Activity content, SurfaceView surfaceView) {
        if (mMediaPlayer == null) {
            synchronized (MediaPlayerHelper.class) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = MediaPlayer.create(content, R.raw.bg_login);
                    mMediaPlayer.start();
                    mMediaPlayer.setLooping(true);
                }
            }
        }
        mSurfaceView = surfaceView;
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }


    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
