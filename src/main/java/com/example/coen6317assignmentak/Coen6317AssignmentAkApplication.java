package com.example.coen6317assignmentak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class Coen6317AssignmentAkApplication {

    public static void main(String[] args) {

        SpringApplication.run(Coen6317AssignmentAkApplication.class, args);
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("audio-api").apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.example.coen6317assignmentak.controller"))
                .paths(PathSelectors.any()).build();

    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Audio API Details")
                .description("Spring rest API").version("1.0").build();
    }

}

