package NNU.Editor.Menus;

import static NNU.Editor.AssetManagement.StringTable.getString;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import NNU.Editor.App;
import NNU.Editor.Menus.Components.UIs.ColorRenderer;
import NNU.Editor.Utils.UserMessager;
import NNU.Editor.Utils.Utils;
import NNU.Editor.Windows.Interfaces.Editor;
import NNU.Editor.Windows.Interfaces.Savable;
import NNU.Editor.Windows.Interfaces.Window;

public class PropertiesMenu extends JPanel implements Savable, Editor {
	
	private static final long serialVersionUID = -1174684557411847360L;
	
	public Window window;
	public App app;
	public JScrollPane sp;
	protected File filepath;
	protected boolean isSaved = true;
	protected JTable table;
	
	public PropertiesMenu(App app, Window w, File f) {
	    final String[] columnNames = {getString("prop.prop"), getString("prop.val")};
		filepath = f;
		this.window = w;
		this.app = app;
		setSize(300, 350);
		setLocation(App.screenSize.width/2-this.getSize().width/2,
				App.screenSize.height/2-this.getSize().height/2);
		setLayout(null);
        this.setBackground(new Color(10,10,12));
        this.setForeground(Color.LIGHT_GRAY);
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        
        this.setFilePath(f.getAbsolutePath());
        
        // Initializing the JTable
        table = new JTable();
        table.putClientProperty("terminateEditOnFocusLost", true);
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem row = new JMenuItem(getString("prop.context.row",0));
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	if (table.getSelectedRows().length==0) {
	                    	Point point = SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table);
	                        int rowAtPoint = table.rowAtPoint(point);
	                    	int columnAtPoint = table.columnAtPoint(point);
	                        
	                        if (rowAtPoint > -1) {
	                            table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
	                            if (columnAtPoint>-1)
	            	            	table.changeSelection(rowAtPoint, columnAtPoint, false, false);
	                        }
                    	}
                        row.setText(getString("prop.context.row",table.getSelectedRow()+1));
                    }
                });
            }

			@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
			@Override public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        JMenuItem deleteItem = new JMenuItem(getString("prop.context.del"));
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int[] rows = table.getSelectedRows();
            	for(int i=0;i<rows.length;i++){
            		dtm.removeRow(rows[i]-i);
            	}
            }
        });popupMenu.add(deleteItem);
        popupMenu.addSeparator();

        JMenuItem insertBelow = new JMenuItem(getString("prop.context.insertbelow"));
        insertBelow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int[] rows = table.getSelectedRows();
            	dtm.insertRow(rows[rows.length-1]+1, new Object[] {"key","value"});
            }
        });popupMenu.add(insertBelow);
        
        JMenuItem insertAbove = new JMenuItem(getString("prop.context.insertabove"));
        insertAbove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int[] rows = table.getSelectedRows();
            	dtm.insertRow(rows[0], new Object[] {"key","value"});
            }
        });popupMenu.add(insertAbove);
        
        
        
        popupMenu.addSeparator();
        popupMenu.add(row);
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	/*int rowAtPoint = table.rowAtPoint(e.getPoint());
            	int columnAtPoint = table.columnAtPoint(e.getPoint());
                if (rowAtPoint > -1&&columnAtPoint>-1)
	            	table.changeSelection(rowAtPoint, columnAtPoint, false, false);*/
            }
		});
        dtm.setColumnIdentifiers(columnNames);
        
        //dtm.addRow(new String[] {"Test"});
        table.setFont(app.getFont());
        table.setRowHeight(50);
        table.setBounds(30, 40, 200, 300);
        table.setModel(dtm);
        JTextField textField = new JTextField();
        textField.setFont(App.stng.getFont("editor.prop.font"));
        textField.setBorder(new LineBorder(Color.BLACK));
        DefaultCellEditor dce = new DefaultCellEditor(textField);
        table.setFocusable(false);
		for (int i = 0; i < table.getColumnCount(); i++) {
		    table.getColumnModel().getColumn(i).setCellRenderer(new ColorRenderer(App.stng,true));
		    table.getColumnModel().getColumn(i).setCellEditor(dce);
		}
        table.getTableHeader().setDefaultRenderer(new ColorRenderer(App.stng,false));//.setFont(app.getFont());
        table.setBackground(getBackground());
        table.setBorder(new EmptyBorder(0,0,0,0));
        
        try {
			read(dtm,f);
		} catch (Exception e) {
			e.printStackTrace();
			UserMessager.showErrorDialogTB("prop.cannotread.title", "prop.cannotread", e.getMessage());
    	}
        dtm.addTableModelListener(e-> {
        	isSaved = false;
        });
        
        sp = new JScrollPane(table);
        sp.setOpaque(true);
        sp.setBorder(new EmptyBorder(0,0,0,0));
        add(sp);
        
		TableColumn tc = table.getColumnModel().getColumn(0);
        tc.setPreferredWidth(getColWidth());
        //tc.setWidth(getColWidth());
        tc.setMinWidth(30);
        tc.setMaxWidth(10000);
        resize();
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

			@Override public void columnMarginChanged(ChangeEvent e) {
				App.stng.set("editor.prop.col0width", table.getColumnModel().getColumn(0).getWidth()+"");
			}
			
			@Override public void columnAdded(TableColumnModelEvent e) {}
			@Override public void columnRemoved(TableColumnModelEvent e) {}
			@Override public void columnMoved(TableColumnModelEvent e) {}
			@Override public void columnSelectionChanged(ListSelectionEvent e) {}
        });
	}
	private void read(DefaultTableModel dtm, File f) throws FileNotFoundException {
		Scanner myReader = new Scanner(new FileInputStream(f));
		ArrayList<String> ls = new ArrayList<String>();
		while (myReader.hasNextLine())
			ls.add(myReader.nextLine());
	    myReader.close();
		for (int i = 0;i<ls.size();i++) {
			try {
				if (ls.get(i).isEmpty()) continue;
				if (ls.get(i).charAt(0)=='#') continue;
				String[] kAV = ls.get(i).split("=", 2);
		        dtm.addRow(new Object[] { kAV[0], kAV[1] });
			} catch (Exception e) {
				System.out.println("Corrupted properties file");
				UserMessager.showErrorDialogTB("prop.cannotreadline.title", "prop.cannotreadline",
						ls.get(i), i+1);
				continue;
			}
		}
	}
	public void resize() {
		//revalidate();
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		sp.setBounds(5,0,Math.min(App.stng.getInt("editor.prop.tablewidth"),getWidth()-5),getHeight());
		sp.revalidate();
		repaint();
	}

	private int getColWidth() {
		try {
			//System.out.println(app.stng.get("editor.prop.col0width").trim());
			return Integer.parseInt(App.stng.get("editor.prop.col0width").trim());
		} catch (Exception e) {
			int x = app.isFolderOpen() ? app.Folder.getWidth() : -5;
			int width = app.contentpane.getWidth();
			return (width - x)/2;
		}
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		window.getScrollPane().paintSeperators((Graphics2D)g);
	}

	public String getFilePath() {
		return filepath.getAbsolutePath();
	}
	public boolean Save(boolean ask) {
		if (isSaved()) return true;
		
		int result = 0;
		if (ask)
			result = UserMessager.confirmTB_c("confirm.save","confirm.save");
    	if (result==UserMessager.CANCEL_OPTION)
    		return false;
    	if (result==UserMessager.NO_OPTION)
    		return true;
		if ("".equals(this.getFilePath())) return false;
		
		isSaved = true;
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		TableModel model = table.getModel();
		for(int count = 0; count < model.getRowCount(); count++) {
			keys.add(Utils.valueOf(model.getValueAt(count, 0)));
			values.add(Utils.valueOf(model.getValueAt(count, 1)));
		}
		try {
			FileWriter fw = new FileWriter(filepath);
			for (int i = 0; i<keys.size();i++) {
				fw.write(keys.get(i) + "=" + values.get(i) + "\n");
			}
			fw.close();
		} catch (IOException e) {
			UserMessager.showErrorDialogTB("prop.cannotsave.title","prop.cannotsave",e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean isSaved() {
		return isSaved||this.filepath==null;
	}
	@Override
	public void setFilePath(String value) {
		filepath = new File(value);
        window.getTab().setIcon(app.Folder.getIcon1(filepath));
	}
	@Override
	public boolean isOpen(String path) {
		return this.filepath.getAbsolutePath().equals(path);
	}
	@Override public void openFind() {/* ¯\_(ツ)_/¯ */}
	@Override public void escape() {this.requestFocus();}

}
