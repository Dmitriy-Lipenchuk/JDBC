package ru.gamesphere.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class Invoice {
    private int id;
    private Timestamp date;
    private int organisationId;
    private List<InvoicePosition> invoicePositions;
}
