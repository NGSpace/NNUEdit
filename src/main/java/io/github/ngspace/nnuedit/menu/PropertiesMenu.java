package io.github.ngspace.nnuedit.menu;

import static io.github.ngspace.nnuedit.asset_manager.StringTable.get;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
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

import io.github.ngspace.nnuedit.App;
import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.asset_manager.AssetManager;
import io.github.ngspace.nnuedit.menu.components.PropertiesRenderer;
import io.github.ngspace.nnuedit.utils.UserMessager;
import io.github.ngspace.nnuedit.utils.Utils;
import io.github.ngspace.nnuedit.window.abstractions.Editor;
import io.github.ngspace.nnuedit.window.abstractions.Savable;
import io.github.ngspace.nnuedit.window.abstractions.Window;
import io.github.ngspace.nnuedit.window.abstractions.WindowMenu;

public class PropertiesMenu extends WindowMenu implements Savable, Editor {
	
	private static final long serialVersionUID = -1174684557411847360L;
	
	public App app;
	public JScrollPane sp;
	protected File filepath;
	protected boolean isSaved = true;
	protected JTable table;
	
	public PropertiesMenu(App app, Window w, File f) {
		super(w);
	    final String[] columnNames = {get("prop.prop"), get("prop.val")};
		filepath = f;
		this.app = app;
		setSize(300, 350);
		setLayout(null);
        this.setBackground(Window.color);
        this.setForeground(Color.LIGHT_GRAY);
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        
        this.setFilePath(f.getAbsolutePath());
        
        // Initializing the JTable
        table = new JTable();
        table.putClientProperty("terminateEditOnFocusLost", true);
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem row = new JMenuItem(get("prop.context.row",0));
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
                        row.setText(get("prop.context.row",table.getSelectedRow()+1));
                    }
                });
            }

			@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
			@Override public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        JMenuItem deleteItem = new JMenuItem(get("prop.context.del"));
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

        JMenuItem insertBelow = new JMenuItem(get("prop.context.insertbelow"));
        insertBelow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int[] rows = table.getSelectedRows();
            	dtm.insertRow(rows[rows.length-1]+1, new Object[] {"key","value"});
            }
        });popupMenu.add(insertBelow);
        
        JMenuItem insertAbove = new JMenuItem(get("prop.context.insertabove"));
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
        final JPopupMenu headerPopupMenu = new JPopupMenu();
        JMenuItem insertHeader = new JMenuItem(get("prop.context.insertbelow"));
        insertHeader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dtm.insertRow(0, new Object[] {"key","value"});
            }
        });headerPopupMenu.add(insertHeader);
        table.getTableHeader().setComponentPopupMenu(headerPopupMenu);
        dtm.setColumnIdentifiers(columnNames);
        
        table.setFont(app.getFont());
        table.setRowHeight(50);
        table.setBounds(30, 40, 200, 300);
        table.setModel(dtm);
        table.getTableHeader().setReorderingAllowed(false);
        JTextField textField = new JTextField();
        textField.setFont(Main.settings.getFont("editor.prop.font"));
        textField.setBorder(new LineBorder(Color.BLACK));
        DefaultCellEditor dce = new DefaultCellEditor(textField);
        table.setFocusable(false);
		for (int i = 0; i < table.getColumnCount(); i++) {
		    table.getColumnModel().getColumn(i).setCellRenderer(new PropertiesRenderer(false));
		    table.getColumnModel().getColumn(i).setCellEditor(dce);
		}
        table.getTableHeader().setDefaultRenderer(new PropertiesRenderer(true));
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
        tc.setMinWidth(30);
        tc.setMaxWidth(10000);
        resize();
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

			@Override public void columnMarginChanged(ChangeEvent e) {
				Main.settings.set("editor.prop.col0width", table.getColumnModel().getColumn(0).getWidth()+"");
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
				if (ls.get(i).trim().isEmpty()) continue;
				if (ls.get(i).charAt(0)=='#') continue;
				String[] kAV = ls.get(i).split("=", 2);
				int ln = kAV[1].length();
				while (kAV[1].charAt(ln-1)=='\\') {
					i++;
					kAV[1]=kAV[1].substring(0, ln - 1) + ls.get(i);
					ln = kAV[1].length();
				}
		        dtm.addRow(new Object[] { kAV[0], kAV[1] });
			} catch (Exception e) {
				System.out.println("Corrupted properties file");
				UserMessager.showErrorDialogTB("prop.cannotreadline.title", "prop.cannotreadline",
						ls.get(i), i+1);
				break;
			}
		}
	}
	public void resize() {
		sp.setBounds(5,0,Math.min(Main.settings.getInt("editor.prop.tablewidth"),getWidth()-5),getHeight());
		sp.revalidate();
		repaint();
	}

	private int getColWidth() {
		try {
			return Integer.parseInt(Main.settings.get("editor.prop.col0width").trim());
		} catch (Exception e) {
			int x = app.isFolderOpen() ? app.Folder.getWidth() : -5;
			int width = app.contentpane.getWidth();
			return (width - x)/2;
		}
	}

	public String getFilePath() {return filepath.getAbsolutePath();}
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
			for (int i = 0; i<keys.size();i++)
				fw.write(keys.get(i) + "=" + values.get(i) + "\n");
			fw.close();
		} catch (IOException e) {
			UserMessager.showErrorDialogTB("prop.cannotsave.title","prop.cannotsave",e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean isSaved() {return isSaved||this.filepath==null;}
	@Override public void setFilePath(String value) {
		filepath = new File(value);
        window.getTab().setIcon(AssetManager.getIconOfFile(filepath));
	}
	@Override public boolean isOpen(String path) {return this.filepath.getAbsolutePath().equals(path);}
	@Override public void openFind() {/* ¯\_(ツ)_/¯ */}
	@Override public void escape() {this.requestFocus();}
	@Override public void openfile(boolean load, boolean save) {}
}
