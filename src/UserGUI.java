import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class UserGUI extends javax.swing.JFrame {
    private final UserChat userChat;
    int n = 0;

    public UserGUI(String host, String porta, String usrName) {
        userChat = new UserChat(host, Integer.valueOf(porta), usrName, this);

        initComponents();
        jLabel2.setText("My Username: " + usrName);
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
    }

    public void listRooms(ArrayList<String> rooms) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < rooms.size(); i++) {
            model.addRow(new Object[]{rooms.get(i)});
        }
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RMI Room Chat (erasch, lfeldens) (User)");

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

        jButton1.setText("Join");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Create New");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("My Username:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
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
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshList() {
        TreeMap<String, IRoomChat> rooms = userChat.getRoomsServer();
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

        /*ArrayList<String> names = userChat.getRoomsServer();
        if (names.size() != model.getRowCount()) {
            model.setRowCount(0);
            if (names != null) {
                for (int i = 0; i < names.size(); i++) {
                    model.addRow(new Object[]{names.get(i)});
                }
            }
        }*/
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        userChat.createRoomServer(jTextField1.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int id = jTable1.getSelectedRow();
        String room = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()));
        new RoomGUI(userChat, this, room).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
