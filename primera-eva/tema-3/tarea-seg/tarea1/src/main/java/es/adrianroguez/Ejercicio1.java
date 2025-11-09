package es.adrianroguez;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio1 {
    private volatile boolean juegoTerminado = false;
    private int hpPikachu = 100;
    private int hpCharmander = 100;
    private String turno = "Pikachu";
    private final Lock m = new ReentrantLock();
    private final Condition turnoCambio = m.newCondition();

    private void atacar(String atacante, boolean esPikachu) {
        int danio = (int) (Math.random() * 16) + 5;
        if (esPikachu) {
            hpCharmander -= danio;
            System.out.println(
                    atacante + " lanza un ataque de " + danio + " puntos de daño. HP del rival: " + hpCharmander);
            if (hpCharmander <= 0 && !juegoTerminado) {
                juegoTerminado = true;
                System.out.println("¡" + atacante + " ha sido el vencedor!");
            }
        } else {
            hpPikachu -= danio;
            System.out.println(
                    atacante + " lanza un ataque de " + danio + " puntos de daño. HP del rival: " + hpPikachu);
            if (hpPikachu <= 0 && !juegoTerminado) {
                juegoTerminado = true;
                System.out.println("¡" + atacante + " ha sido el vencedor!");
            }
        }
        try {
            Thread.sleep((int) (Math.random() * 2000) + 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private class HiloPikachu implements Runnable {
        @Override
        public void run() {
            while (!juegoTerminado) {
                m.lock();
                try {
                    while (!turno.equals("Pikachu") && !juegoTerminado) {
                        turnoCambio.await();
                    }
                    if (juegoTerminado)
                        break;
                    atacar("Pikachu", true);
                    turno = "Charmander";
                    turnoCambio.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class HiloCharmander implements Runnable {
        @Override
        public void run() {
            while (!juegoTerminado) {
                m.lock();
                try {
                    while (!turno.equals("Charmander") && !juegoTerminado) {
                        turnoCambio.await();
                    }
                    if (juegoTerminado)
                        break;
                    atacar("Charmander", false);
                    turno = "Pikachu";
                    turnoCambio.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        Ejercicio1 ejercicio1 = new Ejercicio1();
        Thread t1 = new Thread(ejercicio1.new HiloPikachu());
        Thread t2 = new Thread(ejercicio1.new HiloCharmander());
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
