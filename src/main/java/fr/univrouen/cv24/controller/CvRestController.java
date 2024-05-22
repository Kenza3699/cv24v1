package fr.univrouen.cv24.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;
import fr.univrouen.cv24.entities.Autre;
import fr.univrouen.cv24.entities.CV;
import fr.univrouen.cv24.entities.Certification;
import fr.univrouen.cv24.entities.Diplome;
import fr.univrouen.cv24.entities.Experience;
import fr.univrouen.cv24.entities.Identite;
import fr.univrouen.cv24.entities.Langue;
import fr.univrouen.cv24.entities.Poste;
import fr.univrouen.cv24.repositories.AutreRepository;
import fr.univrouen.cv24.repositories.CertificationRepository;
import fr.univrouen.cv24.repositories.DiplomeRepository;
import fr.univrouen.cv24.repositories.ExperienceRepository;
import fr.univrouen.cv24.repositories.IdentiteRepository;
import fr.univrouen.cv24.repositories.LangueRepository;
import fr.univrouen.cv24.repositories.PosteRepository;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CvRestController {

    private static final Logger logger = LoggerFactory.getLogger(CvController.class);

    private final IdentiteRepository identiteRepository;
    private final PosteRepository posteRepository;
    private final ExperienceRepository experienceRepository;
    private final DiplomeRepository diplomeRepository;
    private final CertificationRepository certificationRepository;
    private final LangueRepository langueRepository;
    private final AutreRepository autreRepository;

    public CvRestController(IdentiteRepository identiteRepository, PosteRepository posteRepository,
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

    @PostMapping("/cv24/insert")
    public ResponseEntity<String> validateXML(@RequestBody String xmlString) {
        Boolean etat = false;
        String xsdFichierPath = "xml/shema.xsd";
    
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
    
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
    
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdStream = getClass().getClassLoader().getResourceAsStream(xsdFichierPath);
            if (xsdStream == null) {
                throw new IOException("Schema file not found in classpath: " + xsdFichierPath);
            }
            Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));
    
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));
    
            etat = true;
        } catch (SAXParseException e) {
            int lineNumber = e.getLineNumber();
            int columnNumber = e.getColumnNumber();
            String errorMessage = e.getMessage();
    
            logger.error("Erreur de validation XML : Ligne {}, Colonne {} - {}", lineNumber, columnNumber, errorMessage);
    
            String response = String.format(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><status>Erreur de validation XML : Ligne %d, Colonne %d - %s</status>",
                    lineNumber, columnNumber, errorMessage);
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                    .body(response);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Une erreur s'est produite pendant la validation XML : {}", e.getMessage());
            etat = false;
        }
    
        if (etat) {
            XMLParser xp = new XMLParser();
            CV cv = xp.parseXML(xmlString);
    
            if (SameCV(cv)) {
                logger.warn("Le CV est déjà présent dans la base de données.");
                String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><status>Erreur : CV déjà existant</status>";
                return ResponseEntity.badRequest()
                        .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                        .body(response);
            } else {
                Long id = saveCV(cv);
                logger.info("CV enregistré avec succès dans la base de données");
                String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cv id=\"" + id + "\" status=\"INSERTED\"/>";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                        .body(response);
            }
        } else {
            logger.warn("Validation XML a échoué");
            String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><status>Erreur de validation XML : Le fichier XML est invalide</status>";
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                    .body(response);
        }
    }
    

    private Long saveCV(CV cv) {

        Identite savedIdentite = identiteRepository.save(cv.getIdentite());
        Long identiteId = savedIdentite.getId();
        Poste poste = cv.getPoste();
        poste.setIdentite(cv.getIdentite());

        posteRepository.save(poste);

        for (Experience experience : cv.getExperiences()) {
            experience.setIdentite(cv.getIdentite());
            experienceRepository.save(experience);
        }

        for (Diplome diplome : cv.getDiplomes()) {
            diplome.setIdentite(cv.getIdentite());
            diplomeRepository.save(diplome);
        }

        for (Certification certification : cv.getCertifications()) {
            certification.setIdentite(cv.getIdentite());
            certificationRepository.save(certification);
        }

        for (Langue langue : cv.getLangues()) {
            langue.setIdentite(cv.getIdentite());
            langueRepository.save(langue);
        }

        for (Autre autre : cv.getAutres()) {
            autre.setIdentite(cv.getIdentite());
            autreRepository.save(autre);
        }

        return identiteId;

    }

    private boolean SameCV(CV cv) {

        List<Identite> identites = identiteRepository.findAll();

        for (Identite identite : identites) {
            String telAsString = identite.getTel().toString();
            String sString = cv.getIdentite().getTel().toString();

            if (identite.getNom().equalsIgnoreCase(cv.getIdentite().getNom()) &&
                    identite.getPrenom().equalsIgnoreCase(cv.getIdentite().getPrenom())) {

                return true;
            }
            if (telAsString.equals(sString)) {
                return true;
            }
        }

        return false;
    }

    // Méthode pour suppprimer un cv en lui entrant un id
    @DeleteMapping(value = "/cv24/delete/{id}", produces = "application/xml")
    @Transactional
    public ResponseEntity<String> deleteCV(@PathVariable Long id) {
        logger.info("Requête reçue pour supprimer le CV avec l'id : {}", id);
        try {
            Optional<Identite> identiteOptional = identiteRepository.findById(id);
            if (identiteOptional.isPresent()) {
                Identite identite = identiteOptional.get();

                certificationRepository.deleteByIdentiteId(identite.getId());

                posteRepository.deleteByIdentiteId(identite.getId());
                experienceRepository.deleteByIdentiteId(identite.getId());
                diplomeRepository.deleteByIdentiteId(identite.getId());
                langueRepository.deleteByIdentiteId(identite.getId());
                autreRepository.deleteByIdentiteId(identite.getId());

                identiteRepository.deleteById(id);

                StringWriter stringWriter = new StringWriter();
                stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                stringWriter.write("<cv id=\"" + id + "\" status=\"DELETED\"/>");
                String response = stringWriter.toString();

                logger.info("CV avec l'id : {} supprimé avec succès", id);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                        .body(response);
            } else {
                logger.warn("CV avec l'id : {} non trouvé", id);
                StringWriter stringWriter = new StringWriter();
                stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                stringWriter.write("<status>Erreur</status>");
                String response = stringWriter.toString();

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                        .body(response);
            }
        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la suppression du CV avec l'id : {}", id, e);
            StringWriter stringWriter = new StringWriter();
            stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            stringWriter.write("<status>Erreur : " + e.getMessage() + "</status>");
            String response = stringWriter.toString();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                    .body(response);
        }
    }

    // Méthode pour faire la recherche d'un cv

    @GetMapping(value = "/cv24/search", produces = "application/xml")
    @ResponseBody
    public String searchCVsByObjectiveAndDate(
            @RequestParam(value = "objectif", required = false) String objectif,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        try {

            if (objectif == null || objectif.isEmpty()) {
                logger.error("Objectif non fourni.");
                return "<status>ERROR</status>";
            }

            List<Identite> identites = identiteRepository.findAll();
            List<CV> cvs = new ArrayList<>();

            for (Identite identite : identites) {
                Optional<Poste> posteOptional = posteRepository.findByIdentiteId(identite.getId());
                posteOptional.ifPresent(poste -> {
                    boolean objectifMatch = poste.getIntiltule() != null &&
                            Arrays.stream(objectif.split(" "))
                                    .anyMatch(keyword -> poste.getIntiltule().contains(keyword));

                    List<Diplome> diplomes = diplomeRepository.findByIdentiteId(identite.getId());
                    boolean dateMatch = diplomes.stream()
                            .anyMatch(diplome -> diplome.getDateObtention() != null &&
                                    (date == null || !diplome.getDateObtention().before(date)));

                    if (objectifMatch && dateMatch) {
                        CV cv = new CV();
                        cv.setIdentite(identite);
                        cv.setPoste(poste);
                        cv.setExperiences(experienceRepository.findByIdentiteId(identite.getId()));
                        cv.setDiplomes(diplomes);
                        cv.setCertifications(certificationRepository.findByIdentiteId(identite.getId()));
                        cv.setLangues(langueRepository.findByIdentiteId(identite.getId()));
                        cv.setAutres(autreRepository.findByIdentiteId(identite.getId()));
                        cvs.add(cv);
                    }
                });
            }

            if (cvs.isEmpty()) {
                logger.info("Aucun CV trouvé pour l'objectif : {}", objectif);
                return "<status>NONE</status>";
            }

            XMLParser xp = new XMLParser();
            return xp.parseDataToXML(cvs);
        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la recherche des CVs : {}", e.getMessage());
            return "<status>ERROR</status>";
        }
    }

}
