package datagen;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Items {

    public static void main(String args[]) throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter no of items : ");
        int noOfItems=sc.nextInt();
        int count=countDigits(noOfItems);
        int divisor=(int)(Math.pow(10,(count-2)));
        int totalNoOfSalesRecords=noOfItems*5;
        int noOfUniqueItemsForSales=totalNoOfSalesRecords/10;
        int noOfSalesRecordsPerItem=totalNoOfSalesRecords/noOfUniqueItemsForSales;
        int noOfThreads=noOfItems/divisor;
        int range=noOfItems/noOfThreads;
        List<Integer> itemIDList=parallelDataGen(noOfThreads,range);
        //Create a file for storing items details
        File file1 = new File("itemsID.csv");
        CSVWriter.writeToItemsCsv(file1,itemIDList.toString());
        range = range/2;
        List<Integer> itemIDForSalesList=parallelDataGen(noOfThreads,range);
        List<Integer> saleItemIDList=new ArrayList<>();
        datagen.Sales sales=new datagen.Sales();
        //Breaking big data list into smaller chunks of sublist
        List<List<Integer>> subList = sales.createSubList(itemIDForSalesList);
        //Generating sales id using each sublist at a time
        for(int i=0;i<subList.size();i++)
            saleItemIDList.addAll(sales.parallelDataGen(subList.get(i).size(),noOfSalesRecordsPerItem,subList.get(i)));
        //Create a file for storing sale items details
        File file2 = new File("salesID.csv");
        CSVWriter.writeToSalesCsv(file2,saleItemIDList.toString());
    }


    //Worker class
    public static class Worker implements Callable<List<Integer>> {
        private int startAt;
        private int endAt;

        public Worker(int startAt, int endAt) {
            this.startAt = startAt;
            this.endAt = endAt;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> numbers = new ArrayList<>();
            for (int index = startAt; index < endAt; index++) {
                numbers.add(index+1);
            }
            return numbers;
        }
    }

    //Count digits in number input by user
    public static int countDigits(int N){
        int count=0;
        while(N>0){
            count++;
            N/=10;
        }
        return count;
    }

    //Method to generate item ids/item ids for sale list in parallel
    public static List<Integer> parallelDataGen(int noOfThreads, int range) throws IOException {

        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
        Worker[] workers = new Worker[noOfThreads];
        List<Integer> aggregatedData=new ArrayList<>();

        for (int index = 0; index < noOfThreads; index++) {
            int startAt = index * range;
            int endAt = startAt + range;
            workers[index] = new Worker(startAt, endAt);
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
}

