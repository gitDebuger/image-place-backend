package com.imageplc.imageplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ImagePlaceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ImagePlaceApplication.class, args);
	}
}
