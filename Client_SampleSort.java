import java.net.*;
import java.io.*;
class Client_SampleSort{
  public static void main(String[] args) throws UnknownHostException,IOException{
    MSocket mSocket = new MSocket("127.0.0.1",3000,socketCallbacks);
    mSocket.connect();
  }

  static SocketCallbacks socketCallbacks = new SocketCallbacks(){
    @Override
    public void onAccept(MSocket mSocket){
      System.out.println("Connected to Server\nWaiting for data to sort...");
      mSocket.beginReceive();
    }

    @Override
    public void onReceive(MSocket mSocket,String message){
      System.out.println(message);
      String[] rawNums = message.substring(0,message.length()-1).split(",");
      int[] arr = new int[rawNums.length];
      for(int i=0;i<rawNums.length;i++){
        arr[i] = Integer.parseInt(rawNums[i]);
      }
      arr=Algorithm_sample_sort.drive_sampe_sort(arr);
      String toSend = message.charAt(0)+"|";
      for(int num : arr){
        toSend=toSend+num+",";
      }
      mSocket.beginSend(toSend);
    }

    @Override
    public void onSend(MSocket mSocket){

    }

    @Override
    public void onClose(MSocket mSocket){

    }
  };
}
