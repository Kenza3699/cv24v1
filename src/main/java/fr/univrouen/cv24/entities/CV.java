package fr.univrouen.cv24.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "CV")
public class CV {
    @JacksonXmlProperty(localName = "Identite")
    private Identite identite;

    @JacksonXmlProperty(localName = "Poste")
    private Poste poste;

    @JacksonXmlElementWrapper(localName = "Experiences")
    @JacksonXmlProperty(localName = "Experience")
    private List<Experience> experiences;

    @JacksonXmlElementWrapper(localName = "Diplomes")
    @JacksonXmlProperty(localName = "Diplome")
    private List<Diplome> diplomes;

    @JacksonXmlElementWrapper(localName = "Certifications")
    @JacksonXmlProperty(localName = "Certification")
    private List<Certification> certifications;

    @JacksonXmlElementWrapper(localName = "Langues")
    @JacksonXmlProperty(localName = "Langue")
    private List<Langue> langues;

    @JacksonXmlElementWrapper(localName = "Autres")
    @JacksonXmlProperty(localName = "Autre")
    private List<Autre> autres;

    public CV() {
    }

    public CV(Identite identite, Poste poste, List<Experience> experiences, List<Diplome> diplomes,
              List<Certification> certifications, List<Langue> langues, List<Autre> autres) {
        this.identite = identite;
        this.poste = poste;
        this.experiences = experiences;
        this.diplomes = diplomes;
        this.certifications = certifications;
        this.langues = langues;
        this.autres = autres;
    }

    // Getters et setters
    public Identite getIdentite() {
        return identite;
    }

    public void setIdentite(Identite identite) {
        this.identite = identite;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Diplome> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<Diplome> diplomes) {
        this.diplomes = diplomes;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public List<Langue> getLangues() {
        return langues;
    }

    public void setLangues(List<Langue> langues) {
        this.langues = langues;
    }

    public List<Autre> getAutres() {
        return autres;
    }

    public void setAutres(List<Autre> autres) {
        this.autres = autres;
    }
}
