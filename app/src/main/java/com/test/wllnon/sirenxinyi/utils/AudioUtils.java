package com.test.wllnon.sirenxinyi.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.test.wllnon.sirenxinyi.R;

/**
 * Created by Administrator on 2016/5/5.
 */
public class AudioUtils {

    private static final String TAG = "AudioUtils";

    private static final AudioUtils instance = new AudioUtils();

    private TextView audioTV;
    private ImageView audioIM;

    private AudioPlayer audioPlayer;
    private AnimateThread animateThread;

    private Activity activity;

    private int playMode = AudioManager.STREAM_MUSIC;
    private boolean isPlaying = false;

    private AudioUtils() {}

    public static AudioUtils getInstance() {
        return instance;
    }

    public void startPlayAudio(Activity activity, String filePath, TextView audioTV, ImageView audioIM) {
        resetPlayAudio();

        if (activity == null || filePath == null) {
            return;
        }

        this.audioTV = audioTV;
        this.audioIM = audioIM;

        this.activity = activity;

        audioPlayer = new AudioPlayer(activity, filePath, onPlayListener);

        startPlayAudio();
    }

    private void startPlayAudio() {
        isPlaying = true;

        audioPlayer.start(playMode);
        animateThread = new AnimateThread();
        animateThread.start();
    }

    private void stopPlayAudio() {
        resetPlayAudio();
    }

    private void resetPlayAudio() {
        if (isPlaying) {
            isPlaying = false;
        }
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.stop();
        }
        if (animateThread != null && animateThread.isRunning()) {
            animateThread.kill();
        }
    }

    private OnPlayListener onPlayListener = new OnPlayListener() {
        @Override
        public void onPrepared() {
            // no-op
        }

        @Override
        public void onCompletion() {
            Log.i(TAG, "onCompletion: ");
            stopPlayAudio();
        }

        @Override
        public void onInterrupt() {
            Log.i(TAG, "onInterrupt: ");
            stopPlayAudio();
        }

        @Override
        public void onError(String s) {
            Log.i(TAG, "onError: ");
            stopPlayAudio();
        }

        @Override
        public void onPlaying(long l) {
            // no-op
        }
    };

    private class AnimateThread extends Thread {

        private boolean isRunning = true;

        private int indexIM = 0;
        private int indexTV = 0;

        private Drawable[] drawables = new Drawable[3];

        private int[] gifs = new int[] {
                R.drawable.ic_volume_mute_black_24dp,
                R.drawable.ic_volume_down_black_24dp,
                R.drawable.ic_volume_up_black_24dp
        };

        private void init() {
            if (isRunning) {
                for (int num = 0; num < gifs.length; ++num) {
                    drawables[num] = activity.getResources().getDrawable(gifs[num]);
                }

                indexIM = 0;
                indexTV = 0;
            }
        }

        private void finish() {
            if (isRunning) {
                indexIM = gifs.length - 1;

                activity.runOnUiThread(updateDrawable);
            }
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void kill() {
            isRunning = false;
        }

        @Override
        public void run() {
            init();

            while (isRunning && isPlaying) {
                activity.runOnUiThread(updateDrawable);

                if (indexIM == 0)
                    ++indexTV;
                indexIM = (indexIM + 1) % gifs.length;

                try {
                    sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            finish();
        }

        private Runnable updateDrawable = new Runnable() {
            @Override
            public void run() {
                audioIM.setImageDrawable(drawables[indexIM]);
                audioTV.setText(indexTV + "″");
            }
        };
    };

}
