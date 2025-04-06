package edu.neu.csye6225;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;


import java.util.TimeZone;

@SpringBootApplication
public class WebAppApplication {

    private static final Logger logger = LoggerFactory.getLogger(WebAppApplication.class);

    public static void main(String[] args) {

            //  Dotenv dotenv = Dotenv.configure().load();
            //  // Setting environment variables
            //  dotenv.entries().forEach(entry ->
            //          System.setProperty(entry.getKey(), entry.getValue())
            //  );

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(WebAppApplication.class, args);
    }

}


