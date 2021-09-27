package songgrp.song.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Edwin W (HTW) on Mai 2021
 */
@JsonIgnoreProperties({"LyricId"})
public class Lyric {

    private Integer lyricId;
    private String lyric;
    private String lyricUrl;
    private String lyricSong;
    private String lyricArtist;

    public Lyric() {
    }

    public Lyric(Integer lyricId, String lyric, String lyricUrl, String lyricSong, String lyricArtist) {
        this.lyricId = lyricId;
        this.lyric = lyric;
        this.lyricUrl = lyricUrl;
        this.lyricSong = lyricSong;
        this.lyricArtist = lyricArtist;
    }

    public Integer getLyricId() {
        return lyricId;
    }

    public void setLyricId(Integer lyricId) {
        this.lyricId = lyricId;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public String getLyricSong() {
        return lyricSong;
    }

    public void setLyricSong(String lyricSong) {
        this.lyricSong = lyricSong;
    }

    public String getLyricArtist() {
        return lyricArtist;
    }

    public void setLyricArtist(String lyricArtist) {
        this.lyricArtist = lyricArtist;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "LyricId='" + lyricId + '\'' +
                ", Lyric='" + lyric + '\'' +
                ", LyricUrl='" + lyricUrl + '\'' +
                ", LyricSong='" + lyricSong + '\'' +
                ", LyricArtist='" + lyricArtist + '\'' +
                '}';
    }
}
