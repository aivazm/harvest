package com.am.harvest.service;

import com.am.harvest.model.Bid;
import com.am.harvest.model.BidState;
import com.am.harvest.model.Deal;
import com.am.harvest.model.Direction;
import com.am.harvest.repository.BidRepository;
import com.am.harvest.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealServiceImpl implements DealService {

    private final BidRepository bidRepo;
    private final DealRepository dealRepo;

    public DealServiceImpl(BidRepository bidRepo, DealRepository dealRepo) {
        this.bidRepo = bidRepo;
        this.dealRepo = dealRepo;
    }

    @Override
    public String addDeal(Bid currentBid, List<Bid> bidList) {
        Bid purposeBid;
        Deal deal;
        if (currentBid.getDirection() == Direction.PURCHASE) {
            purposeBid = getMinBid(bidList);
            if (currentBid.getPrice() >= purposeBid.getPrice()) {
                deal = createDeal(currentBid, purposeBid);
                deal.setPrice(currentBid.getPrice());
                dealRepo.save(deal);
                return "\nСделка совершена.\nТекущая заявка закрыта.";
            }
        } else {
            purposeBid = getMaxBid(bidList);
            if (currentBid.getPrice() <= purposeBid.getPrice()) {
                deal = createDeal(currentBid, purposeBid);
                deal.setPrice(purposeBid.getPrice());
                dealRepo.save(deal);
                return "\nСделка совершена.\nТекущая заявка закрыта.";
            }
        }
        return "";

    }

    private Deal createDeal(Bid currentBid, Bid purposeBid) {
        currentBid.setState(BidState.CLOSE);
        purposeBid.setState(BidState.CLOSE);
        bidRepo.save(currentBid);
        return Deal.builder()
                .product(currentBid.getProduct())
                .quantity(Integer.min(currentBid.getQuantity(), purposeBid.getQuantity()))
                .date(LocalDateTime.now())
                .build();
    }

    private Bid getMinBid(List<Bid> bidList) {
        Optional<Bid> minOptional = bidList.stream().min(Comparator.comparingInt(Bid::getPrice));
        List<Bid> minPriceList = bidList.stream().filter(e -> e.getPrice().equals(minOptional.get().getPrice())).collect(Collectors.toList());
        if (minPriceList.size() > 1) {
            return minPriceList.stream().min(Comparator.comparing(Bid::getDate)).get();
        }
        return minPriceList.get(0);
    }

    private Bid getMaxBid(List<Bid> bidList) {
        Optional<Bid> maxOptional = bidList.stream().max(Comparator.comparingInt(Bid::getPrice));
        List<Bid> maxPriceList = bidList.stream().filter(e -> e.getPrice().equals(maxOptional.get().getPrice())).collect(Collectors.toList());
        if (maxPriceList.size() > 1) {
            return maxPriceList.stream().min(Comparator.comparing(Bid::getDate)).get();
        }
        return maxPriceList.get(0);
    }
}
