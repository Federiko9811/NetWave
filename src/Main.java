import entità.InformazioniTabella;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static QueryCreazione.Creazione.getMetaDataTabella;
import static QueryCreazione.Creazione.inserimento;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Scrivi il nome della tabella nella quale vuoi inserire un record:");
        String tabella = scan.nextLine();
        InformazioniTabella i = getMetaDataTabella(tabella);

        inserimento(i);
    }
}
