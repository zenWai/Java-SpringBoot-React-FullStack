package WebsiteImage;

import com.presa.exception.ResourceNotFoundException;
import com.presa.s3.S3Buckets;
import com.presa.s3.S3Service;
import com.presa.websiteimages.WebsiteImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class WebsiteImageServiceTest {

    @Mock
    private S3Service s3Service;

    private final S3Buckets s3Buckets = new S3Buckets();

    private WebsiteImageService websiteImageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        websiteImageService = new WebsiteImageService(s3Service, s3Buckets);
    }

    @Test
    void shouldReturnImageDataWhenWebsiteImageExists() {
        // Given
        String imageName = "icon_customers.png";
        byte[] imageData = new byte[]{1, 2, 3};
        when(s3Service.getObject(s3Buckets.getWebsiteImages(), "website-images/" + imageName))
                .thenReturn(imageData);

        // When
        byte[] result = websiteImageService.getWebsiteImage(imageName);

        // Then
        assertThat(result).isEqualTo(imageData);
    }

    @Test
    void shouldThrowExceptionWhenWebsiteImageNotFound() {
        // Given
        String imageName = "no_image_here.png";
        when(s3Service.getObject(s3Buckets.getWebsiteImages(), "website-images/" + imageName))
                .thenReturn(null);

        // When and Then
        assertThatThrownBy(() -> websiteImageService.getWebsiteImage(imageName))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Website image with name " + imageName + " not found");
    }
}
