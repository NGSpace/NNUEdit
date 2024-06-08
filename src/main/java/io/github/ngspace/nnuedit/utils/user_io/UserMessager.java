package io.github.ngspace.nnuedit.utils.user_io;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UserMessager extends JOptionPane {

    public static final int YES = 0;
    public static final int NO = 1;
    public static final int CANCEL = 2;
    public static final int CLOSED = -1;
	
	
	private static final long serialVersionUID = -2519403971515413979L;
	
	public static void showErrorDialogTB(String titlekey, String textkey, Object... textargs) {
		showErrorDialog(get(titlekey), get(textkey, textargs));
	}
	public static void showErrorDialog(String title, String text) {
		showMessage(title,text, ERROR_MESSAGE);
	}
	public static void showWarningDialogTB(String titlekey, String textkey, Object... textargs) {
		showWarningDialog(get(titlekey), get(textkey, textargs));
	}
	public static void showWarningDialog(String title, String text) {
		showMessage(title,text, WARNING_MESSAGE);
	}
	public static void showMessageDialogTB(String titlekey, String textkey, Object... textargs) {
		showMessageDialog(get(titlekey), get(textkey, textargs));
	}
	public static void showMessageDialog(String title, String text) {
		showMessage(title, text, INFORMATION_MESSAGE);
	}
	public static int confirmTB(String title, String text,Object...v) {
		return confirmDialog(get(text,v), get(title), YES_NO_OPTION);
	}
	public static int showConfirmAndCancelTB(String title, String text,Object...v) {
		return confirmDialog(get(text,v), get(title), YES_NO_CANCEL_OPTION);
	}
	public static Object inputTB(String v, String text,Object...va) {
		return inputTB(v, text, text,va);
	}
	public static Object inputTB(String v, String title, String text,Object...va) {
		return input(v,get(title),get(text,va));
	}
	public static <T> T comboInput(String title,String text,Map<String, T> map, String initval) {
		String init = initval;
		String[] items = new String[map.size()];
		int i = 0;
		for (Entry<String, T> entry: map.entrySet()) items[i++] = entry.getKey();
        JComboBox<String> jcy = new JComboBox<String>(items);
        
        UserMessager jop = new UserMessager(title,QUESTION_MESSAGE,DEFAULT_OPTION,null,new String[] {"OK"});
        jcy.setSelectedItem(init);
		
		jop.add(jcy,1);
		JDialog dialog = jop.createDialog(text);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
		for(Entry<String, T> entry: map.entrySet()) 
			if (entry.getKey().equals(jcy.getSelectedItem()))
				return entry.getValue();
		return null;
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
	public static Object input(String v, String title, String text) {
		return showInputDialog(null,text,title,QUESTION_MESSAGE,null,null,v);
	}
	public static int confirmDialog(String title, String text, int type) {
		return showConfirmDialog(null, text, title, type);
	}
}
