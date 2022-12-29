package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByMakeAndModelAndKilometers(String make, String model, Integer kilometers);

    @Query("SELECT c FROM Car AS c ORDER BY size(c.pictures) DESC, c.make")
    List<Car> findAllCarsOrderByPicturesCountDescThanByMake();

}
