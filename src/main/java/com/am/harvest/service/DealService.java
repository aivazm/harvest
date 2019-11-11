package com.am.harvest.service;

import com.am.harvest.model.Bid;

import java.util.List;

public interface DealService {
    String addDeal(Bid currentBid, List<Bid> bidList);
}
