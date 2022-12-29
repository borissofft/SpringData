package com.example.jsonex.repository;

import com.example.jsonex.model.dto.CategoryProductSummaryDto;
import com.example.jsonex.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal lower, BigDecimal upper);

    @Query("SELECT new com.example.jsonex.model.dto.CategoryProductSummaryDto" +
            "(c.name, COUNT(p.id), AVG(p.price), SUM(p.price))" +
            "FROM Product AS p " +
            "JOIN p.categories AS c " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(p.id)")
    List<CategoryProductSummaryDto> getSummaryCategoriesByProductsCount();
}
