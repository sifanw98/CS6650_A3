package com.albumstore.bean;

public class Review {
    private String albumID;
    private boolean isLike;

    public Review(String albumID, boolean isLike) {
        this.albumID = albumID;
        this.isLike = isLike;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }
}
