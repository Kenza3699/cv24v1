package fr.univrouen.cv24.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model) {
        // Ajoutez les informations à afficher dans le modèle
        model.addAttribute("projectName", "JobFounder");
        model.addAttribute("versionNumberr", "1");
        model.addAttribute("teamMembers", new String[] { "Kenza DJELLAOUI", "Zahra CHAHI" });
        // Ajoutez le chemin vers le logo de l'Université de Rouen

        // Retournez le nom de la vue à afficher (fichier HTML/XHTML)
        return "index";
    }

}