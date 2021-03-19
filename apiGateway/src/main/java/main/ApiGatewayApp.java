package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApp {
  public static void main(String[] args){SpringApplication.run(ApiGatewayApp.class, args); }

  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
      .route(p -> p
        .path("/auth")
        .uri("localhost:9002/auth"))
      .route(p -> p
        .path("/songs")
        .uri("localhost:9001/songs"))
      .build();
  }
}


