package com.doan.backend.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final String INVOICE_PREFIX = "BILL-";
    public static final int SHIPPING_FEE = 35000;
}
