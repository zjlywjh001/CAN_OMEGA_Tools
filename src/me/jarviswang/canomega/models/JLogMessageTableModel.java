package me.jarviswang.canomega.models;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class JLogMessageTableModel implements TableModel {
	
	private final String[] titles = {"Time (ms)", "Type", "Data"};
	@SuppressWarnings("rawtypes")
	private final Class[] classes = {String.class, ImageIcon.class,String.class};
	private ImageIcon[] icons = { new ImageIcon(getClass().getResource("/res/icons/receive.png")), 
			new ImageIcon(getClass().getResource("/res/icons/send.png")) };
	private final ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	private final ArrayList<JLogMessage> messages = new ArrayList<JLogMessage>();

	public void addMessage(JLogMessage msg) {
		int i = this.messages.size();
		this.messages.add(msg);
		TableModelEvent evt = new TableModelEvent(this,i,i,TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT);
		int j = 0;
		int k = this.listeners.size();
		while (j<k) {
			this.listeners.get(j).tableChanged(evt);
			j ++ ;
		}
	}
	
	public JLogMessage getMessage(int i) {
		return (JLogMessage)this.messages.get(i);
	}
	
	public void clear() {
		if (this.messages.size()==0) {
			return ;
		}
		int i = this.messages.size() - 1;
		this.messages.clear();
		TableModelEvent evt = new TableModelEvent(this,0,i,TableModelEvent.ALL_COLUMNS,TableModelEvent.DELETE);
		int j = 0;
		int k = this.listeners.size();
		while (j<k) {
			this.listeners.get(j).tableChanged(evt);
			j++;
		}
	}
	
	@Override
	public int getRowCount() {
		return this.messages.size();
	}

	@Override
	public int getColumnCount() {
		return this.titles.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.titles[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.classes[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		JMessage msg = (this.messages.get(rowIndex)).getKmsg();
		if (msg == null) {
			return "";
		}
		switch (columnIndex) {
		case 0:
			return Long.valueOf(this.messages.get(rowIndex).getTimestamp());
		case 1:
			return this.icons[this.messages.get(rowIndex).getType().ordinal()];
		case 2:
			String str = "";
			byte[] arrayOfByte = msg.getData();
			for (int i = 0; i < arrayOfByte.length; i++) {
				if (i > 0) {
					str = str.concat(" ");
				}
				str = str.concat(String.format("%02X", arrayOfByte[i]));
			}
			return str;
		}
		return "";
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException("Not supported!");
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(l);
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove(l);
		
	}

}
