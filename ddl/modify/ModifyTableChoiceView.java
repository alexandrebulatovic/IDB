package ddl.modify;

import java.awt.Color;
import java.awt.Graphics2D;
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

import ddl.create.CreateTableView;
import ddl.drop.DropTableGUI;
import useful.ConnectionManager;
import ddl.DDLController;
import ddl.DDLManager;

public class ModifyTableChoiceView
extends JFrame 
implements ActionListener, ItemListener
{
	private final int width=280;
	private final int height=200;
	private int elementHeight=30;
	private int elementWidth;
	private final int marge=15;
	private static ModifyTableChoiceView INSTANCE = null;
	
    private JButton buttonConfirm;
    private JLabel label1;
	private JComboBox<String> comboTables;

	private DDLController controller;

	
	ModifyTableChoiceView(){
		super("modifier table vue");
		this.INSTANCE = this;	
		this.elementWidth=width-(2*marge);

		this.controller = DDLController.getInstance();

		
		setProperties();
		
		
		this.handleCombos();
		this.handleButtons();
		this.handleLabels();
		
		JLabel lab = new JLabel("rien");
		lab.setEnabled(false);
		lab.setVisible(false);		
		this.add(lab);
				
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
		this.comboTables.setBounds(marge,elementHeight+marge,elementWidth,elementHeight);
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
		this.buttonConfirm.setBounds(marge,(elementHeight*2)+(marge*2),elementWidth,elementHeight);
		
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
		this.label1.setBounds(marge, marge, elementWidth, elementHeight);
		//this.label1.setBounds(500, 0, 0, 0);
		
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



	public static ModifyTableChoiceView getInstance() {
		if (INSTANCE == null) ModifyTableChoiceView.INSTANCE = new ModifyTableChoiceView();
		return INSTANCE;
	}

}
