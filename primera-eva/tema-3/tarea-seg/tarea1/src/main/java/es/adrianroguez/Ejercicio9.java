package es.adrianroguez;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejercicio9 {
    private static final AtomicBoolean fin = new AtomicBoolean(false);
    private static final AtomicBoolean destruida = new AtomicBoolean(false);
    private static final int tiempoMisionMs = 4000;
    private static long inicio;
    private static final AtomicInteger velocidad = new AtomicInteger(0);
    private static final AtomicInteger escudos = new AtomicInteger(100);

    private static class HanSolo implements Runnable {
        @Override
        public void run() {
            while (!fin.get()) {
                velocidad.addAndGet((int) (Math.random() * 10) + 5);
                int falloHiperimpulsor = (int) (Math.random() * 100) + 1;
                if (falloHiperimpulsor <= 5) {
                    destruida.set(true);
                    fin.set(true);
                    System.out.println("Fallo de hiperimpulsor. ¡La nave se destruye!");
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (System.currentTimeMillis() - inicio >= tiempoMisionMs) {
                    fin.set(true);
                }
            }
        }
    }

    private static class Chewbacca implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (!fin.get()) {
                escudos.addAndGet(random.nextInt(16) - 10);
                if (escudos.get() <= 0) {
                    destruida.set(true);
                    fin.set(true);
                    System.out.println("¡Escudos agotados! La nave se destruye!");
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (System.currentTimeMillis() - inicio >= tiempoMisionMs) {
                    fin.set(true);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        inicio = System.currentTimeMillis();
        Thread t1 = new Thread(new HanSolo());
        Thread t2 = new Thread(new Chewbacca());
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        if (!destruida.get()) {
            System.out.println("¡Han y Chewie escapan! Vel=" + velocidad.get() + ", Escudos=" + escudos.get());
        }
    }

}
