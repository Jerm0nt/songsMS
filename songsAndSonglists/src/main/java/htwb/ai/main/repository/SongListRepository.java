package htwb.ai.main.repository;

import htwb.ai.main.model.SongList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongListRepository extends CrudRepository<SongList, Integer> {
}
