package course.springdata.advanced.dao;

import course.springdata.advanced.entity.Ingredient;
import course.springdata.advanced.entity.Label;
import course.springdata.advanced.entity.Shampoo;
import course.springdata.advanced.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {

    List<Shampoo> findAllBySizeOrderById(Size size);

    List<Shampoo> findAllBySizeOrLabelOrderByPrice(Size size, Label label);

    List<Shampoo> findAllByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

//    @Query(value = "SELECT s FROM Shampoo AS s, IN(s.ingredients) AS i WHERE i IN :ingredients")
    @Query(value = "SELECT s FROM Shampoo AS s JOIN s.ingredients AS i WHERE i IN :ingredients")
    List<Shampoo> selectShampoosByIngredients(@Param("ingredients") Set<Ingredient> ingredients);

    int countShampooByPriceLessThan(BigDecimal price);

    @Query(value = "SELECT s FROM Shampoo AS s JOIN s.ingredients AS i WHERE i IN :name")
    List<Shampoo> selectShampoosByNameOfIngredients(@Param("name") Set<String> nameIngredients);

    @Query(value = "SELECT s FROM Shampoo AS s WHERE s.ingredients.size < :number")
    List<Shampoo> selectShampoosByIngredientsCount(@Param("number") int maxCount);

}
