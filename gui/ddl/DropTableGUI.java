package gui.ddl;

import gui.abstrct.AbstractBasicGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.WindowConstants;

import controller.DDLController;

import useful.Response;
import useful.ResponseData;



/**
 * IHM pour supprimer des tables de la base de données.
 * 
 * @author UGOLINI Romain.
 */
@SuppressWarnings("serial")
public class DropTableGUI 
extends AbstractBasicGUI
implements ActionListener
{
	//Attributes
	/** Controleur pour supprimer des tables.*/
	private DDLController control;
	
	/** Liste déroulantes contenant les tables disponibles.*/
	private JComboBox<String> tableComboBox;
	
	/** Case à cocher pour forcer la suppression malgré les références.*/
	private JCheckBox cascadeCheckBox;
	
	/** Case à cocher pour forcer la suppression en chaine.*/
	private JCheckBox dominoCheckBox;
	
	/** Bouton pour envoyer la requête de suppression.*/
	private JButton okButton;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 */
	public DropTableGUI(DDLController control)
	{
		super("Suppression de table", null, 400, 250, 30);
		this.control = control;
		this.handleComponents();
		this.enableOrDisableComponent();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
	}


	//Méthodes
	@Override
	public boolean isComplete() 
	{
		return tableComboBox.getItemCount() != 0;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object ev = e.getSource();
		if (ev == this.okButton) this.okButtonAction();
	}


	@Override
	public void windowActivated(WindowEvent e)
	{
		this.refreshView();
	}
	
	
	//Privées
	/**
	 * Instancie, positionne et dimensionne les différents composants de $this.
	 */
	private void handleComponents()
	{
		//Liste déroulante
		this.tableComboBox = new JComboBox<String>();
		this.bindAndAdd(this.tableComboBox);

		//Case à cocher
		this.cascadeCheckBox = new JCheckBox
				("Supprimer malgré les références.");
		this.bindAndAdd(this.cascadeCheckBox);


		this.dominoCheckBox = new JCheckBox("Réaction en chaîne.");
		this.bindAndAdd(this.dominoCheckBox);
		
		//Bouton
		this.okButton = new JButton("Supprimer");
		this.okButton.addActionListener(this);
		this.bindAndAdd(this.okButton);
	}
	
	
	/**
	 * Remplit la liste déroulante avec le nom des 
	 * tables de données disponibles pour l'utilisateur.
	 * Affiche le nombre de tables récupérées.
	 */
	private void fillComboBox()
	{
		String msg;
		ResponseData<String> 
			response = this.control.getTables();
		msg = response.getMessage();
		this.tableComboBox.removeAllItems();
		if (response.hasSuccess()) {
			msg += " : " + response.getCollection().size();
			for (String s : response.getCollection()) {
				this.tableComboBox.addItem(s);
			}
		}
		this.talk(msg);
	}
	
	
	/**
	 * Détermine ce qu'il se passe en appuyant sur le bouton 'Supprimer'.
	 */
	private void okButtonAction()
	{
		if (this.isComplete()) {
			String table = (String)this.tableComboBox.getSelectedItem();
			boolean domino = this.dominoCheckBox.isSelected();
			Response response;
			
			if (domino) {
				response = this.control.dropTableDomino(table);
			}
			else {
				boolean cascade = this.cascadeCheckBox != null 
						? this.cascadeCheckBox.isSelected() 
						: false;
				response = this.control.dropTable(table, cascade);
			}
			
			if (response.hasSuccess()) this.refreshView();
			this.talk(response); 
		}
	}

	
	/**
	 * Active les composants de $this si et seulement s'il
	 * y a des tables à supprimer, les désactive sinon.
	 */
	private void enableOrDisableComponent()
	{
		boolean b = this.isComplete();
		enableOrDisable(b);
	}

	
	/**
	 * Active les composants de $this si et seulement si b est vrai,
	 * les désactive sinon.
	 * 
	 * @param b vrai pour activer tous les composants, faux pour les désactiver.
	 */
	private void enableOrDisable(boolean b) {
		for (JComponent jc : this.components) {
			jc.setEnabled(b);
		}
	}
	
	
	/**
	 * Rafraichit l'IHM.
	 */
	private void refreshView()
	{
		this.fillComboBox();
		this.enableOrDisableComponent();
	}
}
