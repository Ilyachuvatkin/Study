import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
        private static int CARS_COUNT;
        private static AtomicBoolean isWinner;
        private static CyclicBarrier allCars;
        protected static CountDownLatch finish;
        static {
            CARS_COUNT = 0;
            isWinner = new AtomicBoolean(false);
            allCars = new CyclicBarrier(MainClass.CARS_COUNT);
            finish = new CountDownLatch(MainClass.CARS_COUNT);
        }
        private CountDownLatch ready;
        private CountDownLatch start;
        private Race race;
        private int speed;
        private Lock lock;
        private String name;
        public String getName() {
            return name;
        }
        public int getSpeed() {
            return speed;
        }
        public Car(Race race, int speed, CountDownLatch cd, CountDownLatch cd1) {
            this.race = race;
            this.ready = cd;
            this.start = cd1;

            this.speed = speed;
            CARS_COUNT++;
            this.name = "Участник #" + CARS_COUNT;
        }
        @Override
        public void run() {
            try {
                System.out.println(this.name + " готовится");
                Thread.sleep(500 + (int)(Math.random() * 800));
                System.out.println(this.name + " готов");
                allCars.await();
                ready.countDown();
                start.await();

            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
                if ((i == race.getStages().size() - 1) && (!isWinner.get()))  {
                    isWinner.set(true);
                    System.err.println("Участник #" + CARS_COUNT + " WIN!");
                }
            }
            finish.countDown();
        }
    }
