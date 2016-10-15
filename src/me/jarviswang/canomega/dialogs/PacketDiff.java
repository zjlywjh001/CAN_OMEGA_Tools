package me.jarviswang.canomega.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import me.jarviswang.canomega.models.MonitorMessageTableModel;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class PacketDiff extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable monitortable;
	private JPanel buttonPane;
	private JButton cancelButton;
	private JToggleButton tglbtnPause;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PacketDiff dialog = new PacketDiff();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PacketDiff() {
		setAlwaysOnTop(true);
		setTitle("Packet Diff Tool");
		setBounds(100, 100, 766, 540);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("750px"),},
			new RowSpec[] {
				RowSpec.decode("454px"),
				RowSpec.decode("33px"),}));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, "1, 1, fill, fill");
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "1, 1, fill, fill");
			{
				monitortable = new JTable();
				monitortable.setModel(new MonitorMessageTableModel());
				monitortable.getColumnModel().getColumn(0).setPreferredWidth(50);
				monitortable.getColumnModel().getColumn(1).setPreferredWidth(50);
				monitortable.getColumnModel().getColumn(2).setPreferredWidth(40);
				monitortable.getColumnModel().getColumn(3).setPreferredWidth(90);
				monitortable.getColumnModel().getColumn(4).setPreferredWidth(40);
				monitortable.getColumnModel().getColumn(5).setPreferredWidth(370);
				scrollPane.setViewportView(monitortable);
			}
		}
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, "1, 2, fill, top");
			{
				tglbtnPause = new JToggleButton("Pause");
				buttonPane.add(tglbtnPause);
			}
			{
				cancelButton = new JButton("Close");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public JButton getCloseButton() {
		
		return this.cancelButton;
	}
	
	public JTable getmonitorTable() {
		
		return this.monitortable;
	}
	
	public JToggleButton getPauseButton() {
		
		return this.tglbtnPause;
	}

}
