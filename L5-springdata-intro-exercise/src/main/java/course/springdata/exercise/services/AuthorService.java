package course.springdata.exercise.services;

import course.springdata.exercise.entites.Author;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AuthorService {

    void seedAuthors() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById(Long id);

    List<Author> findAllAuthorsByCountOfBooks();

    List<String> findAllAuthorsWithBookReleasedBefore1990();
    List<Author> findDistinctByBooksReleaseDateBefore(LocalDate releaseDate);

    List<Author> findAllAuthors();

}
