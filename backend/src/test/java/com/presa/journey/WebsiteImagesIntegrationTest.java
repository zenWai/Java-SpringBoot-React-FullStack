package com.presa.journey;

import com.presa.exception.ResourceNotFoundException;
import com.presa.s3.S3Buckets;
import com.presa.s3.S3Service;
import com.presa.websiteimages.WebsiteImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.google.common.io.Files;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebsiteImagesIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private S3Client s3Client;

    @Autowired
    private S3Service s3Service;
    private final S3Buckets s3Buckets = new S3Buckets();

    private static final String CUSTOMER_PATH = "/api/v1/website-images/";
    private static final String bucketName = "my-random-bucket";

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3Client);
    }

    @Test
    void canDownloadWebsiteImage() throws IOException {
        // Given
        String imageName = "icon_customers.png";

        Resource image = new ClassPathResource(imageName);

        // Upload the image to the S3 bucket
        byte[] imageBytes = Files.toByteArray(image.getFile());
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(imageName).build(),
                RequestBody.fromBytes(imageBytes));
        // When
        byte[] downloadedImage = webTestClient.get()
                .uri(CUSTOMER_PATH + imageName)
                .accept(MediaType.IMAGE_PNG)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.IMAGE_PNG)
                .expectBody(byte[].class)
                .returnResult()
                .getResponseBody();

        // Then
        byte[] actual = Files.toByteArray(image.getFile());
        assertThat(actual).isEqualTo(downloadedImage);
    }

    @Test
    void shouldThrowExceptionWhenWebsiteImageNotFound() {
        WebsiteImageService websiteImageService = new WebsiteImageService(s3Service, s3Buckets);
        String imageName = "no_image_here.png";
        assertThatThrownBy(() -> websiteImageService.getWebsiteImage(imageName))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Website image with name " + imageName + " not found");
    }
}
