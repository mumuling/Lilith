package com.youloft.lilith.login;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;

/**
 * Created by gyh on 2017/7/14.
 */

public class MediaPlayerHelper implements SurfaceHolder.Callback {
    private static MediaPlayerHelper instance = null;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private HashSet<Activity> list = new HashSet<>();

    public static MediaPlayerHelper getInstance() {
        if (instance == null) {
            synchronized (MediaPlayerHelper.class) {
                if (instance == null) {
                    instance = new MediaPlayerHelper(Utils.getContext());
                }
            }
        }
        return instance;
    }

    public void register(Activity content, SurfaceView surfaceView) {
        list.add(content);
        if (mHolder != null) {
            mHolder.removeCallback(this);
        }
        mSurfaceView = surfaceView;
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }

    public void unregister(Activity content) {
        list.remove(content);
        if (list.isEmpty()) {
            release();
        }
    }

    private MediaPlayerHelper(Context content) {
        mMediaPlayer = MediaPlayer.create(content, R.raw.bg_login);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }


    /**
     * 释放MediaPlayer
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            instance = null;
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
        holder.removeCallback(this);
    }
}
