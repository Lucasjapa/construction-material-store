package projectpoo.construction_material_store;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import projectpoo.construction_material_store.screens.MainScreen;

@SpringBootApplication
public class ConstructionMaterialStoreApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(ConstructionMaterialStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(MainScreen mainScreen) {
		return args -> {
			mainScreen.setVisible(true);
		};
	}

}
