package es.adrianroguez;

public class Ejercicio7 {
    private static volatile boolean amenazaNeutralizada = false;
    private static final String[] zonasA = { "Norte", "Centro", "Este" };
    private static final String[] zonasB = { "Oeste", "Sur" };
    private static volatile String ganador = null;

    private static class Superman implements Runnable {
        @Override
        public void run() {
            for (String zona : zonasA) {
                if (amenazaNeutralizada) {
                    break;
                }
                try {
                    Thread.sleep((int) (Math.random() * 300) + 200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Superman salvó " + zona);
            }
            if (!amenazaNeutralizada) {
                amenazaNeutralizada = true;
                ganador = "Superman";
                System.out.println("Amenaza neutralizada por Superman");
            }

        }

    }

    private static class Batman implements Runnable {
        @Override
        public void run() {
            for (String zona : zonasB) {
                if (amenazaNeutralizada) {
                    break;
                }
                try {
                    Thread.sleep((int) (Math.random() * 300) + 300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Batman salvó " + zona);
            }
            if (!amenazaNeutralizada) {
                amenazaNeutralizada = true;
                ganador = "Batman";
                System.out.println("Amenaza neutralizada por Batman");
            }
        }

    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Superman());
        Thread t2 = new Thread(new Batman());
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
