package htwb.ai.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GroupApp {

  public static void main(String[] args){
    SpringApplication.run(GroupApp.class, args);
  }
}
