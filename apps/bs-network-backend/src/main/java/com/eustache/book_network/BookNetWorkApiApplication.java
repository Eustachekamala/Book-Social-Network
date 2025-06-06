package com.eustache.book_network;

import com.eustache.book_network.repositories.RoleRepository;
import com.eustache.book_network.role.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetWorkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetWorkApiApplication.class, args);
	}

  @Bean
  public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
    return args -> {
      if (roleRepository.findByName("USER").isEmpty()){
        roleRepository.save(
          Role.builder().name("USER").build()
        );
      }
    };
  }
}
