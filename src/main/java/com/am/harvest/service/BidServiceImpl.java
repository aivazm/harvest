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
import java.util.Set;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository repository;
    private final Validator validator;

    public BidServiceImpl(BidRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String addBid(BidDto bidDto) {
        if (bidDto == null)
            return "Пустое тело";

        Bid bid = getValidBid(bidDto);
        repository.save(bid);
        return "Заявка добавлена.";
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
                .State(BidState.ACTIVE)
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
}