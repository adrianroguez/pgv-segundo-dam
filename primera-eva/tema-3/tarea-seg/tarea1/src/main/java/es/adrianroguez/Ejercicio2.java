package es.adrianroguez;

public class Ejercicio2 {
    private static volatile boolean encontrado = false;
    private static volatile String ganador = null;

    private static class Buscador implements Runnable {
        final String nombre;
        final String ubicacion;

        Buscador(String nombre, String ubicacion) {
            this.nombre = nombre;
            this.ubicacion = ubicacion;
        }

        @Override
        public void run() {
            try {
                int tiempo = (int) (Math.random() * 1500) + 500;
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!encontrado) {
                encontrado = true;
                ganador = nombre;
                System.out.println(ganador + " encontró un Horrocrux en " + ubicacion + ". ¡Búqueda terminada!");
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Buscador("Harry", "Bosque Prohibido"));
        Thread t2 = new Thread(new Buscador("Hermione", "Biblioteca Antigua"));
        Thread t3 = new Thread(new Buscador("Ron", "Mazmorras del castillo"));
        t1.start();
        t2.start();
        t3.start();
    }

}
