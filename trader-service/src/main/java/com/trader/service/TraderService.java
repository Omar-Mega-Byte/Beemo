package com.trader.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trader.model.Trader;
import com.trader.repository.TraderRepository;

@Service
public class TraderService {
	@Autowired
	private TraderRepository traderRepository;

	public Trader registerTrader(Trader trader) {
		return traderRepository.save(trader);
	}

	public Trader findTraderById(Long id) {
		return traderRepository.findById(id).orElse(null);
	}

	public void deleteTrader(Long id) {
		traderRepository.deleteById(id);
	}

}
