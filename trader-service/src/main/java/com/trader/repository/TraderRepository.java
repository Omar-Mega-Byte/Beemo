package com.trader.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trader.model.Trader;

public interface TraderRepository extends JpaRepository<Trader, Long> {

}
