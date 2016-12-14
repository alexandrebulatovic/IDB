package ddl.modify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import manager.connection.ConnectionManager;
import manager.ddl.DDLManager;

import ddl.create.CreateTableGUI;
import ddl.drop.DropTableGUI;

import ddl.DDLController;

public class ModifyTableView
extends JFrame 
implements ActionListener, ItemListener
{
	static final int width=190;
	static final int height=100*2;
	static final int componentHeight=30;
	static final int componentWidth=170;
	static final int componentMarge=5;
	private static final ModifyTableView INSTANCE = null;
	
    private JButton buttonConfirm;
    private JLabel label1;
	private JComboBox<String> comboTables;

	private DDLController controller;

	
	ModifyTableView(){
		super("modifier table vue");

		this.controller = DDLController.getInstance();
		setProperties();
		this.handleCombos();
		this.handleButtons();
		this.handleLabels();
				
	}



	private void handleCombos() {
		this.createCombos();
		this.bindCombos();
		this.initCombos();
		this.addCombos();
		
	}



	private void initCombos() {
		String[] valeurs = null;
		DDLManager manager = DDLManager.getInstance();
		valeurs = manager.getTablesString();
		if (valeurs == null){
			this.comboTables.setEnabled(false);
			valeurs = new String[]{"pas de tables"};
		}
		
		
		this.comboTables.setModel(new javax.swing.DefaultComboBoxModel<>(valeurs));
		//TODO faire une requette pour mettre les bonnes vaeurs;
	}



	private void addCombos() {
		this.add(this.comboTables);		
	}



	private void bindCombos() {
		this.comboTables.setBounds(componentMarge,componentHeight+componentMarge,componentWidth,componentHeight);
	}



	private void createCombos() {
		this.comboTables = new JComboBox<String>();
	}



	private void handleButtons() {
		this.createButtons();
		this.bindButtons();
		this.initButtons();
		this.addButton();
		
	}

	private void initButtons() {
		this.buttonConfirm.addActionListener(this);
		
	}



	private void addButton() {
		this.add(buttonConfirm);
		
	}

	private void bindButtons() {
		this.buttonConfirm.setBounds(componentMarge,(componentHeight*2)+(componentMarge*2),componentWidth,componentHeight);
		
	}

	private void createButtons() {
		this.buttonConfirm = new JButton("Modifier");
		
	}
	
	private void handleLabels() {
		this.createLabels();
		this.bindLabels();
		this.addLabels();
		
	}

	private void addLabels() {
		this.add(label1);
	}



	private void bindLabels() {
		//this.label1.setBounds(componentMarge, 30, componentWidth, componentHeight);
		this.label1.setBounds(500, 0, 0, 0);
		
	}



	private void createLabels() {
		this.label1 = new JLabel("Choisissez une table � modifier :");
		
	}

	
	private void setProperties()
	{
		this.setSize(width, height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		DDLController controller = DDLController.getInstance();
		this.controller.modifier(this.comboTables.getSelectedItem().toString());
		
	}



	public static ModifyTableView getInstance() {
		if (INSTANCE == null) new ModifyTableView();
		return INSTANCE;
	}
}
