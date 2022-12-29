package com.example.springintro.service;

import com.example.springintro.model.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> getAllAuthorsOrderByCountOfTheirBooks();

    List<String> findAuthorsFirstNameEndsWithStr(String endStr);

    List<String> findAllAuthorsAndTheirTotalCopies();

    List<String> findAllAuthorsAndTheirTotalCopiesVariant2();

    List<String[]> findAllAuthorsAndTheirTotalCopiesVariant3();
}
