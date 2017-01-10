package home;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;

import gui.BasicGUI;

/**
 * IHM du menu principal, une fois la connexion établie.
 * 
 * @author UGOLINI Romain
 */
@SuppressWarnings("serial")
public class HomeGUI 
extends BasicGUI
implements ActionListener
{
	//Attributes
	/** Controleur principal de l'application.*/
	private HomeController control;

	/** Bouton pour se rendre vers l'IHM de code SQL.*/
	private JButton sqlButton;

	/** Bouton pour ouvrir l'IHM de création des tables.*/
	private JButton createButton;

	/** Bouton pour ouvrir l'IHM de modification des tables.*/
	private JButton alterButton;

	/** Bouton pour ouvrir l'IHM de suppression des tables.*/
	private JButton dropButton;

	/** Bouton pour ouvrir l'IHM du CRUD.*/
	private JButton crudButton;


	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public HomeGUI(HomeController control)
	{
		super("Menu principal", null, 400, 400, 30);
		this.control = control;
		this.talk("Bienvenue " + this.control.getUser());
		this.handleButtons();
		this.setProperties(EXIT_ON_CLOSE);
	}


	//Méthodes
	@Override
	public boolean isComplete() {return true;}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if (o == this.sqlButton) this.sqlButtonAction();
		else 
			if (o == this.createButton) this.createButtonAction();
			else 
				if (o == this.dropButton) this.dropButtonAction();
				else
					if (o == this.alterButton) this.alterButtonAction();
					else
						if (o == this.crudButton) this.crudButtonAction();

	}


	@Override
	public void windowClosing (WindowEvent e)
	{
		this.control.disconnect();
	}


	//Privates
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les boutons.
	 */
	private void handleButtons()
	{
		this.createButtons();
		this.bindButtons();
	}


	/**
	 * Instancie les boutons.
	 */
	private void createButtons()
	{
		this.sqlButton = new JButton("SQL");
		this.sqlButton.setActionCommand("sql_mode");

		this.createButton = new JButton("LDD : créer tables");
		this.createButton.setActionCommand("ldd_create_table");

		this.alterButton = new JButton("LDD : modifier tables");
		this.alterButton.setActionCommand("ldd_alter_table");

		this.dropButton = new JButton("LDD : supprimer tables");
		this.dropButton.setActionCommand("ldd_drop_table");

		this.crudButton = new JButton("CRUD");
		this.crudButton.setActionCommand("crud");

		for (JComponent jc : this.components) {
			if (jc.getClass().getName().endsWith("JButton")) {
				((JButton)jc).addActionListener(this);
			}
		}
	}


	/**
	 * Positionne et dimensionne les boutons.
	 */
	private void bindButtons()
	{
		this.bindAndAdd(this.sqlButton);
		this.bindAndAdd(this.createButton);
		this.bindAndAdd(this.alterButton);
		this.bindAndAdd(this.dropButton);
		this.bindAndAdd(this.crudButton);
		for (JComponent jc : this.components) {
			if (jc.getClass().getName().endsWith("JButton")) {
				((JButton)jc).addActionListener(this);
			}
		}
	}


	/**
	 * Gère les actions sur l'appui du bouton 'SQL'
	 */
	private void sqlButtonAction(){
		this.control.openSQLGUI();
	}


	/**
	 * Gère les actions sur l'appui du bouton 'Créer des tables'.
	 */
	private void createButtonAction(){
		this.control.openCreateGUI();
	}


	/**
	 * Gère les actions sur l'appui du bouton 'Supprimer des tables'.
	 */
	private void dropButtonAction(){
		this.control.openDropGUI();
	}

	/**
	 * Gère l'action du bouton 'LDD : modifier tables'.
	 */
	private void alterButtonAction() 
	{
	}

	/**
	 * Gère l'action du bouton 'CRUD'.
	 */
	private void crudButtonAction() 
	{
		this.control.openCRUDGUI();
	}
}
