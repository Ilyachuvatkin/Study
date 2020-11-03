public class Main {
    private final Object mon = new Object();
    private volatile String letter = "A";

    public static void main(String[] args) {
        Main main = new Main();

        new Thread(() -> {
            main.threadA();
        }).start();
        new Thread(() -> {
            main.threadB();
        }).start();
        new Thread(() -> {
            main.threadC();
        }).start();

    }

    private void threadA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!letter.equals("A")) {
                        mon.wait();
                    }
                    System.out.println("A");
                    letter = "B";
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();}
        }
    }

    private void threadB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!letter.equals("B")) {
                        mon.wait();
                    }
                    System.out.println("B");
                    letter = "C";
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void threadC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!letter.equals("C")) {
                        mon.wait();
                    }
                    System.out.println("C");
                    letter = "A";
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
