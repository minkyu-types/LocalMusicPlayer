package com.gnapse.tractor.tractormusic.activity;

import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gnapse.tractor.tractormusic.R;
import com.gnapse.tractor.tractormusic.model.MusicRepository;
import com.gnapse.tractor.tractormusic.viewmodel.ViewModelFactory;
import com.gnapse.tractor.tractormusic.manager.PermissionManager;
import com.gnapse.tractor.tractormusic.model.MusicItem;
import com.gnapse.tractor.tractormusic.adapter.MusicListAdapter;
import com.gnapse.tractor.tractormusic.viewmodel.MusicListViewModel;
import com.gnapse.tractor.tractormusic.musicplayer.MusicPlayer;
import java.util.ArrayList;

public class MusicListActivity extends BaseActivity<MusicListViewModel> {
    public static final String TAG = MusicListActivity.class.getSimpleName();

    private ArrayList<MusicItem> mMusicItemList = new ArrayList<MusicItem>();

    private RecyclerView mRecyclerView = null;
    private MusicListAdapter mMusicListAdapter = null;
    private TextView mMusicCount;
    private TextView mMusicTitle;
    private TextView mMusicArtist;
    private Parcelable mScrollState;
    private RecyclerView.LayoutManager mLayoutManager;

    public MusicListActivity() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        Log.e(TAG, "onCreate");

        PermissionManager.getInstance(this).checkPermissions();
        MusicRepository.getInstance(getApplication()).getExternalMusicData();
        initView();
        initViewModel();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        observeViewModel();
    }

    @Override
    protected void onPause() {
        mScrollState = mLayoutManager.onSaveInstanceState();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
        initRecyclerView();
        mLayoutManager.onRestoreInstanceState(mScrollState);
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        MusicPlayer.getInstance(getApplication()).destroyMediaPlayer();
        super.onDestroy();
    }

    private void initView() {
        mMusicCount = findViewById(R.id.music_count);
        mRecyclerView = findViewById(R.id.list_recyclerview);
    }

    private void initRecyclerView() {
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        mRecyclerView.addItemDecoration(divider);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (MusicPlayer.getInstance(getApplication()).isShuffled()) {
            mMusicItemList = MusicRepository.getInstance(getApplication()).getShuffledList();
            MusicPlayer.getInstance(getApplication()).setIsShuffled(false);
        } else {
            mMusicItemList = getViewModel().getMusicList();
        }
        mMusicListAdapter = new MusicListAdapter(mMusicItemList);
        mRecyclerView.setAdapter(mMusicListAdapter);
        mMusicCount.setText(mMusicListAdapter.getItemCount() + " Songs");

        mMusicListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
    }

    @Override
    public MusicListViewModel getViewModel() {
        MusicListViewModel viewModel = super.getViewModel();
        if (viewModel == null) {
            viewModel = new ViewModelProvider(getViewModelStore(), new ViewModelFactory(getApplication())).get(MusicListViewModel.class);
        }
        return viewModel;
    }

    @Override
    public void observeViewModel() {
        super.observeViewModel();

        getViewModel().getMusicTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String title) {
                mMusicTitle.setText(title);
            }
        });

        getViewModel().getMusicArtist().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String artist) {
                mMusicArtist.setText(artist);
            }
        });
    }
}