package course.springdata.intro.dao;

import course.springdata.intro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // not required
public interface AccountRepository extends JpaRepository<Account, Long> {


}
