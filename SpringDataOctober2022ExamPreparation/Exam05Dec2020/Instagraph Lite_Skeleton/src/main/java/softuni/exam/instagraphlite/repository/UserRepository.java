package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

//    @Query("SELECT DISTINCT u FROM User AS u JOIN FETCH u.posts AS p ORDER BY size(p) DESC, u.id") // without DISTINCT the query will return each user as many times as the post he has
    @Query("SELECT DISTINCT u FROM User AS u JOIN u.posts AS p ORDER BY size(p) DESC, u.id") // if we set fetch = FetchType.EAGER for field: private Set<Post> posts, at User.class
    List<User> findAllOrderByCountDescThanById();

}
