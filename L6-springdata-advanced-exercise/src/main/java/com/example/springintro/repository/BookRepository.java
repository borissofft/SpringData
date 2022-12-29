package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookSummary;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate lower, LocalDate upper);

    List<Book> findAllByTitleContainsIgnoreCase(String pattern);

    List<Book> findAllByAuthor_LastNameStartsWith(String pattern);

    @Query(value = "SELECT COUNT(b) FROM Book AS b WHERE length(b.title) > :number")
    int countOfBooksWithTitleLengthMoreThan(@Param(value = "number") int titleLength);

    @Query("SELECT b.title AS title, b.editionType AS editionType, b.ageRestriction AS ageRestriction, b.price AS price " +
            " FROM Book AS b WHERE b.title = :title")
    BookSummary findSummaryForTitle(@Param("title") String title);

    @Modifying
    @Transactional
    @Query("UPDATE Book AS b SET b.copies = b.copies + :amount WHERE b.releaseDate > :localDate")
    int addCopiesToBooksAfter(@Param("localDate") LocalDate localDate, @Param("amount") int amount);

    @Modifying
    @Transactional
    int deleteByCopiesLessThan(int amount);

//    @Procedure("books_written_by_author")
    @Procedure(name = "Book.countAuthorBookTitles")
    int getTotalCountOfBooksWrittenByAuthor(@Param("first_name") String first_name, @Param("last_name") String last_name);

    @Procedure("change_book_price_by_id")
    void changePriceById(Long book_id);

}
