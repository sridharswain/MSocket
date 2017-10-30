import java.io.*;
import java.net.*;
public class client
{
  public static void main(String[] args) throws Exception
  {
    SocketCallbacks socketCallbacks = new SocketCallbacks(){
      @Override
      public void onAccept(MSocket mSocket){
          //ON CONNECTED TO SERVER
          mSocket.beginReceive(); //START LISTENING TO SERVER
          BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
          while(true)
          {
            try{
              System.out.print("Enter Text to send to Server : ");
              mSocket.beginSend(inputReader.readLine());
            }
            catch(IOException e){
              //IO Excetion;
            }
          }
      }

      @Override
      public void onClose(MSocket mSocket){
        //NO ERROR OR CLOSING OF SERVER (NOT PROPERLY IMPLEMENTED)
        System.out.println("Socket Closed");
      };

      @Override
      public void onReceive(MSocket mSocket,String message){
        //ON MESSAGE RECEIVED AFTER "beginReceive" is CALLED ONCE
        System.out.println("RECEIVED "+message);
      }

      @Override
      public void onSend(MSocket mSocket){
        //ON MESSAGE SENT
        System.out.println("Sent");
      }
    };
    MSocket mSocket = new MSocket("127.0.0.1",3000,socketCallbacks);
    mSocket.connect();
  }
}
