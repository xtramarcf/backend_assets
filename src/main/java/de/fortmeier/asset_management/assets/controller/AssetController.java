package de.fortmeier.asset_management.assets.controller;


import de.fortmeier.asset_management.assets.controller.requests.AssetRequest;
import de.fortmeier.asset_management.assets.controller.requests.AssetResponse;
import de.fortmeier.asset_management.assets.domain.Asset;
import de.fortmeier.asset_management.assets.domain.Document;
import de.fortmeier.asset_management.assets.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;


    @GetMapping("/find-all")
    public ResponseEntity<List<AssetResponse>> findAll() {

        List<Asset> assets = assetService.findAll();

        List<AssetResponse> assetResponseList = new ArrayList<>();

        for (Asset asset : assets) {
            assetResponseList.add(AssetResponse.fromAsset(asset));
        }

        return ResponseEntity.ok(assetResponseList);
    }


    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AssetRequest assetRequest) {

        Asset asset = new Asset(assetRequest);

        assetService.saveAsset(asset);

        return ResponseEntity.ok().build();
    }


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


    @GetMapping("/load-documents")
    public ResponseEntity<List<Document>> loadDocumentsById(
            @RequestParam("id") int id) {

        List<Document> documents = assetService.getDocumentsById(id);

        return ResponseEntity.ok(documents);
    }


    @DeleteMapping("/delete-document")
    public ResponseEntity<String> deleteDocumentById(
            @RequestParam("assetId") int assetId,
            @RequestParam("docId") int docId) {

        assetService.deleteDocument(assetId, docId);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam("id") int id
    ) {
        assetService.delete(id);

        return ResponseEntity.ok().build();
    }
}
