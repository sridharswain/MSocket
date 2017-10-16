import java.net.*;
import java.io.*;
class MSocket{
  private static ServerSocket sSocket;
  public boolean isConnected = false;
  private SocketCallbacks socketCallbacks;
  private Socket cSocket;
  private BufferedReader reader;
  private PrintWriter writer;
  MSocket(int port, SocketCallbacks socketCallbacks){
    try{
      if(sSocket==null)
        sSocket = new ServerSocket(3000);
      this.socketCallbacks = socketCallbacks;
    }
    catch(IOException e){
      isConnected=false;
      socketCallbacks.onClose(MSocket.this);
    }
  }

  MSocket(String host,int port,SocketCallbacks socketCallbacks) throws UnknownHostException,IOException{
    cSocket = new Socket(host, port);
    this.socketCallbacks = socketCallbacks;
  }

  public void connect(){
    try{
      isConnected=true;
      OutputStream ostream = cSocket.getOutputStream();
      writer = new PrintWriter(ostream, true);
      InputStream istream = cSocket.getInputStream();
      reader = new BufferedReader(new InputStreamReader(istream));
      socketCallbacks.onAccept(MSocket.this);
    }
    catch(IOException e){
      isConnected=false;
      socketCallbacks.onClose(MSocket.this);
    }
  }

  public void beginAccept(){
    Thread acceptThread = new Thread(){
      @Override
      public void run(){
        try{
          Socket serverSocket=sSocket.accept();
          isConnected = true;
          OutputStream ostream = serverSocket.getOutputStream();
          writer = new PrintWriter(ostream, true);
          InputStream istream = serverSocket.getInputStream();
          reader = new BufferedReader(new InputStreamReader(istream));
          socketCallbacks.onAccept(MSocket.this);
        }
        catch(IOException e){
          isConnected=false;
          socketCallbacks.onClose(MSocket.this);
        }
      }
    };
    acceptThread.start();
  }

  public void beginReceive(){
    Thread recieveThread = new Thread(new Runnable(){
      @Override
      public void run(){
        String receiveMessage;
        while(true){
          try{
            if((receiveMessage = reader.readLine()) != null)
            {
              socketCallbacks.onReceive(MSocket.this,receiveMessage);
            }
          }
          catch(Exception e){
            isConnected=false;
            socketCallbacks.onClose(MSocket.this);
          }
        }
      }
    });
    recieveThread.start();
  }

  public void beginSend(String toSend){
      writer.println(toSend);
      writer.flush();
      socketCallbacks.onSend(MSocket.this);
  }
}
