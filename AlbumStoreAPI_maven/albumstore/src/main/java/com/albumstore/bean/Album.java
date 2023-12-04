package com.albumstore.bean;

import lombok.Data;

@Data
public class Album {
    private int id;
    private String title;
    private String artist;
    private String year;
    private byte[] image;
}

