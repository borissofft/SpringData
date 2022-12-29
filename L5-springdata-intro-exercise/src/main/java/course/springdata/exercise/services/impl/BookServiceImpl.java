package course.springdata.exercise.services.impl;

import course.springdata.exercise.constants.GlobalConstants;
import course.springdata.exercise.entites.*;
import course.springdata.exercise.repositories.BookRepository;
import course.springdata.exercise.services.AuthorService;
import course.springdata.exercise.services.BookService;
import course.springdata.exercise.services.CategoryService;
import course.springdata.exercise.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final FileUtil fileUtil;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, FileUtil fileUtil, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.fileUtil = fileUtil;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {

        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] fileContent = this.fileUtil
                .readFileContent(GlobalConstants.BOOKS_FILE_PATH);

        Arrays.stream(fileContent).forEach(row -> {

            String[] params = row.split("\\s+");
            Author author = this.getRandomAuthor();
            EditionType editionType = EditionType.values()[Integer.parseInt(params[0])];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate releaseDate = LocalDate.parse(params[1], formatter);
            int copies = Integer.parseInt(params[2]);
            BigDecimal price = new BigDecimal(params[3]);
            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(params[4])];
            String title = getTitle(params);

            Set<Category> categories = this.getRandomCategories();

            Book book = new Book();
            book.setAuthor(author);
            book.setEditionType(editionType);
            book.setReleaseDate(releaseDate);
            book.setCopies(copies);
            book.setPrice(price);
            book.setAgeRestriction(ageRestriction);
            book.setTitle(title);
            book.setCategories(categories);

            this.bookRepository.save(book);
        });

    }

    @Override
    public List<Book> getAllBooksAfter2000() {
        LocalDate releaseDate = LocalDate.of(2000, 12, 31); // All books after the End ot the 2000 Year
        return this.bookRepository
                .findAllByReleaseDateAfter(releaseDate);
    }

    @Override
    public List<Book> getBooksAfter2000() {
        return this.bookRepository.findBooksAfter2000();
    }

    @Override
    public List<Book> getAllBooksByAuthorGeorgePowell() {
        return this.bookRepository.findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitleAsc("George", "Powell");
    }

    @Override
    public List<Book> findBooksGeorgePowell() {
        return this.bookRepository.findBooksGeorgePowell();
    }


    private Set<Category> getRandomCategories() {

        Random random = new Random();
        int bound = random.nextInt(3) + 1; // random number from 1 to 3. Get only 3 categories from 8

        Set<Category> result = new HashSet<>();
        for (int i = 1; i <= bound; i++) {
            int categoryId = random.nextInt(8) + 1; // we have 8 categories(hard codded). Get random category every cycle
            result.add(this.categoryService.getCategoryById((long) categoryId));
        }

        return result;
    }

    private String getTitle(String[] params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 5; i < params.length; i++) {
            sb.append(params[i]).append(" ");
        }
        return sb.toString().trim();
    }

    private Author getRandomAuthor() {
        Random random = new Random();
        long randomId = random.nextInt(authorService.getAllAuthorsCount()) + 1; // In SQL indexes start from 1
        return this.authorService.findAuthorById(randomId);
    }

}
