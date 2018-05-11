import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRoomChat extends UnicastRemoteObject implements IServerRoomChat {

    private static Registry registry;
    private TreeMap<String, IRoomChat> roomList;
    private ServerGUI serverGUI;

    public ServerRoomChat(int porta) throws RemoteException, AlreadyBoundException {
        super();
        roomList = new TreeMap();
        try {
            //IServerRoomChat stub = (IServerRoomChat) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(porta);
            registry.bind("Servidor", this);
            //registry = LocateRegistry.createRegistry(porta);
            
            //registry.rebind("Servidor", stub);

            System.err.println("Server ready");

            
            /*Runnable r = new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if (roomList.size() > 0) {
                                System.out.println("listausers size= " + roomList.get("aa"));
                            }

                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();*/
        } catch (RemoteException ex) {
            Logger.getLogger(ServerRoomChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TreeMap<String, IRoomChat> getRooms() {
        return roomList;
    }

    @Override
    public synchronized void createRoom(String roomName) {
        try {
            System.out.println("Creating room: " + roomName);
            RoomChat roomChat = new RoomChat(roomName);
            IRoomChat stub = (IRoomChat) UnicastRemoteObject.exportObject(roomChat, 0);
            roomList.put(roomName, stub);
        } catch (RemoteException ex) {
            Logger.getLogger(ServerRoomChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
