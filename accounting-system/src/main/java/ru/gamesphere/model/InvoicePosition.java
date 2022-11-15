package ru.gamesphere.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoicePosition {
    private int id;
    private int productId;
    private int invoiceId;
    private int price;
    private int quantity;
}
