package course.springdata.exercise.services;

import course.springdata.exercise.entites.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    void seedBooks() throws IOException;

    List<Book> getAllBooksAfter2000();

    List<Book> getBooksAfter2000();

    List<Book> getAllBooksByAuthorGeorgePowell();

    List<Book> findBooksGeorgePowell();

}
