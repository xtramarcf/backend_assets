package de.fortmeier.asset_management.assets.repository;

import de.fortmeier.asset_management.assets.domain.Asset;
import de.fortmeier.asset_management.assets.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {


    @Query("select a.documents from Asset a where a.id = :id")
    List<Document> findDocumentsByAssetId(int id);

}
