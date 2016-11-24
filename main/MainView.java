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
	public MainView()
	{
		super("Menu principal", null, 400, 200, 40);
		this.handleButtons();
		this.addComponents();
		this.setProperties();
	}
	
	
	//Méthodes
	/**
	 * {@inheritDoc}
	 */
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
		this.mhiButton = new JButton("Mode graphique");
		this.mhiButton.setActionCommand("graphic_mode");
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
		//TODO : Ecrire une action dans le controleur.
	}
	
	
	/**
	 * Gère les actions sur l'appui du bouton 'Graphique'
	 */
	private void ihmButtonAction()
	{
		//TODO : Ecrire une action dans le controleur.
	}
}
