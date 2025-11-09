package es.adrianroguez;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio4 {
    private static volatile boolean snitchAtrapada = false;
    private static int puntosEquipoA = 0;
    private static int puntosEquipoB = 0;
    private static Lock m = new ReentrantLock();

    private static class CazadorA implements Runnable {
        @Override
        public void run() {
            while (!snitchAtrapada) {
                try {
                    Thread.sleep((int) (Math.random() * 300) + 200);
                    int g = (int) (Math.random() * 2) * 10;
                    if (g > 0) {
                        m.lock();
                        try {
                            puntosEquipoA += g;
                            System.out.println("Equipo A anota 10. Total A=" + puntosEquipoA);
                        } finally {
                            m.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class CazadorB implements Runnable {
        @Override
        public void run() {
            while (!snitchAtrapada) {
                try {
                    Thread.sleep((int) (Math.random() * 300) + 200);
                    int g = (int) (Math.random() * 2) * 10;
                    if (g > 0) {
                        m.lock();
                        try {
                            puntosEquipoB += g;
                            System.out.println("Equipo B anota 10. Total B=" + puntosEquipoB);
                        } finally {
                            m.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class Buscador implements Runnable {
        @Override
        public void run() {
            while (!snitchAtrapada) {
                try {
                    Thread.sleep((int) (Math.random() * 400) + 300);
                    int chance = (int) (Math.random() * 100) + 1;
                    if (chance <= 15) {
                        snitchAtrapada = true;
                        System.out.println("¡Snitch dorada atrapada!");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new CazadorA());
        Thread t2 = new Thread(new CazadorB());
        Thread t3 = new Thread(new Buscador());
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("Marcador final:\nA=" + puntosEquipoA + "\nB=" + puntosEquipoB);
    }

}
