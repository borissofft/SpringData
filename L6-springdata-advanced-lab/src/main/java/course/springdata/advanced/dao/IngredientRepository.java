package course.springdata.advanced.dao;

import course.springdata.advanced.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Ingredient findIngredientByName(String name);

    List<Ingredient> findAllByNameStartingWith(String pattern);

    List<Ingredient> findAllByNameInOrderByPrice(List<String> name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Ingredient AS i WHERE i.name = :name")
    int deleteByName(@Param("name") String name);
    @Transactional
    @Modifying
    @Query(value = "UPDATE Ingredient AS i SET i.price = i.price * :percent WHERE i.name IN :names")
    int updatePriceOfIngredientsInList(@Param("percent") BigDecimal percent, @Param("names") Iterable<String> names);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Ingredient AS i SET i.price = i.price * :percent")
    int updatePriceOfAllIngredients(@Param("percent") BigDecimal percent);


}
