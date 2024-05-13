package de.fortmeier.asset_management.assets;


import de.fortmeier.asset_management.assets.requests.AssetRequest;
import de.fortmeier.asset_management.assets.type.ItemType;
import de.fortmeier.asset_management.assets.type.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Asset database Entity
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;
    private ItemType itemType;
    private String name;
    private String description;
    private String owner;
    private PaymentType paymentType;
    private double price;
    private boolean lendable;
    private LocalDate borrowedAt;
    private LocalDate returnAt;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Document> documents = new ArrayList<>();

    public Asset(AssetRequest assetRequest) {

        this.id = assetRequest.id();
        this.itemType = ItemType.valueOf(assetRequest.itemType());
        this.name = assetRequest.name();
        this.description = assetRequest.description();
        this.owner = assetRequest.owner();
        this.paymentType = PaymentType.valueOf(assetRequest.paymentType());
        this.price = assetRequest.price();
        this.lendable = assetRequest.lendable();
        this.borrowedAt = assetRequest.borrowedAt() != null ? LocalDate.of(
                assetRequest.borrowedAt().year(),
                assetRequest.borrowedAt().month(),
                assetRequest.borrowedAt().day()) : null;
        this.returnAt = assetRequest.returnAt() != null ? LocalDate.of(
                assetRequest.returnAt().year(),
                assetRequest.returnAt().month(),
                assetRequest.returnAt().day()) : null;
    }
}
