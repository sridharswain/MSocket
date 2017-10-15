import java.io.*;
import java.net.*;
public class server
{
  //MAIN
  public static void main(String[] args) throws Exception
  {
      MSocket mSocket = new MSocket(3000,socketCallbacks);
      mSocket.beginAccept(); //START LISTENING FOR CLIENTS
  }

  static SocketCallbacks socketCallbacks = new SocketCallbacks(){
    @Override
    public void onAccept(MSocket mSocket){
        //ON CONNECTED TO A CLIENT
        mSocket.beginReceive();
        MSocket newSocket =  new MSocket(3000, socketCallbacks); //LISTEN TO OTHER CLIENTS
        newSocket.beginAccept();
    }

    @Override
    public void onClose(MSocket mSocket){
      //NO ERROR OR CLOSING OF CLIENT (NOT PROPERLY IMPLEMENTED)
      System.out.println("Socket Closed");
    }

    @Override
    public void onReceive(MSocket mSocket,String message){
      //ON MESSAGE RECEIVED AFTER "beginReceive" is CALLED ONCE
      System.out.println("RECEIVED "+message);
      mSocket.beginSend("Thankyou"+ message);
    }

    @Override
    public void onSend(MSocket mSocket){
      //ON MESSAGE SENT
      System.out.println("Sent");
    }
  };
}
