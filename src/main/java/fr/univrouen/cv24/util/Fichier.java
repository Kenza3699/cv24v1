package fr.univrouen.cv24.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Fichier {
    private Resource resource;

    public Fichier() {
        // Initialise resource avec "smallCV.xml"
        resource = new DefaultResourceLoader().getResource("XML/smallCV.xml");
    }

    public String loadFileXML() {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            // En cas d'erreur, retourne un message d'erreur
            return "Erreur lors de la lecture du fichier : " + e.getMessage();
        }
        // Retourne le contenu du fichier au format String
        return contentBuilder.toString();
    }
}