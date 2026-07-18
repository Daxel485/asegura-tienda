package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.NoCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class StorageConfig {

    @Value("${firebase.json.path}")
    private String jsonPath;

    @Value("${firebase.json.file}")
    private String jsonFile;

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource(jsonPath + File.separator + jsonFile);
        if (!resource.exists()) {
            // Modo desarrollo local sin credenciales de Firebase: la app arranca
            // igual, pero la subida de imágenes no estará disponible hasta colocar
            // el archivo techshop-firebase.json en src/main/resources/firebase/.
            return StorageOptions.newBuilder()
                    .setProjectId("local-dev")
                    .setCredentials(NoCredentials.getInstance())
                    .build()
                    .getService();
        }
        try (InputStream inputStream = resource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }
}
