package gui.ddl;

import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;

import controller.DDLController;

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
//		this.bindAndAdd(new JLabel("Modifier le nom : "), 6, true);
		super.handleTableInputs();
	}
		
	@Override
	protected void createTableButtonAction(){}


	@Override
	public void windowActivated(WindowEvent e) {this.fillTablesComboBox();}


	@Override
    public void itemStateChanged(ItemEvent e) 
	{
		super.itemStateChanged(e);
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource() == this.tablesCombo){
				if ("".equals(e.getItem().toString())){
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
		
		ResponseData<String[]> response = 
				this.control.getAttributes(table);
		
		this.tableNameField.setText(table);
		if (response.hasSuccess()) {
			for (String [] attribute : response.getCollection()) {
				this.showExistingAttribute(attribute);
			}
		}
	}
	
	
	private void showExistingAttribute(String [] attribute)
	{
		
	}
}
