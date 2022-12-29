package course.springdata.exercise.services.impl;

import course.springdata.exercise.constants.GlobalConstants;
import course.springdata.exercise.entites.Author;
import course.springdata.exercise.repositories.AuthorRepository;
import course.springdata.exercise.services.AuthorService;
import course.springdata.exercise.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedAuthors() throws IOException {

        if (this.authorRepository.count() != 0) {
            return;
        }

        String[] fileContent = this.fileUtil
                .readFileContent(GlobalConstants.AUTHORS_FILE_PATH);

        Arrays.stream(fileContent).forEach(row -> {
            String[] params = row.split("\\s+");
            Author author = new Author(params[0], params[1]);
            this.authorRepository.save(author);
        });

    }

    @Override
    public int getAllAuthorsCount() {
        return (int) this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(Long id) {
        return this.authorRepository.getReferenceById(id);
    }

    @Override
    public List<Author> findAllAuthorsByCountOfBooks() {
        return this.authorRepository.getAllAuthors();
    }

    @Override
    public List<String> findAllAuthorsWithBookReleasedBefore1990() {
        return this.authorRepository.getAllAuthorsWithBookReleasedBefore1990();
    }

    @Override
    public List<Author> findDistinctByBooksReleaseDateBefore(LocalDate releaseDate) {
        return this.authorRepository.findDistinctByBooksReleaseDateBefore(releaseDate);
    }

    @Override
    public List<Author> findAllAuthors() {
        return this.authorRepository.findAll();
    }

}
