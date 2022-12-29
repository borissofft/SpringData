package com.example.cardealer.model.repository;

import com.example.cardealer.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make); // This don't work, why????

    @Query("SELECT c FROM Car AS c WHERE c.make = :make ORDER BY c.model, c.travelledDistance DESC")
    List<Car> findAllToyota(@Param("make") String make);

}
