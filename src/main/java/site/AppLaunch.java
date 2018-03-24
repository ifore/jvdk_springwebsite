package site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppLaunch {
    public static void main(String[] args) {
        System.out.println("start here...");
        SpringApplication.run(AppLaunch.class, args);
    }
}
