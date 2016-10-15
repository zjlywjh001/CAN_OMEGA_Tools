package me.jarviswang.canomega.models;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import me.jarviswang.canomega.models.LogMessage.MessageType;

public class MonitorMessageTableModel implements TableModel {
	
	private final String[] titles = { "Period", "Count", "Type", "Id", "DLC", "Data" };
	private final Class[] classes = {String.class, String.class, ImageIcon.class, String.class, String.class, String.class};
	private ImageIcon[] icons = { new ImageIcon(getClass().getResource("/res/icons/info.png")), new ImageIcon(getClass().getResource("/res/icons/error.png")), new ImageIcon(getClass().getResource("/res/icons/receive.png")), new ImageIcon(getClass().getResource("/res/icons/send.png")) };
	private final ArrayList<TableModelListener> liseners = new ArrayList<TableModelListener>();
	private final TreeMap<Integer,MonitorMessage> messages = new TreeMap<Integer,MonitorMessage>();
	
	public void add(LogMessage msg) {
		int i = msg.getCanmsg().getId() << 2;
		if (msg.getCanmsg().isExtended()) {
			i |= 0x01;
		}
		if (msg.getType() == MessageType.OUT) {
			i |= 0x02;
		}
		TableModelEvent evt;
		if (this.messages.containsKey(Integer.valueOf(i))) {
			MonitorMessage mmsg = this.messages.get(Integer.valueOf(i));
			mmsg.increaseCount();
			mmsg.setPeriod(msg.getTimestamp() - mmsg.getLastLogMessage().getTimestamp());
			mmsg.setLastLogMessage(msg);
			int k = this.messages.headMap(Integer.valueOf(i)).size();
			evt = new TableModelEvent(this,k,k,TableModelEvent.ALL_COLUMNS,TableModelEvent.UPDATE);
			
		} else {
			this.messages.put(Integer.valueOf(i), new MonitorMessage(msg));
			int j = this.messages.headMap(Integer.valueOf(i)).size();
			evt = new TableModelEvent(this,j,j,TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT);
		}
		int j = 0;
		int k = this.liseners.size();
		while (j < k) {
			this.liseners.get(j).tableChanged(evt);
			j++;
		}
	}
	
	public void clear() {
		
		if (this.messages.size() == 0) {
			return ;
		}
		int i = this.messages.size() - 1;
		this.messages.clear();
		TableModelEvent evt = new TableModelEvent(this,0,i,TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
		int j = 0;
		int k = this.liseners.size();
		while (j < k) {
			this.liseners.get(j).tableChanged(evt);
			j ++;
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
		
		MonitorMessage msg = (MonitorMessage) this.messages.values().toArray()[rowIndex];
		LogMessage lmsg = msg.getLastLogMessage();
		CANMessage cmsg = lmsg.getCanmsg();
		switch (columnIndex) {
		case 0:
			return Long.valueOf(msg.getPeriod());
		case 1:
			return Long.valueOf(msg.getCount());
		case 2:
			return this.icons[lmsg.getType().ordinal()];
		case 3:
			if (cmsg.isExtended()) {
				return String.format("%08Xh", cmsg.getId());
			}
			return String.format("%03Xh", cmsg.getId());
		case 4:
			return Integer.valueOf(cmsg.getData().length);
		case 5:
			if (cmsg.isRtr()) {
				return "Remote Transmission Request";
			}
			String str = "";
			byte[] arrayOfByte = cmsg.getData();
			for (int i = 0;i < arrayOfByte.length; i++) {
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
		
		throw new UnsupportedOperationException("Not support Operation.");
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		
		this.liseners.add(l);
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
		this.liseners.remove(l);
		
	}
	
	
}
