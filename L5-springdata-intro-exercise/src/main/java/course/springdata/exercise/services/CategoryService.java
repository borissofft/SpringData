package course.springdata.exercise.services;

import course.springdata.exercise.entites.Category;

import java.io.IOException;

public interface CategoryService {

    void seedCategories() throws IOException;

    Category getCategoryById(Long id);

}
