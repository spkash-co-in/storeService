package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {
    public static final String PROCESSED_SUCCESSFULLY = "Processed Successfully";
    public static final String INCORRECT_VERSION = "Incorrect Version : ";
    public static final String EXPIRED_MATURITY = "Expired Maturity : ";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
    private String tradeId;
    private Long version;
    private String counterPartyId;
    private String bookId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date maturityDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;
    private boolean expired;
    private String status;
    public boolean isValid() {
        return tradeId!=null && !tradeId.isEmpty() && !tradeId.isBlank()
                && counterPartyId!=null && !counterPartyId.isBlank() && !counterPartyId.isEmpty()
                && bookId!=null && !bookId.isBlank() && !bookId.isEmpty()
                && maturityDate!=null && version!=null && !(version<0L);
    }
}


