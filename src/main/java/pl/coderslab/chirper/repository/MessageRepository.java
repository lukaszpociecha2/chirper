package pl.coderslab.chirper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.chirper.entity.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByRecepient(Long id);

    @Query(nativeQuery = true, value = "select * from message where recepient_id=?1")
    List<Message> findAllByRecepientId(Long id);
}
