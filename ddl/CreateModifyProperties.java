package ddl;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import controller.DDLController;

import gui.abstrct.AbstractBasicGUI;

@SuppressWarnings("serial")
public abstract class CreateModifyProperties 
extends AbstractBasicGUI
{
	//Statique
	/** Police d'écriture de l'IHM.*/
	protected static final String FONT = null;

	//Attributs
	/** Controleur lié à l'IHM. */
	protected DDLController control;

	/** Model de la Table pour gérer les lignes/colonnes. */
	protected AttributesAbstractTableModel  models;
	
	/** Etat de la modification */
	protected boolean updateState;

	
	// ==========================TABLE=========================
	/** Tableau contenant les attributs. */
	protected JTable table;
	
	
	// ==========================FIELDS========================
	/** Boite de saisie du nom de la table. */
	protected JTextField tableNameField;

	/** Boite de saisie du nom de l'attribut. */
	protected JTextField attributeNameField;

	/** Boite de saisie de la taille de l'attribut. */
	protected JTextField attributeSizeField;


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

	protected String [] types;
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	protected CreateModifyProperties() 
	{
		super("Création de table",null, 900, 550, 20);
	}
}
