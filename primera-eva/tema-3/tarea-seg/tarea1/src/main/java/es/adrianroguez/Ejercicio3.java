package es.adrianroguez;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejercicio3 {
    private final static BlockingQueue<String> ensamblados = new LinkedBlockingQueue<>();
    private final static int N = 10;
    private final static AtomicInteger activados = new AtomicInteger(0);

    static class Ensamblador implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= N; i++) {
                try {
                    Thread.sleep((int) (Math.random() * 200) + 100);
                    String d = "Droid-" + i;
                    System.out.println("Ensamblado " + d);
                    ensamblados.put(d);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Activador implements Runnable {
        @Override
        public void run() {
            int cuenta = 0;
            while (cuenta < N) {
                try {
                    String d = ensamblados.take();
                    System.out.println("Activado " + d);
                    activados.incrementAndGet();
                    cuenta++;
                    Thread.sleep((int) (Math.random() * 100) + 50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread tE = new Thread(new Ensamblador());
        Thread tA = new Thread(new Activador());
        tE.start();
        tA.start();
    }

}
