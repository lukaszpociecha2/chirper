package pl.coderslab.chirper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.chirper.entity.Tweet;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
