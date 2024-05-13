package de.fortmeier.asset_management.assets.requests;


import de.fortmeier.asset_management.assets.Asset;
import de.fortmeier.asset_management.assets.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Record for sending Asset to the frontend.
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
public record AssetResponse(
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

    /**
     * Method for converting an asset to an assetResponse.
     * @param asset
     * @return assetResponse
     */
    public static AssetResponse fromAsset(Asset asset) {

        List<DocumentDto> documentDtoList = new ArrayList<>();
        if (!asset.getDocuments().isEmpty()) {
            for (Document doc : asset.getDocuments()) {
                documentDtoList.add(
                        new DocumentDto(doc)
                );
            }
        }

        NgbDate borrowedAtNgb = asset.getBorrowedAt() != null ?
                new NgbDate(
                        asset.getBorrowedAt().getYear(),
                        asset.getBorrowedAt().getMonthValue(),
                        asset.getBorrowedAt().getDayOfMonth()) :
                null;

        NgbDate returnAtNgb = asset.getReturnAt() != null ?
                new NgbDate(
                        asset.getReturnAt().getYear(),
                        asset.getReturnAt().getMonthValue(),
                        asset.getReturnAt().getDayOfMonth()) :
                null;

        return new AssetResponse(asset.getId(),
                asset.getItemType().toString(),
                asset.getName(),
                asset.getDescription(),
                asset.getOwner(),
                asset.getPaymentType().toString(),
                asset.getPrice(),
                asset.isLendable(),
                borrowedAtNgb,
                returnAtNgb,
                documentDtoList
        );
    }
}
