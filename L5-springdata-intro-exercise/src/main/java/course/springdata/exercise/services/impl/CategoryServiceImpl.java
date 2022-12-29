package course.springdata.exercise.services.impl;

import course.springdata.exercise.constants.GlobalConstants;
import course.springdata.exercise.entites.Category;
import course.springdata.exercise.repositories.CategoryRepository;
import course.springdata.exercise.services.CategoryService;
import course.springdata.exercise.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategories() throws IOException {

        if (this.categoryRepository.count() != 0) { // best way is to check every category one by one if exist
            return;
        }

        String[] fileContent = this.fileUtil
                .readFileContent(GlobalConstants.CATEGORIES_FILE_PATH);

        Arrays.stream(fileContent).forEach(row -> {
            Category category = new Category(row);
            this.categoryRepository.saveAndFlush(category);
        });
    }

    @Override
    public Category getCategoryById(Long id) {
        return this.categoryRepository.getReferenceById(id);
    }

}
