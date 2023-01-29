package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws InterruptedException {



        BlockingQueue q = new SynchronousQueue();
        q.offer("Hello");
        System.out.println("Hello");
        Exchanger<String> ex= new Exchanger<>();

        var threadpool = Executors.newFixedThreadPool(4);
        for(int i = 0 ; i < 10000; ++i){
            threadpool.submit(()->{
                int r = 0;
                for (int j = 0; j < 10000 * 10000 ; ++j){
                    r++;
                }
                return r;
            });
        }
        Executors.unconfigurableExecutorService(threadpool);


        boolean b = threadpool.awaitTermination(1, TimeUnit.DAYS);

        System.out.println(b);

    }
}