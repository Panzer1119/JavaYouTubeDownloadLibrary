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

package de.codemakers.download.database.entities.impl;

import de.codemakers.download.database.YouTubeDatabase;
import de.codemakers.download.database.entities.AbstractRequester;

import java.util.List;

public class YouTubeRequester extends AbstractRequester<YouTubeRequester, YouTubeDatabase, QueuedYouTubeVideo> {
    
    public YouTubeRequester() {
        super();
    }
    
    public YouTubeRequester(int requesterId, String tag, String name) {
        super(requesterId, tag, name);
    }
    
    @Override
    public List<QueuedYouTubeVideo> getQueuedVideos() {
        return useDatabaseOrNull((database) -> database.getQueuedVideosByRequesterId(getRequesterId()));
    }
    
    @Override
    public List<String> getQueuedVideoIds() {
        return useDatabaseOrNull((database) -> database.getQueuedVideoIdsByRequesterId(getRequesterId()));
    }
    
    @Override
    public void set(YouTubeRequester youTubeRequester) {
        if (youTubeRequester == null) {
            //TODO Maybe just set every value in this object to null?
            return;
        }
        setRequesterId(youTubeRequester.getRequesterId());
        setTag(youTubeRequester.getTag());
        setName(youTubeRequester.getName());
    }
    
    @Override
    public String toString() {
        return "YouTubeRequester{" + "requesterId=" + requesterId + ", tag='" + tag + '\'' + ", name='" + name + '\'' + '}';
    }
    
}