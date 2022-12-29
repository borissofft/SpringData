package course.springdata.intro.dao;

import course.springdata.intro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // not required
public interface UserRepository extends JpaRepository<User, Long> {


}
