package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByEmail(String email);

    List<Player> findAllByBirthDateBetweenOrderByStat_ShootingDescStat_PassingDescStat_EnduranceDescLastName(LocalDate start, LocalDate end);

}
