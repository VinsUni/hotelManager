/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingGUI;

/**
 *
 * @author Cmato
 */
public class SetErrorMessage {
    public SetErrorMessage(String message)
    {
	Error frame = new Error(message);
	frame.setTitle("Error !");
	frame.setDefaultCloseOperation(2);
	frame.setVisible(true);
	frame.setLocationRelativeTo(null);
    }
}
