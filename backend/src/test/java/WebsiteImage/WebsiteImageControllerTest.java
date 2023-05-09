package WebsiteImage;

import com.presa.exception.ResourceNotFoundException;
import com.presa.websiteimages.WebsiteImageController;
import com.presa.websiteimages.WebsiteImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class WebsiteImageControllerTest {

    @Mock
    private WebsiteImageService websiteImageService;

    private WebsiteImageController websiteImageController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        websiteImageController = new WebsiteImageController(websiteImageService);
    }

    @Test
    void shouldReturnWebsiteImageWhenFound() {
        // Given
        String imageName = "icon_customers.png";
        byte[] imageData = new byte[]{1, 2, 3};
        when(websiteImageService.getWebsiteImage(imageName)).thenReturn(imageData);

        // When
        ResponseEntity<byte[]> responseEntity = websiteImageController.getWebsiteImage(imageName);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(imageData);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
    }

    @Test
    void shouldReturn404WhenWebsiteImageNotFound() {
        // Given
        String imageName = "no_image_here.png";
        when(websiteImageService.getWebsiteImage(imageName))
                .thenThrow(new ResourceNotFoundException("Website image with name " + imageName + " not found"));

        // When
        ResponseEntity<byte[]> responseEntity = websiteImageController.getWebsiteImage(imageName);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }
}
