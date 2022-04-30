package entità;

public class Dipendente {
    private final String codiceFiscale;
    private final String nome;
    private final String cognome;
    private final String dataNascita;
    private final String email;
    private final String telefono;
    private final String iban;
    private final String documento;

    public Dipendente(
            String codiceFiscale,
            String nome,
            String cognome,
            String dataNascita,
            String email,
            String telefono,
            String iban,
            String documento
    ) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.email = email;
        this.telefono = telefono;
        this.iban = iban;
        this.documento = documento;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIban() {
        return iban;
    }

    public String getDocumento() {
        return documento;
    }

    @Override
    public String toString() {
        return "Dipendente{" +
                "codiceFiscale='" + codiceFiscale + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dataNascita='" + dataNascita + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", iban='" + iban + '\'' +
                ", documento='" + documento + '\'' +
                '}';
    }
}
