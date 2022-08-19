package com.gnapse.tractor.tractormusic.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.gnapse.tractor.tractormusic.R;
import com.gnapse.tractor.tractormusic.viewmodel.ViewModelFactory;
import com.gnapse.tractor.tractormusic.model.MusicRepository;
import com.gnapse.tractor.tractormusic.musicplayer.MusicPlayer;
import com.gnapse.tractor.tractormusic.viewmodel.MusicPlayerViewModel;

public class MusicPlayerActivity extends BaseActivity<MusicPlayerViewModel> implements View.OnClickListener {
    public static final String TAG = MusicPlayerActivity.class.getSimpleName();

    private static final int HANDLER_START_PROGRESS = 1;
    private static final int HANDLER_UPDATE_PROGRESS = 2;
    private static final int HANDLER_UPDATE_UI = 3;
    private MusicPlayer mMusicPlayer;
    private MusicRepository mMusicRepository;

    private ImageView mThumbnail;
    private TextView mTitleTextView;
    private TextView mArtistTextView;

    private TextView mCurrPlayingTime;
    private TextView mDuration;

    private SeekBar mSeekbar;

    private Button mShuffleButton;
    private Button mPrevButton;
    private Button mPlayButton;
    private Button mNextButton;
    private Button mRepeatButton;

    private int mMax;
    private int mRepeatMode = 0; // 0: false | 1: all | 2: one=
    private boolean mIsPlaying;

    public MusicPlayerActivity() {
        mMusicPlayer = MusicPlayer.getInstance(getApplication());
        mMusicRepository = MusicRepository.getInstance(getApplication());
    }

    private Handler mSeekbarUpdateHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_START_PROGRESS:
                    mSeekbar.setProgress(mMusicPlayer.getMediaPlayer().getCurrentPosition()/1000);
                    mSeekbarUpdateHandler.sendEmptyMessage(HANDLER_UPDATE_PROGRESS);
                    break;
                case HANDLER_UPDATE_PROGRESS:
                    mSeekbarUpdateHandler.sendEmptyMessageDelayed(HANDLER_UPDATE_PROGRESS, 1000);
                    mSeekbar.setProgress(mMusicPlayer.getMediaPlayer().getCurrentPosition()/1000);

                    // TO-DO
                    // Update player UI automatically when current song ends
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Log.e(TAG, "onCreate");

        mMusicPlayer.initMediaSetting();
        initView();
        initViewModel();
        initButtonSetting();
        updatePlayerUI();

        mMusicPlayer.play();
        mIsPlaying = true;

        setSeekbarChangeListener();
        setSeekbarMax();
        startSeekbarThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        observeViewModel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSeekbarUpdateHandler.hasMessages(HANDLER_UPDATE_PROGRESS)) {
            mSeekbarUpdateHandler.removeMessages(HANDLER_UPDATE_PROGRESS);
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        mMusicPlayer.setPrevPlayIndex(mMusicPlayer.getCurrentPlayIndex());
        super.onDestroy();
    }

    private void initView() {
        mThumbnail = findViewById(R.id.cover_flow);
        mTitleTextView = findViewById(R.id.player_music_title);
        mTitleTextView.setSelected(true);
        mArtistTextView = findViewById(R.id.player_music_artist);
        mArtistTextView.setSelected(true);

        mSeekbar = findViewById(R.id.custom_slider);

        mCurrPlayingTime = findViewById(R.id.player_curr_time);
        mDuration = findViewById(R.id.player_full_time);

        mShuffleButton = findViewById(R.id.shuffle_button);
        mPrevButton = findViewById(R.id.prev_button);
        mPlayButton = findViewById(R.id.play_button);
        mNextButton = findViewById(R.id.next_button);
        mRepeatButton = findViewById(R.id.repeat_button);
    }

    private void initButtonSetting() {
        mShuffleButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_shuffle_a));
        mPrevButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_prev));
        mPlayButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_pause));
        mNextButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_next));
        mRepeatButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_repeat_a_p));

        mShuffleButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);
    }

    private void setPlayPauseButton() {
        if (mIsPlaying) {
            mPlayButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_pause));
        } else {
            mPlayButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_play));
        }
    }

    private void updatePlayerUI() {
        mThumbnail.setImageURI(MusicRepository.getInstance(getApplication()).getMusicItemList().get(mMusicPlayer.getCurrentPlayIndex()).getAlbumArtUri());
        mTitleTextView.setText(MusicRepository.getInstance(getApplication()).getMusicItemList().get(mMusicPlayer.getCurrentPlayIndex()).getMusicTitle());
        mArtistTextView.setText(MusicRepository.getInstance(getApplication()).getMusicItemList().get(mMusicPlayer.getCurrentPlayIndex()).getMusicArtist());
        mDuration.setText(MusicRepository.getInstance(getApplication()).convertDuration());
    }

    private void setSeekbarMax() {
        mMax = Integer.parseInt(
                MusicRepository.getInstance(getApplication()).getMusicItemList().get(mMusicPlayer.getCurrentPlayIndex()).getDuration())/1000;
        mSeekbar.setMax(mMax);
    }

    private void setSeekbarChangeListener() {
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String curr;
                int min = progress/60;
                int sec = progress%60;

                if (min < 10) {
                    if (sec <= 9) {
                        curr = "0" + min + ":0" + sec;
                    } else {
                        curr = "0" + min + ":" + sec;
                    }
                } else {
                    if (sec <= 9) {
                        curr = "" + min + ":0" + sec;
                    } else {
                        curr = "" + min + ":" + sec;
                    }
                }

                mCurrPlayingTime.setText(curr);
                if (fromUser) {
                    mMusicPlayer.getMediaPlayer().seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "onStopTrackingTouch");
            }
        });
    }

    private void startSeekbarThread() {
        mSeekbarUpdateHandler.sendEmptyMessage(HANDLER_START_PROGRESS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shuffle_button:
                mMusicPlayer.shuffle();
                mSeekbar.setProgress(0);
                updatePlayerUI();
                mMusicPlayer.play();
                break;
            case R.id.prev_button:
                mIsPlaying = true;
                mSeekbar.setProgress(0);
                mMusicPlayer.prev();
                setPlayPauseButton();
                updatePlayerUI();
                setSeekbarMax();
                break;
            case R.id.play_button:
                if (mIsPlaying) {
                    mMusicPlayer.pause();
                    mIsPlaying = false;
                } else {
                    mMusicPlayer.play();
                    mIsPlaying = true;
                }
                setPlayPauseButton();
                break;
            case R.id.next_button:
                mIsPlaying = true;
                mSeekbar.setProgress(0);
                mMusicPlayer.next();
                setPlayPauseButton();
                updatePlayerUI();
                setSeekbarMax();
                break;
            case R.id.repeat_button:
                setRepeatMode(mRepeatMode);
                break;
        }
    }

    private void setRepeatMode(int repeat) {
        switch (repeat) {
            case 0:
                mRepeatMode = 1;
                mMusicRepository.setRepeatMode(1);
                mRepeatButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_repeat_a_n));
                mMusicPlayer.repeatAll();
                break;
            case 1:
                mRepeatMode = 2;
                mMusicRepository.setRepeatMode(2);
                mRepeatButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_repeat_1_n));
                mMusicPlayer.repeatThis();
                break;
            case 2:
                mRepeatMode = 0;
                mMusicRepository.setRepeatMode(0);
                mRepeatButton.setBackground(getResources().getDrawable(R.drawable.me_ico_player_repeat_a_p));
                mMusicPlayer.repeatNone();
                break;
        }
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
    }

    @Override
    public MusicPlayerViewModel getViewModel() {
        MusicPlayerViewModel viewModel = super.getViewModel();
        if (viewModel == null) {
            viewModel = new ViewModelProvider(getViewModelStore(), new ViewModelFactory(getApplication())).get(MusicPlayerViewModel.class);
        }
        return viewModel;
    }

    @Override
    public void observeViewModel() {
        super.observeViewModel();

        getViewModel().getMusicTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newTitle) {
                mTitleTextView.setText(newTitle);
            }
        });

        getViewModel().getMusicArtist().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newArtist) {
                mArtistTextView.setText(newArtist);
            }
        });

        getViewModel().getDuration().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String endtime) {
                mDuration.setText(endtime);
            }
        });

        getViewModel().getMusicRepeatMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer repeat) {
                mRepeatMode = repeat;
            }
        });

        getViewModel().getAlbumArtUri().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                mThumbnail.setImageURI(uri);
            }
        });
    }
}