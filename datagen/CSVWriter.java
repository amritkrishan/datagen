package datagen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;

public class CSVWriter {

    private static final String CHAR_LIST =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 10;
    private static final String COLOR_LIST [] = {"Green","Blue","Black","Orange","Yellow","White","Blue","Red"};

    //Write data to items file
    public static void writeToItemsCsv(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        text = text.substring(1, text.length() - 1);
        String[] split = text.split(", ");
        for(int i=0;i<split.length;i++) {
            writer.write(split[i]);
            writer.write(",");
            writer.write(generateRandomItemName());
            writer.write(",");
            writer.write(generateRandomItemWeight());
            writer.write(",");
            writer.write(generateRandomItemColor());
            writer.write(",");
            writer.write("\n");
        }
        writer.flush();
        writer.close();
    }

    //Write data to sales file
    public static void writeToSalesCsv(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        text = text.substring(1, text.length() - 1);
        String[] split = text.split(", ");
        for(int i=0;i<split.length;i++) {
            writer.write(split[i]);
            writer.write(",");
            writer.write(generateRandomTimestamp());
            writer.write(",");
            writer.write(generateRandomPrice());
            writer.write(",");
            writer.write(generateRandomUnit());
            writer.write(",");
            writer.write(generateRandomDiscount());
            writer.write(",");
            writer.write("\n");
        }
        writer.flush();
        writer.close();
    }

    //Auto-generate sale timestamp
    private static String generateRandomTimestamp(){
        long start = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
        long stop = Timestamp.valueOf("2018-01-01 00:00:00").getTime();
        long diff = stop-start+1;
        Timestamp random = new Timestamp(start + (long)(Math.random() * diff));
        return String.valueOf(random);
    }

    //Auto-generate item name
    private static String generateRandomItemName(){
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    //Auto-generate random number so that it can be used as index to pick any character at random
    private static int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    //Auto-generate discount for sale table
    private static String generateRandomDiscount(){
        double discount=Math.floor(Math.random()*(100-0));
        return String.valueOf((int)discount);
    }

    //Auto-generate price
    private static String generateRandomPrice(){
        double price=Math.floor(Math.random()*(10000-10));
        return String.valueOf((int)price);
    }

    //Auto-generate unit
    private static String generateRandomUnit(){
        double unit=Math.floor(Math.random()*(100-1));
        return String.valueOf((int)unit);
    }

    //Auto-generate weight
    private static String generateRandomItemWeight(){
        double weight=Math.floor(Math.random()*(100-1));
        return String.valueOf(weight);
    }

    //Auto-generate color randomly from COLOR_LIST array
    private static String generateRandomItemColor(){
        int rnd = new Random().nextInt(COLOR_LIST.length);
        return COLOR_LIST[rnd];
    }
}
