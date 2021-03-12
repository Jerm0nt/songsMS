package htwb.ai.repository;

import htwb.ai.model.SongList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongListRepository extends CrudRepository<SongList, Integer> {
}
