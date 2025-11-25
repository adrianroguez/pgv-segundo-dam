package org.docencia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMazmorras {

    static class PeticionMazmorra implements Runnable {
        private final String nombreJugador;
        private final String mazmorra;

        public PeticionMazmorra(String nombreJugador, String mazmorra) {
            this.nombreJugador = nombreJugador;
            this.mazmorra = mazmorra;
        }

        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Preparando mazmorra '" + mazmorra +
                    "' para el jugador " + nombreJugador);
            try {
                Thread.sleep(1000 + (int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                System.out.println("[" + hilo + "] Petición de " + nombreJugador + " interrumpida");
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[" + hilo + "] Mazmorra '" + mazmorra +
                    "' lista para " + nombreJugador + " 🎮");
        }
    }

    public static void main(String[] args) {

        ExecutorService gmBots = Executors.newFixedThreadPool(3);

        String[] jugadores = {
                "Link", "Zelda", "Geralt", "Yennefer", "Gandalf",
                "Frodo", "Aragorn", "Leia", "Luke", "DarthVader"
        };
        String[] mazmorras = {
                "Catacumbas de Hyrule", "Torre Oscura", "Moria",
                "Estrella de la Muerte", "Nido de Dragón"
        };

        for (int i = 0; i < jugadores.length; i++) {
            String jugador = jugadores[i];
            String dungeon = mazmorras[i % mazmorras.length];
            gmBots.execute(new PeticionMazmorra(jugador, dungeon));
        }

        gmBots.shutdown();
        System.out.println("Servidor: todas las peticiones han sido enviadas a los GM bots.");
    }
}
