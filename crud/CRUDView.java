package crud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import gui.BasicGUI;
import useful.ResponseData;

/** IHM qui permet les opérations {@code Create|Read|Update|Select} sur une base de données
 * en manipulant un objet graphique {@code JTable}.*/
@SuppressWarnings("serial")
public class CRUDView extends BasicGUI implements ActionListener {

	/* CONSTANTES */ 

	public static final int PREVIOUS = 0;

	public static final int LAST = 1;

	public static final int CURRENT = 2;


	/* ATTRIBUTS */

	/** Controller lié à cette view. */
	private CRUDController crud_controller;

	/** Liste déroulante contenant les tables disponibles. */
	private JComboBox<String> tableComboBox;

	/** Bouton pour déclencher l'action de suppression des données.*/
	private JButton deleteButton;

	/** Bouton pour déclencher l'action de mettre à jour des données.*/
	private JButton updateButton;

	/** Bouton pour déclencher l'action d'insérer des données.*/
	private JButton insertButton;

	/** {@code JScrollpane} contenant la la {@code JTable} et permettant de scroller si la taille dépasse. */
	private JScrollPane tableScrollPane;

	/** {@code JTable} correspondant à la table sélectionnée dans la combobox. */
	private JTable tableJTable;

	/** Un état pour activer/désactiver l'event listener de la {@code JComboBox} si nécessaire. */
	private boolean comboBoxListener;

	/** Index de la dernière ligne sélectionnée dans la {@code JTable}. */
	private int currentIndex;

	/** Modèle de données pour la {@code JTable}. */
	private DefaultTableModel tableModel;

	/** Un état pour savoir si une insertion de tuple est en cours. */
	private boolean INSERTING;

	/** Un état pour savoir si la {@code CRUDView} autorise la modification des tuples ou non */
	private boolean ALLOW_EDITS;


	/* CONSTRUCTEUR */

	/** Met en place les élements affichés et initialise le controller de la view. */
	public CRUDView(CRUDController control)
	{
		super("CRUD", null, 900, 550, 30);
		this.crud_controller = control;
		this.handleComponents();
		this.disableComboBoxListener(); // on désactive avant de peupler la combobox pour ne pas déclencher l'event listener
		this.fillComboBox();
		this.enableComboBoxListener();
		this.INSERTING = false;
		this.ALLOW_EDITS = false;

		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
	}


	/* METHODES */

	@Override
	public void actionPerformed(ActionEvent event) {

		Object pressedButton = event.getSource();

		if (pressedButton == this.deleteButton)
			deleteButtonAction();

		else if (pressedButton == this.updateButton)
			updateButtonAction();

		else if (pressedButton == this.insertButton)
			insertButtonAction();
	}

	private void insertButtonAction() 
	{
		if (!this.INSERTING)
		{
			this.INSERTING = true;

			int columnCount = this.tableModel.getColumnCount();

			this.tableModel.addRow(new Vector[columnCount]);
			this.changeSelection(CRUDView.LAST);
		} 
		else
		{ // on ajoute à la base de données le dernier tuple de la table, c-à-d celui qui vient d'être ajouté
			int new_row_index = this.tableModel.getRowCount()-1;
			String table_name = (String)this.tableComboBox.getSelectedItem();

			String reply = this.crud_controller.addRow(new_row_index, table_name);

			if (!reply.equals("OK"))
				this.showError(reply);

			this.INSERTING = false;								
		}
	}

	private void updateButtonAction() {
		if (this.updateButton.isEnabled()) {

			if (!this.ALLOW_EDITS)
			{
				this.insertButton.setEnabled(false);
				this.deleteButton.setEnabled(false);
				this.ALLOW_EDITS = true;
				this.updateButton.setText("Mettre à jour : "+ this.updateButtonState());
			}
			else
			{
				this.insertButton.setEnabled(true);
				this.deleteButton.setEnabled(true);
				this.ALLOW_EDITS = false;
				this.updateButton.setText("Mettre à jour : "+ this.updateButtonState());
			}
		}

	}

	/** @return "{@code ACTIVE}" si le mode modification est activé, "{@code DESACTIVE}" sinon. */
	private String updateButtonState()
	{
		String state;

		if (this.ALLOW_EDITS)
			state = "ACTIVE";
		else
			state = "DESACTIVE";

		return state;
	}

	private void deleteButtonAction() 
	{
		// on essaie de l'effacer de la base de données
		String reply = this.crud_controller.deleteRow(this.currentIndex);

		if (reply.equals("OK"))
		{
			tableModel.removeRow(this.currentIndex); // si ça marche on le vire de l'affichage
			this.changeSelection(CRUDView.PREVIOUS); // puis on replace la selection
		} else
			showError(reply); // sinon on affiche simplement message d'erreur
	}

	/** Crée un pop-up du message d'erreur.
	 * @param errorMessage : message d'erreur à afficher. */
	private void showError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, "	Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/** Permet de replacer manuellement la nouvelle ligne sélectionnée de 
	 * la {@code JTable} car par défaut elle revient au début. 
	 * @param newSelection : constante correspondant à la nouvelle sélection, au choix
	 * {@code PREVIOUS}, {@code CURRENT} ou {@code LAST}. */
	private void changeSelection(int newSelection) 
	{
		if (this.tableJTable.getRowCount() > 1 && this.currentIndex > 0)
		{
			int newPosition = 0;

			switch (newSelection)
			{
			case CRUDView.PREVIOUS: 
				newPosition = this.currentIndex-1; // on se repositionne sur le tuple précédent
				break;
			case CRUDView.LAST: 
				newPosition = this.tableModel.getRowCount()-1; // on se repositionne sur le dernier tuple
				break;
			case CRUDView.CURRENT: 
				newPosition = this.currentIndex; // on se repositionne sur le même tuple
				break;
			}

			this.tableJTable.changeSelection(newPosition, 0, false, false);
		}
	}

	/** Demande au controller d'afficher la table choisie dans la combobox.
	 * @param tableName : nom de la table demandée. */
	private void requestTable(String tableName) 
	{
		JTable table = this.crud_controller.requestTable(tableName);

		this.tableDisplay(table);
	}

	/** Instancie, positionne et dimensionne les différents composants de l'IHM et 
	 * instancie leur {@code ActionListener} respectif. */
	private void handleComponents()	
	{
		this.tableComboBox = new JComboBox<String>();
		this.bindAndAdd(this.tableComboBox);
		this.tableComboBox.addActionListener(new ActionListener(){ 
			// choix volontaire par rapport à un ItemListener pour permettre d'actualiser la JTable
			// car ItemListener ne déclenche pas d'action si on re-sélectionne la même table dans la combobox

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (CRUDView.this.comboBoxListener)
				{
					String selectedTable = (String) CRUDView.this.tableComboBox.getSelectedItem();

					if (selectedTable != null) 
					{
						CRUDView.this.requestTable(selectedTable);
						CRUDView.this.revealJButtons();
					}
				}
			}
		});

		this.tableScrollPane = new JScrollPane();
		this.bindAndAdd(this.tableScrollPane, 320);
		this.tableScrollPane.setVisible(false); // caché au début quand il n'y a pas de sélection

		this.insertButton = new JButton("Insérer");
		this.bindAndAdd(this.insertButton);
		this.insertButton.addActionListener(this);


		this.updateButton = new JButton("Mettre à jour : "+ this.updateButtonState());
		this.bindAndAdd(this.updateButton);
		this.updateButton.addActionListener(this);


		this.deleteButton = new JButton("Supprimer");
		this.bindAndAdd(this.deleteButton);
		this.deleteButton.addActionListener(this);


		this.hideJButtons();
	}

	/** Rend inutilisable les boutons de l'IHM. */
	private void hideJButtons() 
	{
		this.insertButton.setEnabled(false);
		this.updateButton.setEnabled(false);
		this.deleteButton.setEnabled(false);
	}

	/** Rend utilisable les boutons de l'IHM. */
	private void revealJButtons() 
	{
		this.insertButton.setEnabled(true);
		// TODO : changer quand la modification sera fonctionnelle
		//		this.updateButton.setEnabled(true); 
		this.deleteButton.setEnabled(true);
	}

	/** Remplit la liste déroulante avec le nom des tables de la 
	 * base de données et affiche le nombre de tables récupérées. */
	private void fillComboBox()	
	{
		ResponseData<String> response = this.crud_controller.getTables();
		String msg = response.getMessage();

		if (response.hasSuccess()) 
		{
			msg += " : " + response.getCollection().size();

			for (String s : response.getCollection()) {
				this.tableComboBox.addItem(s);
			}
		}
		this.talk(msg);

		this.tableComboBox.setSelectedItem(null); // aucune sélection par défaut pour combobox
	}

	/** Affiche une table dans l'IHM.
	 * @param table : {@code JTable} à afficher. */
	private void tableDisplay(JTable table)
	{
		this.initJTable(table);
		this.displayJTable();
	}

	/** Active l'event listener de la {@code JComboBox}. */
	private void enableComboBoxListener() {
		this.comboBoxListener = true;
	}

	/** Désactive l'event listener de la {@code JComboBox}. */
	private void disableComboBoxListener() {
		this.comboBoxListener = false;
	}

	/** Crée et assigne un {@code ListSelectionListener} à notre {@code JTable} ce qui 
	 *  permet de mettre à jour dynamiquement l'attribut {@code this.currentIndex} en fonction du tuple sélectionné. */
	private void initSelectionListener() 
	{
		//		obligé de stocker l'index en attribut car un clic sur un des JButton 
		//		déselectionne à chaque fois la ligne de la JTable ce qui fait que getSelectedRow() retourne -1
		//		au lieu de retourner l'index de la ligne sélectionnée.

		ListSelectionModel selection_model = this.tableJTable.getSelectionModel();

		selection_model.addListSelectionListener(new ListSelectionListener()
		{

			public void valueChanged(ListSelectionEvent e) 
			{
				if (!e.getValueIsAdjusting()) // pour ne garder que le dernier évenèment d'une action qui en déclenche plusieurs
					CRUDView.this.currentIndex = CRUDView.this.tableJTable.getSelectedRow();
			}
		});
	}


	/** Crée et assigne un {@code TableModelListener} à notre modèle de données {@code DefaultTableModel} ce qui
	 * permet de capter la cellule où les modifications ont été effectuées et de mettre à jour 
	 * la base de données dynamiquement en conséquence. */
	private void initTableModelListener() {
		this.tableModel.addTableModelListener(new TableModelListener() 
		{

			public void tableChanged(TableModelEvent event) 
			{
				if (CRUDView.this.ALLOW_EDITS) // si le mode "modification" est actif
				{

					int eventType = event.getType();

					if (eventType == TableModelEvent.UPDATE)
					{
						int row = event.getFirstRow();  
						int column = event.getColumn();  
						Object value = tableModel.getValueAt(row, column);

						String reply = CRUDView.this.crud_controller.updateRow(row, column, value);

						if (!reply.equals("OK"))
							CRUDView.this.showError(reply);
					}

				}
			}
		});
	}

	/** Initialise et paramètre la {@code JTable} et son modèle de données {@code DefaultTableModel}. 
	 * @param table : {@code JTable} correspondant au choix de la combobox. */
	private void initJTable(JTable table) 
	{
		this.tableJTable = table;
		this.tableModel = (DefaultTableModel)this.tableJTable.getModel();
		this.tableJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // une seule ligne sélectionnée autorisée
		this.initSelectionListener();
		this.initTableModelListener();
	}

	/** Initialise et affiche la {@code JScrollpane} composé de la {@code JTable}. */
	private void displayJTable() 
	{
		this.tableScrollPane.setViewportView(this.tableJTable); // on met la JTable dans le JScrollPane
		this.tableScrollPane.setVisible(true); // on rend visible la JScrollPane qui n'est désormais plus vide
	}

	@Override
	public boolean isComplete() {
		return this.tableComboBox.getItemCount() > 0;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}