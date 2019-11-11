package com.am.harvest.service;

import com.am.harvest.model.Bid;
import com.am.harvest.model.BidState;
import com.am.harvest.model.Deal;
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
// среди заявок на продажу в том же товаре и статусе «Активная»,
// выбирается заявка с минимальной ценой Pmin.
// Если несколько заявок с Pmin,
// выбирается с самым ранним временем регистрации заявки
//2.	если цена заявки на покупку >= Pmin, то:
//a.	заявки переводятся в статус «Исполнена» (частичное исполнение приравнивается к полному)
//b.	создается объект «Сделка» с полями:
//i.	товар из исполненных заявок
//ii.	количество = min (количество из исполненных заявок)
//iii.	цена = цена из исполненной заявки на покупку
//iv.	время регистрации = время создания объекта «Сделка»

        Bid minBid = getMinBid(bidList);
        if (currentBid.getPrice() >= minBid.getPrice()){
            currentBid.setState(BidState.CLOSE);
            bidRepo.save(currentBid);
            Deal deal = Deal.builder()
                    .product(currentBid.getProduct())
                    .quantity(Math.abs(currentBid.getQuantity()-minBid.getQuantity()))
                    .price(currentBid.getPrice())
                    .date(LocalDateTime.now())
                    .build();
            dealRepo.save(deal);
            return "\nТекущая заявка закрыта";
        }
        return "";
    }

    private Bid getMinBid(List<Bid> bidList) {
        Optional<Bid> minOptional = bidList.stream().min(Comparator.comparingInt(Bid::getPrice));
        List<Bid> minPriceList = bidList.stream().filter(e -> e.getPrice().equals(minOptional.get().getPrice())).collect(Collectors.toList());
        if (minPriceList.size() > 1) {
            return minPriceList.stream().max(Comparator.comparing(Bid::getDate)).get();
        }
        return minPriceList.get(0);
    }
}
