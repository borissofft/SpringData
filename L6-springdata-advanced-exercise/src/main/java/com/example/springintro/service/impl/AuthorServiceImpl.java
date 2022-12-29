package com.example.springintro.service.impl;

import com.example.springintro.model.entity.Author;
import com.example.springintro.model.entity.Book;
import com.example.springintro.repository.AuthorRepository;
import com.example.springintro.service.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHORS_FILE_PATH = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (authorRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(AUTHORS_FILE_PATH))
                .forEach(row -> {
                    String[] fullName = row.split("\\s+");
                    Author author = new Author(fullName[0], fullName[1]);

                    authorRepository.save(author);
                });
    }

    @Override
    public Author getRandomAuthor() {
        long randomId = ThreadLocalRandom
                .current().nextLong(1,
                        authorRepository.count() + 1);

        return authorRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public List<String> getAllAuthorsOrderByCountOfTheirBooks() {
        return authorRepository
                .findAllByBooksSizeDESC()
                .stream()
                .map(author -> String.format("%s %s %d",
                        author.getFirstName(),
                        author.getLastName(),
                        author.getBooks().size()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAuthorsFirstNameEndsWithStr(String endStr) {
        return this.authorRepository.findAllByFirstNameEndingWith(endStr)
                .stream()
                .map(author -> String.format("%s %s", author.getFirstName(), author.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllAuthorsAndTheirTotalCopies() {
        return this.authorRepository.findAll()
                .stream()
                .sorted((f, s) -> {
                    int first = f.getBooks().stream().mapToInt(Book::getCopies).sum();
                    int second = s.getBooks().stream().mapToInt(Book::getCopies).sum();
                    return Integer.compare(second, first);
                })
                .map(author -> {
                    int copiesCount = author.getBooks().stream().mapToInt(Book::getCopies).sum();
//                    int copiesCount = author.getBooks().stream().map(Book::getCopies).reduce(Integer::sum).orElse(0);
                    return String.format("%s %s - %d", author.getFirstName(), author.getLastName(), copiesCount);
                }).collect(Collectors.toList());
    }

    @Override
    public List<String> findAllAuthorsAndTheirTotalCopiesVariant2() {
        List<String> result = new ArrayList<>();
        this.authorRepository.findTotalNumberOfCopiesByAuthorDescending()
                .forEach(str -> {
                    String[] params = str.split(",");
                    result.add(String.format("%s %s - %d", params[0], params[1], Integer.parseInt(params[2])));
                });

        return result;

    }

    @Override
    public List<String[]> findAllAuthorsAndTheirTotalCopiesVariant3() {
        return this.authorRepository.findTotalNumberOfCopiesByAuthorDescendingVariant2();
    }

}
