import java.net.*;
import java.io.*;
class MSocket{
  private ServerSocket sSocket;
  public boolean isConnected = false;
  private SocketCallbacks socketCallbacks;
  private Socket cSocket;
  private BufferedReader reader;
  private PrintWriter writer;
  MSocket(int port, SocketCallbacks socketCallbacks){
    try{
      sSocket = new ServerSocket(3000);
      this.socketCallbacks = socketCallbacks;
    }
    catch(IOException e){
      socketCallbacks.onClose(MSocket.this);
    }
  }

  MSocket(String host,int port,SocketCallbacks socketCallbacks) throws UnknownHostException,IOException{
    cSocket = new Socket(host, port);
    this.socketCallbacks = socketCallbacks;
  }

  public void connect(){
    try{
      OutputStream ostream = cSocket.getOutputStream();
      writer = new PrintWriter(ostream, true);
      InputStream istream = cSocket.getInputStream();
      reader = new BufferedReader(new InputStreamReader(istream));
      socketCallbacks.onAccept(MSocket.this);
    }
    catch(IOException e){
      socketCallbacks.onClose(MSocket.this);
    }
  }

  public void beginAccept(){
    Thread acceptThread = new Thread(){
      @Override
      public void run(){
        try{
          System.out.println("A server socket has started for clients.");
          Socket serverSocket=sSocket.accept();
          isConnected = true;
          System.out.println("A server socket has connected to a client.");
          OutputStream ostream = serverSocket.getOutputStream();
          writer = new PrintWriter(ostream, true);
          InputStream istream = serverSocket.getInputStream();
          reader = new BufferedReader(new InputStreamReader(istream));
          socketCallbacks.onAccept(MSocket.this);
        }
        catch(IOException e){
          socketCallbacks.onClose(MSocket.this);
        }
      }
    };
    acceptThread.start();
  }

  public void beginReceive(){
    Thread recieveThread = new Thread(){
      @Override
      public void run(){
        String receiveMessage;
        while(true)
        {
          try{
            if((receiveMessage = reader.readLine()) != null)
            {
              socketCallbacks.onReceive(MSocket.this,receiveMessage);
            }
          }
          catch(Exception e){
            socketCallbacks.onClose(MSocket.this);
          }
        }
      }
    };
    recieveThread.start();
  }

  public void beginSend(String toSend){
      writer.println(toSend);
      writer.flush();
      socketCallbacks.onSend(MSocket.this);
  }
}
