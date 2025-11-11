package es.adrianroguez;

public class Ejercicio8 {
    private static final int durationMs = 5000;
    private static volatile boolean tiempoTerminado = false;
    private static volatile int totalThor = 0;
    private static volatile int totalHulk = 0;

    private static class Temporizador implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(durationMs);
                tiempoTerminado = true;
                System.out.println("¡Tiempo!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Thor implements Runnable {
        @Override
        public void run() {
            while (!tiempoTerminado) {
                int peso = (int) (Math.random() * 15) + 5;
                totalThor += peso;
                try {
                    Thread.sleep((int) (Math.random() * 115) + 5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class Hulk implements Runnable {
        @Override
        public void run() {
            while (!tiempoTerminado) {
                int peso = (int) (Math.random() * 15) + 5;
                totalHulk += peso;
                try {
                    Thread.sleep((int) (Math.random() * 115) + 5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t0 = new Thread(new Temporizador());
        Thread t1 = new Thread(new Thor());
        Thread t2 = new Thread(new Hulk());
        t0.start();
        t1.start();
        t2.start();
        t0.join();
        t1.join();
        t2.join();
        if (totalThor > totalHulk) {
            System.out.println("Thor gana con " + totalThor + " vs " + totalHulk);
        } else if (totalThor < totalHulk) {
            System.out.println("Hulk gana con " + totalHulk + " vs " + totalThor);
        } else {
            System.out.println("Empate: " + totalThor);
        }
    }

}
