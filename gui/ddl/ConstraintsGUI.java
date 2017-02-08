package gui.ddl;

import gui.abstrct.AbstractBasicGUI;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controller.DDLController;
import controller.HomeController;


public class ConstraintsGUI extends AbstractBasicGUI{

	DDLController control;
	public ConstraintsGUI() {
		super("Création de table",null, 600, 600, 25);
		this.handleComponents();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
		this.enableFkComponents(false);
		//this.control=control;
	}

	private JScrollPane j;

	private JScrollPane jj;
	private JLabel typeConstraintLabel;

	private JLabel listConstraintLabel;
	
	private JComboBox typeConstraintComboBox;

	private  JComboBox tableNameComboBox;

	private  JComboBox fkTableNameComboBox;

	private JLabel attributeLabelOne;

	private JLabel attributeLabelTwo;

	private JTable namesAttributeSourceTable;

	private JTable namesAttributeReferenceTable;

	private JButton createButton;

	private Object[] types = new Object[]{"UNIQUE", "FOREIGN KEY"};
	String [] header={"attributs"};
	DefaultTableModel model = new DefaultTableModel(header,10);

	private JLabel tableLabel;

	private JComponent fkTableLabel;

	private JTable constraintsTable;

	private JScrollPane jjj;

	String [] header2={"Nom de la contrainte","Type","Table cible","Attributs","Table référencée","Attributs"};
	private TableModel model2 = new DefaultTableModel(header2,10);

	private JButton deleteButton;

	private void handleComponents(){

		this.typeConstraintLabel = new JLabel("Type de contrainte");
		this.bindAndAdd(this.typeConstraintLabel,3,true);

		this.typeConstraintComboBox = new JComboBox(types);
		this.typeConstraintComboBox.addActionListener(this);
		this.bindAndAdd(this.typeConstraintComboBox,3,false);


		this.tableLabel = new JLabel("Table : ");
		this.bindAndAdd(this.tableLabel,8,true);

		this.tableNameComboBox = new JComboBox(types);
		this.bindAndAdd(this.tableNameComboBox,6,true);

		this.increaseLeft(120);
		this.fkTableLabel = new JLabel("Table référencée : ");
		this.bindAndAdd(this.fkTableLabel,5,true);

		this.fkTableNameComboBox = new JComboBox(types);
		this.bindAndAdd(this.fkTableNameComboBox,6,false);

		this.namesAttributeSourceTable = new JTable(model);
		this.jj = new JScrollPane(namesAttributeSourceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		namesAttributeSourceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		namesAttributeSourceTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		this.bindAndAdd(jj,3,true);
		jj.setSize(new Dimension(220,100));
		jj.setVisible(true);

		this.increaseLeft(100);


		this.namesAttributeReferenceTable = new JTable(model);
		this.j =new JScrollPane(namesAttributeReferenceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		namesAttributeReferenceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		namesAttributeReferenceTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		this.bindAndAdd(j,3,false);
		j.setSize(new Dimension(220,100));
		j.setVisible(true);
		
		this.increaseTop(90);
		
		this.createButton = new JButton("Ajouter la contrainte");
		this.bindAndAdd(this.createButton);

		this.increaseTop(15);
		
		this.bindAndAdd(new JSeparator(SwingConstants.HORIZONTAL));
		
		this.tableLabel = new JLabel("Liste des contraintes sur la table source : ");
		this.bindAndAdd(this.tableLabel);

		this.constraintsTable = new JTable(this.model2);
		this.jjj = new JScrollPane(constraintsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		constraintsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		constraintsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		constraintsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		this.bindAndAdd(jjj,150);
		jj.setVisible(true);
		
		this.deleteButton = new JButton("Supprimer la contrainte");
		this.bindAndAdd(this.deleteButton);
		
		
	}




	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == this.typeConstraintComboBox) {
			this.selectTypeConstraintComboBoxAction();
		}

	}




	private void selectTypeConstraintComboBoxAction() {
		Object selected = this.typeConstraintComboBox.getSelectedItem();
		enableFkComponents("FOREIGN KEY".equals(selected.toString()));

	}




	private void enableFkComponents(boolean b) {
		this.fkTableLabel.setVisible(b);
		this.fkTableNameComboBox.setVisible(b);
		this.j.setVisible(b);
		this.namesAttributeReferenceTable.setVisible(b);
	}

}
