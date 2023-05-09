package com.presa.websiteimages;

import com.presa.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class WebsiteImageController {

    private final WebsiteImageService websiteImageService;

    public WebsiteImageController(WebsiteImageService websiteImageService) {
        this.websiteImageService = websiteImageService;
    }

    @GetMapping("/website-images/{key}")
    public ResponseEntity<byte[]> getWebsiteImage(@PathVariable String key) {
        try {
            byte[] imageData = websiteImageService.getWebsiteImage(key);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            HttpStatus status = imageData == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
            return new ResponseEntity<>(imageData, headers, status);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }
}
