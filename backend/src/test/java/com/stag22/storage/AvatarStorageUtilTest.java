package com.stag22.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.util.HexFormat;

import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;

class AvatarStorageUtilTest {

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
    }

    @AfterEach
    void tearDown() throws IOException {
    	AvatarStorageUtil.drop_avatars();
    }

    @Test
    void testGetCustomerAvatar() throws Exception {
    	// Create an empty image (byte array)
        byte[] testImage = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");

        // Generate an empty image, store it, and retrieve it using AvatarStorageUtil
        try {

            // Upload the empty image
            AvatarStorageUtil.uploadCustomerImage(1, new MockMultipartFile("empty-image.png","empty-image.png","image/png", testImage));

            // Retrieve the empty image
            byte[] retrievedImage = AvatarStorageUtil.getCustomerImage(1);

            // Assert that the retrieved image matches the empty image
            assertArrayEquals(testImage, retrievedImage);
        } finally {
            //No need to clean up, as the temporary directory is automatically deleted in @AfterEach
        }
    }


}
