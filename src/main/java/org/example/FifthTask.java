package main.java.org.example;

public class FifthTask implements Task {

    private final int startValue;
    private volatile boolean running = false;
    private Thread worker;

    public FifthTask(int startValue) {
        this.startValue = startValue;
    }

    @Override
    public void start() {
        if (running) {
            return;
        }

        running = true;

        worker = new Thread(() -> {
            int current = startValue;

            while (running && current >= 0) {

                System.out.println("Осталось: " + current + " сек.");
                current--;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            running = false;
        });

        worker.start();
    }

    @Override
    public void stop() {
        running = false;
        if (worker != null) {
            worker.interrupt();
        }
    }
}
