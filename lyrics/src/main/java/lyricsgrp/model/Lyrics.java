package lyricsgrp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Edwin W (HTW) on Mai 2021
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lyrics {

    private Integer lyrics_id;
    private Integer explicit;

    private String lyrics_body;
    private String script_tracking_url;
    private String pixel_tracking_url;
    private String lyrics_copyright;
    private String updated_time;

    public Lyrics() {
    }

    public Lyrics(Integer lyrics_id, Integer explicit, String lyrics_body, String script_tracking_url, String pixel_tracking_url, String lyrics_copyright, String updated_time) {
        this.lyrics_id = lyrics_id;
        this.explicit = explicit;
        this.lyrics_body = lyrics_body;
        this.script_tracking_url = script_tracking_url;
        this.pixel_tracking_url = pixel_tracking_url;
        this.lyrics_copyright = lyrics_copyright;
        this.updated_time = updated_time;
    }

    public Integer getLyrics_id() {
        return lyrics_id;
    }

    public void setLyrics_id(Integer lyrics_id) {
        this.lyrics_id = lyrics_id;
    }

    public String getLyrics_body() {
        return lyrics_body;
    }

    public void setLyrics_body(String lyrics_body) {
        this.lyrics_body = lyrics_body;
    }

    public String getScript_tracking_url() {
        return script_tracking_url;
    }

    public void setScript_tracking_url(String script_tracking_url) {
        this.script_tracking_url = script_tracking_url;
    }

    public String getLyrics_copyright() {
        return lyrics_copyright;
    }

    public void setLyrics_copyright(String lyrics_copyright) {
        this.lyrics_copyright = lyrics_copyright;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public Integer getExplicit() {
        return explicit;
    }

    public void setExplicit(Integer explicit) {
        this.explicit = explicit;
    }

    public String getPixel_tracking_url() {
        return pixel_tracking_url;
    }

    public void setPixel_tracking_url(String pixel_tracking_url) {
        this.pixel_tracking_url = pixel_tracking_url;
    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "lyrics_id=" + lyrics_id +
                ", explicit=" + explicit +
                ", lyrics_body='" + lyrics_body + '\'' +
                ", script_tracking_url='" + script_tracking_url + '\'' +
                ", pixel_tracking_url='" + pixel_tracking_url + '\'' +
                ", lyrics_copyright='" + lyrics_copyright + '\'' +
                ", updated_time='" + updated_time + '\'' +
                '}';
    }
}
