package com.example.xmlex.repository;

import com.example.xmlex.model.dto.CategoryByNameDto;
import com.example.xmlex.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper);

    @Query("SELECT new com.example.xmlex.model.dto.CategoryByNameDto" +
            "(c.name, COUNT(p.id), AVG(p.price), SUM(p.price))" +
            "FROM Product AS p " +
            "JOIN p.categories AS c " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(p.id)")
    List<CategoryByNameDto> getSummaryCategoriesByProductsCount();

}
