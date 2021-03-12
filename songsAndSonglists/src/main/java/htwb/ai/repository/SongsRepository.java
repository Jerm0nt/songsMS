package htwb.ai.repository;

import htwb.ai.model.Songs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongsRepository extends CrudRepository<Songs, Integer> {
}
