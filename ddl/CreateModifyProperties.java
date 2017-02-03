package ddl;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import gui.BasicGUI;
import useful.ResponseData;

@SuppressWarnings("serial")
public class CreateModifyProperties extends BasicGUI{
	
	protected CreateModifyProperties() {
		super("Création de table",null, 900, 550, 20);
	}

	protected static final String FONT = null;

	/** Controleur lié à l'IHM. */
	protected DDLController control;

	/** Préfixe des messages d'erreurs. */
	protected static final String ERROR_ATTRIBUTE = "ERREUR : ";

	/** Préfixe des messages de succès. */
	protected static final String SUCCES_ATTRIBUTE = "SUCCES : ";

	/** Model de la Table pour gérer les lignes/colonnes. */
	protected AttributesAbstractTableModel  models;

	/** Tableau contenant les attributs. */
	protected JTable table;

	/** ScrollPane du Tableau. */
	protected JScrollPane scrollPane;
	
	/** Etat de la modification */
	protected boolean updateState;

	
	// ==========================FIELDS========================
	/** Boite de saisie du nom de la table. */
	protected JTextField tableNameField;

	/** Boite de saisie du nom de l'attribut. */
	protected JTextField attributeNameField;

	/** Boite de saisie de la taille de l'attribut. */
	protected JTextField attributeSizeField;

	
	// ==========================LABELS========================
	/** Etiquette pour le nom de la Table. */
	protected JLabel tableNameLabel;

	/** Etiquette pour le titre Table. */
	protected JLabel tableLabel;

	/** Etiquette pour le titre Attribut. */
	protected JLabel attributeLabel;


	// ==========================BUTTONS========================
	/** Bouton 'Reset'. */
	protected JButton resetButton;

	/** Bouton "UP". */
	protected JButton upPositionAttributeButton;

	/** Bouton "DOWN". */
	protected JButton downPositionAttributeButton;
	
	/** Bouton 'Ajouter l'attribut'. */
	protected JButton attributeButton;

	/** Bouton 'Créer la table'. */
	protected JButton createTableButton;

	/** Bouton 'Modifier attribut'. */
	protected JButton updateAttributeButton;

	/** Bouton 'Supprimer attribut'. */
	protected JButton deleteAttributeButton;

	/** Bouton "Modifier". */
	protected JButton confirmUpdateAttributeButton;

	/** Bouton "Annuler la modification". */
	protected JButton cancelUpdateAttributeButton;


	// ==========================PANELS========================
	/** Panel Contenant le tableau. */
	protected JPanel panelAttributes;


	// ==========================COMBOBOX========================
	/** ComboBox du choix du type de l'attribut. */
	protected JComboBox<String> attributeTypeComboBox;

	// ==========================CHECKBOXS========================
	/** Case à cocher pour la contrainte NotNull */
	protected JCheckBox notNullCheckBox;

	/** Case à cocher pour la contrainte PrimaryKey */
	protected JCheckBox primaryKeyCheckBox;


	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
