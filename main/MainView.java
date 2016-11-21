package main;

import javax.swing.*;

import interf.IDBFrame;

import java.awt.Component;
import java.awt.event.*;
import java.util.ArrayList;

public class MainView 
extends JFrame 
implements ActionListener, IDBFrame
{
	//Attributes
	/**
	 * Hauteur de l'IHM.
	 */
	private final int height = 300;
	
	/**
	 * Largeur de l'IHM.
	 */
	private final int width = 500;
	
	/**
	 * Incrément pour positionner les éléments.
	 */
	private int topElement;
	
	/**
	 * Marge de l'IHM.
	 */
	private int margin;
	
	/**
	 * Hauteur des éléments.
	 */
	private final int elementHeight = 40;
	
	/**
	 * Largeur des éléments.
	 */
	private int elementWidth;
	
	/**
	 * Contient les boutons de l'IHM.
	 */
	private ArrayList <JButton> buttons;
	
	/**
	 * Bouton pour se rendre vers l'IHM de code SQL.
	 */
	private JButton sqlButton;
	
	/**
	 * Bouton pour se rendre vers l'IHM pour les nuls.
	 */
	private JButton ihmButton;
	
	/**
	 * Contient les étiquettes de l'IHM.
	 */
	private ArrayList<JLabel> labels;
	
	/**
	 * Etiquette destinée au dialogue avec l'utilisateur.
	 */
	private JLabel message;
	
	/**
	 * Etiquette de titre.
	 */
	private JLabel titleLabel;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public MainView()
	{
		super("Menu principal");
		this.topElement = 0;
		this.setLayout(null);
		this.setDimension();
		this.handleLabels();
		this.handleButtons();
		this.setProperties();
	}
	
	
	//Méthodes
	/**
	 * {@inheritDoc}
	 */
	public void talk(String msg)
	{
		this.message.setText(msg);
	}
	
	
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
		else if (e.getSource() == this.ihmButton) {
			this.ihmButtonAction();
		}
	}
	
	
	//Privates
	/**
	 * Dimensionne l'IHM.
	 */
	private void setDimension()
	{
		this.margin = (int) (0.05 * this.width);
		this.elementWidth = (int) (0.9 * this.width);
	}
	
	
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les boutons.
	 */
	private void handleButtons()
	{
		this.createButtons();
		this.bindButtons();
		this.addButtons();
	}
	
	
	/**
	 * Instancie les boutons.
	 */
	private void createButtons()
	{
		this.buttons = new ArrayList <JButton> ();
		buttons.add(this.sqlButton = new JButton("Mode SQL"));
		this.sqlButton.setActionCommand("sql_mode");
		buttons.add(this.ihmButton = new JButton("Mode graphique"));
		this.ihmButton.setActionCommand("graphic_mode");
	}
	
	
	/**
	 * Positionne et dimensionne les boutons.
	 */
	private void bindButtons()
	{
		this.bindElements(this.sqlButton);
		this.bindElements(this.ihmButton);
	}
	
	
	/**
	 * Associe les boutons à l'IHM.
	 */
	private void addButtons()
	{
		for (JButton jb : buttons) {
			this.add(jb);
		}
	}
	
	
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 */
	private void handleLabels()
	{
		this.createLabels();
		this.bindLabels();
		this.addLabels();
	}
	
	
	/**
	 * Instancie les étiquettes.
	 */
	private void createLabels()
	{
		this.labels = new ArrayList <JLabel> ();
		this.labels.add(this.message = new JLabel());
		this.labels.add(this.titleLabel = new JLabel("Bienvenue sur IDB."));
	}
	
	
	/**
	 * Dimensionne les étiquettes.
	 */
	private void bindLabels()
	{
		this.bindElements(this.titleLabel);
		this.bindElements(this.message);

	}
	
	
	/**
	 * Associe les étiquettes à l'IHM.
	 */
	private void addLabels()
	{
		for (JLabel jl : labels) {
			this.add(jl);
		}
	}
	
	
	/**
	 * Gère les actions sur l'appui du bouton 'SQL'
	 */
	private void sqlButtonAction()
	{
		
	}
	
	
	/**
	 * Gère les actions sur l'appui du bouton 'Graphique'
	 */
	private void ihmButtonAction()
	{
		
	}
	
	
	/**
	 * Définit certaines propriétés de l'IHM.
	 */
	private void setProperties()
	{
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}
	
	
	/**
	 * 
	 * @param element : un objet Component
	 */
	private void bindElements(Component element)
	{
		//TODO : Supprimer le +10 car risque d'erreur
		
		element.setBounds(this.margin, this.topElement, this.elementWidth, this.elementHeight);
		this.topElement += this.elementHeight + 10;
	}
}
