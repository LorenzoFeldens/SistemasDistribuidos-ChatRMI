

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.TreeMap;

public interface IServerRoomChat extends Remote{
    public TreeMap<String, IRoomChat> getRooms() throws RemoteException;
    public void createRoom(String roomName) throws RemoteException;
}

