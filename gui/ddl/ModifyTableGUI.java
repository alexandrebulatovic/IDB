package gui.ddl;

import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import controller.DDLController;

import business.Table;
import useful.ResponseData;

@SuppressWarnings("serial")
public class ModifyTableGUI 
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
	public ModifyTableGUI(DDLController control) 
	{
		super(control);
		this.changeComponents();
		this.synchronizeTablesComboBox();
	}
	
	
	//Méthodes
	@Override
	protected void handleTableInputs()
	{
		this.tablesCombo = new JComboBox<String>();
		this.tablesCombo.addItemListener(this);
		this.bindAndAdd(this.tablesCombo, 6, true);
//		this.bindAndAdd(new JLabel("Modifier le nom : "), 6, true);
		super.handleTableInputs();
	}
		
		@Override
	protected void createTableButtonAction(){}


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
	private void synchronizeTablesComboBox()
	{
		this.tablesCombo.removeAllItems();
		ResponseData<String> response = this.control.getTables();
		this.talk(response);
		
		for (String table : response.getCollection()){
			this.tablesCombo.addItem(table);
		}
	}
	

    @Override
	public void windowActivated(WindowEvent e) {this.synchronizeTablesComboBox();}


	@Override
    public void itemStateChanged(ItemEvent e) {
    	super.itemStateChanged(e);
       if (e.getStateChange() == ItemEvent.SELECTED) {
    	   if (e.getSource() == this.tablesCombo){
    		   if (!e.getItem().toString().equals("")){
    				String tableSelected = e.getItem().toString();
//    				this.setValuesView(tableSelected);
    				this.tableNameField.setText(tableSelected);
//    				this.tableSource = this.getTable();
    		   }
    	   }

       }
    }


}
