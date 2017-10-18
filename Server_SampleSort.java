
import java.io.*;
import java.util.*;

class Server_SampleSort{
  static ArrayList<MSocket> clients;
  static int numOfClients=0;
  static int recivedFrom = 0;
  static int arr[] = {10,2,3,1,6,4,5,2,21,34,14,7};
  //MAIN
  static int sortedArr[] = new int[0];
  public static void main(String[] args) throws IOException{

    clients = new ArrayList<MSocket>();


    System.out.print("Enter the number of clients to come online for sorting : ");
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    numOfClients = Integer.parseInt(inputReader.readLine());
    while(numOfClients<1){
      System.out.println("\nPlease enter number greater than 0.\n");
      System.out.print("Enter the number of clients to come online for sorting : ");
      numOfClients = Integer.parseInt(inputReader.readLine());
    }

    MSocket mSocket = new MSocket(3000,socketCallbacks);
    mSocket.beginAccept();

    System.out.println("Waiting for "+numOfClients+" clients to come online...\n");
  }

  static void afterAllOnlline(){
    int len = arr.length;
    int div = len/numOfClients;

    for(int i=0;i<numOfClients;i++){
      int start=i*div;
      int end = start+(div);
      String messageToSend = "";
      for(int j=start;j<end;j++){
        messageToSend= messageToSend+arr[j]+",";
      }
      System.out.println("Client-"+(i+1)+" is sorting "+messageToSend);
        clients.get(i).beginSend(messageToSend);
    }
  }

  static SocketCallbacks socketCallbacks = new SocketCallbacks(){
    @Override
    public void onAccept(MSocket mSocket){
      mSocket.beginReceive();
      clients.add(mSocket);

      int cNumOfClients = clients.size();
      System.out.println(cNumOfClients+"/"+numOfClients+" are connected.");
      if(cNumOfClients<numOfClients){
        //LISTEN TO OTHER CLIENTS FROM OTHER PORTS
         MSocket newSocket =  new MSocket(3000, socketCallbacks);
         newSocket.beginAccept();
      }
      else{
        System.out.println("All clients are online\n");
        recivedFrom=0;
        afterAllOnlline();
      }
    }

    @Override
    public void onReceive(MSocket mSocket, String message){
      System.out.println("Received a part of sorted array from a client : "+message);
      String[] rawNums = message.split(",");
      recivedFrom++;


        int nums[]=new int[rawNums.length];


        for(int i=0;i<rawNums.length;i++)
        {
          nums[i]=Integer.parseInt(rawNums[i]);
        }

        sortedArr=merge(sortedArr,nums);


      if(recivedFrom==numOfClients){
        System.out.print("Sorted Array : ");
        for(int num : sortedArr) System.out.print(num+" ");
        System.out.println();
      }
    }

    @Override
    public void onSend(MSocket mSocket){
      //System.out.println("Sent");
    }

    @Override
    public void onClose(MSocket mSocket){
      System.out.println("Socket CLosed");
    }

  };

  static int[] merge(int arr1[],int arr2[])
  {
    int i = 0, j = 0, k = 0;
    int arr3[]=new int[arr1.length+arr2.length];
    int n1=arr1.length;
    int n2=arr2.length;

        while (i<n1 && j <n2)
        {

            if (arr1[i] < arr2[j])
                arr3[k++] = arr1[i++];
            else
                arr3[k++] = arr2[j++];
        }

        while (i < n1)
            arr3[k++] = arr1[i++];

        while (j < n2)
            arr3[k++] = arr2[j++];

            return arr3;
  }
}
