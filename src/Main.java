import utils.InformazioniTabella;

import java.util.Scanner;

import static QueryCreazione.Creazione.getMetaDataTabella;
import static QueryCreazione.Creazione.inserimento;

public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Benvenuto su NetWave");

        boolean isRunning = true;

        while (isRunning) {
            boolean isAdding = true;
            boolean isQuerying = true;
            System.out.println("1) Premi 1 per inserire nuovi record all'interno del database");
            System.out.println("2) Premi 2 per effettuare delle interrogazioni al database");
            System.out.println("3) Premi 3 per chiudere l'applicazione");

            try {
                int scelta = Integer.parseInt(scan.nextLine());
                switch (scelta) {
                    case 1 -> {
                        String tabella;
                        while (isAdding) {
                            System.out.println("Scrivi il nome della tabella nella quale vuoi inserire un record");
                            System.out.println("Scrivi exit se vuoi tornare al menù precedente");

                            tabella = scan.nextLine();

                            if (tabella.toLowerCase().equals("exit")) {
                                isAdding = false;
                            } else {
                                InformazioniTabella i = getMetaDataTabella(tabella);
                                inserimento(i);
                            }
                        }
                    }
                    case 2 -> {
                        String query;
                        while (isQuerying) {
                            System.out.println("In fase di progettazione");
                            System.out.println("Scrivi exit se vuoi tornare al menù precedente");

                            query = scan.nextLine();

                            if (query.toLowerCase().equals("exit")) {
                                isQuerying = false;
                            } else {
                                System.out.println("Coming Soon");
                            }
                        }
                    }
                    case 3 -> isRunning = false;
                    default -> System.out.println("Opzione non disponibile");
                }
            } catch (final NumberFormatException e) {
                System.out.println("Devi scrivere un numero");
            }

        }

    }
}
