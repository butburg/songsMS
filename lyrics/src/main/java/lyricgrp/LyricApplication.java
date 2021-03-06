package lyricgrp;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


/***
 * This class should be everytime in the root package, otherwise spring ist not finding controllers etc!!!
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class LyricApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyricApplication.class, args);
    }

    //tell Spring we are dealing with microservices and will not be using real URLs for our requests
    @LoadBalanced
    @Bean
    @Qualifier("internalServices")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    //external api consumption
    @Primary
    @Bean
    @Qualifier("externalServices")
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
