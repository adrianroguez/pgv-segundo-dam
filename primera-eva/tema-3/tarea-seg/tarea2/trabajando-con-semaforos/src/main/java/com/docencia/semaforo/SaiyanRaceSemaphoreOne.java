package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaiyanRaceSemaphoreOne implements Runnable {
    private final String name;
    private int distance = 0;
    private static final int GOAL = 100;
    private static final Semaphore turnGoku = new Semaphore(1);
    private static final Semaphore turnVegeta = new Semaphore(0);
    private static final AtomicBoolean WINNER_DECLARED = new AtomicBoolean(false);

    public SaiyanRaceSemaphoreOne(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        final boolean isGoku = "Goku".equals(name);
        final Semaphore myTurn = isGoku ? turnGoku : turnVegeta;
        final Semaphore otherTurn = isGoku ? turnVegeta : turnGoku;

        try {
            while (distance < GOAL && !WINNER_DECLARED.get()) {
                myTurn.acquire();

                if (WINNER_DECLARED.get()) {
                    otherTurn.release();
                    break;
                }
                int step = ThreadLocalRandom.current().nextInt(1, 11);
                distance += step;
                System.out.println(
                        name + " avanzó: " + step + " metros. Distancia recorrida total: " + distance + " metros.");

                if (distance >= GOAL) {
                    WINNER_DECLARED.compareAndSet(false, true);
                    System.out.println("¡" + name + " ha ganado la carrera!");
                }
                otherTurn.release();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 401));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    otherTurn.release();
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            (isGoku ? turnVegeta : turnGoku).release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread goku = new Thread(new SaiyanRaceSemaphoreOne("Goku"));
        Thread vegeta = new Thread(new SaiyanRaceSemaphoreOne("Vegeta"));

        goku.start();
        vegeta.start();

        goku.join();
        vegeta.join();
    }

}
