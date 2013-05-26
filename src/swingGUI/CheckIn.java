/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingGUI;

import common.DBUtils;
import common.IllegalEntityException;
import hotelmanager.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.SwingWorker;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Cmato
 */
public class CheckIn extends javax.swing.JFrame {

    private ResourceBundle general = HotelManagerFrame.LANGUAGE;
    private PersonListModel checkInListModel;
    private PersonListModel checkOutListModel;
    private Room currentRoom;
    private Parser parser;
    private Person personTmp;
    
    /**
     * Creates new form CheckIn
     */
    public CheckIn() throws SQLException {
	setUp();
	initComponents();
    }
    
    public CheckIn(String index) throws IOException, SQLException {
	parser = new Parser();
	setCurrentRoom(index);
	setUp();
	initComponents();
	roomIdLabel.setText(general.getString("roomId")+": "+currentRoom.getId().toString());
    }
    
    public CheckIn(boolean isPerson, String index) throws IOException, SQLException {
	parser = new Parser();
	currentRoom = null;
	getPersonRoom(index);
	setUp();
	initComponents();
    }
    
    private void setCurrentRoom(String index) throws IOException
    {
	try{
	    currentRoom = parser.parseStringToRoom(index);
	} catch (IOException e) {
	    Logger.getLogger(this.getName()).log(Level.SEVERE, null, e);
            new SetErrorMessage("<html>"+general.getString("errFromatRoom")+" </html>");
	}
    }
    
    private void getPersonRoom(final String person)
    {
	new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
		try {
		    personTmp = parser.parseStringToPerson(person);
		    currentRoom = HotelManagerFrame.getHotelManager().findRoomWithPerson(personTmp);
		} catch (IOException ex) {
		    Logger.getLogger(HotelManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
                return true;
            }
 
            @Override
            protected void done() {
		if(currentRoom != null)
		{
		    roomIdLabel.setText(general.getString("roomId")+": "+currentRoom.getId().toString());
		}
		else
		{
		    CheckIn.this.setVisible(false);
		    new SetErrorMessage("<html>"+general.getString("errPersonNonCheckedIn")+"</html>");
		}
            }
        }.execute();
    }
    
    @Before
    public void setUp() throws SQLException {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                    return true;
            }
 
            @Override
            protected void done() {
		if(currentRoom != null)
		{
		    List<Person> setUpPersons = HotelManagerFrame.getHotelManager().findPersonsNotInRooms();
		    checkInListModel = (PersonListModel) checkInList.getModel();

		    for (Person s : setUpPersons) {
			checkInListModel.addPerson(s);
		    }

		    List<Person> setUpPersonsInRoom = HotelManagerFrame.getHotelManager().findPersonsInRoom(currentRoom);
		    checkOutListModel = (PersonListModel) checkOutList.getModel();
		    for (Person s : setUpPersonsInRoom) {
			checkOutListModel.addPerson(s);
		    }
		}
            }
        }.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        roomIdLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        checkInList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        checkOutList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText(general.getString("cancel"));
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jLabel1.setText("jLabel1");

        jLabel2.setText(general.getString("checkIn"));

        checkInList.setModel(new swingGUI.PersonListModel());
        checkInList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkInListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(checkInList);

        checkOutList.setModel(new swingGUI.PersonListModel());
        checkOutList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkOutListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(checkOutList);

        jLabel3.setText(general.getString("checkOut"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(roomIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(77, 77, 77)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(36, 36, 36)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2)))))
                        .addGap(0, 72, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(266, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addContainerGap(266, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roomIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(143, 143, 143)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(319, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
	this.setVisible(false);
    }//GEN-LAST:event_jButton2MouseClicked

    private void checkInListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkInListMouseClicked
	new SwingWorker<Void, Void>() {
	    private Person currentPersonCheckIn;
            @Override
            protected Void doInBackground() throws IOException{
                currentPersonCheckIn = parser.parseStringToPerson(checkInList.getSelectedValue().toString());
		try{
		    HotelManagerFrame.getHotelManager().checkIn(currentPersonCheckIn, currentRoom);
		} catch(IllegalEntityException e){
		    currentPersonCheckIn = null;
		    new SetErrorMessage("<html>"+general.getString("errPersonExist")+"</html>");
		    Logger.getLogger(CheckIn.this.getName()).log(Level.INFO, "Error: Person is already placed in some room.");
		    return null;
		}
		Logger.getLogger(CheckIn.this.getName()).log(Level.INFO, "Successfuly checked in.");
		
                return null;
            }

            @Override
            protected void done() {
		if(currentPersonCheckIn != null)
		{
		    checkInListModel.removePerson(currentPersonCheckIn);
		    checkOutListModel.addPerson(currentPersonCheckIn);
		    currentPersonCheckIn = null;
		}
            }
        }.execute();
    }//GEN-LAST:event_checkInListMouseClicked

    private void checkOutListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkOutListMouseClicked
	new SwingWorker<Void, Void>() {
	    private Person currentPersonCheckOut;
            @Override
            protected Void doInBackground() throws IOException{
                currentPersonCheckOut = parser.parseStringToPerson(checkOutList.getSelectedValue().toString());
		try{
		    HotelManagerFrame.getHotelManager().checkOut(currentPersonCheckOut, currentRoom);
		} catch(IllegalEntityException e)
		{
		    currentPersonCheckOut = null;
		    new SetErrorMessage("<html>"+general.getString("errPersonNotInRoom")+"</html>");
		    Logger.getLogger(CheckIn.this.getName()).log(Level.INFO, "Error: Person is not in room.");
		    return null;
		}
		Logger.getLogger(CheckIn.this.getName()).log(Level.INFO, "Successfuly checked out.");
		
                return null;
            }

            @Override
            protected void done() {
		if(currentPersonCheckOut != null)
		{
		    checkOutListModel.removePerson(currentPersonCheckOut);
		    checkInListModel.addPerson(currentPersonCheckOut);
		    currentPersonCheckOut = null;
		}
            }
        }.execute();
    }//GEN-LAST:event_checkOutListMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/*
	 * Set the Nimbus look and feel
	 */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
	 * If Nimbus (introduced in Java SE 6) is not available, stay with the
	 * default look and feel. For details see
	 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(CheckIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(CheckIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(CheckIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(CheckIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/*
	 * Create and display the form
	 */
	java.awt.EventQueue.invokeLater(new Runnable() {

	    public void run() {
		try {
		    new CheckIn().setVisible(true);
		} catch (SQLException ex) {
		    Logger.getLogger(CheckIn.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList checkInList;
    private javax.swing.JList checkOutList;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel roomIdLabel;
    // End of variables declaration//GEN-END:variables
}
