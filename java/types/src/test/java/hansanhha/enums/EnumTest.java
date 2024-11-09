package hansanhha.enums;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EnumTest {

    @Test
    void deliveryStatusTest() throws Exception {
        var preparing = DeliveryStatusManager.PREPARING;

        var preParingNextStatus = preparing.nextStatus();

        System.out.println(preparing.getStart());
        System.out.println(preparing.getFinish());
        System.out.println(preParingNextStatus.getStart());
    }

    @Test
    void multiThreadDeliveryStatusTest() throws Exception {
        var threadCount = 10;
        var executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                var preparing = DeliveryStatusManager.PREPARING;
                preparing.set();
                var nextStatus = preparing.nextStatus();
                var threadId = Thread.currentThread().threadId();

                System.out.println("[threadId : " + threadId + "] " + preparing + " start: " + preparing.getStart());
                System.out.println("[threadId : " + threadId + "] " + preparing + " finish: " + preparing.getFinish());
                System.out.println("[threadId : " + threadId + "] " + nextStatus + " start: " + nextStatus.getStart());
            });
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void colorTest() throws Exception {
        Color.colorMap.keySet()
                .forEach(key -> System.out.println("enum constant: " + key + ", this: " + Color.colorMap.get(key)));
    }
}
