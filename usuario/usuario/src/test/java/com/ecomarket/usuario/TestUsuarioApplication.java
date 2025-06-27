package com.ecomarket.usuario;

import org.springframework.boot.SpringApplication;

public class TestUsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.from(UsuarioApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
