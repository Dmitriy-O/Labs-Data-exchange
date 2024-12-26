package org.byovsiannikov.server.repository;

import org.byovsiannikov.server.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}