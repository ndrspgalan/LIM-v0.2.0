package presentation.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class ConsoleInput {
    private final BufferedReader reader;
    private final PrintStream output;

    public ConsoleInput(InputStream input, PrintStream output) {
        Objects.requireNonNull(input, "La entrada no puede ser nula.");
        this.output = Objects.requireNonNull(output, "La salida no puede ser nula.");
        this.reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    }

    public int readIntegerBetween(String prompt, int minimum, int maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("El mínimo no puede ser mayor que el máximo.");
        }

        while (true) {
            output.print(prompt);
            String value = readLine();

            try {
                int number = Integer.parseInt(value.trim());
                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // Se utiliza el mensaje común de validación.
            }

            output.printf("Introduce un número entre %d y %d.%n%n", minimum, maximum);
        }
    }

    public void waitForEnter(String prompt) {
        output.print(prompt);
        readLine();
    }

    public String readText(String prompt) {
        output.print(prompt);
        return readLine();
    }

    private String readLine() {
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalStateException("La entrada estándar se ha cerrado inesperadamente.");
            }
            return line;
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo leer la entrada de la consola.", exception);
        }
    }
}
