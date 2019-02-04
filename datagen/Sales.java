package datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Sales {

    //Count digits to define divisor (Controlling number of threads to be spawned)
    public static int countDigits(int N){
        int count=0;
        while(N>0){
            count++;
            N/=10;
        }
        return count;
    }

    public static class Worker implements Callable<List<Integer>> {

        private int startAt;
        private int endAt;
        private int saleID;


        public Worker(int startAt, int endAt, int saleID) {
            this.startAt = startAt;
            this.endAt = endAt;
            this.saleID = saleID;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> numbers = new ArrayList<>();
            for (int index = startAt; index < endAt; index++) {
                numbers.add(saleID);
            }
            return numbers;
        }
    }

    //Parallelism for Sales ID List
    public static List<Integer> parallelDataGen(int noOfThreads, int range, List<Integer> list){

        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
        Worker[] workers = new Worker[noOfThreads];
        List<Integer> aggregatedData=new ArrayList<Integer>();
        for (int index = 0; index < noOfThreads; index++) {
            int startAt = index * range;
            int endAt = startAt + range;
            workers[index] = new Worker(startAt, endAt, list.get(index));
        }

        try {
            List<Future<List<Integer>>> results = executor.invokeAll(Arrays.asList(workers));
            for (Future<List<Integer>> future : results) {
                aggregatedData.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
        return aggregatedData;
    }

    //Break list into N sublists
    public static List<List<Integer>> createSubList(List<Integer> list){
        int size=list.size();
        int count=countDigits(size);
        int divisor=(int)(Math.pow(10,(count-2)));
        List<List<Integer>> parts = new ArrayList<List<Integer>>();
        final int N = list.size();
        int L=size/divisor;
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;

    }
}
