package pl.coderslab.chirper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.chirper.entity.Comment;

import javax.transaction.Transactional;
import java.util.Collection;


@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "DELETE FROM comment WHERE id=?1", nativeQuery = true)
    @Modifying
    public void myDeleteById(Long id);

    Collection<Comment> findAllByUserId(Long id);
}
