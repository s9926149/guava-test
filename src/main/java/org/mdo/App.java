package org.mdo;

import com.google.common.util.concurrent.*;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<String> future1 = service.submit(new Callable<String>() {
            public String call() throws Exception {
                System.out.println("task1");
                Thread.sleep(10);
                return "OK";
            }
        });

        ListenableFuture<Integer> future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                System.out.println("task2");
                return 200;
            }
        });

        Futures.addCallback(future1, new FutureCallback<String>() {
            public void onSuccess(@Nullable String s) {
                System.out.println("task1 success");
            }

            public void onFailure(Throwable throwable) {
                System.out.println("task1 oops");
            }
        }, MoreExecutors.directExecutor());

        Futures.addCallback(future2, new FutureCallback<Integer>() {
            public void onSuccess(@Nullable Integer s) {
                System.out.println("task2 success");
            }

            public void onFailure(Throwable throwable) {
                System.out.println("task2 oops");
            }
        }, MoreExecutors.directExecutor());

        service.shutdown();
        try {
            service.awaitTermination(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new Exception("failed to complete in given time", e);
        }


        System.out.println( "Everything is done." );
    }
}
