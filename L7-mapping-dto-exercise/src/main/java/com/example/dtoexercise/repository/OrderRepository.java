package com.example.dtoexercise.repository;

import com.example.dtoexercise.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByBuyer_FullName(String buyer_fullName);

}
