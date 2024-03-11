package co.com.oro.microservice.resolveEnigmaApi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "co.com.oro.microservice.resolveEnigmaApi", "co.com.oro.microservice.resolveEnigmaApi.api" , "co.com.oro.microservice.resolveEnigmaApi.config"})
public class EnigmaResolveApp{


    public static void main(String[] args) throws Exception {
        new SpringApplication(EnigmaResolveApp.class).run(args);
    }


   
}
