package de.fortmeier.asset_management.assets;


import de.fortmeier.asset_management.assets.type.ItemType;
import de.fortmeier.asset_management.assets.type.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service which contains the business logic for assets.
 */

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;


    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    public Optional<Asset> findById(Integer id) {
        return assetRepository.findById(id);
    }


    @Transactional
    public void saveAsset(Asset asset) {
        assetRepository.save(asset);
    }


    @Transactional
    public void delete(int id) {
        assetRepository.deleteById(id);
    }

    /**
     * Deletes a document by assetId and docId.
     * @param assetId id of the asset.
     * @param docId id of the document.
     */

    @Transactional
    public void deleteDocument(int assetId, int docId) {
        assetRepository.findById(assetId).ifPresent(asset -> {
            List<Document> documents = asset.getDocuments();
            documents.removeIf(document -> document.getId() == docId);
            asset.setDocuments(documents);
            assetRepository.save(asset);
        });
    }


    /**
     * Safes a document to an asset.
     * @param id of the asset.
     * @param file Multipart pdf-file.
     * @throws IOException for accessing the MultipartFile.
     * @throws IllegalStateException if there is no asset with the given id.
     */
    @Transactional
    public void safeDocument(int id, MultipartFile file) throws IOException, IllegalStateException {

        Document document = getDocumentFromMultipartFile(file);
        document.setAsset(
                findById(id).orElseThrow()
        );
        Asset asset = assetRepository.findById(id).orElseThrow(IllegalStateException::new);
        List<Document> documentList = new ArrayList<>();
        if (asset.getDocuments() != null) documentList.addAll(asset.getDocuments());
        documentList.add(document);
        asset.setDocuments(documentList);

        assetRepository.save(asset);
    }


    public List<Document> getDocumentsById(int id) {
        return assetRepository.findDocumentsByAssetId(id);
    }

    /**
     * Creates a Document-Object out of the MultipartFile.
     * @param file received MultipartFile.
     * @return the built Document-Object.
     * @throws IOException if there is an error by accessing the file- data.
     */
    public Document getDocumentFromMultipartFile(MultipartFile file) throws IOException {
        return Document.builder()
                .docName(file.getOriginalFilename())
                .content(file.getBytes())
                .build();
    }


    /**
     * Method for creating and saving three example assets.
     */
    public void saveExampleAsset() {

        if (!findAll().isEmpty()) return;

        List<Asset> assets = new ArrayList<>();

        Asset asset1 = Asset.builder()
                .itemType(ItemType.DEVICE)
                .name("Laptop")
                .description("High performance laptop")
                .owner("John Doe")
                .paymentType(PaymentType.PURCHASED)
                .price(1500.00)
                .lendable(true)
                .borrowedAt(LocalDate.of(2024, 4, 15))
                .returnAt(LocalDate.of(2024, 4, 25))
                .build();
        assets.add(asset1);

        Asset asset2 = Asset.builder()
                .itemType(ItemType.FURNISHINGS)
                .name("Desk")
                .description("Wooden desk with drawers")
                .owner("Jane Smith")
                .paymentType(PaymentType.PURCHASED)
                .price(300.00)
                .lendable(true)
                .borrowedAt(LocalDate.of(2024, 4, 20))
                .returnAt(LocalDate.of(2025, 4, 30))
                .build();
        assets.add(asset2);

        Asset asset3 = Asset.builder()
                .itemType(ItemType.DEVICE)
                .name("Drill")
                .description("Cordless drill with accessories")
                .owner("Jack Johnson")
                .paymentType(PaymentType.PURCHASED)
                .lendable(false)
                .price(120.00)
                .build();
        assets.add(asset3);

        assetRepository.saveAll(assets);
    }


}
