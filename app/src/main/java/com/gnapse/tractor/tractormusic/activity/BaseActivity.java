package com.gnapse.tractor.tractormusic.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.gnapse.tractor.tractormusic.viewmodel.BaseViewModel;

public abstract class BaseActivity<V extends BaseViewModel> extends AppCompatActivity {
    private V mViewModel = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
            @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getViewModel().onResume();
        getViewModel().onViewFocused();
    }

    @Override
    protected void onPause() {
        getViewModel().onPause();
        getViewModel().onViewLostFocus();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reserveViewModel();
    }

    protected void initViewModel() {
        mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewModel.setLifecycleOwner(this);
    }

    public void observeViewModel() {
        getViewModel().observeViewModel();
    }

    public void reserveViewModel() {
        getViewModel().reserveViewModel();
    }

    public V getViewModel() {
        return mViewModel;
    }
}
