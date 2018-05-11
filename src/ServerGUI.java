import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class ServerGUI extends javax.swing.JFrame {
    private ServerRoomChat server;
    int n = 0;

    public ServerGUI(String porta) {
        server = null;
        try {
            try {
                server = new ServerRoomChat(Integer.valueOf(porta));
            } catch (AlreadyBoundException ex) {
                Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            initComponents();
            Runnable r = new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            refreshList();
                            Thread.sleep(400);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        } catch (RemoteException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RMI Room Chat (erasch, lfeldens) (Server)");

        jLabel1.setText("List Of Rooms:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Close Room");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String room = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()));
        //server.close(room);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void refreshList() {
        TreeMap<String, IRoomChat> rooms = server.getRooms();

        if (rooms.size() != n) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            Set set = rooms.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                model.addRow(new Object[]{mentry.getKey()});
            }
            n = rooms.size();
        }
        /*ArrayList<String> names = server.getRooms();
        if (names.size() != model.getRowCount()) {
            model.setRowCount(0);
            if (names != null) {
                for (int i = 0; i < names.size(); i++) {
                    model.addRow(new Object[]{names.get(i)});
                }
            }
        }*/
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
