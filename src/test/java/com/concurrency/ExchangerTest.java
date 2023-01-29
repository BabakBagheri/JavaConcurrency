package com.concurrency;
import org.junit.jupiter.api.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExchangerTest {

    @DisplayName("Test Exchanger synchronizer")
    @Test
    @Order(2)
    void testGet() throws InterruptedException{
        Exchanger<String> ex = new Exchanger<>();
        var tp = Executors.newFixedThreadPool(2);
        
        tp.submit(() -> {
            try {
                String message = ex.exchange("Hello");
                Thread.sleep(2000);
                Assertions.assertEquals(message,"World");
            }catch (InterruptedException exception){
                exception.printStackTrace();
            }finally {
                
            }
        });

        tp.submit(() -> {
            try {
                Thread.sleep(2000);
                String message = ex.exchange("World");
                Assertions.assertEquals(message,"Hello");
            }catch (InterruptedException exception){
                exception.printStackTrace();
            }finally {

            }
        });

        tp.shutdown();

        boolean isTerminated = tp.awaitTermination(5, TimeUnit.MINUTES);

        Assertions.assertEquals(isTerminated,true);
        Assertions.assertEquals(tp.isShutdown(),true);
    }

    @Test
    @DisplayName("TestExchangerWithFuture")
    @Order(1)
    void TestExchangerWithFuture(){
        Exchanger<String> ex = new Exchanger<>();

        var future = CompletableFuture.runAsync(() -> {
            try {
                String message = ex.exchange("Hello");
                Thread.sleep(2000);
                Assertions.assertEquals(message, "World");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        });

        try {
            String hello = ex.exchange("World");
            Assertions.assertEquals(hello,"Hello");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

        future.join();
    }
}
