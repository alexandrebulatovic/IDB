package sql;

import gui.BasicGUI;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.*;

/** 
 * IHM qui permet d'écrire des requêtes {@code SQL} et de les envoyer au serveur manuellement.
 * <P>
 * Elle intéragit également avec l'utilisateur en lui affichant une {@link JTable} ou
 *  un pop-up de la réponse du serveur.
 * <P>
 *  
 *  @see SQLController
 *  */

@SuppressWarnings("serial")
public class SQLView extends BasicGUI
implements ActionListener
{
	/* ATTRIBUTS */

	/** Controleur lié à l'IHM. */
	private SQLController sql_controller;

	/** Zone de texte pour écrire les requêtes. */
	private JTextArea sqlArea;

	/** Bouton pour soumettre la requête. */
	private JButton okButton;

	/* CONSTRUCTEUR */

	/** Met en place les élements affichés et initialise le controller de la vue. */
	public SQLView(SQLController control) 
	{
		super("Menu SQL", null, 400, 350, 20);
		this.sql_controller = control;
		this.handleArea();
		this.handleButtons();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/* METHODES */

	/**Instancie, dimensionne, positionne et associe les
	 * zones de textes. */
	private void handleArea()
	{
		this.sqlArea = new JTextArea();
		this.sqlArea.setText("N'entrez qu'une requete à fois!");
		this.bindAndAdd(this.sqlArea, 200);
	}

	/** Instancie, dimensionne, positionne et associe les
	 * boutons. */
	private void handleButtons()
	{
		this.okButton = new JButton("Envoyer");
		this.okButton.addActionListener(this);
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
		if (!this.isComplete()) {

			this.talk("Syntaxe attendue : ma_requête;");

		} else {

			this.talk("Requete envoyée.");

			String qry = this.sqlArea.getText();
			this.transmitSQL(qry);
		}
	}

	/** Envoie la requête au controller.
	 * @param qry : requête sous forme de chaîne de caractères à envoyer. */
	private void transmitSQL(String qry) 
	{
		this.sql_controller.transmitQuery(qry);
	}

	@Override
	public boolean isComplete() 
	{
		String qry = this.sqlArea.getText();
		return qry.endsWith(";") && qry.length() > 1;
	}

	/** Pop-up pour afficher une table sous la forme d'un objet {@code JTable}.
	 * @param table : {@code JTable} à afficher. */
	public void showTable(final JTable table)
	{
		// cette portion de code sert à rendre la JOptionPane redimensionnable
		table.addHierarchyListener(new HierarchyListener() { 
			public void hierarchyChanged(HierarchyEvent e) {
				Window window = SwingUtilities.getWindowAncestor(table);
				if (window instanceof Dialog) {
					Dialog dialog = (Dialog)window;
					if (!dialog.isResizable()) {
						dialog.setResizable(true);
					}
				}
			}
		});
		JOptionPane.showMessageDialog(null, new JScrollPane(table), "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Pop-up pour afficher un message.
	 * @param str : Message à afficher. */
	public void showReply(String str)
	{
		JOptionPane.showMessageDialog(null, str, "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}
}
