package sql;

import interf.BasicGUI;
import interf.IDBGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ddl.DDLController;
import ddl.create.CreateTableGUI;


@SuppressWarnings("serial")
public class SQLView 
extends BasicGUI
implements ActionListener
{
	//Attributs

	/** Instance en cours. */
	private static SQLView INSTANCE;
	
	
	/**
	 * Controleur lié à l'IHM.
	 */
	private SQLController control;

	/**
	 * Zone de texte pour écrire les requêtes.
	 */
	private JTextArea sqlArea;

	/**
	 * Bouton pour soumettre la requête
	 */
	private JButton okButton;

	//Constructeurs
	/**
	 * Constructeur commun.
	 */
	public SQLView()
	{
		super("Menu SQL", null, 400, 350, 20);
		this.control = SQLController.getInstance();
		this.handleArea();
		this.handleButtons();
		this.setProperties(DISPOSE_ON_CLOSE);
	}


	//Public
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		if (event.getSource() == this.okButton) {
			this.okButtonAction();
		}
	}
	
	/**
	 * Retourne l'IHM active si et seulement si elle existe déjà.
	 * Retourne une nouvelle IHM sinon.
	 * 
	 * @return CreateTableView
	 */
	public static SQLView getInstance()
	{
		if (INSTANCE == null) new SQLView();
		return INSTANCE;
	}


	@Override
	public boolean isComplete() 
	{
		String query = this.sqlArea.getText();
		return query.endsWith(";") && query.length() > 1;
	}


	//Privates
	/**
	 * Instancie, dimensionne, positionne et associe les
	 * zones de textes.
	 */
	private void handleArea()
	{
		this.sqlArea = new JTextArea();
		this.sqlArea.setText("N'entrez qu'une requete à fois!");
		this.bindAndAdd(this.sqlArea, 200);
	}

	/**
	 * Instancie, dimensionne, positionne et associe les
	 * boutons.
	 */
	private void handleButtons()
	{
		this.okButton = new JButton("Envoyer");
		this.okButton.setActionCommand("send_sql_query");
		this.okButton.addActionListener(this);
		this.bindAndAdd(this.okButton, 40);
	}


	/**
	 * Exécute l'action voulue par un clic sur le bouton 'ok'.
	 */
	private void okButtonAction()
	{
		if (! this.isComplete()) {
			this.talk("Syntaxe attendue : ma_requête;");
		}
		else{
			this.talk("Requete envoyée.");
			this.control.sendSQL(this.sqlArea.getText().replaceAll(";", "")); // on envoie la requete au controleur sans le ";"
		}
	}
}
