import java.net.*;
public interface SocketCallbacks{
  public void onAccept(MSocket mSocket);
  public void onClose(MSocket mSocket);
  public void onReceive(MSocket mSocket,String message);
  public void onSend(MSocket mSocket);
}
