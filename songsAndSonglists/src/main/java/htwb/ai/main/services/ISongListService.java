package htwb.ai.main.services;

import htwb.ai.main.model.SongList;
import htwb.ai.main.model.Songs;
import javassist.NotFoundException;

import java.util.Set;

public interface ISongListService {
    int postSongList(SongList songList, String token) throws NotFoundException;

    SongList getSongList(int id) throws NotFoundException;

    void deleteSongList(Integer id) throws NotFoundException;

    Set<SongList> getSongListSetByUser(String userId, boolean privateAudience) throws NotFoundException;

    void updateSongList(SongList songList) throws NotFoundException;

    void deleteSongFromSongLists(Songs songs);
}
