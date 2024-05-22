package fr.univrouen.cv24.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HelpController {

    @GetMapping("/help")
    public String help(Model model) {
        // Liste des opérations gérées par le service REST
        List<Operation> operations = Arrays.asList(
            new Operation("/example1", "GET", "Retourne un exemple de ressource"),
            new Operation("/example2", "POST", "Crée une nouvelle ressource"),
            // Ajoutez d'autres opérations ici
            new Operation("/example3", "PUT", "Met à jour une ressource existante"),
            new Operation("/example4", "DELETE", "Supprime une ressource")
        );

        model.addAttribute("operations", operations);
        return "help";
    }

    // Classe pour représenter une opération REST
    public static class Operation {
        private String url;
        private String method;
        private String summary;

        public Operation(String url, String method, String summary) {
            this.url = url;
            this.method = method;
            this.summary = summary;
        }

        public String getUrl() {
            return url;
        }

        public String getMethod() {
            return method;
        }

        public String getSummary() {
            return summary;
        }
    }
}
