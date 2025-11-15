package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author adrianroguez
 * @version 1.0
 */
public class Estudiante extends Thread {
    private final String nombre;
    private static final Semaphore semaforo = new Semaphore(4, true);

    /**
     * Constructor que recibe String nombre
     * 
     * @param nombre
     */
    public Estudiante(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            semaforo.acquire();
            try {
                System.out.println(nombre + " ha comenzado a usar un equipo");
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5001));
                System.out.println(nombre + " ha dejado de usar un equipo");
            } finally {
                semaforo.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new EstudianteMejorado("Estudiante 1"));
        Thread t2 = new Thread(new EstudianteMejorado("Estudiante 2"));
        Thread t3 = new Thread(new EstudianteMejorado("Estudiante 3"));
        Thread t4 = new Thread(new EstudianteMejorado("Estudiante 4"));
        Thread t5 = new Thread(new EstudianteMejorado("Estudiante 5"));
        Thread t6 = new Thread(new EstudianteMejorado("Estudiante 6"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
    }

}
