package es.adrianroguez;

import java.util.ArrayList;
import java.util.List;

public class Ejercicio6 {
    private static volatile boolean destinoAlcanzado = false;
    private static volatile String eraGanadora = null;

    private static class Viaje implements Runnable {
        private final  String era;

        public Viaje(String era) {
            this.era = era;
        }

        @Override
        public void run() {
            try {
                int tiempo = (int) (Math.random() * 1500) + 500;
                Thread.sleep(tiempo);
                if (!destinoAlcanzado) {
                    destinoAlcanzado = true;
                    eraGanadora = era;
                    System.out.println("La TARDIS llegó primero a " + era);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        String[] eras = { "Roma Antigua", "Futuro Lejano", "Era Victoriana", "Año 3000" };
        List<Thread> hilos = new ArrayList<>();
        for (String era : eras) {
            Thread hilo = new Thread(new Viaje(era));
            hilos.add(hilo);
            hilo.start();
        }
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
