package ddl;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import gui.AbstractBasicGUI;
import home.HomeController;

public class ConstraintsGUI extends AbstractBasicGUI{

	DDLController control;
	public ConstraintsGUI() {
		super("Création de table",null, 400, 300, 25);
		this.handleComponents();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
		//this.control=control;
	}

	
	private JLabel typeConstraintLabel;
	
	private JComboBox typeConstraintComboBox;
	
	private JComboBox fkTableNameComboBox;
	
	private JLabel attributeLabelOne;
	
	private JLabel attributeLabelTwo;
	
	private JTable namesAttributeSourceTable;
	
	private JTable namesAttributeReferenceTable;
	
	private JButton createButton;
	
	private Object[] types = new Object[]{"UNIQUE", "FOREIGN KEY"};
	String [] header={"attributs"};
	DefaultTableModel model = new DefaultTableModel(header,10);
	
	private void handleComponents(){
		
		this.typeConstraintLabel = new JLabel("Type de contrainte");
		this.bindAndAdd(this.typeConstraintLabel,3,true);
		
		this.typeConstraintComboBox = new JComboBox(types);
		this.bindAndAdd(this.typeConstraintComboBox,3,false);
		
		this.attributeLabelOne = new JLabel("Attributs Source");
		this.bindAndAdd(this.attributeLabelOne,3,true);
		
		this.attributeLabelTwo = new JLabel("Attributs référencé");
		this.bindAndAdd(this.attributeLabelTwo,3,false);
		this.attributeLabelTwo.setBounds(240, 25, 150, 100);
		
		
		this.namesAttributeSourceTable = new JTable(model);
		JScrollPane jj = new JScrollPane(namesAttributeSourceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		namesAttributeSourceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.bindAndAdd(jj,7,true);
		jj.setBounds(20, 90, 100, 100);
		jj.setVisible(true);
		
		this.typeConstraintComboBox = new JComboBox(types);
		this.bindAndAdd(this.typeConstraintComboBox,3,true);
		this.typeConstraintComboBox.setBounds(130, 90, 100, 20);
		
		this.namesAttributeReferenceTable = new JTable(model);
		JScrollPane j =new JScrollPane(namesAttributeReferenceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		namesAttributeReferenceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.bindAndAdd(j,7,false);
		j.setBounds(240, 90, 100, 100);
		j.setVisible(true);
		
		this.createButton = new JButton("Creer la contrainte");
		this.bindAndAdd(this.createButton);
		createButton.setBounds(20, 220, 150, 20);
		
		
		
	}
	
	
	
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
