import entità.Dipendente;

import static Query.Test.inserisciDipendente;
import static Query.Test.testQuery;

public class Main {
    public static void main(String[] args) {
        Dipendente dipendente = new Dipendente(
                "XRHMLC75M55H783A",
                "Fabio",
                "Verdi",
                "01/01/1995",
                "fabioverdi@gmail.com",
                "3332223331",
                "IT98S0300203280637625659932",
                null
        );
        inserisciDipendente(dipendente);
        testQuery();
    }
}
