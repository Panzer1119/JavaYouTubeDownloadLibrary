/*
 *    Copyright 2019 - 2020 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package de.codemakers.download.database.entities;

import java.util.List;

public abstract class AbstractPlaylist<T extends AbstractPlaylist, M extends AbstractFile, E extends AbstractFile, D extends AbstractDatabase, V extends AbstractVideo> extends AbstractEntity<T, D> {
    
    protected String playlistId;
    protected String title;
    protected String playlist;
    protected String uploaderId;
    
    public AbstractPlaylist() {
        this(null, null, null, null);
    }
    
    public AbstractPlaylist(String playlistId, String title, String playlist, String uploaderId) {
        this.playlistId = playlistId;
        this.title = title;
        this.playlist = playlist;
        this.uploaderId = uploaderId;
    }
    
    public String getPlaylistId() {
        return playlistId;
    }
    
    public T setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
        return (T) this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public T setTitle(String title) {
        this.title = title;
        return (T) this;
    }
    
    public String getPlaylist() {
        return playlist;
    }
    
    public T setPlaylist(String playlist) {
        this.playlist = playlist;
        return (T) this;
    }
    
    public String getUploaderId() {
        return uploaderId;
    }
    
    public T setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
        return (T) this;
    }
    
    @Override
    protected T getFromDatabase() {
        return (T) useDatabaseOrNull((database) -> database.getPlaylistByPlaylistId(getPlaylistId()));
    }
    
    public List<V> getVideos() {
        return useDatabaseOrNull((database) -> database.getVideosByPlaylistId(getPlaylistId()));
    }
    
    public List<String> getVideoIds() {
        return useDatabaseOrNull((database) -> database.getVideoIdsByPlaylistId(getPlaylistId()));
    }
    
    public boolean containsVideo(V video) {
        if (video == null) {
            return false;
        }
        return containsVideo(video.getVideoId());
    }
    
    public boolean containsVideo(final String videoId) {
        return useDatabase((database) -> database.playlistContainsVideo(getPlaylistId(), videoId), false);
    }
    
    public int getIndex(V video) {
        if (video == null) {
            return -1;
        }
        return getIndex(video.getVideoId());
    }
    
    public abstract int getIndex(String videoId);
    
}
