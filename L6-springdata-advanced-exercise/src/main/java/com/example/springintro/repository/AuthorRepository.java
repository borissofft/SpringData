package com.example.springintro.repository;

import com.example.springintro.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY size(a.books) DESC")
    List<Author> findAllByBooksSizeDESC();

    List<Author> findAllByFirstNameEndingWith(String pattern);

    @Query(value = "SELECT a.firstName, a.lastName, SUM(b.copies) FROM Author AS a JOIN a.books AS b GROUP BY a ORDER BY SUM(b.copies) DESC")
    List<String> findTotalNumberOfCopiesByAuthorDescending();

    /**
     * It is so easy when you find that the query return String[], not just a String with words concatenated with coma!!!
     */
    @Query(value = "SELECT a.firstName, a.lastName, SUM(b.copies) FROM Author AS a JOIN a.books AS b GROUP BY a ORDER BY SUM(b.copies) DESC")
    List<String[]> findTotalNumberOfCopiesByAuthorDescendingVariant2();

}
