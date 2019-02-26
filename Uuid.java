package io.netty.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by p0po on 2019/2/26 0026.
 */
public class Uuid {
    static SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    static int[] numArray = new int[90000];

    //TODO 保证乱序不重复
    static {
        Random random = new Random();
        for(int i=0;i<numArray.length;i++){
            numArray[i] = 10000+random.nextInt(89000);
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int index = 0;
        for(int i=0;i<10000000;i++){
            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            String uuid = dateFm.format(now)+"1"+numArray[index++];
            if(index>=89999){
                index = 0;
            }
            System.out.println(uuid);
        }
        System.out.println(System.currentTimeMillis()-start);
    }
}
