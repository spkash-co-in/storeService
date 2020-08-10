package com.example.demo.web.rest;

import com.example.demo.model.Trade;
import com.example.demo.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/store")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class StoreController {
    private StoreService storeService;
    @GetMapping("/getTrades")
    public List<Trade> getTrades() {
        return storeService.listTrades();
    }
    @PostMapping("/updateTrade")
    public Trade updateTrade(@RequestBody Trade trade) {
        if (trade.isValid()) {
            return storeService.updateTrade(trade);
        } else {
            String errorMsg = "TradeId, counterpartyId, bookId cannot be null or empty. Maturity date should follow yyyy-MM-dd. Version cannot be less than 0";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
