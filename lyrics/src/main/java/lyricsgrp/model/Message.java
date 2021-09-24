package lyricsgrp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Edwin W (HTW) on Mai 2021
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private MsgBody body;
    private MsgHeader header;

    public Message() {
    }

    public MsgBody getBody() {
        return body;
    }

    public Message(MsgBody body, MsgHeader header) {
        this.body = body;
        this.header = header;
    }

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public void setBody(MsgBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "body=" + body +
                ", header=" + header +
                '}';
    }
}
