package lyricsgrp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Edwin W (HTW) on Mai 2021
 */
@XmlRootElement
public class LyricsResponse {

    @JacksonXmlProperty
    private String LyricArtist;
    @JacksonXmlProperty
    private String Lyric;
    @JacksonXmlProperty
    private String LyricCorrectUrl;

    public LyricsResponse(String lyricArtist, String lyric, String lyricCorrectUrl) {
        LyricArtist = lyricArtist;
        Lyric = lyric;
        LyricCorrectUrl = lyricCorrectUrl;
    }

    public String getLyricCorrectUrl() {
        return LyricCorrectUrl;
    }

    public void setLyricCorrectUrl(String lyricCorrectUrl) {
        LyricCorrectUrl = lyricCorrectUrl;
    }


    public LyricsResponse() {
    }

    public String getLyricArtist() {
        return LyricArtist;
    }

    public void setLyricArtist(String lyricArtist) {
        LyricArtist = lyricArtist;
    }

    public String getLyric() {
        return Lyric;
    }

    public void setLyric(String lyric) {
        Lyric = lyric;
    }

    public LyricsResponse(String lyricArtist, String lyric) {
        LyricArtist = lyricArtist;
        Lyric = lyric;
    }

    @Override
    public String toString() {
        return "LyricsResponse{" +
                "LyricArtist='" + LyricArtist + '\'' +
                ", Lyric='" + Lyric + '\'' +
                ", LyricCorrectUrl='" + LyricCorrectUrl + '\'' +
                '}';
    }
}
