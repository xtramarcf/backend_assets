package de.fortmeier.asset_management.assets.requests;


import java.util.List;

/**
 * Record for editing or adding assets
 * @param id
 * @param itemType
 * @param name
 * @param description
 * @param owner
 * @param paymentType
 * @param price
 * @param lendable
 * @param borrowedAt
 * @param returnAt
 * @param docs
 */

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
