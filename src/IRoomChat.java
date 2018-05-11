

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRoomChat extends Remote{
    public int joinRoom(String username, IUserChat localObjRef) throws RemoteException;
    public void leaveRoom(String usrName) throws RemoteException; 
    public void closeRoom() throws RemoteException;
}
