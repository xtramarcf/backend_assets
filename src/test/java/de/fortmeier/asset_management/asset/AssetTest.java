package de.fortmeier.asset_management.asset;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fortmeier.asset_management.assets.requests.AssetRequest;
import de.fortmeier.asset_management.assets.requests.AssetResponse;

import de.fortmeier.asset_management.assets.Asset;
import de.fortmeier.asset_management.assets.type.ItemType;
import de.fortmeier.asset_management.assets.type.PaymentType;
import de.fortmeier.asset_management.assets.AssetService;
import de.fortmeier.asset_management.iam.requests.AuthRequest;
import de.fortmeier.asset_management.iam.requests.AuthResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests all asset classes.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
class AssetTest {
    @Autowired
    AssetService assetService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Value("${key.admin-password}")
    String adminPassword;

    private final String description = "laptop";
    private final String owner = "johnDoe";
    private final LocalDate returnAt = LocalDate.now().plusDays(100);

    /**
     * Tests saving, finding, and deleting assets.
     */
    @Test
    @Order(1)
    void assetSaveFindDeleteTest() {

        Asset asset = new Asset();
        asset.setDescription(description);
        asset.setOwner(owner);
        asset.setReturnAt(returnAt);
        asset.setItemType(ItemType.DEVICE);

        assetService.saveAsset(asset);
        Assertions.assertEquals(4, assetService.findAll().size());

        Asset foundAsset = assetService.findAll().get(0);

        assetService.delete(foundAsset.getId());

        Assertions.assertEquals(3, assetService.findAll().size());
    }


    /**
     * Tests saving document function.
     */
    @Test
    @Order(2)
    void testSaveDocument() {

        Asset asset = new Asset();
        asset.setDescription(description);
        asset.setOwner(owner);
        asset.setReturnAt(returnAt);
        asset.setItemType(ItemType.DEVICE);

        assetService.saveAsset(asset);


        byte[] pdfContent = "Test-Bytearray".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", pdfContent);

        try {
            assetService.safeDocument(asset.getId(), mockFile);
        } catch (IOException e) {
            Assertions.fail(e);
        }

        Asset foundAsset = assetService.findAll().get(3);

        byte[] content = foundAsset.getDocuments().get(0).getContent();

        Assertions.assertArrayEquals(pdfContent, content);
    }


    /**
     * Tests find all assets request.
     */
    @Test
    @Order(3)
    void saveFindDeleteRequestTest() {
        String token = null;

        try {
            token = authenticate();
        } catch (Exception e) {
            Assertions.fail(e);
        }

        saveAsset(token);

        try {

            ResultActions resultActions = this.mockMvc.perform(get("/asset/find-all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
            ).andExpect(
                    status().isOk()
            );

            MvcResult result = resultActions.andReturn();
            String contentAsString = result.getResponse().getContentAsString();
            List<AssetResponse> assetList = objectMapper.readValue(contentAsString, new TypeReference<List<AssetResponse>>() {
            });

            Assertions.assertEquals(description, assetList.get(3).description());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }


    /**
     * Tests upload document request.
     */
    @Test
    @Order(4)
    void safeUploadDocRequestTest() {
        String token = null;

        try {
            token = authenticate();
        } catch (Exception e) {
            Assertions.fail(e);
        }

        saveAsset(token);

        Integer id = assetService.findAll().get(0).getId();

        try {

            byte[] pdfContent = "Test-Bytearray".getBytes();
            MockMultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", pdfContent);

            this.mockMvc.perform(multipart("/asset/upload-document")
                    .file(mockFile)
                    .param("id", objectMapper.writeValueAsString(id))
                    .header("Authorization", token)
            ).andExpect(
                    status().isOk()
            );

            Asset asset = assetService.findAll().get(0);

            Assertions.assertArrayEquals(pdfContent, asset.getDocuments().get(0).getContent());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }


    /**
     * Tests delete asset request.
     */
    @Test
    @Order(5)
    void deleteRequestTest() {
        String token = null;

        try {
            token = authenticate();
        } catch (Exception e) {
            Assertions.fail(e);
        }

        saveAsset(token);

        Integer id = assetService.findAll().get(0).getId();

        try {
            this.mockMvc.perform(delete("/asset/delete")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", objectMapper.writeValueAsString(id))
                    .header("Authorization", token)
            ).andExpect(
                    status().isOk()
            );
        } catch (Exception e) {
            Assertions.fail(e);
        }

        Assertions.assertEquals(3, assetService.findAll().size());

    }


    /**
     * Saves asset by request.
     * @param token jwt token for authorization.
     */
    void saveAsset(String token) {

        AssetRequest asset = new AssetRequest(
                null,
                ItemType.DEVICE.toString(),
                "test",
                description,
                owner,
                PaymentType.PURCHASED.toString(),
                100,
                false,
                null,
                null,
                null
        );


        try {
            this.mockMvc.perform(post("/asset/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(asset))
                    .header("Authorization", token)
            ).andExpect(
                    status().isOk()
            );
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    /**
     * Authenticates the admin.
     *
     * @return authentication information.
     * @throws Exception if error occurs.
     */
    String authenticate() throws Exception {
        AuthRequest request = new AuthRequest("Admin", adminPassword);

        ResultActions resultActions = this.mockMvc.perform(post("/iam/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(
                        status().isOk()
                );

        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(contentAsString, AuthResponse.class);

        return "Bearer " + authResponse.accessToken();
    }
}
