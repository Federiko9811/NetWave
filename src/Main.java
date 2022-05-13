import exceptions.TabellaNonTrovataException;
import utils.ConsoleColors;
import utils.InformazioniTabella;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static Query.Interrogazioni.*;
import static QueryCreazione.Creazione.getMetaDataTabella;
import static QueryCreazione.Creazione.inserimento;

public class Main {

    private static final List<String> listaQuery = new ArrayList<>(
            List.of(
                    "Nomi e Cognomi dei tecnici che hanno effettuato un'istallazione il primo giorno di lavoro",
                    "Codice fiscale dei dipendenti che hanno effettuato un abbonamento con NetWave",
                    "Numero di furgoni utilizzati nelle istallazioni di (mese e anno), utilizzati da tecnici"
            )
    );

    public static final String EXIT = ConsoleColors.RED + "exit" + ConsoleColors.RESET;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE_BOLD + "Benvenuto su NetWave" + ConsoleColors.RESET);

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
                            System.out.println("Scrivi "+EXIT+" se vuoi tornare al menù precedente");

                            tabella = scan.nextLine();

                            if (tabella.equalsIgnoreCase("exit")) {
                                isAdding = false;
                            } else {

                                try {
                                    InformazioniTabella i = getMetaDataTabella(tabella);
                                    inserimento(i);
                                } catch (TabellaNonTrovataException e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }
                    case 2 -> {
                        String query;
                        while (isQuerying) {
                            System.out.println("Seleziona il numero della query che vuoi eseguire:");
                            System.out.println("Scrivi "+EXIT+" se vuoi tornare al menù precedente");

                            for (int k=0; k<listaQuery.size(); k++) {
                                System.out.println((k+1)+") "+listaQuery.get(k));
                            }

                            query = scan.nextLine();

                            if (query.equalsIgnoreCase("exit")) {
                                isQuerying = false;
                            } else {

                                try {
                                    int numQuery = Integer.parseInt(query);

                                    switch (numQuery) {
                                        case 1 -> {
                                            query1();
                                        }
                                        case 2 -> {
                                            query2();
                                        }
                                        case 3 -> {
                                            query3();
                                        }
                                        default -> {
                                            System.out.println("Opzione non disponibile");
                                        }
                                    }
                                } catch ( NumberFormatException e) {
                                    System.out.println("Devi scrivere un numero");
                                }
                            }
                        }
                    }
                    case 3 -> isRunning = false;
                    default -> System.out.println("Opzione non disponibile");
                }
            } catch (NumberFormatException e) {
                System.out.println("Devi scrivere un numero");
            }

        }

    }
}
