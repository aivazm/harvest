package com.am.harvest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/bid", produces = APPLICATION_JSON_VALUE)
public class BidController {

    @GetMapping("new")
    public String addBid() {
        return "Добавить новую заявку";
    }
}
