package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import controller.SQLController;
import gui.abstrct.AbstractBasicGUI;

/**
 * Un objet servant d'IHM pour pouvoir écrire des requêtes SQL et les envoyer à 
 * un serveur de base de données.
 * <P>
 * Le résultat est automatiquement affiché à l'utilisateur sous la forme d'une boîte
 * de dialogue.
 */
@SuppressWarnings("serial")
public class SQLGUI 
extends AbstractBasicGUI implements ActionListener
{
	/* ATTRIBUTS */

	/** Contrôleur lié à l'objet IHM. */
	private SQLController sqlController;

	/** Zone de texte pour écrire les requêtes SQL. */
	private JTextArea sqlArea;

	/** Bouton pour soumettre la requête. */
	private JButton okButton;


	/* --------------------------------------------------------------------------------- */

	/* CONSTRUCTEUR */

	/**
	 * Construit un nouvel objet {@code SQLGUI}.
	 * @param controller : SQLController à lier à cette IHM.
	 */
	public SQLGUI(SQLController controller) 
	{
		super("Menu SQL", null, 400, 350, 20);
		this.sqlController = controller;
		this.handleArea();
		this.handleButtons();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/* --------------------------------------------------------------------------------- */

	/* METHODES */

	/** Instancie, dimensionne et positionne la zone de texte. */
	private void handleArea()
	{
		this.sqlArea = new JTextArea();
		this.sqlArea.setText("N'entrez qu'une requête à fois!");
		this.bindAndAdd(this.sqlArea, 200);
	}

	/** Instancie, dimensionne et positionne le bouton "Envoyer". */
	private void handleButtons()
	{
		this.okButton = new JButton("Envoyer");
		this.okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SQLGUI.this.okButtonAction();
			}
		});
		this.bindAndAdd(this.okButton, 40);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == this.okButton) {
			this.okButtonAction();
		}
	}

	/** Exécute l'action voulue par un clic sur le bouton "Envoyer". */
	private void okButtonAction()
	{
		if (!this.isComplete())
			this.talk("Syntaxe attendue : ma_requête;");

		else 
		{
			this.talk("Requête envoyée.");

			String query = this.sqlArea.getText();
			this.transmitSQL(query);
		}
	}

	/**
	 * Demande au {@code SQLController} l'exécution d'une requête SQL.
	 * @param query : requête SQL à exécuter.
	 */
	private void transmitSQL(String query) 
	{
		this.sqlController.sendQuery(query);
	}

	@Override
	public boolean isComplete() 
	{
		String qry = this.sqlArea.getText();
		return qry.endsWith(";") && qry.length() > 1;
	}
}