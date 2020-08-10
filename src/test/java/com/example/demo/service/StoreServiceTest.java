package com.example.demo.service;

import com.example.demo.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class StoreServiceTest {
    @Autowired private StoreService storeService;
    @Test
    public void testExpiredTradeMaturityDate() throws ParseException {
        Trade trade = new Trade("T1",1L,"CP-1","B1",new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-20"),null,true,null);
        storeService.updateTrade(trade);
        assertTrue(trade.isExpired());
        assertTrue(trade.getStatus().startsWith(Trade.EXPIRED_MATURITY));
    }
    @Test
    public void testValidTradeMaturityDate() throws ParseException {
        Trade trade = new Trade("T1",1L,"CP-1","B1",new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-20"),null,true,null);
        storeService.updateTrade(trade);
        assertFalse(trade.isExpired());
        assertTrue(trade.getStatus().startsWith(Trade.PROCESSED_SUCCESSFULLY));
    }
    @Test
    public void testTradeVersion() throws ParseException {
        Trade trade = new Trade("T1",1L,"CP-1","B1",new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-20"),null,true,null);
        storeService.updateTrade(trade);
        assertTrue(trade.getStatus().startsWith(Trade.PROCESSED_SUCCESSFULLY));
        trade.setVersion(0L);
        storeService.updateTrade(trade);
        assertTrue(trade.getStatus().startsWith(Trade.INCORRECT_VERSION));
    }
}
