package course.springdata.exercise.controllers;

import course.springdata.exercise.entites.Author;
import course.springdata.exercise.entites.Book;
import course.springdata.exercise.services.AuthorService;
import course.springdata.exercise.services.BookService;
import course.springdata.exercise.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


@Controller
public class AppController implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final Scanner scanner;

    @Autowired
    public AppController(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {

        // Seed data
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        // Exercises

        System.out.println("Enter number from 1 to 4 to check queries:");
        int taskNumber = Integer.parseInt(scanner.nextLine());

        switch (taskNumber) {
            case 1:

                // Variant 1
                List<Book> allBooksAfter2000 = this.bookService.getAllBooksAfter2000();
                allBooksAfter2000.forEach(book -> System.out.println(book.getTitle()));

                // Variant 2
//        List<Book> booksAfter2000 = this.bookService.getBooksAfter2000();
//        booksAfter2000.forEach(book -> System.out.println(book.getTitle()));

                break;

            case 2:

//                List<String> withBookReleasedBefore1990 = this.authorService.findAllAuthorsWithBookReleasedBefore1990();
//
//                withBookReleasedBefore1990.forEach(pair -> {
//                    String[] names = pair.split(",");
//                    System.out.printf("%s %s%n", names[0], names[1]);
//                });

                // Variant 2
                LocalDate year1990 = LocalDate.of(1990, 1, 1);
                List<Author> authors = this.authorService.findDistinctByBooksReleaseDateBefore(year1990);

                authors.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));

                break;

            case 3:

                // Variant 1
                this.authorService.findAllAuthorsByCountOfBooks()
                        .forEach(author -> System.out.printf(" %s %s %d%n",
                                author.getFirstName(), author.getLastName(), author.getBooks().size()));

                // Variant 2
//        List<Author> authors = this.authorService.findAllAuthors();
//        authors.sort((f, s) -> Integer.compare(s.getBooks().size(), f.getBooks().size()));
//        authors.forEach(author -> System.out.printf(" %s %s %d%n",
//                author.getFirstName(), author.getLastName(), author.getBooks().size()));
                break;

            case 4:

                // Variant 1
                List<Book> allBooksByAuthorGeorgePowell = this.bookService.getAllBooksByAuthorGeorgePowell();
                allBooksByAuthorGeorgePowell.forEach(book ->
                        System.out.printf("%s %s %d%n", book.getTitle(), book.getReleaseDate(), book.getCopies()));

                // Variant 2
//        List<Book> books = this.bookService.findBooksGeorgePowell();
//        books.forEach(book -> System.out.printf("%s %s %d%n", book.getTitle(), book.getReleaseDate(), book.getCopies()));
                break;

        }
    }

}
