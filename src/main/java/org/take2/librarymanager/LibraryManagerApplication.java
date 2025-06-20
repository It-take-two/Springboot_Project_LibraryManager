package org.take2.librarymanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.take2.librarymanager.mapper")
public class LibraryManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagerApplication.class, args);
    }

}
