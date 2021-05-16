package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("ru.alefilas")
@EnableJpaRepositories("ru.alefilas.repository")
@EntityScan("ru.alefilas.model")
public class TestDocumentsApp {

    public static void main(String[] args) {
        SpringApplication.run(TestDocumentsApp.class, args);
    }

}
