package gui.ddl;

import gui.ddl.tools.I_AttributeModel;

import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import controller.DDLController;
import useful.Response;
import useful.ResponseData;

@SuppressWarnings("serial")
public class AlterTableGUI 
extends CreateTableGUI 
{
	//Attributs
	/** Liste déroulante contenant les tables engistrées en base.*/
	private JComboBox<String> tablesCombo;
	
	
	//Controleurs
	/**
	 * Constructeur commun.
	 * 
	 * @param control : null interdit.
	 */
	public AlterTableGUI(DDLController control) 
	{
		super(control);
		this.changeComponents();
		//Evènement de focus met à jour la liste déroulante.
	}
	
	
	//Méthodes
	@Override
	protected void handleTableInputs()
	{
		this.tablesCombo = new JComboBox<String>();
		this.tablesCombo.addItemListener(this);
		this.bindAndAdd(this.tablesCombo, 6, true);
		super.handleTableInputs();
	}
		
	
	@Override
	protected void createTableButtonAction()
	{
		
//		this.control.alterTable();
		//TODO : ne verras jamais le jour
//		this.createTable("TEMPORARY");
		String oldTable = this.tablesCombo.getSelectedItem().toString();
		String newTable = this.tableNameField.getText();
		
		
		List<Object[]> attributes = new ArrayList<Object[]>();
		
		for (int i=0 ; i<this.table.getRowCount() ; i++){
			attributes.add(new Object[]{
					this.table.getValueAt(i, 0),
					this.table.getValueAt(i, 1),
					this.table.getValueAt(i, 2),
					this.table.getValueAt(i, 3),
					this.table.getValueAt(i, 4)
			});
			
			
		}
		
		Response r = this.control.alterTable(oldTable, newTable,attributes);
		this.talk(r);
	}


	@Override
	protected void resetView()
	{
		super.resetView();
		if (this.tablesCombo.getItemCount() > 0) {
			String table = this.tablesCombo.getItemAt(0).toString();
			this.tablesCombo.setSelectedItem(table);
			this.fillGuiWithAttribute(table);
		}
	}


	@Override
	public void windowActivated(WindowEvent e) {this.fillTablesComboBox();}


	@Override
    public void itemStateChanged(ItemEvent e) 
	{
		super.itemStateChanged(e);
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource() == this.tablesCombo){
				if (! "".equals(e.getItem().toString())){
					String table = e.getItem().toString();
					this.fillGuiWithAttribute(table);
				}
			}

		}
	}


	/**
	 * Modifie quelques composants de la superIHM pour coller au 
	 * mieux au principe de modification.
	 */
	private void changeComponents() 
	{
		this.setTitle("Modifier une table");
		this.createTableButton.setText("Modifier");
	}


	/**
	 * Met à jour la liste déroulante contenant le noms des tables
	 * enregistrées en base.
	 */
	private void fillTablesComboBox()
	{
		this.tablesCombo.removeAllItems();
		ResponseData<String> response = this.control.getTables();
		this.talk(response);
		
		for (String table : response.getCollection()){
			this.tablesCombo.addItem(table);
		}
	}


	//TODO : nom de merde
	private void fillGuiWithAttribute(String table)
	{
		ResponseData<String[]> attributesData = 
				this.control.getAttributes(table);
		this.tableNameField.setText(table);
		this.models.removeAll();
		for (String [] att : attributesData.getCollection()) {
			this.showExistingAttribute(att);
		}
		this.talk("");
	}
	
	
	private void showExistingAttribute(String [] att)
	{
		int size = ("".equals(att[2]) || att[2] == null 
				? 1 
				: Integer.parseInt(att[2]));
		boolean nul = "NOTNULL".equals(att[3]);
		boolean primary = "PRIMARY".equals(att[4]);
		
		I_AttributeModel iam = this.control.getAttributeModel
				(att[0], att[1], size, nul, primary);
		this.addAttributeToTable(iam);
	}
}
