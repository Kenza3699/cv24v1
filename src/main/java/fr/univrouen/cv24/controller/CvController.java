package fr.univrouen.cv24.controller;

import fr.univrouen.cv24.entities.*;
import fr.univrouen.cv24.repositories.AutreRepository;
import fr.univrouen.cv24.repositories.CertificationRepository;
import fr.univrouen.cv24.repositories.DiplomeRepository;
import fr.univrouen.cv24.repositories.ExperienceRepository;
import fr.univrouen.cv24.repositories.IdentiteRepository;
import fr.univrouen.cv24.repositories.LangueRepository;
import fr.univrouen.cv24.repositories.PosteRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.xml.sax.InputSource;

import org.w3c.dom.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import java.io.InputStream;


import java.util.ArrayList;
import java.util.List;

@Controller
public class CvController {

    private static final Logger logger = LoggerFactory.getLogger(CvController.class);

    private final PosteRepository posteRepository;
    private final ExperienceRepository experienceRepository;

    private final CertificationRepository certificationRepository;
    private final LangueRepository langueRepository;
    private final AutreRepository autreRepository;

    @Autowired
    private IdentiteRepository identiteRepository;

    @Autowired
    private DiplomeRepository diplomeRepository;

    public CvController(IdentiteRepository identiteRepository, PosteRepository posteRepository,
            ExperienceRepository experienceRepository, DiplomeRepository diplomeRepository,
            CertificationRepository certificationRepository, LangueRepository langueRepository,
            AutreRepository autreRepository) {
        this.identiteRepository = identiteRepository;
        this.posteRepository = posteRepository;
        this.experienceRepository = experienceRepository;
        this.diplomeRepository = diplomeRepository;
        this.certificationRepository = certificationRepository;
        this.langueRepository = langueRepository;
        this.autreRepository = autreRepository;
    }

    // méthode pour afficher tous les cv disponibles dans la bases sous forme html
    @GetMapping("/cv24/resume")
    public String getAllCVsForHTML(Model model) {
        List<Identite> identites = identiteRepository.findAll();
        List<CV> cvs = new ArrayList<>();

        for (Identite identite : identites) {
            CV cv = new CV();
            cv.setIdentite(identite);
            cv.setPoste(posteRepository.findByIdentiteId(identite.getId()).orElse(null));
            cv.setExperiences(experienceRepository.findByIdentiteId(identite.getId()));
            cv.setDiplomes(diplomeRepository.findByIdentiteId(identite.getId()));
            cv.setCertifications(certificationRepository.findByIdentiteId(identite.getId()));
            cv.setLangues(langueRepository.findByIdentiteId(identite.getId()));
            cv.setAutres(autreRepository.findByIdentiteId(identite.getId()));
            cvs.add(cv);
        }

        model.addAttribute("cvs", cvs);
        return "resume";
    }

    // méthode pour afficher tous le cv en détaille sous forme xml

    @GetMapping(value = "/cv24/xml", produces = "application/xml")
    @ResponseBody
    public String getCVDetailInXML(@RequestParam("id") Long id) throws ParserConfigurationException, IOException, SAXException {
        Identite identite = identiteRepository.findById(id).orElse(null);
        XMLParser xp = new XMLParser();
        if (identite == null) {
            return xp.generateErrorXML("Identité non trouvée pour l'ID: " + id);
        }
    
        CV cv = new CV();
        cv.setIdentite(identite);
        cv.setPoste(posteRepository.findByIdentiteId(identite.getId()).orElse(null));
        cv.setExperiences(experienceRepository.findByIdentiteId(identite.getId()));
        cv.setDiplomes(diplomeRepository.findByIdentiteId(identite.getId()));
        cv.setCertifications(certificationRepository.findByIdentiteId(identite.getId()));
        cv.setLangues(langueRepository.findByIdentiteId(identite.getId()));
        cv.setAutres(autreRepository.findByIdentiteId(identite.getId()));
    
        String fxml = xp.parseDataCVToXML(cv);
        String xsdFichierPath = "xml/shema.xsd";
    
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
    
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(fxml)));
    
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdStream = getClass().getClassLoader().getResourceAsStream(xsdFichierPath);
            if (xsdStream == null) {
                throw new IOException("Schema file not found in classpath: " + xsdFichierPath);
            }
            Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));
            Validator validator = schema.newValidator();
            logger.info("génération du XML du CV");
            validator.validate(new DOMSource(document));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Une erreur est survenue lors de la génération du XML d'detail d'un CV: {}", e.getMessage());
            return xp.generateErrorXML("Erreur de validation du XML par rapport au schéma XSD: " + e.getMessage());
        }
        return fxml;
    }
    

    // méthode pour afficher tous les cv disponibles dans la base de données sous
    // forme xml
    @GetMapping(value = "/cv24/resume/xml", produces = "application/xml")
    @ResponseBody
    public String getAllCVsForXML() {
        XMLParser xp = new XMLParser();

        try {
            List<Identite> identites = identiteRepository.findAll();
            List<CV> cvs = new ArrayList<>();

            for (Identite identite : identites) {
                CV cv = new CV();
                cv.setIdentite(identite);
                cv.setPoste(posteRepository.findByIdentiteId(identite.getId()).orElse(null));
                cv.setExperiences(experienceRepository.findByIdentiteId(identite.getId()));
                cv.setDiplomes(diplomeRepository.findByIdentiteId(identite.getId()));
                cv.setCertifications(certificationRepository.findByIdentiteId(identite.getId()));
                cv.setLangues(langueRepository.findByIdentiteId(identite.getId()));
                cv.setAutres(autreRepository.findByIdentiteId(identite.getId()));
                cvs.add(cv);
            }

            return xp.parseDataToXML(cvs);
        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la génération du XML pour les CVs : {}", e.getMessage());
            return xp.generateErrorXML("Une erreur est survenue lors de la génération du XML pour les CVs.");
        }
    }

    // Méthode pour affichier les détails d'un cv sous forme html
    @GetMapping("/cv24/html")
    public ResponseEntity<String> getCVAsHTML(@RequestParam("id") Long id) throws ParserConfigurationException, IOException, SAXException {
        Identite identite = identiteRepository.findById(id).orElse(null);
        XMLParser xp = new XMLParser();
        if (identite == null) {
            String errorXml = xp.generateErrorXML("Identité non trouvée pour l'ID: " + id);
            String errorHtml = xp.genereateHTMLWithXSLT(errorXml);
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(errorHtml);
        }
    
        CV cv = new CV();
        cv.setIdentite(identite);
        cv.setPoste(posteRepository.findByIdentiteId(identite.getId()).orElse(null));
        cv.setExperiences(experienceRepository.findByIdentiteId(identite.getId()));
        cv.setDiplomes(diplomeRepository.findByIdentiteId(identite.getId()));
        cv.setCertifications(certificationRepository.findByIdentiteId(identite.getId()));
        cv.setLangues(langueRepository.findByIdentiteId(identite.getId()));
        cv.setAutres(autreRepository.findByIdentiteId(identite.getId()));
    
        String xmlData = xp.parseDataCVToXML(cv);
        String htmlData = xp.genereateHTMLWithXSLT(xmlData);
    
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(htmlData);
    }
    

}
