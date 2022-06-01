package org.doxa;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class DoxaContractApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoxaContractApplication.class, args);
	}
	
    @Bean
	public ModelMapper modelMapper() {
    	ModelMapper modelMapper = new ModelMapper();
    	modelMapper.getConfiguration().setAmbiguityIgnored(true);
		return modelMapper;
	}

}
