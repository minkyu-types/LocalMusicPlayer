package com.gnapse.tractor.tractormusic.model;

import android.net.Uri;

public class MusicItem {
    private Uri mMusicUri;
    private String mMusicTitle;
    private String mMusicArtist;
    private Uri mAlbumArtUri;
    private String mDuration;

    public MusicItem(Uri musicUri, String musicTitle, String musicArtist, Uri albumArtUri, String duration) {
        this.mMusicUri = musicUri;
        this.mMusicTitle = musicTitle;
        this.mMusicArtist = musicArtist;
        this.mAlbumArtUri = albumArtUri;
        this.mDuration = duration;
    }

    public Uri getMusicUri() {
        return mMusicUri;
    }

    public void setMusicUri(Uri mMusicUri) {
        this.mMusicUri = mMusicUri;
    }

    public String getMusicTitle() {
        return mMusicTitle;
    }

    public void setMusicTitle(String mMusicTitle) {
        this.mMusicTitle = mMusicTitle;
    }

    public String getMusicArtist() {
        return mMusicArtist;
    }

    public void setMusicArtist(String mMusicArtist) {
        this.mMusicArtist = mMusicArtist;
    }

    public Uri getAlbumArtUri() {
        return mAlbumArtUri;
    }

    public void setAlbumArtUri(Uri mAlbumArtUri) {
        this.mAlbumArtUri = mAlbumArtUri;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }
}
