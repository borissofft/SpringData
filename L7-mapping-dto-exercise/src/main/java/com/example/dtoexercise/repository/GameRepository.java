package com.example.dtoexercise.repository;

import com.example.dtoexercise.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findFirstByTitle(String title);

}
