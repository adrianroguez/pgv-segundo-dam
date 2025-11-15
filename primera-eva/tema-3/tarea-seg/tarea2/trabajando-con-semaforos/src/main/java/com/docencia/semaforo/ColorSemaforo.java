package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author adrianroguez
 * @version 1.0
 */
public class ColorSemaforo implements Runnable {
    private final String color;
    private final Semaphore semaforo = new Semaphore(1, true);
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static volatile String colorActual = "ROJO";

    /**
     * Constructor que recibe String color
     * 
     * @param color
     */
    public ColorSemaforo(String color) {
        this.color = color;
    }

    /**
     * Metodo para calcular el tiempo que dura cada color
     * 
     * @param color
     * @return int
     */
    private int tiempoPorColor(String color) {
        return switch (color.toUpperCase()) {
            case "AMBAR" -> 1000;
            default -> 3000;
        };
    }

    /**
     * Metodo para calcular el siguiente color teniendo en cuenta el actual
     * 
     * @param color
     * @return String
     */
    private String siguienteColor(String color) {
        return switch (color.toUpperCase()) {
            case "ROJO" -> "VERDE";
            case "VERDE" -> "AMBAR";
            default -> "ROJO";
        };
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                semaforo.acquire();
                try {
                    if (!color.equals(colorActual)) {
                        semaforo.release();
                        continue;
                    }
                    System.out.println("Color: " + color);
                    Thread.sleep(tiempoPorColor(color));
                    colorActual = siguienteColor(colorActual);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaforo.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new ColorSemaforo("ROJO"));
        Thread t2 = new Thread(new ColorSemaforo("VERDE"));
        Thread t3 = new Thread(new ColorSemaforo("AMBAR"));

        t1.start();
        t2.start();
        t3.start();
    }

}
