package gui.ddl;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import controller.DDLController;
import gui.abstrct.AbstractBasicGUI;
import useful.ResponseData;

@SuppressWarnings("serial")
public class QbeGUI extends AbstractBasicGUI{

	private int lastColumnUse = 0;

	private JLabel tableNameLabel;

	private JLabel attributeNameLabel;

	private JComboBox<String> tableNameComboBox;

	private JComboBox<String> attributeNameComboBox;

	private JButton addAttributeButton;

	private JButton execRequestButton;

	private JTable requestTable;

	private DefaultTableModel model;

	private DDLController control;

	private JButton resetButton;


	public QbeGUI(DDLController ddlController) {
		super("Requètes", null, 600, 400, 25);
		this.control = ddlController;
		this.handleComponents();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
		this.fillTableComboBox();
		this.fillAttributeComboBox();
	}

	private void handleComponents(){
		this.tableNameLabel = new JLabel("Table :");
		this.bindAndAdd(tableNameLabel, 10, true);

		this.tableNameComboBox = new JComboBox<String>();
		this.tableNameComboBox.addActionListener(this);
		this.bindAndAdd(tableNameComboBox, 5, true);

		this.attributeNameLabel = new JLabel("Attribut :");
		this.bindAndAdd(attributeNameLabel, 9, true);

		this.attributeNameComboBox = new JComboBox<String>();
		this.bindAndAdd(attributeNameComboBox, 5, true);

		this.addAttributeButton = new JButton("Ajouter attribut");
		this.addAttributeButton.addActionListener(this);
		this.bindAndAdd(addAttributeButton, 3, false);

		this.increaseTop(20);

		this.bindAndAdd(new JSeparator(SwingConstants.HORIZONTAL));

		this.model = new DefaultTableModel(20,20);

		this.requestTable = new JTable(model){

			public TableCellRenderer getCellRenderer(int row, int column) {
				if(getValueAt(row, column) instanceof Boolean) {
					return super.getDefaultRenderer(Boolean.class);
				} else {
					return super.getCellRenderer(row, column);
				}
			}
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				if(getValueAt(row, column) instanceof Boolean) {
					return super.getDefaultEditor(Boolean.class);
				} else {
					return super.getCellEditor(row, column);
				}
			}

			public boolean isCellEditable(int row, int col) {
				if(col <1 || col > lastColumnUse || row == 0 || row == 1){
					return false;
				}else{
					return true;
				}
			}
		};
		int i;
		for(i = 1 ; i<this.model.getRowCount();i++){
			this.requestTable.getColumnModel().getColumn(i).setPreferredWidth(100);
		}
		this.requestTable.setTableHeader(null);
		this.model.setValueAt("Table", 0, 0);
		this.model.setValueAt("Attribut", 1, 0);
		this.model.setValueAt("Selectionné", 2, 0);
		this.model.setValueAt("WHERE", 3, 0);
		this.model.setValueAt("OR", 4, 0);
		this.requestTable.getColumnModel().getColumn(0).setCellRenderer(new MyCellRenderer());
		requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane requestScrollPane = new JScrollPane(requestTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.bindAndAdd(requestScrollPane,200);
		requestScrollPane.setVisible(true);

		this.increaseTop(5);

		this.execRequestButton = new JButton("Executer la requête");
		this.bindAndAdd(execRequestButton,3,true);
		this.increaseLeft(225);
		this.resetButton = new JButton("Reset");
		this.resetButton.addActionListener(this);
		this.bindAndAdd(resetButton,5,false);
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == this.tableNameComboBox)
			this.fillAttributeComboBox();
		if(o == this.addAttributeButton)
			this.addAttributeButtonAction();
		if(o == this.resetButton)
			this.resetButtonAction();


	}
	/**
	 * Remet à 0 la vue des requètes.
	 */
	private void resetButtonAction() {
		int i;
		for(i=1; i<lastColumnUse+1; i++){
			this.model.setValueAt("", 0, i);
			this.model.setValueAt("", 1, i);
			this.model.setValueAt("", 2, i);
			this.model.fireTableCellUpdated(0, i);
			this.model.fireTableCellUpdated(1, i);
			this.model.fireTableCellUpdated(2, i);
		}
		this.lastColumnUse=0;

	}
	/**
	 *Ajoute un attribut à la table des requètes.
	 */
	private void addAttributeButtonAction() {
		this.lastColumnUse++;
		this.model.setValueAt(this.tableNameComboBox.getSelectedItem().toString(), 0, this.lastColumnUse);
		this.model.setValueAt(this.attributeNameComboBox.getSelectedItem().toString(), 1, this.lastColumnUse);
		this.model.setValueAt(true, 2, this.lastColumnUse);
		this.model.fireTableCellUpdated(0, lastColumnUse);
		this.model.fireTableCellUpdated(1, lastColumnUse);
		this.model.fireTableCellUpdated(2, lastColumnUse);

	}

	/**
	 * Remplit la liste déroulante avec le nom des 
	 * attributs de la table.
	 */
	private void fillAttributeComboBox()
	{
		ResponseData<String[]> 
		response = this.control.getAttributes(this.tableNameComboBox.getSelectedItem().toString());
		this.attributeNameComboBox.removeAllItems();
		if (response.hasSuccess()) {
			this.attributeNameComboBox.addItem("*");
			for (String[] s : response.getCollection()) {
				this.attributeNameComboBox.addItem(s[0]);
			}
		}
	}

	/**
	 * Remplit la liste déroulante avec le nom des 
	 * tables de données disponibles pour l'utilisateur.
	 * Affiche le nombre de tables récupérées.
	 */
	private void fillTableComboBox()
	{
		String msg;
		ResponseData<String> 
		response = this.control.getTables();
		msg = response.getMessage();
		this.tableNameComboBox.removeAllItems();
		if (response.hasSuccess()) {
			msg += " : " + response.getCollection().size();
			for (String s : response.getCollection()) {
				this.tableNameComboBox.addItem(s);
			}
		}
		this.talk(msg);
	}

	public class MyCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent (table,  value, isSelected, hasFocus, row, column);
			c.setBackground(new Color(210,210,210));
			return c;
		}
	}
}
