package ddl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import useful.ResponseData;

public class ModifyTableGUI extends CreateTableGUI {
	
	private JComboBox<String> comboChoiceTable;
	
	//private int numberOfItemComboChoiceTableTemp = 0;
	

	public ModifyTableGUI(DDLController control) {
		super(control);
		this.setName("Modifier une table");
		this.changeComponents();
		this.setValues();
		
	}

	/**
	 * Cette méthode va changer quelques composants de
	 * la classe hérité pour créer la vue
	 * de modifications
	 */
	private void changeComponents() {
		this.tableNameField.setVisible(false);
		
		
		initComboBoxChoice();
		
		this.createTableButton.setText("Modifier");
	}

	/**
	 * Initialise la nouvelle comboBox
	 * Possédant la liste des tables
	 */
	private void initComboBoxChoice() {
		Rectangle coords = this.tableNameField.getBounds();
		ResponseData<String> tables = this.control.getTables();
		Vector<String> vectTables = new Vector<String>(tables.getCollection());
		DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<String>(vectTables);
		
		if (this.comboChoiceTable == null)
			this.comboChoiceTable = new JComboBox<String>();
		
		this.comboChoiceTable.setModel(boxModel);
		this.comboChoiceTable.setBounds(coords);
		this.add(this.comboChoiceTable);

	}

	


	/**
	 * Cette méthode va initialiser toutes les valeurs 
	 * selon la table entrée en Paramètre dans le comboBox
	 */
	private void setValues() {
		this.resetView();
		//TODO ajouter les valeurs de la table sélectionné
		
	}
	
	@Override
	protected void createTableButtonAction(){
		this.control.modifyTable(this.getTable());
	}
	
	@Override
	public void windowActivated(WindowEvent e)
	{
		acualiseComboBox();
	}

	private void acualiseComboBox() {
		int time = this.comboChoiceTable.getSelectedIndex();
		this.initComboBoxChoice();
		try{
			this.comboChoiceTable.setSelectedIndex(time);
		}
		catch(IllegalArgumentException e){}
	}


}
