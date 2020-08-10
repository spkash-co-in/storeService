package com.example.demo.service;

import com.example.demo.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class StoreService {
    private final Map<String, Long> tradeVersion = new TreeMap<String,Long>();
    private final TreeMap<String, Trade> tradeMap = new TreeMap<String, Trade>(Collections.reverseOrder());
    public Trade updateTrade(Trade trade) {
        processTrade(trade);
        return trade;
    }
    private void processTrade(Trade trade) {
        if (null==trade.getCreatedDate()) {
            trade.setCreatedDate(new Date());
        }
        trade.setExpired(trade.getMaturityDate().before(trade.getCreatedDate()));
        log.info("Trade {}", trade);
        if (trade.isExpired()) {
            trade.setStatus(Trade.EXPIRED_MATURITY.concat(Trade.DATE_FORMAT.format(trade.getMaturityDate())));
        } else {
            String tradeVerTuple = trade.getTradeId().concat("-").concat(trade.getVersion().toString());
            synchronized (tradeVersion) {
                if (null == tradeVersion.get(trade.getTradeId())) {
                    log.info("Trade {} Version {} not found", trade.getTradeId(), trade.getVersion());
                    tradeVersion.put(trade.getTradeId(),trade.getVersion());
                    tradeMap.put(tradeVerTuple, trade);
                    trade.setStatus(Trade.PROCESSED_SUCCESSFULLY.concat(" Current Version : ").concat(tradeVersion.get(trade.getTradeId()).toString()));
                } else {
                    if (trade.getVersion() >= tradeVersion.get(trade.getTradeId())) {
                        log.info("Updating - Trade {} Current Version {} Transaction Version {}", trade.getTradeId(), tradeVersion.get(trade.getTradeId()), trade.getVersion());
                        tradeVersion.put(trade.getTradeId(),trade.getVersion());
                        tradeMap.put(tradeVerTuple, trade);
                        trade.setStatus(Trade.PROCESSED_SUCCESSFULLY.concat(" Current Version : ").concat(tradeVersion.get(trade.getTradeId()).toString()));
                    } else {
                        log.info("Not updating - Trade {} Current Version {} Transaction Version {}", trade.getTradeId(), tradeVersion.get(trade.getTradeId()), trade.getVersion());
                        trade.setStatus(Trade.INCORRECT_VERSION.concat(" Current Version : ").concat(tradeVersion.get(trade.getTradeId()).toString()));
                    }
                }
            }
        }
    }
    public List<Trade> listTrades() {
        return new ArrayList<Trade>(tradeMap.values());
    }
    public void retireTrades() {
        tradeMap.entrySet().stream().filter(trade -> trade.getValue().getMaturityDate().before(new Date())).forEach(trade -> {
            log.info("Retiring Trade {} ", trade);
            trade.getValue().setExpired(true);
        });
    }
}
