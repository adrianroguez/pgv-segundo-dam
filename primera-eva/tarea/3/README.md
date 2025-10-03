# Programación de Servicios y Procesos: "Comenzando con Procesos en Java"
## Actividad final
### Diseña un programa en Java que:
Liste los procesos (ps aux).
Filtre solo los que contengan java.
Guarde el resultado en mis_procesos.txt.
Muestre en pantalla cuántas líneas tiene el archivo (wc -l).
Si hay más de 3 procesos java, imprime:
"¡Cuidado, muchos procesos de Java activos!".
```php
# Ayuda: comandos equivalentes en la terminal
ps aux | grep java > mis_procesos.txt
wc -l mis_procesos.txt
```
```java
import java.io.*;

public class ProcesosJava {
    public static void main(String[] args) {
        try {
            /*
             * Listar los procesos
             */
            ProcessBuilder psBuilder = new ProcessBuilder("ps", "aux");
            Process psProcess = psBuilder.start();

            /*
             * Filtrar solo los que contengan java y guardar el resultado en mis_procesos.txt
             */
            BufferedReader reader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter("mis_procesos.txt"));

            /*
             * Mostrar en pantalla cuantas lineas tiene el archivo wc -l
             */
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("java")) {
                    writer.write(line);
                    writer.newLine();
                    count++;
                }
            }

            writer.close();
            reader.close();

            System.out.println("Número de procesos Java: " + count);

            /*
             * Si hay mas de 3 procesos
             */
            if (count > 3) {
                System.out.println("¡Cuidado, muchos procesos de Java activos!");
            }

        } catch (IOException e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
        }
    }
}

```
