package course.springdata.exercise.repositories;

import course.springdata.exercise.entites.Author;
import course.springdata.exercise.entites.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = "SELECT a FROM Author AS a ORDER BY a.books.size DESC")
    List<Author> getAllAuthors();

    @Override
    List<Author> findAll();

    @Query(value = "SELECT DISTINCT a.first_name, a.last_name FROM authors AS a\n" +
            "JOIN books AS b ON a.author_id = b.author_id\n" +
            "WHERE YEAR(b.release_date) < '1990'",nativeQuery = true)
    List<String> getAllAuthorsWithBookReleasedBefore1990();

    List<Author> findDistinctByBooksReleaseDateBefore(LocalDate releaseDate);

}
