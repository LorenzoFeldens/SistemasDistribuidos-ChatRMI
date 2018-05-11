

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.TreeMap;

public interface IUserChat extends Remote{
    public void deliverMsg(String senderUsrName, String msg, int id, Integer[] clockMatrix) throws RemoteException;;
    public void updateUserList(TreeMap<String, IUserChat> userList) throws RemoteException;;
}

