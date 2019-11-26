package com.am.harvest.service;

import com.am.harvest.model.Bid;
import com.am.harvest.model.BidState;
import com.am.harvest.model.Deal;
import com.am.harvest.model.Direction;
import com.am.harvest.repository.BidRepository;
import com.am.harvest.repository.DealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(DealServiceImpl.class);

    public DealServiceImpl(BidRepository bidRepo, DealRepository dealRepo) {
        this.bidRepo = bidRepo;
        this.dealRepo = dealRepo;
    }

    @Override
    public String addDeal(Bid currentBid, List<Bid> bidList) {
        Bid purposeBid;
        Deal deal;
        switch (currentBid.getDirection()) {
            case PURCHASE:
                purposeBid = getMinBid(bidList);
                if (currentBid.getPrice().intValue() >= purposeBid.getPrice().intValue()) {
                    deal = createDeal(currentBid, purposeBid);
                    deal.setPrice(currentBid.getPrice());
                    saveDeal(deal, currentBid, purposeBid);
                    return "\nСделка совершена.\nТекущая заявка закрыта.";
                }
            case SALE:
                purposeBid = getMaxBid(bidList);
                if (currentBid.getPrice().intValue() <= purposeBid.getPrice().intValue()) {
                    deal = createDeal(currentBid, purposeBid);
                    deal.setPrice(purposeBid.getPrice());
                    saveDeal(deal, currentBid, purposeBid);
                    return "\nСделка совершена.\nТекущая заявка закрыта.";
                }
            default:
                return "";
        }
    }

    private void saveDeal(Deal deal, Bid currentBid, Bid purposeBid) {
        dealRepo.save(deal);
        logger.info("Совершена сделка {}", deal.toString());
        logger.info("Закрыта заявка {}", currentBid.toString());
        logger.info("Закрыта заявка {}", purposeBid.toString());
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
        Optional<Bid> minOptional = bidList.stream().min(Comparator.comparingInt(bid -> bid.getPrice().intValue()));
        List<Bid> minPriceList = bidList.stream().filter(e -> e.getPrice().equals(minOptional.get().getPrice())).collect(Collectors.toList());
        if (minPriceList.size() > 1) {
            return minPriceList.stream().min(Comparator.comparing(Bid::getDate)).get();
        }
        return minPriceList.get(0);
    }

    private Bid getMaxBid(List<Bid> bidList) {
        Optional<Bid> maxOptional = bidList.stream().max(Comparator.comparingInt(bid -> bid.getPrice().intValue()));
        List<Bid> maxPriceList = bidList.stream().filter(e -> e.getPrice().equals(maxOptional.get().getPrice())).collect(Collectors.toList());
        if (maxPriceList.size() > 1) {
            return maxPriceList.stream().min(Comparator.comparing(Bid::getDate)).get();
        }
        return maxPriceList.get(0);
    }
}
