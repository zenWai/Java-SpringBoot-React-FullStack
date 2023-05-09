package com.presa.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.presa.customer.Gender;
import com.presa.exception.ResourceNotFoundException;
import com.presa.jwt.JWTUtil;
import com.presa.s3.S3Buckets;
import com.presa.s3.S3Service;
import com.presa.customer.CustomerDTO;
import com.presa.customer.CustomerRegistrationRequest;
import com.presa.customer.CustomerUpdateRequest;
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
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebsiteImagesIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;
    private static final Random RANDOM = new Random();
    @Mock
    private S3Client s3Client;
    private static final String CUSTOMER_PATH = "/api/v1/customers";

    @Autowired
    private S3Service s3Service;
    private final S3Buckets s3Buckets = new S3Buckets();

    private static final String WebsiteImages_PATH = "/api/v1/website-images/";
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

        // create a test customer
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@example.com";
        int age = RANDOM.nextInt(1, 100);

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );


        // get a JWT token for the test customer
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // Upload the image to the S3 bucket
        byte[] imageBytes = Files.toByteArray(image.getFile());
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(imageName).build(),
                RequestBody.fromBytes(imageBytes));

        // When
        byte[] downloadedImage = webTestClient.get()
                .uri(WebsiteImages_PATH + imageName)
                .accept(MediaType.IMAGE_PNG)
                .header("Authorization", "Bearer " + jwtToken)
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
