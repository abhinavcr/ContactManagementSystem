package com.sts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyOrderRepository extends JpaRepository<Myorder, Long> {

}
