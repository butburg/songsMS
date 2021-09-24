package lyricsgrp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Edwin W (HTW) on Mai 2021
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LyricsResponse {

    @JsonProperty
    private Message message;

    public LyricsResponse() {
    }

    public LyricsResponse(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LyricsResponse{" +
                "message=" + message +
                '}';
    }
}
