import java.util.*;
import java.io.*;

class bitonic_Thread{

  static boolean part1=false, part2 =false;
  static int arr[] = {10,2,3,1,6,4,5,2,21,34,14,7};
  static int[] sortedArr;
  public static void main(String args[]){
    System.out.println("\n");
    sortedArr = new int[arr.length];
    //CREATING 2 THREADS FOR sorting
    createThreadAndSort(0,arr.length/2,1);
    createThreadAndSort(arr.length/2,arr.length,2);

    //WAITING FOR BOTH THREADS TO COMPLETE THE SORTING
    while(!part1 || !part2){};
    System.out.print("Unmerged sorted array from the clients: ");
    for(int x : sortedArr) System.out.print(x+"  ");
    System.out.println("\n");

    //MERGING THE 2 PARTS IN THE UNMERGED ARRAY
    Algorithm.bitonicMerge(sortedArr,0,sortedArr.length,0);

    //FINAL RESULT
    System.out.print("Sorted array after merging ");
    for(int x : sortedArr) System.out.print(x+"  ");
    System.out.println("\n");
  }

  static void createThreadAndSort(final int start,final int end,final int part){
    Thread thread = new Thread(new Runnable(){
      @Override
      public void run(){
        int partArr[] = new int[end-start];
        for(int i=0;i<end-start;i++) partArr[i] = arr[i+start];

        Algorithm.sortByBitIonic(partArr);
        for(int i=0;i<end-start;i++) sortedArr[i+start] = partArr[i];
        if(part==1)  part1=true;
        else part2=true;
      }
    });
    thread.run();
  }
}
