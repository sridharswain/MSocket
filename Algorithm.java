import java.util.*;
class Algorithm{
  static void sortByBitIonic(int[] arr){
    bitonicSort(arr, 0, arr.length, 1);
  }
  private static void compAndSwap(int a[], int i, int j, int dir)
    {
        if ( (a[i] > a[j] && dir == 1) ||
             (a[i] < a[j] && dir == 0))
        {
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }

    public static void bitonicMerge(int a[], int low, int cnt, int dir)
    {
      Arrays.sort(a);
        /*if (cnt>1)
        {
            int k = cnt/2;
            for (int i=low; i<low+k; i++)
                compAndSwap(a,i, i+k, dir);
            bitonicMerge(a,low, k, dir);
            bitonicMerge(a,low+k, k, dir);
        }*/
    }

    public static void bitonicSort(int a[], int low, int cnt, int dir)
    {
      Arrays.sort(a);
        /*if (cnt>1)
        {
            int k = cnt/2;
            bitonicSort(a, low, k, 1);
            bitonicSort(a,low+k, k, 0);
            bitonicMerge(a, low, cnt, dir);
        }*/
    }
}
