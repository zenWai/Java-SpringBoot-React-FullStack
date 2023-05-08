package com.presa.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3Buckets {

    private String customer;
    private String websiteImages;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getWebsiteImages() {
        return websiteImages;
    }

    public void setWebsiteImages(String websiteImages) {
        this.websiteImages = websiteImages;
    }

}
