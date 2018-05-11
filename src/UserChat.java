
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserChat implements IUserChat {

    private Registry registry;
    private IServerRoomChat stub;
    private String usrName;
    private IRoomChat room;
    private UserGUI userGUI;
    private int id;
    private ArrayList<Buffer> listaBuffer;

    private TreeMap<String, IRoomChat> roomList;
    private TreeMap<String, IUserChat> userList;
    private RoomGUI roomGUI;

    private TreeMap<Integer, TreeMap<Integer, String>> messages;

    private Integer[][] clockMatrix;

    public UserChat(String host, int porta, String usrName, UserGUI userGUI) {
        listaBuffer = new ArrayList<>();
        try {
            registry = LocateRegistry.getRegistry(host, porta);
            stub = (IServerRoomChat) registry.lookup("Servidor");

            this.usrName = usrName;
            this.userGUI = userGUI;

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(UserChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        clockMatrix = new Integer[20][20];
        messages = new TreeMap();
    }

    public TreeMap<String, IRoomChat> getRoomsServer() {
        try {
            roomList = stub.getRooms();
            return roomList;
        } catch (RemoteException ex) {
            Logger.getLogger(UserChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void joinRoom(String name) {
        try {
            room = roomList.get(name);
            IUserChat stub = (IUserChat) UnicastRemoteObject.exportObject(this, 0);
            id = room.joinRoom(usrName, stub);
            System.out.println("ID: " + id);
        } catch (RemoteException ex) {
            Logger.getLogger(UserChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createRoomServer(String name) {
        try {
            System.out.println("UserChat.createRoomServer");
            stub.createRoom(name);
        } catch (RemoteException ex) {
            Logger.getLogger(UserChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessageRoom(String user, String text) {
        this.clockMatrix[this.id][this.id]++;
        if (userList != null) {
            final IUserChat userF = userList.get(user);
            new Thread() {

                @Override
                public void run() {
                    try {
                        Integer[] inte = new Integer[userList.size()];
                        inte = clockMatrix[id];
                        Thread.sleep(10000);
                        userF.deliverMsg(usrName, text, id, inte);
                    } catch (InterruptedException ex) {
                        System.out.println("Erro1: " + ex.getMessage());
                    } catch (RemoteException ex) {
                        System.out.println("Erro2: " + ex.getMessage());
                    }
                }
            }.start();
            System.out.println("AQUI");
            ArrayList<IUserChat> usersAux = new ArrayList();
            for (Map.Entry<String, IUserChat> entry : userList.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(user)) {
                    usersAux.add(entry.getValue());
                }
            }
            for (int i = 0; i < usersAux.size(); i++) {
                try {
                    usersAux.get(i).deliverMsg(usrName, text, id, clockMatrix[id]);
                } catch (RemoteException ex) {
                    Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void sendMessageRoom(String text) {
        this.clockMatrix[this.id][this.id]++;
        if (userList != null) {
            for (Map.Entry<String, IUserChat> entry : userList.entrySet()) {
                System.out.println("SENT TO ALL:" + entry.getKey());
                try {
                    entry.getValue().deliverMsg(usrName, text, id, clockMatrix[id]);
                } catch (RemoteException ex) {
                    Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void deliverMsg(String msg) {
        roomGUI.print(msg);
    }

    public void leaveRoom() {
        try {
            room.leaveRoom(usrName);
        } catch (RemoteException ex) {
            Logger.getLogger(UserChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public RoomGUI getRoomGUI() {
        return roomGUI;
    }

    public void setRoomGUI(RoomGUI roomGUI) {
        this.roomGUI = roomGUI;
    }

    @Override
    public void deliverMsg(String senderUsrName, String msg, int id, Integer[] clockMatrix) {
        try {
            String text = senderUsrName + "> " + msg;
            if (messages.get(id) == null) {
                messages.put(id, new TreeMap());
                messages.get(id).put(clockMatrix[id], text);
            } else {
                messages.get(id).put(clockMatrix[id], text);
            }

            final Integer[] vet = new Integer[userList.size()];
            //clockMatrix o que chega com a mensagem
            //vet[i] local precisa ser criado
            for (int i = 0; i < userList.size(); i++) {
                vet[i] = this.clockMatrix[i][this.id];
                if (clockMatrix[i] > vet[i]+1 ) { //NÃO PODE SER ENTREGUE
                    
                } else { // ENTREGA MENSAGEM
                    
                    break;
                }
            }

            
            
            
            
            
            
            if (this.id != id) {
                this.clockMatrix[id] = clockMatrix;
                this.clockMatrix[this.id][id] = clockMatrix[id];
            }

            final Integer[][] clockAux = this.clockMatrix;
            final int idAux = this.id;

            for (int i = 0; i < userList.size(); i++) {
                vet[i] = this.clockMatrix[i][this.id];
                if (i != id) {
                    if (clockMatrix[i] > vet[i]) {
                        listaBuffer.add(new Buffer(id, msg, clockMatrix));

                        Runnable r = new Runnable() {
                            public void run() {
                                while (true) {
                                    try {
                                        if (listaBuffer.size() > 0) {
                                            Buffer b = listaBuffer.get(0);

                                            Integer[] vet2 = new Integer[userList.size()];
                                            int e = 0;
                                            for (int i = 0; i < userList.size(); i++) {
                                                vet[i] = clockAux[i][idAux];
                                                if (i != id) {
                                                    if (clockMatrix[i] > vet[i]) {
                                                        e = 1;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (e == 0) {
                                                System.out.println("AQUI 2");
                                                delivery(text);
                                            }
                                            listaBuffer.remove(0);
                                        }
                                        Thread.sleep(400);
                                    } catch (InterruptedException ex) {
                                        System.out.println("Erro3: " + ex.getMessage());
                                    }
                                }
                            }
                        };
                        Thread t = new Thread(r);
                        t.start();
                    } else {
                        System.out.println("AQUI 3");
                        delivery(text);
                    }
                }
                System.out.println("ClockMatrix = ");
                for (int k = 0; k < clockMatrix.length; k++) {
                    System.out.print(clockMatrix[i]);
                }
                System.out.println("VET = ");
                for (int k = 0; k < vet.length; k++) {
                    System.out.print(vet[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro 4: " + e.getMessage());
        }
    }

    @Override
    public void updateUserList(TreeMap<String, IUserChat> userList) {
        this.userList = userList;
        System.out.println("UPDATE USERLIST");
        listaBuffer = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < userList.size(); j++) {
                clockMatrix[i][j] = 0;
            }
        }
        messages = new TreeMap<>();
        roomGUI.atualizaUsers(userList);
    }

    private void delivery(String text) {
        for (int i = 0; i < userList.size(); i++) {
            int min = this.clockMatrix[0][i];
            for (int j = 1; j < userList.size(); j++) {
                if (this.clockMatrix[j][i] < min) {
                    min = this.clockMatrix[j][i];
                }
            }
            for (int j = min; j >= 0; j--) {
                if (messages.get(i) == null) {
                    System.out.println("MESSAGE NULL");
                } else if (messages.get(i).get(j) != null) {
                    messages.get(i).remove(j);
                }
            }
        }
        System.out.println("AQUI1");
        deliverMsg(text);
    }

}
