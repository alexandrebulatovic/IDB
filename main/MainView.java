package main;

import javax.swing.*;
import java.awt.event.*;
import interf.BasicView;

@SuppressWarnings("serial")
public class MainView 
extends BasicView
implements ActionListener
{
	//Attributes
	/**
	 * Controleur de l'IHM.
	 */
	private MainController control;

	/**
	 * Bouton pour se rendre vers l'IHM de code SQL.
	 */
	private JButton sqlButton;

	/**
	 * Bouton pour se rendre vers l'IHM pour les nuls.
	 */
	private JButton mhiButton;


	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public MainView(MainController control)
	{
		super("Menu principal", null, 400, 200, 40);
		this.control = control;
		this.handleButtons();
		this.setProperties();
	}


	//Méthodes
    @Override
	public boolean isComplete() {return true;}


	/**
	 * Gestionnaire d'évènements.
	 * 
	 * @param e : un évènement attrapé par l'IHM.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.sqlButton) {
			this.sqlButtonAction();
		}
		else if (e.getSource() == this.mhiButton) {
			this.ihmButtonAction();
		}
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
		this.sqlButton = new JButton("Mode SQL");
		this.sqlButton.setActionCommand("sql_mode");
		this.sqlButton.addActionListener(this);

		this.mhiButton = new JButton("Mode graphique");
		this.mhiButton.setActionCommand("graphic_mode");
		this.mhiButton.addActionListener(this);
	}


	/**
	 * Positionne et dimensionne les boutons.
	 */
	private void bindButtons()
	{
		this.bindElements(this.sqlButton);
		this.bindElements(this.mhiButton);
	}


	/**
	 * Gère les actions sur l'appui du bouton 'SQL'
	 */
	private void sqlButtonAction()
	{
		this.control.openSqlMode();
	}


	/**
	 * Gère les actions sur l'appui du bouton 'Graphique'
	 */
	private void ihmButtonAction()
	{
		//this.control.openMhiMode();
		this.control.openModifyMode();	
	}
}
