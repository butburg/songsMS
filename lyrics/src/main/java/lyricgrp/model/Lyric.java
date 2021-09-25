package lyricgrp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Edwin W (HTW) on Mai 2021
 */
@XmlRootElement
public class Lyric {

    @JacksonXmlProperty
    private Integer LyricId;
    @JacksonXmlProperty
    private String Lyric;
    @JacksonXmlProperty
    private String LyricUrl;
    @JacksonXmlProperty
    private String LyricSong;
    @JacksonXmlProperty
    private String LyricArtist;

    public Lyric() {
    }

    public Lyric(Integer lyricId, String lyric, String lyricUrl, String lyricSong, String lyricArtist) {
        LyricId = lyricId;
        Lyric = lyric;
        LyricUrl = lyricUrl;
        LyricSong = lyricSong;
        LyricArtist = lyricArtist;
    }

    public Integer getLyricId() {
        return LyricId;
    }

    public void setLyricId(Integer lyricId) {
        LyricId = lyricId;
    }

    public String getLyric() {
        return Lyric;
    }

    public void setLyric(String lyric) {
        Lyric = lyric;
    }

    public String getLyricUrl() {
        return LyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        LyricUrl = lyricUrl;
    }

    public String getLyricSong() {
        return LyricSong;
    }

    public void setLyricSong(String lyricSong) {
        LyricSong = lyricSong;
    }

    public String getLyricArtist() {
        return LyricArtist;
    }

    public void setLyricArtist(String lyricArtist) {
        LyricArtist = lyricArtist;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "LyricId='" + LyricId + '\'' +
                ", Lyric='" + Lyric + '\'' +
                ", LyricUrl='" + LyricUrl + '\'' +
                ", LyricSong='" + LyricSong + '\'' +
                ", LyricArtist='" + LyricArtist + '\'' +
                '}';
    }
}
