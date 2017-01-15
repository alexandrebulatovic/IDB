package ddl;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import business.Attribute;
import business.Table;
import useful.ResponseData;

public class ModifyTableGUI extends CreateTableGUI {
	
	private JComboBox<String> comboChoiceTable;
	
	private boolean bug = false;
	
	private Table tableSource;
	

	public ModifyTableGUI(DDLController control) {
		super(control);
		this.setTitle("Modifier une table");
		this.changeComponents();
		
		
	}

	
	/**
	 * Cette méthode va changer quelques composants de
	 * la classe hérité pour créer la vue
	 * de modifications
	 */
	private void changeComponents() {
//		this.tableNameField.setVisible(false);
		
		
		initComboBoxChoice();
		
		Rectangle r = tableNameField.getBounds();
		this.tableNameField.setBounds(r.x+r.width,r.y,r.width,r.height);
		this.createTableButton.setText("Modifier");
	}

	
	/**
	 * Initialise la nouvelle comboBox
	 * Possédant la liste des tables
	 */
	private void initComboBoxChoice() 
	{

		Rectangle coords = new Rectangle(this.tableNameField.getBounds());
		
		ResponseData<String> tables = this.control.getTables();
		this.talk(tables.getMessage());

		this.comboChoiceTable = new JComboBox(tables.getCollection().toArray());
		 
		this.comboChoiceTable.setBounds(coords);

		this.add(this.comboChoiceTable);
		
		comboChoiceTable.addItemListener(this);

	}


	/**
	 * Cette méthode va initialiser toutes les valeurs TODO : on sait que c'est une méthode
	 * selon la table entrée en Paramètre dans le comboBox //TODO : Paramètre n'est pas un Dieu :p
	 */
	private void setValues(String table) {
		this.setViewModify(this.control.getAttributes(table), table);
		
	}
	
	
	public void setViewModify(List<Attribute> attributes, String tableName) {	
		this.resetView();
		for (Attribute a : attributes){
			this.models.addAttribute(a);
		}
	}
	
	
	@Override
	protected void createTableButtonAction(){
		this.control.modifyTable(this.getTable(),this.tableSource);
	}
	
	
	@Override
	public void windowActivated(WindowEvent e)
	{
		acualiseComboBox();
	}

	
	private void acualiseComboBox() {
		int index = this.comboChoiceTable.getSelectedIndex();
		this.initComboBoxChoice();
		try{
			this.comboChoiceTable.setSelectedIndex(index);
		}
		catch(IllegalArgumentException e){}
	}
	

	
    @Override
    public void itemStateChanged(ItemEvent e) {
    	super.itemStateChanged(e);
       if (e.getStateChange() == ItemEvent.SELECTED) {
			String tableSelected = e.getItem().toString();
			this.setValues(tableSelected);
			this.tableNameField.setText(tableSelected);
			this.tableSource = this.getTable();
       }
    }


}
