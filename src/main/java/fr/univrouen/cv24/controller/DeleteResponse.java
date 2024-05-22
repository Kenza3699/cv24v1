package fr.univrouen.cv24.controller;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "result")
public class DeleteResponse {
    private Long id;
    private String status;

    public DeleteResponse() {}

    public DeleteResponse(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
