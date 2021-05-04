package com.se.se_file_server;

import com.se.se_file_server.file.infra.config.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileUploadProperties.class
})
public class SeFileServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeFileServerApplication.class, args);
	}

}
