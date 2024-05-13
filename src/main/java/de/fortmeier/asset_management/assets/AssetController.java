package de.fortmeier.asset_management.assets;


import de.fortmeier.asset_management.assets.requests.AssetRequest;
import de.fortmeier.asset_management.assets.requests.AssetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Controller for CRUD operations on Assets
 */
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    /**
     * Finds all assets.
     * @return list with all Assets
     */
    @GetMapping("/find-all")
    public ResponseEntity<List<AssetResponse>> findAll() {

        List<Asset> assets = assetService.findAll();

        List<AssetResponse> assetResponseList = new ArrayList<>();

        for (Asset asset : assets) {
            assetResponseList.add(AssetResponse.fromAsset(asset));
        }

        return ResponseEntity.ok(assetResponseList);
    }


    /**
     * Save a new asset or an edited asset.
     * @param assetRequest with information to create or edit an asset.
     * @return an empty ResponseEntity.
     */
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AssetRequest assetRequest) {

        Asset asset = new Asset(assetRequest);

        assetService.saveAsset(asset);

        return ResponseEntity.ok().build();
    }


    /**
     * Uploads a document for an asset.
     * @param file uploaded file. Should be pdf-type.
     * @param id of the asset, where a file will be added.
     * @return ResponseEntity, which includes information, if an error occurs.
     */
    @PostMapping("/upload-document")
    public ResponseEntity<String> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestParam("id") int id
    ) {
        try {
            if (!Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest().body("Only PDF-files are allowed");
            }

            assetService.safeDocument(id, file);
        } catch (NullPointerException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    /**
     * Loads a single document by its assetId
     * @param id of the asset.
     * @return ResponseEntity with the found document.
     */
    @GetMapping("/load-documents")
    public ResponseEntity<List<Document>> loadDocumentsById(
            @RequestParam("id") int id) {

        List<Document> documents = assetService.getDocumentsById(id);

        return ResponseEntity.ok(documents);
    }


    /**
     * Deletes a single document by its assetId and its id.
     * @param assetId of the selected asset.
     * @param docId of the document, which will be deleted
     * @return empty ResponseEntity.
     */
    @DeleteMapping("/delete-document")
    public ResponseEntity<String> deleteDocumentById(
            @RequestParam("assetId") int assetId,
            @RequestParam("docId") int docId) {

        assetService.deleteDocument(assetId, docId);

        return ResponseEntity.ok().build();
    }


    /**
     * Deletes an asset.
     * @param id of the asset, which will be deleted.
     * @return empty Response Entity.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam("id") int id
    ) {
        assetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
