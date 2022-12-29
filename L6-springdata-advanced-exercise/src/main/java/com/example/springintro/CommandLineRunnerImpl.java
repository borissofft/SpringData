package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookSummary;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

//        printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
//        printAllAuthorsAndNumberOfTheirBooks();
//        printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Please select exercise number:");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> booksTitlesByAgeRest();
            case 2 -> goldenBooks();
            case 3 -> booksByPrice();
            case 4 -> notReleasedBooks();
            case 5 -> booksReleasedBefore();
            case 6 -> authorsSearch();
            case 7 -> booksSearch();
            case 8 -> bookTitlesSearch();
            case 9 -> countBooks();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();
            case 12 -> increaseBookCopies();
            case 13 -> removeBooks();
//            case 14 -> storedProcedure(); // Not working...
//            case 99 -> myTests();
        }


    }

    private void myTests() {
        bookService.changePrice(1L);
    }

    private void storedProcedure() throws IOException {
        System.out.println("Please enter existing author first name:");
        String firstName = bufferedReader.readLine();
        System.out.println("Please enter existing author last name:");
        String lastName = bufferedReader.readLine();
        System.out.println(this.bookService.findTotalAmountOfBooksByAuthor(firstName, lastName));
    }

    private void removeBooks() throws IOException {
        System.out.println("Please enter number:");
        int amount = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Deleted books are: " + this.bookService.deleteWithCopiesLessThan(amount));
    }

    private void increaseBookCopies() throws IOException {
        System.out.println("Please enter date in format dd MMM yyyy:");
        String date = bufferedReader.readLine();
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy"));
        System.out.println("Please enter number of book copies each book should be increased with:");
        int amount = Integer.parseInt(bufferedReader.readLine());
        int booksUpdated = this.bookService.addCopiesToBooksAfter(localDate, amount);
        System.out.printf("%d books are released after %s, so total of %d book copies were added%n", booksUpdated, date, amount * booksUpdated);
    }

    private void reducedBook() throws IOException {
        System.out.println("Please enter valid book title:");
        String title = bufferedReader.readLine();
        BookSummary summary = this.bookService.getReducedInformationForBookByTitle(title);
        System.out.printf("%s %s %s %.2f%n", summary.getTitle(), summary.getEditionType(), summary.getAgeRestriction(), summary.getPrice());
    }

    private void totalBookCopies() throws IOException {
        // Variant 1
//        this.authorService.findAllAuthorsAndTheirTotalCopies()
//                .forEach(System.out::println);

        // Variant 2
//        this.authorService.findAllAuthorsAndTheirTotalCopiesVariant2()
//                .forEach(System.out::println);

        // Variant 3
        this.authorService.findAllAuthorsAndTheirTotalCopiesVariant3()
                .forEach(e-> System.out.printf("%s %s - %s\n",e[0],e[1],e[2]));
    }

    private void countBooks() throws IOException {
        System.out.println("Please enter title length:");
        int titleLength = Integer.parseInt(bufferedReader.readLine());
        System.out.println(this.bookService.findCountOfBooksWithTitleLengthLongerThan(titleLength));
    }

    private void bookTitlesSearch() throws IOException {
        System.out.println("Please enter author last name starts with str");
        String startsWith = bufferedReader.readLine();
        this.bookService.findAllTitlesWithAuthorLastNameStarsWith(startsWith)
                .forEach(System.out::println);
    }

    private void booksSearch() throws IOException {
        System.out.println("Please check results manually in DB, because we insert random Authors for every books");
        System.out.println("Please enter containing string:");
        String containingStr = bufferedReader.readLine();
        this.bookService.findAllBooksTitleWhereTitleContainsStr(containingStr)
                .forEach(System.out::println);
    }

    private void authorsSearch() throws IOException {
        System.out.println("Please enter first name ens with str:");
        String endStr = bufferedReader.readLine();
        this.authorService.findAuthorsFirstNameEndsWithStr(endStr)
                .forEach(System.out::println);
    }

    private void booksReleasedBefore() throws IOException {
        System.out.println("Please enter date in format dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.bookService.findAllBooksBeforeDate(localDate)
                .forEach(System.out::println);
    }

    private void notReleasedBooks() throws IOException {
        System.out.println("Please enter year:");
        int year = Integer.parseInt(bufferedReader.readLine());
        this.bookService.findNotReleasedBookTitlesInYear(year)
                .forEach(System.out::println);
    }

    private void booksByPrice() {
        this.bookService.findAllBookTitlesWithPriceLesThan5OrMoreThan40()
                .forEach(System.out::println);
    }

    private void goldenBooks() {
        this.bookService.findAllBookTitlesWithCopiesLessThan5000()
                .forEach(System.out::println);
    }

    private void booksTitlesByAgeRest() throws IOException {
        System.out.println("Please enter Age Restriction:");
        try {
            AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());
            this.bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
                    .forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("!".repeat(170) + "\n");
            System.out.println("Please rerun and enter valid AgeRestriction." + "\n");
            System.out.println("!".repeat(170) + "\n");
        }
    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }



}
