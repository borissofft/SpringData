package com.example.jsonex.repository;

import com.example.jsonex.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value = "SELECT u FROM User AS u JOIN u.soldProducts AS p WHERE p.buyer IS NOT NULL ORDER BY u.lastName, u.firstName")
//    @Query(value = "SELECT u FROM User AS u JOIN Product AS p WHERE COUNT(p.id) > 0 ORDER BY u.lastName, u.firstName")

    @Query("SELECT u FROM User AS u WHERE (SELECT COUNT(p) FROM Product AS p WHERE p.seller.id = u.id AND p.buyer IS NOT NULL) > 0 " +
            "ORDER BY u.lastName, u.firstName")
    List<User> findAllUsersWithMoreThanOneSoldProductOrderByLastNameThenFirstName();

    @Query("SELECT u FROM User AS u JOIN u.soldProducts AS p WHERE p.size > 0 ORDER BY p.size DESC, u.lastName")
    List<User> findAllUsersWithMoreThanOneSoldProductOrderByCountOfProductsThenLastName();

}
