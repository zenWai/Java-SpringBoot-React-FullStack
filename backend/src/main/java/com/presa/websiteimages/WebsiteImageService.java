package com.presa.websiteimages;

import com.presa.s3.S3Buckets;
import com.presa.s3.S3Service;
import org.springframework.stereotype.Service;

@Service
public class WebsiteImageService {
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    public WebsiteImageService(S3Service s3Service, S3Buckets s3Buckets) {
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
    }

    public byte[] getWebsiteImage(String key) {
        return s3Service.getObject(s3Buckets.getWebsiteImages(), "website-images/" + key);
    }
}
