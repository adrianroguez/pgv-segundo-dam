package es.adrianroguez;

public class Ejercicio5 {
    private static volatile boolean pistaEncontrada = false;
    private static volatile String ganador = null;

    private static class Jedi implements Runnable {
        private final String nombre;
        private final String planeta;

        Jedi(String nombre, String planeta) {
            this.nombre = nombre;
            this.planeta = planeta;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((int) (Math.random() * 1100) + 400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!pistaEncontrada) {
                pistaEncontrada = true;
                ganador = nombre;
                System.out.println(nombre + " halló una pista en " + planeta + ". Fin de búsqueda.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Jedi("Kenobi", "Tatooine"));
        Thread t2 = new Thread(new Jedi("Skywalker", "Dagobah"));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}
