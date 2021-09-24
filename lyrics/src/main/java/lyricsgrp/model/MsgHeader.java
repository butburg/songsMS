package lyricsgrp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgHeader {
    private String status_code;
    private String execute_time;

    public MsgHeader() {
    }

    public MsgHeader(String status_code, String execute_time) {
        this.status_code = status_code;
        this.execute_time = execute_time;
    }


    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(String execute_time) {
        this.execute_time = execute_time;
    }

    @Override
    public String toString() {
        return "MsgHeader{" +
                "status_code='" + status_code + '\'' +
                ", execute_time='" + execute_time + '\'' +
                '}';
    }
}
