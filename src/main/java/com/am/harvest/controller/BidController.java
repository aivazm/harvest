package com.am.harvest.controller;

import com.am.harvest.dto.BidDto;
import com.am.harvest.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/bid")
public class BidController {

    private final BidService service;

    public BidController(BidService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<String> addBid(@RequestBody BidDto bidDto) {
        return new ResponseEntity<>(service.addBid(bidDto), HttpStatus.CREATED);

    }

}
