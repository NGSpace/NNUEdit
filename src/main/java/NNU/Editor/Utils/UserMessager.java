package NNU.Editor.Utils;

import static NNU.Editor.AssetManagement.StringTable.getString;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UserMessager extends JOptionPane {
	
	private static final long serialVersionUID = -2519403971515413979L;
	
	public static void showErrorDialogTB(String titlekey, String textkey, Object... textargs) {
		showErrorDialog(getString(titlekey), getString(textkey, textargs));
	}
	public static void showErrorDialog(String title, String text) {
		showMessage(title,text, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showWarningDialogTB(String titlekey, String textkey, Object... textargs) {
		showWarningDialog(getString(titlekey), getString(textkey, textargs));
	}
	public static void showWarningDialog(String title, String text) {
		showMessage(title,text, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void showMessageDialogTB(String titlekey, String textkey, Object... textargs) {
		showMessageDialog(getString(titlekey), getString(textkey, textargs));
	}
	public static void showMessageDialog(String title, String text) {
		showMessage(title, text, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static int confirmTB(String title, String text,Object...v) {
		return confirmDialog(getString(text,v), getString(title), YES_NO_OPTION);
	}
	public static int confirmTB_c(String title, String text,Object...v) {
		return confirmDialog(getString(text,v), getString(title), YES_NO_CANCEL_OPTION);
	}
	
	public static String inputTB(String v, String text,Object...va) {
		return inputTB(v, text, text,va);
	}
	public static String inputTB(String v, String title, String text,Object...va) {
		return input(v,getString(title),getString(text,va));
	}
	
	/* A bunch of bullshit beyond this point! */
	
    public UserMessager() {
        this("Message");
    }
    public UserMessager(Object message) {
    	this(message, PLAIN_MESSAGE);
    }
	public UserMessager(Object message, int messageType) {
        this(message, messageType, DEFAULT_OPTION);
	}
    public UserMessager(Object message, int messageType, int optionType) {
    	this(message, messageType, optionType, null);
    }
    public UserMessager(Object message, int messageType, int optionType, Icon icon) {
        this(message, messageType, optionType, icon, null);
    }
    public UserMessager(Object message, int messageType, int optionType, Icon icon, Object[] options) {
        this(message, messageType, optionType, icon, options, null);
    }
    public UserMessager(Object message, int messageType, int optionType,
            Icon icon, Object[] options, Object initialValue) {
    	super(message,messageType,optionType,icon,options,initialValue);
    }
	public static void showMessage(String title, String text, int type) {
		UserMessager optionPane = new UserMessager(text, type);    
		JDialog dialog = optionPane.createDialog(title);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}
	public static String input(String v, String title, String text) {
		return showInputDialog(null,text,title,QUESTION_MESSAGE,null,null,v).toString();
	}
	public static int confirmDialog(String title, String text, int type) {
		return showConfirmDialog(null, text, title, type);
	}
}
