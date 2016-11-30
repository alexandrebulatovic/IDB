package sql;

import interf.BasicView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class SQLView 
extends BasicView
implements ActionListener
{
	//Attributs
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
		this.handleArea();
		this.handleButtons();
		this.setProperties();
	}
	
	
	//Public
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event) 
	{
		if (event.getSource() == this.okButton) {
			this.okButtonAction();
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
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
		this.bindElements(this.sqlArea, 200);
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
		this.bindElements(this.okButton, 40);
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
			this.talk("Envoi de la requête...");
		}
	}


	public JTextArea getSqlArea() {
		return sqlArea;
	}
}
