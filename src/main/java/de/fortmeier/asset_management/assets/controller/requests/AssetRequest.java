package de.fortmeier.asset_management.assets.controller.requests;


import java.util.List;

public record AssetRequest(
        Integer id,
        String itemType,
        String name,
        String description,
        String owner,
        String paymentType,
        double price,
        boolean lendable,
        NgbDate borrowedAt,
        NgbDate returnAt,
        List<DocumentDto> docs
) {


}
