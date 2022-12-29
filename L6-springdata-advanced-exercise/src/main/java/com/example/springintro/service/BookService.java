package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookSummary;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    // Better return all titles by String in service layer, not all books like object. The object may contain confidential information
    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllBookTitlesWithCopiesLessThan5000();

    List<String> findAllBookTitlesWithPriceLesThan5OrMoreThan40();

    List<String> findNotReleasedBookTitlesInYear(int year);

    List<String> findAllBooksBeforeDate(LocalDate localDate);

    List<String> findAllBooksTitleWhereTitleContainsStr(String containingStr);

    List<String> findAllTitlesWithAuthorLastNameStarsWith(String startsWith);

    int findCountOfBooksWithTitleLengthLongerThan(int titleLength);

    BookSummary getReducedInformationForBookByTitle(String title);

    int addCopiesToBooksAfter(LocalDate localDate, int amount);

    int deleteWithCopiesLessThan(int number);

    void changePrice(long bookId);

    int findTotalAmountOfBooksByAuthor(String firstName, String lastName);
}