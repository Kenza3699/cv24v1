package fr.univrouen.cv24.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import fr.univrouen.cv24.modele.testCV;
import fr.univrouen.cv24.util.Fichier;

@RestController
public class GetController {

    @GetMapping("/resume")
    public String getListCVinXML() {
        return "Envoi de la liste des CV";
    }

    @GetMapping("/cvid")
    public String getCVinXML(@RequestParam(value = "texte") String texte) {
        return "Détail du CV n°" + texte;
    }

    @GetMapping("/test")
    public String getTestXML(@RequestParam(value = "id") int id, @RequestParam(value = "titre") String titre) {
        return "Test: \n id= " + id + "\n titre= " + titre;
    }

    @Autowired
    private Fichier fichier;

    @GetMapping("/testfic")
    public String afficherContenuFichier() {
        return fichier.loadFileXML();
    }

    @GetMapping(value = "/testxml", produces = MediaType.APPLICATION_XML_VALUE)
    public testCV getXML() {
        testCV cv = new testCV("HAMILTON", "Margaret", "1969/07/21", "Appollo11@nasa.us");
        return cv;
    }
}
