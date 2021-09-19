package songgrp.song.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Edwin W (HTW) on Mai 2021
 */

@Entity
public class SongList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ownerId;
    private String name;
    private boolean isPrivate;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "SongList_Song",
            joinColumns = {@JoinColumn(name = "songList_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    @OrderBy(value = "id")
    private Set<Song> songList = new HashSet<>();

    public SongList() {
    }

    public SongList(Integer id, String ownerId, String name, boolean isPrivate, Set<Song> songList) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.isPrivate = isPrivate;
        this.songList = songList;
    }

    public SongList(String name, boolean isPrivate, Set<Song> songList) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.songList = songList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("isPrivate")
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Set<Song> getSongList() {
        return songList;
    }

    public void setSongList(Set<Song> songList) {
        this.songList = songList;
    }

    public void addSong(Song song) {
        songList.add(song);
    }

    @Override
    public String toString() {
        return "SongList{" +
                "id=" + id +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", songList=" + songList +
                '}';
    }
}
