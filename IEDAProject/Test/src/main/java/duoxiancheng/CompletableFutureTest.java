package duoxiancheng;

import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class CompletableFutureTest {
    public static void main(String[] args) throws Exception{
        CompletableFuture<Double> cfuture = CompletableFuture.supplyAsync(CompletableFutureTest::fetch);
        cfuture.thenAccept((result)->{
            System.out.println("result:"+result);
        });
        cfuture.exceptionally((e)->{
            e.printStackTrace();
            return null;
        });
        sleep(200);
    }
    static double fetch(){
        try{
            sleep(200);
        } catch (InterruptedException e) {

        }
        if(Math.random()<0.1){
            throw new RuntimeException("fetch price failed");
        }
        return 5+Math.random()*20;
    }
}

