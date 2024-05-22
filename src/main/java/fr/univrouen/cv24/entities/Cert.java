package fr.univrouen.cv24.entities;

public enum Cert {
    MAT("MAT"),
    CLES("CLES"),
    TOEIC("TOEIC");

    private final String label;

    Cert(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
