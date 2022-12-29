package course.springdata.exercise.repositories;

import course.springdata.exercise.entites.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b FROM Book AS b WHERE b.releaseDate > 2000")
    List<Book> findBooksAfter2000();
    List<Book> findAllByReleaseDateAfter(LocalDate localDate);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitleAsc(String firstName, String lastName);

    @Query(value = "SELECT b FROM Book AS b " +
            "WHERE b.author.firstName = 'George' AND b.author.lastName = 'Powell' " +
            " ORDER BY b.releaseDate DESC, b.title ")
    List<Book> findBooksGeorgePowell();

}
