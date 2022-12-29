package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Mechanic;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    boolean existsByEmail(String email);

    boolean existsByFirstName(String firstName);
    Mechanic findByFirstName(String firstName);

}
