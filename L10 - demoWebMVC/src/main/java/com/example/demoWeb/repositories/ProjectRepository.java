package com.example.demoWeb.repositories;

import com.example.demoWeb.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findFirstByNameAndCompany_Name(String name, String companyName);
    boolean existsAllBy();

}
