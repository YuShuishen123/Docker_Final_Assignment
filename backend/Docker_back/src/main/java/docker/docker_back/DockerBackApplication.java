package docker.docker_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
@ComponentScan({"com.example.demo", "docker.docker_back"})
public class DockerBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerBackApplication.class, args);
    }

}
