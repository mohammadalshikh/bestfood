package bestfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BestfoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestfoodApplication.class, args);
    }

}