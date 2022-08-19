package com.gnapse.tractor.tractormusic.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gnapse.tractor.tractormusic.R;
import com.gnapse.tractor.tractormusic.model.MusicItem;
import com.gnapse.tractor.tractormusic.activity.MusicPlayerActivity;
import com.gnapse.tractor.tractormusic.musicplayer.MusicPlayer;
import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private ArrayList<MusicItem> mData = null;

    public MusicListAdapter(ArrayList<MusicItem> list) {
        mData = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView musicTitle;
        TextView musicArtist;

        @SuppressLint("NewApi")
        ViewHolder(View itemView) {
            super(itemView);
            musicTitle = itemView.findViewById(R.id.list_music_title);
            musicTitle.setSelected(true);
            musicArtist = itemView.findViewById(R.id.list_music_artist);
            musicArtist.setSelected(true);

            itemView.setOnClickListener(view -> {
                int pos = getAbsoluteAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    MusicPlayer.getInstance((Application) itemView.getContext().getApplicationContext()).setCurrentPlayIndex(pos);
                    Log.e("Adapter : ", "" + MusicPlayer.getInstance((Application) itemView.getContext().getApplicationContext()).getCurrentPlayIndex());
                    Intent intent = new Intent(itemView.getContext(), MusicPlayerActivity.class);
                    itemView.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) itemView.getContext()).toBundle());
                }
            });
        }
    }

    @NonNull
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_recyclerview, parent, false);
        MusicListAdapter.ViewHolder vh = new MusicListAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, int position) {
        MusicItem musicItem = mData.get(position);
        holder.musicTitle.setText(musicItem.getMusicTitle());
        holder.musicArtist.setText(musicItem.getMusicArtist());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
