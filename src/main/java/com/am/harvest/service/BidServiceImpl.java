package com.am.harvest.service;

import com.am.harvest.dto.BidDto;
import com.am.harvest.model.Bid;
import com.am.harvest.model.BidState;
import com.am.harvest.model.Direction;
import com.am.harvest.repository.BidRepository;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final Validator validator;
    private final DealService dealService;

    public BidServiceImpl(BidRepository bidRepository, Validator validator, DealService dealService) {
        this.bidRepository = bidRepository;
        this.validator = validator;
        this.dealService = dealService;
    }

    @Override
    public String addBid(BidDto bidDto) {
        if (bidDto == null)
            return "Пустое тело";

        Bid bid = getValidBid(bidDto);
        bidRepository.save(bid);
        StringBuilder sb = new StringBuilder();
        sb.append("Заявка добавлена.");
        List<Bid> bidList = checkBid(bid);
        if (!bidList.isEmpty()) {
            sb.append(dealService.addDeal(bid, bidList));
        }
        return sb.toString();
    }

    private Bid getValidBid(BidDto bidDto) {
        Set<ConstraintViolation<BidDto>> validate = validator.validate(bidDto);
        if (!validate.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<BidDto> violation : validate) {
                message.append(violation.getMessage());
                message.append("; ");
            }
            throw new RuntimeException(message.toString().trim());
        }
        if (bidDto.getQuantity() * bidDto.getPrice() > 1_000_000) {
            throw new RuntimeException("Итоговая сумма по заявке не должна превышать 1 000 000");
        }
        Bid bid = Bid.builder()
                .product(bidDto.getProduct())
                .quantity(bidDto.getQuantity())
                .price(bidDto.getPrice())
                .state(BidState.ACTIVE)
                .date(LocalDateTime.now())
                .build();
        switch (bidDto.getDirection()) {
            case "Покупка":
                bid.setDirection(Direction.PURCHASE);
                break;
            case "Продажа":
                bid.setDirection(Direction.SALE);
                break;
            default:
                throw new RuntimeException("Направление должно быть либо Покупка, либо Продажа");
        }
        return bid;
    }

    private List<Bid> checkBid(Bid bid) {
        if (bid.getDirection() == Direction.PURCHASE) {
            return bidRepository.findAllByStateAndProductAndDirection(BidState.ACTIVE, bid.getProduct(), Direction.SALE);
        }
        return bidRepository.findAllByStateAndProductAndDirection(BidState.ACTIVE, bid.getProduct(), Direction.PURCHASE);

    }
}
