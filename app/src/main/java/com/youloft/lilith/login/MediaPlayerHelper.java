package com.youloft.lilith.login;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.youloft.lilith.LLApplication;
import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;

/**
 * Created by gyh on 2017/7/14.
 * 注册界面背景视屏播放辅助类；使用时采用注册与反注册来使用；
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

    private MediaPlayerHelper(Context content) {
        initMediaPlayIfNeed(content);
    }

    private void initMediaPlayIfNeed(Context content) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(content, R.raw.bg_login);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }

    /**
     * 注册需要使用此播放器的Activity,在activity中注册时需要在onResume()中注册，防止回退时候没法播放；
     *
     * @param content
     * @param surfaceView
     */
    public void register(Activity content, SurfaceView surfaceView) {

        if (surfaceView == mSurfaceView) {
            return;
        }
        list.add(content);
        if (mHolder != null) {
            mHolder.removeCallback(this);
        }
        mSurfaceView = surfaceView;
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
//        Log.d(TAG, "register() called with: content = [" + content + "], surfaceView = [" + surfaceView + "]");
    }

    /**
     * 反注册,如果引用数为0，则释放MediaPlayer；在Activity的onDestroy()中进行反注册；
     *
     * @param content
     */
    public void unregister(Activity content) {
        list.remove(content);
        if (list.isEmpty()) {
            release();
        }
    }


    /**
     * 释放MediaPlayer
     */
    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            instance = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initMediaPlayIfNeed(LLApplication.getInstance());
        init = true;
    }

//    private static final String TAG = "MediaPlayerHelper";

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (init) {
            mMediaPlayer.setDisplay(holder);
        }
    }


    boolean init = false;

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        init = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
