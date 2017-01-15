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
	
	
	private String tableSelected;
	

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
		
		this.comboChoiceTable = new JComboBox();
		 
		this.comboChoiceTable.setBounds(coords);

		this.setComboBoxChoixValues();
		
		
		this.add(this.comboChoiceTable);
		
		comboChoiceTable.addItemListener(this);

	}
	
	private void setComboBoxChoixValues(){
		this.comboChoiceTable.removeAllItems();
//		this.comboChoiceTable.addItem("");
		
		
		ResponseData<String> tables = this.control.getTables();
		this.talk(tables.getMessage());
		
		for (String item : tables.getCollection()){
			this.comboChoiceTable.addItem(item);
		}
	}


	/**
	 * initialise toutes les valeurs
	 * selon la table entrée en paramètre dans le comboBox
	 */
	private void setValuesView(String table) {
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
	
	
	/**
	 * Resélectionn le bon Item et recalcule les valeurs du comboBoxChoice
	 */
	private void acualiseComboBoxChoice() {
		this.tableSelected = this.comboChoiceTable.getSelectedItem().toString();
		this.setComboBoxChoixValues();
		this.comboChoiceTable.setSelectedItem(this.tableSelected);
	}
	

	
    @Override
	public void windowActivated(WindowEvent e)
	{
		acualiseComboBoxChoice();
	}


	@Override
    public void itemStateChanged(ItemEvent e) {
    	super.itemStateChanged(e);
       if (e.getStateChange() == ItemEvent.SELECTED) {
    	   if (e.getSource() == this.comboChoiceTable){
    		   if (!e.getItem().toString().equals("")){
    				String tableSelected = e.getItem().toString();
    				this.setValuesView(tableSelected);
    				this.tableNameField.setText(tableSelected);
    				this.tableSource = this.getTable();
    		   }
    	   }

       }
    }


}
