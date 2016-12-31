package crud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import gui.BasicGUI;
import useful.ResponseData;


public class CRUDView extends BasicGUI implements ActionListener {

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

	/** Sert à activer/désactiver l'event listener en fonction des besoins. */
	private boolean actionListener;

	/** Index de la ligne sélectionnée dans la {@code JTable}. */
	private int index;


	/* CONSTRUCTEUR */

	/** Met en place les élements affichés et initialise le controller de la view. */
	public CRUDView(CRUDController control)
	{
		super("CRUD", null, 900, 550, 30);
		this.crud_controller = control;
		this.disableListener(); // on désactive car sinon fillComboBox() appelle requestTable(...) avec l'event listener
		this.handleComponents();
		this.fillComboBox();

		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
	}


	/* METHODES */

	/** Active l'event listener de la fenêtre. */
	private void enableListener() {
		this.actionListener = true;
	}

	/** Désactive l'event listener de la fenêtre. */
	private void disableListener() {
		this.actionListener = false;
	}

	/** Créé et attribue un {@code MouseListener} à notre {@code JTable}. */
	private void initMouseListener() 
	{
		// obligé d'utiliser la souris et de stocker l'index en attribut car un clic sur un des JButton 
		// déselectionne à chaque fois la ligne de la JTable et du coup getSelectedRow() retourne -1
		// au lieu de retourner l'index de la ligne sélectionnée. */

		this.tableJTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				index = tableJTable.getSelectedRow();
			}
		});
	}


	@Override
	public void actionPerformed(ActionEvent event) {

		if (this.actionListener)
		{
			String selectedTable = (String) this.tableComboBox.getSelectedItem();

			if (selectedTable != null) 
				this.requestTable(selectedTable);


			Object pressedButton = event.getSource();

			/*
				SI BESOIN POUR CONSTRUIRE UN OBJET TABLE : 
				this.tableJTable.getColumnCount() --> nombre de colonnes
				this.tableJTable.getRowCount()  --> nombre de tuples
			 */
			if (pressedButton == this.deleteButton) {

				System.out.println("delete");

			} else if (pressedButton == this.updateButton) {

				System.out.println("update");

			} else if (pressedButton == this.insertButton) {

				System.out.println("insert");

			}

		}
	}


	/** Demande au controller d'afficher la table choisie dans la combobox.
	 * @param tableName : nom de la table demandée. */
	private void requestTable(String tableName) 
	{
		// TODO demander la Jtable au controller puis le controller 
		//		appelle tableDisplay(...) pour mettre a jour la vue
		//		--> les lignes dessous servent juste à tester sans controller, on supprimera a la fin

		/*
		if (tableName.equals("test1")) {
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			Vector<String> columnNames = new Vector<String>(Arrays.asList("colonne1", "colonne2", "colonne3"));


			for (int i = 0; i < 4; i++)
			{
				Vector<Object> vector = new Vector<Object>(Arrays.asList("test1"));
				data.add(vector);
			}


			DefaultTableModel dm = new DefaultTableModel(data, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {return false;} // empêche d'éditer les cellules de la JTable
			};

			this.tableDisplay(new JTable(dm));

		} else if (tableName.equals("test2")) {
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			Vector<String> columnNames = new Vector<String>(Arrays.asList("colonne1", "colonne2", "colonne3","colonne4"));


			for (int i = 0; i < 10; i++)
			{
				Vector<Object> vector = new Vector<Object>(Arrays.asList("test2"));
				data.add(vector);
			}

			DefaultTableModel dm = new DefaultTableModel(data, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {return false;} // empêche d'éditer les cellules de la JTable
			};

			this.tableDisplay(new JTable(dm));
		}
		 */

	}

	@Override
	public boolean isComplete() { // pas encore utilisé
		return tableComboBox.getItemCount() > 0;
	}

	/** Instancie, positionne et dimensionne les différents composants de {@code this} et instancie leur {@code ActionListener} respectif. */
	private void handleComponents()	
	{
		this.tableComboBox = new JComboBox<String>();
		this.bindAndAdd(this.tableComboBox);
		this.tableComboBox.addActionListener(this);

		this.tableScrollPane = new JScrollPane();
		this.bindAndAdd(this.tableScrollPane, 320);
		this.tableScrollPane.setVisible(false); // caché au début quand il n'y a pas de sélection

		this.insertButton = new JButton("Insérer");
		this.bindAndAdd(this.insertButton);
		this.insertButton.addActionListener(this);

		this.updateButton = new JButton("Mettre à jour");
		this.bindAndAdd(this.updateButton);
		this.updateButton.addActionListener(this);

		this.deleteButton = new JButton("Supprimer");
		this.bindAndAdd(this.deleteButton);
		this.deleteButton.addActionListener(this);
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

		/* 
		// commentez au-dessus et decommentez en dessous pour tester -->

		this.tableComboBox.addItem("test1");
		this.tableComboBox.addItem("test2");
		 */

		this.tableComboBox.setSelectedItem(null); // aucune sélection par défaut pour combobox
		this.enableListener();
	}


	/** Affiche une table dans la fenêtre courante.
	 * @param table : table au format {@code JTable} à afficher. */
	private void tableDisplay(JTable table)
	{
		this.initJTable(table);
		this.initMouseListener();
		this.displayJTable();
	}


	/** Initialise et paramètre la {@code JTable} servant à l'affichage. 
	 * @param table : {@code JTable} correspondant au choix de la combobox. */
	private void initJTable(JTable table) 
	{
		this.tableJTable = table;
		this.tableJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // une seule ligne sélectionnée autorisée

	}

	/** Initialise le {@code JScrollpane} et le rend visible ce qui rend l'affichage de la table actif. */
	private void displayJTable() 
	{
		this.tableScrollPane.setViewportView(this.tableJTable); // on met la JTable dans le JScrollPane
		this.tableScrollPane.setVisible(true); // on rend visible la JScrollPane qui n'est désormais plus vide
	}



}
