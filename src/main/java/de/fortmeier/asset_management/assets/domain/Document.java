package de.fortmeier.asset_management.assets.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Document {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;
    private String docName;
    private byte[] content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

}
