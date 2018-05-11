import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomChat implements IRoomChat, Serializable {
    private Registry registry;
    private String roomName;
    private TreeMap<String, IUserChat> userList;

    public RoomChat(String roomName) {
        userList = new TreeMap<>();
        this.roomName = roomName;
        this.registry = null;
    }

    @Override
    public void leaveRoom(String usrName) {
        userList.remove(usrName);
        updateUsers();
    }
    
    private void updateUsers(){
        for(Map.Entry<String, IUserChat> entry : userList.entrySet()) {
            try {
                entry.getValue().updateUserList(userList);
            } catch (RemoteException ex) {
                Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void closeRoom() {
        userList.clear();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public int joinRoom(String username, IUserChat iUserChat) throws RemoteException {
        System.out.println("Joining ROOM NAME: " + roomName);
        userList.put(username, iUserChat);
        Set set = userList.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            System.out.println(">" + mentry.getKey());
        }
        updateUsers();
        return userList.size()-1;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public TreeMap<String, IUserChat> getUserList() {
        return userList;
    }

    public void setUserList(TreeMap<String, IUserChat> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "RoomChat{" + "registry=" + registry + ", roomName=" + roomName + ", userList=" + userList + '}';
    }
    
   

}
