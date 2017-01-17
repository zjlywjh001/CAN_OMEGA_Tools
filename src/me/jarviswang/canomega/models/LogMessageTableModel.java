package me.jarviswang.canomega.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class LogMessageTableModel implements TableModel {
	
	private final String[] titles = {"Time (ms)", "Type", "Id", "DLC", "Data"};
	@SuppressWarnings("rawtypes")
	private final Class[] classes = {String.class, ImageIcon.class,String.class,String.class,String.class};
	private ImageIcon[] icons = { new ImageIcon(LogMessageTableModel.class.getResource("/res/icons/info.png")),
			new ImageIcon(LogMessageTableModel.class.getResource("/res/icons/error.png")), 
			new ImageIcon(LogMessageTableModel.class.getResource("/res/icons/receive.png")), 
			new ImageIcon(LogMessageTableModel.class.getResource("/res/icons/send.png")) };
	private final ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	private final ArrayList<LogMessage> messages = new ArrayList<LogMessage>();
	
	public void addMessage(LogMessage msg) {
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
	
	public LogMessage getMessage(int i) {
		return (LogMessage)this.messages.get(i);
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
	
	public List<LogMessage> getAllMessages() {
		return this.messages;
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
		CANMessage msg = (this.messages.get(rowIndex)).getCanmsg();
		if (msg == null) {
			switch(columnIndex) {
			case 1:
				return this.icons[this.messages.get(rowIndex).getType().ordinal()];
			case 4:
				return this.messages.get(rowIndex).getMessage();
			}
			return "";
		}
		switch (columnIndex) {
		case 0:
			return Long.valueOf(this.messages.get(rowIndex).getTimestamp());
		case 1:
			return this.icons[this.messages.get(rowIndex).getType().ordinal()];
		case 2:
			if (msg.isExtended()) {
				return String.format("%08Xh",msg.getId());
			}
			return String.format("%03Xh", msg.getId());
		case 3:
			return Integer.valueOf(msg.getData().length);
		case 4:
			if (msg.isRtr()) {
				return "Remote Transmission Request";
			}
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
