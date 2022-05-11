package utils;

import java.util.List;

public class InformazioniTabella {
    private String nomeTabella;
    private List<String> nomiColonne;
    private List<String> tipiColonne;

    public String getNomeTabella() {
        return nomeTabella;
    }

    public List<String> getNomiColonne() {
        return nomiColonne;
    }

    public List<String> getTipiColonne() {
        return tipiColonne;
    }

    public void setNomeTabella(String nomeTabella) {
        this.nomeTabella = nomeTabella;
    }

    public void setNomiColonne(List<String> nomiColonne) {
        this.nomiColonne = nomiColonne;
    }

    public void setTipiColonne(List<String> tipiColonne) {
        this.tipiColonne = tipiColonne;
    }

    @Override
    public String toString() {
        return "Tabella: " + nomeTabella + "\n" +
                "Nomi Colonne: " + nomiColonne + "\n" +
                "Tipi Colonne: " + tipiColonne + "\n";
    }
}
