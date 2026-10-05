package rtp;



import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HelperUtils {
    public static List<int[]> createBrandNewList(List<int[]> lists){
        List<int[]> newList = new ArrayList<>();

        for(int[] integerList : lists) {
            int[] convertedArray = new int[integerList.length];
            for(int i = 0; i < integerList.length; i++){
                convertedArray[i] = integerList[i];
            }
            newList.add(convertedArray);
        }

        return newList;
    }

    public static void logJVMStatistics(){
        try{
            double megs = 1048576.0;
            System.out.println("JVMStatistics: available threads " + Runtime.getRuntime().availableProcessors());
            System.out.println("JVMStatistics: free memory   " + Runtime.getRuntime().freeMemory()/megs + " MiB" +
                    "\nJVMStatistics: total memory   " + Runtime.getRuntime().totalMemory()/megs + " MiB" +
                    "\nJVMStatistics: max memory   " + Runtime.getRuntime().maxMemory()/megs + " MiB") ;

            List<GarbageCollectorMXBean> beans =
                    ManagementFactory.getGarbageCollectorMXBeans();

            for(var bean : beans){
                System.out.println("JVMStatistics: GC " + bean.getName());
//      System.out.println(bean.getMemoryPoolNames());
            }

        } catch (Exception e) {
            System.out.println("JVMStatistics error: not able to log out JVM statistics");
        }
    }



}
