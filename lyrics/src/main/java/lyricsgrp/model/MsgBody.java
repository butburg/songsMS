package lyricsgrp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Edwin W (HTW) on Mai 2021
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgBody {

    private Lyrics lyrics;

    public MsgBody() {
    }

    public Lyrics getLyrics() {
        return lyrics;
    }

    public void setLyrics(Lyrics lyrics) {
        this.lyrics = lyrics;
    }

    public MsgBody(Lyrics lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return "MsgBody{" +
                "lyrics=" + lyrics +
                '}';
    }
}
