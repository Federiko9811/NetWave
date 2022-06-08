import exceptions.TabellaNonTrovataException;
import utils.InformazioniTabella;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static Query.Interrogazioni.*;
import static QueryCreazione.Creazione.getMetaDataTabella;
import static QueryCreazione.Creazione.inserimento;
import static utils.ConsoleColors.makePurple;
import static utils.ConsoleColors.makeRed;

public class Main {

    private static final List<String> listaQuery = new ArrayList<>(
            List.of(
                    "Nomi e Cognomi dei tecnici che hanno effettuato un'istallazione il primo giorno di lavoro",
                    "Codice fiscale ed Email dei dipendenti che hanno effettuato un abbonamento con NetWave",
                    "Numero di furgoni utilizzati nelle istallazioni di (mese e anno), utilizzati da tecnici",
                    "Nome e Email dei clienti che hanno effettuato un abbonamento il giorno di scadenza di una Tariffa",
                    "Numero di Sim in possesso da clienti che hanno sia un abbonamento con tariffa fissa che mobile",
                    "Numero di Installazioni che hanno coinvolto un'azienda collaboratrice ma che non hanno necessitato di un furgone",
                    "Nomi dei fornitori che hanno effettuato il maggior numero di consegne",
                    "Comune e via delle sedi che hanno almeno 2000 materiali di ogni tipo",
                    "Nomi e Cognomi di Assistenti che non hanno partecipato a nessun lavoro nell'ultimo mese",
                    "Targhe e Comuni delle Sedi delle Automobili con il maggior numero di chilometri registrati",
                    "Media degli stipendi per ogni categoria di dipendente",
                    "Fatture e clienti delle installazioni a cui hanno partecipato più di due aziende collaboratrici e il cui cliente dispone del solo abbonamento fisso",
                    "Età media dei dipendenti che sono stati assunti nel 2019 con un contratto FullTime",
                    "Nomi delle tariffe con il rapporto durata/prezzoMensile maggiore",
                    "Email dei clienti con una spesa mensile degli abbonamenti più elevata",
                    "Comuni con sede operativa che hanno il minor numero di furgoni ma il maggior numero di tecnici ",
                    "Aziende che hanno ricevuto assistenza da un dipendente il cui contratto era in scadenza (stesso mese della richiesta)",
                    "Email dei tecnici che hanno effettuato il maggior numero di installazioni e hanno un contratto valido da meno tempo"
            )
    );

    public static final String EXIT = makeRed("exit");

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println(makePurple("Benvenuto su NetWave"));

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
                            System.out.println("Scrivi " + EXIT + " se vuoi tornare al menù precedente");

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
                            System.out.println("Scrivi " + EXIT + " se vuoi tornare al menù precedente");

                            for (int k = 0; k < listaQuery.size(); k++) {
                                System.out.println((k + 1) + ") " + listaQuery.get(k));
                            }

                            query = scan.nextLine();

                            if (query.equalsIgnoreCase("exit")) {
                                isQuerying = false;
                            } else {

                                try {
                                    int numQuery = Integer.parseInt(query);

                                    switch (numQuery) {
                                        case 1 -> query1();
                                        case 2 -> query2();
                                        case 3 -> query3();
                                        case 4 -> query4();
                                        case 5 -> query5();
                                        case 6 -> query6();
                                        case 7 -> query7();
                                        case 8 -> query8();
                                        case 9 -> query9();
                                        case 10 -> query10();
                                        case 11 -> query11();
                                        case 12 -> query12();
                                        case 13 -> query13();
                                        case 14 -> query14();
                                        case 15 -> query15();
                                        case 16 -> query16();
                                        case 17 -> query17();
                                        case 18 -> query18();
                                        default -> System.out.println("Opzione non disponibile");
                                    }
                                    TimeUnit.SECONDS.sleep(2);
                                } catch (NumberFormatException e) {
                                    System.out.println("Devi scrivere un numero");
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
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
