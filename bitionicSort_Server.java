import java.io.*;
import java.util.*;

class bitionicSort_Server{
  static ArrayList<MSocket> clients;
  static int numOfClients=0;
  static int recivedFrom = 0;
  static int arr[] = {10,2,3,1,6,4,5,2,21,34,14,7};
  //MAIN
  static int sortedArr[] = new int[arr.length];
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
      if(i==0)
        clients.get(i).beginSend("1|"+messageToSend);
      else
        clients.get(i).beginSend("0|"+messageToSend);
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
      String[] rawNums = message.substring(2,message.length()-1).split(",");
      if(message.charAt(0)=='1'){
        for(int i=0;i<arr.length/2;i++) sortedArr[i]= Integer.parseInt(rawNums[i]);
        recivedFrom++;
      }
      else{
        recivedFrom++;
        for(int i=arr.length/2;i<arr.length;i++) sortedArr[i]= Integer.parseInt(rawNums[i-arr.length/2]);
      }
      if(recivedFrom==numOfClients){
        Algorithm.bitonicMerge(sortedArr,0,sortedArr.length,1);
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
}
