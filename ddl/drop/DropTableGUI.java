package ddl.drop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import useful.CustomizedResponse;
import useful.CustomizedResponseWithData;

import ddl.DDLController;
import ddl.Table;

import interf.BasicGUI;

/**
 * IHM pour supprimer des tables de la base de données.
 * Singleton.
 * 
 * @author UGOLINI Romain.
 */
@SuppressWarnings("serial")
public class DropTableGUI 
extends BasicGUI
implements ActionListener
{
	//Instance
	/** Instance singleton en cours.*/
	private static DropTableGUI INSTANCE;
	
	//Attributes
	/** Controleur pour supprimer des tables.*/
	private DDLController control;
	
	/** Liste déroulantes contenant les tables disponibles.*/
	private JComboBox<String> tableComboBox;
	
	/** Case à cocher pour forcer la suppression en cascade.*/
	private JCheckBox cascadeCheckBox;
	
	/** Bouton pour envoyer la requête de suppression.*/
	private JButton okButton;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 */
	private DropTableGUI()
	{
		super("Suppression de table", null, 400, 180, 30);
		INSTANCE = this;
		this.control = DDLController.getInstance();
		this.handleComponents();
		this.fillComboBox();
		this.enableOrDisableComponent();
		this.setProperties(DISPOSE_ON_CLOSE);
	}
	
	
	//Méthodes
	/**
	 * Retourne l'instance en cours si et seulement si elle existe déjà,
	 * retourne une nouvelle instance sinon.
	 * 
	 * @return 
	 */
	public static DropTableGUI getInstance()
	{
		if (INSTANCE == null) new DropTableGUI();
		return INSTANCE;
	}
	
	
	@Override
	public boolean isComplete() {return true;}
	
	
	@Override
	public void windowClosing(WindowEvent arg0) {INSTANCE = null;}
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object ev = e.getSource();
		if (ev == this.okButton) this.okButtonAction();
	}


	//Privées
	/**
	 * Instancie, positionne et dimensionne les différents composants de $this.
	 */
	private void handleComponents()
	{
		//Liste déroulante
		this.tableComboBox = new JComboBox<String>();
		this.bindElement(this.tableComboBox);
		
		//Case à cocher.
		this.cascadeCheckBox = new JCheckBox("supprimer malgré les références");
		this.bindElement(this.cascadeCheckBox);

		//Bouton
		this.okButton = new JButton("Supprimer");
		this.okButton.addActionListener(this);
		this.bindElement(this.okButton);
	}
	
	
	/**
	 * Remplit la liste déroulante avec le nom des 
	 * tables de données disponibles pour l'utilisateur.
	 * Affiche le nombre de tables récupérées.
	 */
	private void fillComboBox()
	{
		String msg;
		CustomizedResponseWithData<String> 
			response = this.control.getTables();
		msg = response.getMessage();
		if (response.hasSuccess()) {
			msg += " : " + response.getCollection().size();
			for (String s : response.getCollection()) {
				this.tableComboBox.addItem(s);
			}
		}
		this.talk(msg);
	}
	
	
	/**
	 * Détermine ce qu'il se passe en appuyant sur le bouton 'ok'.
	 */
	private void okButtonAction()
	{
		if (tableComboBox.getItemCount() != 0) {
			String selection = (String)this.tableComboBox.getSelectedItem();
			Table table = new Table(
					selection, 
					this.cascadeCheckBox.isSelected());
			CustomizedResponse response = this.control.dropTable(table);
			this.talk(response.toString()); 
			if (response.hasSuccess()) {
				this.tableComboBox.removeItem(selection);
				this.enableOrDisableComponent();
			}
		}
	}
	
	
	/**
	 * Active les composants de $this si et seulement s'il
	 * y a des tables à supprimer, les désactive sinon.
	 */
	private void enableOrDisableComponent()
	{
		boolean b = this.tableComboBox.getItemCount() != 0;
		for (JComponent jc : this.components) {
			jc.setEnabled(b);
		}
	}
	
}
