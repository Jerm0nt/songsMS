package htwb.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
@EnableEurekaClient
public class SongsListApp {

  public static void main(String[] args){
    SpringApplication.run(SongsListApp.class, args);
  }
}
