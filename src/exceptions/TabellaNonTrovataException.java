package exceptions;

import utils.ConsoleColors;

public class TabellaNonTrovataException extends Exception {
    @Override
    public String toString() {
        return ConsoleColors.RED_UNDERLINED + "La tabella inserita non è presente nel database" + ConsoleColors.RESET;
    }
}
