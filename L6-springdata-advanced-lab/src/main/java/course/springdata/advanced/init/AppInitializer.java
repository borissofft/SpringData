package course.springdata.advanced.init;

import course.springdata.advanced.dao.IngredientRepository;
import course.springdata.advanced.dao.LabelRepository;
import course.springdata.advanced.dao.ShampooRepository;
import course.springdata.advanced.entity.Ingredient;
import course.springdata.advanced.entity.Label;
import course.springdata.advanced.entity.Shampoo;
import course.springdata.advanced.entity.Size;
import course.springdata.advanced.util.PrintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class AppInitializer implements CommandLineRunner {

    private final ShampooRepository shampooRepository;
    private final LabelRepository labelRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public AppInitializer(ShampooRepository shampooRepository, LabelRepository labelRepository, IngredientRepository ingredientRepository) {
        this.shampooRepository = shampooRepository;
        this.labelRepository = labelRepository;
        this.ingredientRepository = ingredientRepository;
    }

//    @Transactional
    @Override
    public void run(String... args) throws Exception {

        // Task 1 - Select Shampoos by Size
//        this.shampooRepository.findAllBySizeOrderById(Size.MEDIUM)
//                .forEach(PrintUtil::printShampoo);

        // Task 2 - Select Shampoos by Size or Label
//        Label label = this.labelRepository.findLabelById(10L);
//        this.shampooRepository.findAllBySizeOrLabelOrderByPrice(Size.MEDIUM, label)
//                .forEach(PrintUtil::printShampoo);

        // Task 3 - Select Shampoos by Price
//        this.shampooRepository.findAllByPriceGreaterThanOrderByPriceDesc(new BigDecimal(5))
//                .forEach(PrintUtil::printShampoo);

//        // Task 4 - Select Ingredients by Name
//       this.ingredientRepository.findAllByNameStartingWith("M")
//               .forEach(ingredient -> System.out.printf("%s%n", ingredient.getName()));

        // Task 5 - Select Ingredients by Names
//        this.ingredientRepository.findAllByNameInOrderByPrice(Arrays.asList("Lavender", "Herbs", "Apple"))
//                .forEach(ingredient -> System.out.printf("%s%n", ingredient.getName()));

        // Task 6 - Count Shampoos by Price
//        System.out.println(this.shampooRepository.countShampooByPriceLessThan(new BigDecimal("8.50")));

        // Task 7 - Select Shampoos by Ingredients
//        Ingredient berry = this.ingredientRepository.findIngredientByName("Berry");
//        Ingredient mineralCollagen = this.ingredientRepository.findIngredientByName("Mineral-Collagen");
//        this.shampooRepository.selectShampoosByIngredients(Set.of(berry, mineralCollagen))
//                .forEach(shampoo -> System.out.printf("%s%n", shampoo.getBrand()));

        // Task 8 - Select Shampoos by Ingredients Count
//        this.shampooRepository.selectShampoosByIngredientsCount(2)
//                .forEach(shampoo -> System.out.printf("%s%n", shampoo.getBrand()));

        // Task 9 - Delete Ingredients by Name
        /**
         * VERY IMPORTANT: We have to remove ingredient from shampoos then we can delete ingredient !!!!!!!
         */

//        String nameToDelete = "Nettle";
//        Ingredient ingredientToDelete = this.ingredientRepository.findIngredientByName(nameToDelete);
//        List<Shampoo> shampoos = this.shampooRepository.selectShampoosByIngredients(Set.of(ingredientToDelete));
//        System.out.printf("Number of shampoos that contains ingredientToDelete is: %d%n", shampoos.size());
//        shampoos.forEach(shampoo -> shampoo.getIngredients().remove(ingredientToDelete));
//        int deleted = this.ingredientRepository.deleteByName(nameToDelete);
//        System.out.printf("Deleted ingredients count is: %d%n", deleted);

        // Task 10 - Update Ingredients by Price
//        BigDecimal percentage = new BigDecimal("1.10");
//        this.ingredientRepository.updatePriceOfAllIngredients(percentage);


        // Task 11 - Update Ingredients by Names
//        this.ingredientRepository.updatePriceOfIngredientsInList(new BigDecimal("1.10"), Arrays.asList("Apple", "Lavender"));
    }

}
