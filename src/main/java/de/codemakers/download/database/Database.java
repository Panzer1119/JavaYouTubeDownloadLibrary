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

package de.codemakers.download.database;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.Logger;
import de.codemakers.download.database.entities.*;
import de.codemakers.io.IOUtil;
import de.codemakers.io.file.AdvancedFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    
    // // // SQL
    // // Tables
    public static final String TABLE_VIDEOS = "videos";
    public static final String TABLE_PLAYLISTS = "playlists";
    public static final String TABLE_PLAYLIST_VIDEOS = "playlistVideos";
    public static final String TABLE_MEDIA_FILES = "mediaFiles";
    public static final String TABLE_EXTRA_FILES = "extraFiles";
    public static final String TABLE_VIDEO_QUEUE = "videoQueue";
    // // Columns
    // Table: videos
    public static final String TABLE_VIDEOS_COLUMN_ID = "id";
    public static final String TABLE_VIDEOS_COLUMN_UPLOADER_ID = "uploaderId";
    public static final String TABLE_VIDEOS_COLUMN_TITLE = "title";
    public static final String TABLE_VIDEOS_COLUMN_ALT_TITLE = "altTitle";
    public static final String TABLE_VIDEOS_COLUMN_DURATION = "duration";
    public static final String TABLE_VIDEOS_COLUMN_UPLOAD_DATE = "uploadDate";
    // Table: uploaders
    public static final String TABLE_UPLOADERS_COLUMN_ID = "id";
    public static final String TABLE_UPLOADERS_COLUMN_NAME = "name";
    // Table: playlists
    public static final String TABLE_PLAYLISTS_COLUMN_ID = "id";
    public static final String TABLE_PLAYLISTS_COLUMN_TITLE = "title";
    public static final String TABLE_PLAYLISTS_COLUMN_PLAYLIST = "playlist";
    public static final String TABLE_PLAYLISTS_COLUMN_UPLOADER = "uploader";
    public static final String TABLE_PLAYLISTS_COLUMN_UPLOADER_ID = "uploaderId";
    // Table: playlistVideos
    public static final String TABLE_PLAYLIST_VIDEOS_COLUMN_PLAYLIST_ID = "playlistId";
    public static final String TABLE_PLAYLIST_VIDEOS_COLUMN_VIDEO_ID = "videoId";
    public static final String TABLE_PLAYLIST_VIDEOS_COLUMN_INDEX = "index";
    // Table: mediaFiles
    public static final String TABLE_MEDIA_FILES_COLUMN_VIDEO_ID = "videoId";
    public static final String TABLE_MEDIA_FILES_COLUMN_FILE = "file";
    public static final String TABLE_MEDIA_FILES_COLUMN_FILE_TYPE = "fileType";
    public static final String TABLE_MEDIA_FILES_COLUMN_FORMAT = "format";
    public static final String TABLE_MEDIA_FILES_COLUMN_VCODEC = "vcodec";
    public static final String TABLE_MEDIA_FILES_COLUMN_ACODEC = "acodec";
    public static final String TABLE_MEDIA_FILES_COLUMN_WIDTH = "width";
    public static final String TABLE_MEDIA_FILES_COLUMN_HEIGHT = "height";
    public static final String TABLE_MEDIA_FILES_COLUMN_FPS = "fps";
    public static final String TABLE_MEDIA_FILES_COLUMN_ASR = "asr";
    // Table: extraFiles
    public static final String TABLE_EXTRA_FILES_COLUMN_VIDEO_ID = "videoId";
    public static final String TABLE_EXTRA_FILES_COLUMN_FILE = "file";
    public static final String TABLE_EXTRA_FILES_COLUMN_FILE_TYPE = "fileType";
    // Table: videoQueue
    public static final String TABLE_VIDEO_QUEUE_COLUMN_ID = "id";
    public static final String TABLE_VIDEO_QUEUE_COLUMN_VIDEO_ID = "videoId";
    public static final String TABLE_VIDEO_QUEUE_COLUMN_PRIORITY = "priority";
    public static final String TABLE_VIDEO_QUEUE_COLUMN_REQUESTED = "requested";
    public static final String TABLE_VIDEO_QUEUE_COLUMN_ARGUMENTS = "arguments";
    public static final String TABLE_VIDEO_QUEUE_COLUMN_CONFIG_FILE = "configFile";
    public static final String TABLE_VIDEO_QUEUE_COLUMN_OUTPUT_DIRECTORY = "outputDirectory";
    // // Queries
    // Table: videos
    public static final String TABLE_VIDEOS_QUERY_GET_ALL = String.format("SELECT * FROM %s;", TABLE_VIDEOS);
    public static final String TABLE_VIDEOS_QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_VIDEOS, TABLE_VIDEOS_COLUMN_ID);
    // Table: playlists
    public static final String TABLE_PLAYLISTS_QUERY_GET_ALL = String.format("SELECT * FROM %s;", TABLE_PLAYLISTS);
    public static final String TABLE_PLAYLISTS_QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_PLAYLISTS, TABLE_PLAYLISTS_COLUMN_ID);
    // Table: playlistVideos
    public static final String TABLE_PLAYLIST_VIDEOS_QUERY_GET_ALL = String.format("SELECT * FROM %s;", TABLE_PLAYLIST_VIDEOS);
    public static final String TABLE_PLAYLIST_VIDEOS_QUERY_GET_ALL_BY_VIDEO_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_PLAYLIST_VIDEOS, TABLE_PLAYLIST_VIDEOS_COLUMN_VIDEO_ID);
    public static final String TABLE_PLAYLIST_VIDEOS_QUERY_GET_ALL_BY_PLAYLIST_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_PLAYLIST_VIDEOS, TABLE_PLAYLIST_VIDEOS_COLUMN_PLAYLIST_ID);
    public static final String TABLE_PLAYLIST_VIDEOS_QUERY_GET_BY_VIDEO_ID_AND_PLAYLIST_ID = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?;", TABLE_PLAYLIST_VIDEOS, TABLE_PLAYLIST_VIDEOS_COLUMN_VIDEO_ID, TABLE_PLAYLIST_VIDEOS_COLUMN_PLAYLIST_ID);
    // Table: mediaFiles
    public static final String TABLE_MEDIA_FILES_QUERY_GET_ALL = String.format("SELECT * FROM %s;", TABLE_MEDIA_FILES);
    public static final String TABLE_MEDIA_FILES_QUERY_GET_ALL_BY_VIDEO_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_MEDIA_FILES, TABLE_MEDIA_FILES_COLUMN_VIDEO_ID);
    public static final String TABLE_MEDIA_FILES_QUERY_GET_BY_VIDEO_ID_AND_FILE = String.format("SELECT * FROM %s WHERE %s = ? AND WHERE %s = ?;", TABLE_MEDIA_FILES, TABLE_MEDIA_FILES_COLUMN_VIDEO_ID, TABLE_MEDIA_FILES_COLUMN_FILE);
    // Table: extraFiles
    public static final String TABLE_EXTRA_FILES_QUERY_GET_ALL = String.format("SELECT * FROM %s;", TABLE_EXTRA_FILES);
    public static final String TABLE_EXTRA_FILES_QUERY_GET_ALL_BY_VIDEO_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_EXTRA_FILES, TABLE_EXTRA_FILES_COLUMN_VIDEO_ID);
    public static final String TABLE_EXTRA_FILES_QUERY_GET_BY_VIDEO_ID_AND_FILE = String.format("SELECT * FROM %s WHERE %s = ? AND WHERE %s = ?;", TABLE_EXTRA_FILES, TABLE_EXTRA_FILES_COLUMN_VIDEO_ID, TABLE_EXTRA_FILES_COLUMN_FILE);
    // Table: videoQueue
    public static final String TABLE_VIDEO_QUEUE_QUERY_GET_NEXT = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 1;", TABLE_VIDEO_QUEUE, TABLE_VIDEO_QUEUE_COLUMN_PRIORITY);
    public static final String TABLE_VIDEO_QUEUE_QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_VIDEO_QUEUE, TABLE_VIDEO_QUEUE_COLUMN_ID);
    // // Updates
    // Table: videos
    public static final String TABLE_VIDEOS_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?;", TABLE_VIDEOS, TABLE_VIDEOS_COLUMN_ID, TABLE_VIDEOS_COLUMN_UPLOADER_ID, TABLE_VIDEOS_COLUMN_TITLE, TABLE_VIDEOS_COLUMN_ALT_TITLE, TABLE_VIDEOS_COLUMN_DURATION, TABLE_VIDEOS_COLUMN_UPLOAD_DATE, TABLE_VIDEOS_COLUMN_ID);
    // Table: playlists
    public static final String TABLE_PLAYLISTS_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?;", TABLE_PLAYLISTS, TABLE_PLAYLISTS_COLUMN_ID, TABLE_PLAYLISTS_COLUMN_TITLE, TABLE_PLAYLISTS_COLUMN_PLAYLIST, TABLE_PLAYLISTS_COLUMN_UPLOADER, TABLE_PLAYLISTS_COLUMN_UPLOADER_ID, TABLE_PLAYLISTS_COLUMN_ID);
    // Table: playlistVideos
    public static final String TABLE_PLAYLIST_VIDEOS_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ? AND %s = ?;", TABLE_PLAYLIST_VIDEOS, TABLE_PLAYLIST_VIDEOS_COLUMN_PLAYLIST_ID, TABLE_PLAYLIST_VIDEOS_COLUMN_VIDEO_ID, TABLE_PLAYLIST_VIDEOS_COLUMN_INDEX, TABLE_PLAYLIST_VIDEOS_COLUMN_PLAYLIST_ID, TABLE_PLAYLIST_VIDEOS_COLUMN_VIDEO_ID);
    // Table: mediaFiles
    public static final String TABLE_MEDIA_FILES_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ? AND %s = ?;", TABLE_MEDIA_FILES, TABLE_MEDIA_FILES_COLUMN_VIDEO_ID, TABLE_MEDIA_FILES_COLUMN_FILE, TABLE_MEDIA_FILES_COLUMN_FILE_TYPE, TABLE_MEDIA_FILES_COLUMN_FORMAT, TABLE_MEDIA_FILES_COLUMN_VCODEC, TABLE_MEDIA_FILES_COLUMN_ACODEC, TABLE_MEDIA_FILES_COLUMN_WIDTH, TABLE_MEDIA_FILES_COLUMN_HEIGHT, TABLE_MEDIA_FILES_COLUMN_FPS, TABLE_MEDIA_FILES_COLUMN_ASR, TABLE_MEDIA_FILES_COLUMN_VIDEO_ID, TABLE_MEDIA_FILES_COLUMN_FILE);
    // Table: extraFiles
    public static final String TABLE_EXTRA_FILES_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ? AND %s = ?;", TABLE_EXTRA_FILES, TABLE_EXTRA_FILES_COLUMN_VIDEO_ID, TABLE_EXTRA_FILES_COLUMN_FILE, TABLE_EXTRA_FILES_COLUMN_FILE_TYPE, TABLE_EXTRA_FILES_COLUMN_VIDEO_ID, TABLE_EXTRA_FILES_COLUMN_FILE);
    // Table: videoQueue
    public static final String TABLE_VIDEO_QUEUE_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?;", TABLE_VIDEO_QUEUE, TABLE_VIDEO_QUEUE_COLUMN_ID, TABLE_VIDEO_QUEUE_COLUMN_VIDEO_ID, TABLE_VIDEO_QUEUE_COLUMN_PRIORITY, TABLE_VIDEO_QUEUE_COLUMN_REQUESTED, TABLE_VIDEO_QUEUE_COLUMN_ARGUMENTS, TABLE_VIDEO_QUEUE_COLUMN_CONFIG_FILE, TABLE_VIDEO_QUEUE_COLUMN_OUTPUT_DIRECTORY, TABLE_VIDEO_QUEUE_COLUMN_ID);
    // // //
    
    private final AbstractConnector connector;
    // // // SQL Stuff
    // // Queries
    // Videos
    private transient PreparedStatement preparedStatement_getAllVideos = null;
    private transient PreparedStatement preparedStatement_getVideoById = null;
    // Playlists
    private transient PreparedStatement preparedStatement_getAllPlaylists = null;
    private transient PreparedStatement preparedStatement_getPlaylistById = null;
    // Playlists and Videos
    private transient PreparedStatement preparedStatement_getAllPlaylistVideos = null;
    private transient PreparedStatement preparedStatement_getPlaylistIdsByVideoId = null;
    private transient PreparedStatement preparedStatement_getVideoIdsByPlaylistId = null;
    private transient PreparedStatement preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId = null;
    // MediaFiles
    private transient PreparedStatement preparedStatement_getAllMediaFiles = null;
    private transient PreparedStatement preparedStatement_getMediaFilesByVideoId = null;
    private transient PreparedStatement preparedStatement_getMediaFileByVideoIdAndFile = null;
    // ExtraFiles
    private transient PreparedStatement preparedStatement_getAllExtraFiles = null;
    private transient PreparedStatement preparedStatement_getExtraFilesByVideoId = null;
    private transient PreparedStatement preparedStatement_getExtraFileByVideoIdAndFile = null;
    // Queued Videos
    private transient PreparedStatement preparedStatement_getNextQueuedVideo = null;
    private transient PreparedStatement preparedStatement_getQueuedVideoById = null;
    // // Updates
    // Videos
    private transient PreparedStatement preparedStatement_updateVideo = null;
    // Playlists
    private transient PreparedStatement preparedStatement_updatePlaylist = null;
    // Playlists and Videos
    private transient PreparedStatement preparedStatement_updatePlaylistVideo = null;
    // MediaFiles
    private transient PreparedStatement preparedStatement_updateMediaFile = null;
    // ExtraFiles
    private transient PreparedStatement preparedStatement_updateExtraFile = null;
    // Queued Videos
    private transient PreparedStatement preparedStatement_updateQueuedVideo = null;
    // // //
    
    public Database(AdvancedFile databaseDirectory) {
        this(new H2Connector(databaseDirectory));
    }
    
    public Database(AbstractConnector connector) {
        this.connector = connector;
    }
    
    public <T extends AbstractConnector> T getConnector() {
        return (T) connector;
    }
    
    public boolean isRunning() {
        return connector.isConnected();
    }
    
    public boolean start() {
        return start(null, null);
    }
    
    public boolean start(String username, byte[] password) {
        if (isRunning()) {
            return false;
        }
        if (!connector.createConnection(username, password)) {
            return false;
        }
        initStatements();
        return true;
    }
    
    private void initStatements() {
        // // Queries
        // Videos
        Standard.silentError(() -> preparedStatement_getAllVideos = connector.prepareStatement(TABLE_VIDEOS_QUERY_GET_ALL));
        Standard.silentError(() -> preparedStatement_getVideoById = connector.prepareStatement(TABLE_VIDEOS_QUERY_GET_BY_ID));
        // Playlists
        Standard.silentError(() -> preparedStatement_getAllPlaylists = connector.prepareStatement(TABLE_PLAYLISTS_QUERY_GET_ALL));
        Standard.silentError(() -> preparedStatement_getPlaylistById = connector.prepareStatement(TABLE_PLAYLISTS_QUERY_GET_BY_ID));
        // Playlists and Videos
        Standard.silentError(() -> preparedStatement_getAllPlaylistVideos = connector.prepareStatement(TABLE_PLAYLIST_VIDEOS_QUERY_GET_ALL));
        Standard.silentError(() -> preparedStatement_getPlaylistIdsByVideoId = connector.prepareStatement(TABLE_PLAYLIST_VIDEOS_QUERY_GET_ALL_BY_VIDEO_ID));
        Standard.silentError(() -> preparedStatement_getVideoIdsByPlaylistId = connector.prepareStatement(TABLE_PLAYLIST_VIDEOS_QUERY_GET_ALL_BY_PLAYLIST_ID));
        Standard.silentError(() -> preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId = connector.prepareStatement(TABLE_PLAYLIST_VIDEOS_QUERY_GET_BY_VIDEO_ID_AND_PLAYLIST_ID));
        // MediaFiles
        Standard.silentError(() -> preparedStatement_getAllMediaFiles = connector.prepareStatement(TABLE_MEDIA_FILES_QUERY_GET_ALL));
        Standard.silentError(() -> preparedStatement_getMediaFilesByVideoId = connector.prepareStatement(TABLE_MEDIA_FILES_QUERY_GET_ALL_BY_VIDEO_ID));
        Standard.silentError(() -> preparedStatement_getMediaFileByVideoIdAndFile = connector.prepareStatement(TABLE_MEDIA_FILES_QUERY_GET_BY_VIDEO_ID_AND_FILE));
        // ExtraFiles
        Standard.silentError(() -> preparedStatement_getAllExtraFiles = connector.prepareStatement(TABLE_EXTRA_FILES_QUERY_GET_ALL));
        Standard.silentError(() -> preparedStatement_getExtraFilesByVideoId = connector.prepareStatement(TABLE_EXTRA_FILES_QUERY_GET_ALL_BY_VIDEO_ID));
        Standard.silentError(() -> preparedStatement_getExtraFileByVideoIdAndFile = connector.prepareStatement(TABLE_EXTRA_FILES_QUERY_GET_BY_VIDEO_ID_AND_FILE));
        // Queued Videos
        Standard.silentError(() -> preparedStatement_getNextQueuedVideo = connector.prepareStatement(TABLE_VIDEO_QUEUE_QUERY_GET_NEXT));
        Standard.silentError(() -> preparedStatement_getQueuedVideoById = connector.prepareStatement(TABLE_VIDEO_QUEUE_QUERY_GET_BY_ID));
        // // Updates
        // Videos
        Standard.silentError(() -> preparedStatement_updateVideo = connector.prepareStatement(TABLE_VIDEOS_UPDATE));
        // Playlists
        Standard.silentError(() -> preparedStatement_updatePlaylist = connector.prepareStatement(TABLE_PLAYLISTS_UPDATE));
        // Playlists and Videos
        Standard.silentError(() -> preparedStatement_updatePlaylistVideo = connector.prepareStatement(TABLE_PLAYLIST_VIDEOS_UPDATE));
        // MediaFiles
        Standard.silentError(() -> preparedStatement_updateMediaFile = connector.prepareStatement(TABLE_MEDIA_FILES_UPDATE));
        // ExtraFiles
        Standard.silentError(() -> preparedStatement_updateExtraFile = connector.prepareStatement(TABLE_EXTRA_FILES_UPDATE));
        // Queued Videos
        Standard.silentError(() -> preparedStatement_updateQueuedVideo = connector.prepareStatement(TABLE_VIDEO_QUEUE_UPDATE));
    }
    
    public boolean stop() {
        if (!isRunning()) {
            return false;
        }
        closeStatements();
        return connector.closeConnection();
    }
    
    private void closeStatements() {
        // // Queries
        // Videos
        IOUtil.closeQuietly(preparedStatement_getAllVideos);
        IOUtil.closeQuietly(preparedStatement_getVideoById);
        // Playlists
        IOUtil.closeQuietly(preparedStatement_getAllPlaylists);
        IOUtil.closeQuietly(preparedStatement_getPlaylistById);
        // Playlists and Videos
        IOUtil.closeQuietly(preparedStatement_getAllPlaylistVideos);
        IOUtil.closeQuietly(preparedStatement_getPlaylistIdsByVideoId);
        IOUtil.closeQuietly(preparedStatement_getVideoIdsByPlaylistId);
        IOUtil.closeQuietly(preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId);
        // MediaFiles
        IOUtil.closeQuietly(preparedStatement_getAllMediaFiles);
        IOUtil.closeQuietly(preparedStatement_getMediaFilesByVideoId);
        IOUtil.closeQuietly(preparedStatement_getMediaFileByVideoIdAndFile);
        // ExtraFiles
        IOUtil.closeQuietly(preparedStatement_getAllExtraFiles);
        IOUtil.closeQuietly(preparedStatement_getExtraFilesByVideoId);
        IOUtil.closeQuietly(preparedStatement_getExtraFileByVideoIdAndFile);
        // Queued Videos
        IOUtil.closeQuietly(preparedStatement_getNextQueuedVideo);
        IOUtil.closeQuietly(preparedStatement_getQueuedVideoById);
        // // Updates
        // Videos
        IOUtil.closeQuietly(preparedStatement_updateVideo);
        // Playlists
        IOUtil.closeQuietly(preparedStatement_updatePlaylist);
        // Playlists and Videos
        IOUtil.closeQuietly(preparedStatement_updatePlaylistVideo);
        // MediaFiles
        IOUtil.closeQuietly(preparedStatement_updateMediaFile);
        // ExtraFiles
        IOUtil.closeQuietly(preparedStatement_updateExtraFile);
        // Queued Videos
        IOUtil.closeQuietly(preparedStatement_updateQueuedVideo);
    }
    
    public List<Video> getAllVideos() {
        if (!isRunning()) {
            return null;
        }
        synchronized (preparedStatement_getAllVideos) {
            try (final ResultSet resultSet = preparedStatement_getAllVideos.executeQuery()) {
                return videosFromResultSet(resultSet);
            } catch (SQLException ex) {
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    public List<Playlist> getAllPlaylists() {
        if (!isRunning()) {
            return null;
        }
        synchronized (preparedStatement_getAllPlaylists) {
            try (final ResultSet resultSet = preparedStatement_getAllPlaylists.executeQuery()) {
                return playlistsFromResultSet(resultSet);
            } catch (SQLException ex) {
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    public Video getVideoById(String videoId) {
        if (!isRunning() || videoId == null || videoId.isEmpty()) {
            return null;
        }
        Video video = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getVideoById) {
            try {
                preparedStatement_getVideoById.setString(1, videoId);
                resultSet = preparedStatement_getVideoById.executeQuery();
                if (resultSet.next()) {
                    video = videoFromResultSet(resultSet);
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return video;
    }
    
    public Playlist getPlaylistById(String playlistId) {
        if (!isRunning() || playlistId == null || playlistId.isEmpty()) {
            return null;
        }
        Playlist playlist = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getPlaylistById) {
            try {
                preparedStatement_getPlaylistById.setString(1, playlistId);
                resultSet = preparedStatement_getPlaylistById.executeQuery();
                if (resultSet.next()) {
                    playlist = playlistFromResultSet(resultSet);
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return playlist;
    }
    
    public List<MediaFile> getMediaFilesForVideo(String videoId) {
        if (!isRunning() || videoId == null || videoId.isEmpty()) {
            return null;
        }
        List<MediaFile> mediaFiles = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getMediaFilesByVideoId) {
            try {
                preparedStatement_getMediaFilesByVideoId.setString(1, videoId);
                resultSet = preparedStatement_getMediaFilesByVideoId.executeQuery();
                mediaFiles = mediaFilesFromResultSet(resultSet);
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return mediaFiles;
    }
    
    public List<ExtraFile> getExtraFilesForVideo(String videoId) {
        if (!isRunning() || videoId == null || videoId.isEmpty()) {
            return null;
        }
        List<ExtraFile> extraFiles = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getExtraFilesByVideoId) {
            try {
                preparedStatement_getExtraFilesByVideoId.setString(1, videoId);
                resultSet = preparedStatement_getExtraFilesByVideoId.executeQuery();
                extraFiles = extraFilesFromResultSet(resultSet);
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return extraFiles;
    }
    
    public List<Video> getVideosInPlaylist(String playlistId) {
        if (!isRunning() || playlistId == null || playlistId.isEmpty()) {
            return null;
        }
        List<Video> videos = new ArrayList<>();
        ResultSet resultSet = null;
        synchronized (preparedStatement_getVideoIdsByPlaylistId) {
            try {
                preparedStatement_getVideoIdsByPlaylistId.setString(1, playlistId);
                resultSet = preparedStatement_getVideoIdsByPlaylistId.executeQuery();
                while (resultSet.next()) {
                    final Video video = getVideoById(resultSet.getString(TABLE_PLAYLIST_VIDEOS_COLUMN_VIDEO_ID));
                    if (video != null) {
                        videos.add(video);
                    }
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
                videos = null;
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return videos;
    }
    
    public List<Playlist> getPlaylistsContainingVideo(String videoId) {
        if (!isRunning() || videoId == null || videoId.isEmpty()) {
            return null;
        }
        List<Playlist> playlists = new ArrayList<>();
        ResultSet resultSet = null;
        synchronized (preparedStatement_getPlaylistIdsByVideoId) {
            try {
                preparedStatement_getPlaylistIdsByVideoId.setString(1, videoId);
                resultSet = preparedStatement_getPlaylistIdsByVideoId.executeQuery();
                while (resultSet.next()) {
                    final Playlist playlist = getPlaylistById(resultSet.getString(TABLE_PLAYLIST_VIDEOS_COLUMN_PLAYLIST_ID));
                    if (playlist != null) {
                        playlists.add(playlist);
                    }
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
                playlists = null;
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return playlists;
    }
    
    public boolean isVideoInPlaylist(String videoId, String playlistId) {
        if (!isRunning() || videoId == null || videoId.isEmpty() || playlistId.isEmpty() || playlistId.isEmpty()) {
            return false;
        }
        boolean contains = false;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId) {
            try {
                preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId.setString(1, videoId);
                preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId.setString(2, playlistId);
                resultSet = preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId.executeQuery();
                contains = resultSet.next();
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return contains;
    }
    
    public int getIndexOfVideoInPlaylist(String videoId, String playlistId) {
        if (!isRunning() || videoId == null || videoId.isEmpty() || playlistId.isEmpty() || playlistId.isEmpty()) {
            return -3;
        }
        int index = -3;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId) {
            try {
                preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId.setString(1, videoId);
                preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId.setString(2, playlistId);
                resultSet = preparedStatement_getPlaylistVideoByVideoIdAndPlaylistId.executeQuery();
                if (resultSet.next()) {
                    index = resultSet.getInt(TABLE_PLAYLIST_VIDEOS_COLUMN_INDEX);
                } else {
                    index = -2;
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return index;
    }
    
    public MediaFile getMediaFileByVideoIdAndFile(String videoId, String file) {
        if (!isRunning()) {
            return null;
        }
        MediaFile mediaFile = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getMediaFileByVideoIdAndFile) {
            try {
                preparedStatement_getMediaFileByVideoIdAndFile.setString(1, videoId);
                preparedStatement_getMediaFileByVideoIdAndFile.setString(2, file);
                resultSet = preparedStatement_getMediaFileByVideoIdAndFile.executeQuery();
                if (resultSet.next()) {
                    mediaFile = mediaFileFromResultSet(resultSet);
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return mediaFile;
    }
    
    public ExtraFile getExtraFileByVideoIdAndFile(String videoId, String file) {
        if (!isRunning()) {
            return null;
        }
        ExtraFile extraFile = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getExtraFileByVideoIdAndFile) {
            try {
                preparedStatement_getExtraFileByVideoIdAndFile.setString(1, videoId);
                preparedStatement_getExtraFileByVideoIdAndFile.setString(2, file);
                resultSet = preparedStatement_getExtraFileByVideoIdAndFile.executeQuery();
                if (resultSet.next()) {
                    extraFile = extraFileFromResultSet(resultSet);
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return extraFile;
    }
    
    public QueuedVideo getNextQueuedVideo() {
        if (!isRunning()) {
            return null;
        }
        QueuedVideo queuedVideo = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getVideoById) {
            try {
                resultSet = preparedStatement_getNextQueuedVideo.executeQuery();
                if (resultSet.next()) {
                    queuedVideo = queuedVideoFromResultSet(resultSet);
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return queuedVideo;
    }
    
    public QueuedVideo getQueuedVideo(int id) {
        if (!isRunning() || id < 0) {
            return null;
        }
        QueuedVideo queuedVideo = null;
        ResultSet resultSet = null;
        synchronized (preparedStatement_getVideoById) {
            try {
                preparedStatement_getQueuedVideoById.setInt(1, id);
                resultSet = preparedStatement_getQueuedVideoById.executeQuery();
                if (resultSet.next()) {
                    queuedVideo = queuedVideoFromResultSet(resultSet);
                }
            } catch (SQLException ex) {
                Logger.handleError(ex);
            }
        }
        if (resultSet != null) {
            Standard.silentError(resultSet::close);
        }
        return queuedVideo;
    }
    
    public QueuedVideo queuedVideoFromResultSet(ResultSet resultSet) throws SQLException {
        return new QueuedVideo(resultSet.getInt(TABLE_VIDEO_QUEUE_COLUMN_ID), resultSet.getString(TABLE_VIDEO_QUEUE_COLUMN_VIDEO_ID), resultSet.getInt(TABLE_VIDEO_QUEUE_COLUMN_PRIORITY), resultSet.getTimestamp(TABLE_VIDEO_QUEUE_COLUMN_REQUESTED), resultSet.getString(TABLE_VIDEO_QUEUE_COLUMN_ARGUMENTS), resultSet.getString(TABLE_VIDEO_QUEUE_COLUMN_CONFIG_FILE), resultSet.getString(TABLE_VIDEO_QUEUE_COLUMN_OUTPUT_DIRECTORY)).setDatabase(this);
    }
    
    public List<Video> videosFromResultSet(ResultSet resultSet) throws SQLException {
        final List<Video> videos = new ArrayList<>();
        while (resultSet.next()) {
            videos.add(videoFromResultSet(resultSet));
        }
        return videos;
    }
    
    public Video videoFromResultSet(ResultSet resultSet) throws SQLException {
        return new Video(resultSet.getString(TABLE_VIDEOS_COLUMN_ID), resultSet.getString(TABLE_VIDEOS_COLUMN_UPLOADER_ID), resultSet.getString(TABLE_VIDEOS_COLUMN_TITLE), resultSet.getString(TABLE_VIDEOS_COLUMN_ALT_TITLE), resultSet.getLong(TABLE_VIDEOS_COLUMN_DURATION), resultSet.getLong(TABLE_VIDEOS_COLUMN_UPLOAD_DATE)).setDatabase(this);
    }
    
    public List<Playlist> playlistsFromResultSet(ResultSet resultSet) throws SQLException {
        final List<Playlist> playlists = new ArrayList<>();
        while (resultSet.next()) {
            playlists.add(playlistFromResultSet(resultSet));
        }
        return playlists;
    }
    
    public Playlist playlistFromResultSet(ResultSet resultSet) throws SQLException {
        return new Playlist(resultSet.getString(TABLE_PLAYLISTS_COLUMN_ID), resultSet.getString(TABLE_PLAYLISTS_COLUMN_TITLE), resultSet.getString(TABLE_PLAYLISTS_COLUMN_PLAYLIST), resultSet.getString(TABLE_PLAYLISTS_COLUMN_UPLOADER), resultSet.getString(TABLE_PLAYLISTS_COLUMN_UPLOADER_ID)).setDatabase(this);
    }
    
    public List<MediaFile> mediaFilesFromResultSet(ResultSet resultSet) throws SQLException {
        final List<MediaFile> mediaFiles = new ArrayList<>();
        while (resultSet.next()) {
            mediaFiles.add(mediaFileFromResultSet(resultSet));
        }
        return mediaFiles;
    }
    
    public MediaFile mediaFileFromResultSet(ResultSet resultSet) throws SQLException {
        return new MediaFile(resultSet.getString(TABLE_MEDIA_FILES_COLUMN_VIDEO_ID), resultSet.getString(TABLE_MEDIA_FILES_COLUMN_FILE), resultSet.getString(TABLE_MEDIA_FILES_COLUMN_FILE_TYPE), resultSet.getString(TABLE_MEDIA_FILES_COLUMN_FORMAT), resultSet.getString(TABLE_MEDIA_FILES_COLUMN_VCODEC), resultSet.getString(TABLE_MEDIA_FILES_COLUMN_ACODEC), resultSet.getInt(TABLE_MEDIA_FILES_COLUMN_WIDTH), resultSet.getInt(TABLE_MEDIA_FILES_COLUMN_HEIGHT), resultSet.getInt(TABLE_MEDIA_FILES_COLUMN_FPS), resultSet.getInt(TABLE_MEDIA_FILES_COLUMN_ASR)).setDatabase(this);
    }
    
    public List<ExtraFile> extraFilesFromResultSet(ResultSet resultSet) throws SQLException {
        final List<ExtraFile> extraFiles = new ArrayList<>();
        while (resultSet.next()) {
            extraFiles.add(extraFileFromResultSet(resultSet));
        }
        return extraFiles;
    }
    
    public ExtraFile extraFileFromResultSet(ResultSet resultSet) throws SQLException {
        return new ExtraFile(resultSet.getString(TABLE_EXTRA_FILES_COLUMN_VIDEO_ID), resultSet.getString(TABLE_EXTRA_FILES_COLUMN_FILE), resultSet.getString(TABLE_EXTRA_FILES_COLUMN_FILE_TYPE)).setDatabase(this);
    }
    
    public boolean saveQueuedVideo(QueuedVideo queuedVideo) {
        try {
            synchronized (preparedStatement_updateQueuedVideo) {
                preparedStatement_updateQueuedVideo.setInt(1, queuedVideo.getId());
                preparedStatement_updateQueuedVideo.setString(2, queuedVideo.getVideoId());
                preparedStatement_updateQueuedVideo.setInt(3, queuedVideo.getPriority());
                preparedStatement_updateQueuedVideo.setTimestamp(4, queuedVideo.getRequested());
                preparedStatement_updateQueuedVideo.setString(5, queuedVideo.getArguments());
                preparedStatement_updateQueuedVideo.setString(6, queuedVideo.getConfigFile());
                preparedStatement_updateQueuedVideo.setString(7, queuedVideo.getOutputDirectory());
                preparedStatement_updateQueuedVideo.setInt(8, queuedVideo.getId()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updateQueuedVideo.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    public boolean saveVideo(Video video) {
        try {
            synchronized (preparedStatement_updateVideo) {
                preparedStatement_updateVideo.setString(1, video.getId());
                preparedStatement_updateVideo.setString(2, video.getUploaderId());
                preparedStatement_updateVideo.setString(3, video.getTitle());
                preparedStatement_updateVideo.setString(4, video.getAltTitle());
                preparedStatement_updateVideo.setLong(5, video.getDurationAsMillis());
                preparedStatement_updateVideo.setLong(6, video.getUploadDateAsLong());
                preparedStatement_updateVideo.setString(7, video.getId()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updateVideo.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    public boolean savePlaylist(Playlist playlist) {
        try {
            synchronized (preparedStatement_updatePlaylist) {
                preparedStatement_updatePlaylist.setString(1, playlist.getId());
                preparedStatement_updatePlaylist.setString(2, playlist.getTitle());
                preparedStatement_updatePlaylist.setString(3, playlist.getPlaylist());
                preparedStatement_updatePlaylist.setString(4, playlist.getUploader());
                preparedStatement_updatePlaylist.setString(5, playlist.getUploaderId());
                preparedStatement_updatePlaylist.setString(6, playlist.getId()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updatePlaylist.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    public boolean saveMediaFile(MediaFile mediaFile) {
        try {
            synchronized (preparedStatement_updateMediaFile) {
                preparedStatement_updateMediaFile.setString(1, mediaFile.getVideoId());
                preparedStatement_updateMediaFile.setString(2, mediaFile.getFile());
                preparedStatement_updateMediaFile.setString(3, mediaFile.getFileType());
                preparedStatement_updateMediaFile.setString(4, mediaFile.getFormat());
                preparedStatement_updateMediaFile.setString(5, mediaFile.getVcodec());
                preparedStatement_updateMediaFile.setString(6, mediaFile.getAcodec());
                preparedStatement_updateMediaFile.setInt(7, mediaFile.getWidth());
                preparedStatement_updateMediaFile.setInt(8, mediaFile.getHeight());
                preparedStatement_updateMediaFile.setInt(9, mediaFile.getFps());
                preparedStatement_updateMediaFile.setInt(10, mediaFile.getAsr());
                preparedStatement_updateMediaFile.setString(11, mediaFile.getVideoId()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updateMediaFile.setString(12, mediaFile.getFile()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updateMediaFile.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    public boolean saveExtraFile(ExtraFile extraFile) {
        try {
            synchronized (preparedStatement_updateExtraFile) {
                preparedStatement_updateExtraFile.setString(1, extraFile.getVideoId());
                preparedStatement_updateExtraFile.setString(2, extraFile.getFile());
                preparedStatement_updateExtraFile.setString(3, extraFile.getFileType());
                preparedStatement_updateExtraFile.setString(4, extraFile.getVideoId()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updateExtraFile.setString(5, extraFile.getFile()); //TODO If the primary key has been changed, than this would also return the new id, and therefore the old id would be lost, so maybe preserve it somehow??
                preparedStatement_updateExtraFile.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Database{" + "connector=" + connector + '}';
    }
    
    public static void createTables(Database database) {
        //TODO Use the "database_create_tables.sql" to create (if not existing) tables...
    }
    
}
