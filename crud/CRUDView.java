package crud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

	/** {@code JScrollpane} contenant la table sélectionnée. */
	private JScrollPane tableScrollPane;

	/** Sert à activer/désactiver l'event listener en fonction des besoins. */
	private boolean actionListener;


	/* CONSTRUCTEUR */

	/** Met en place les élements affichés et initialise le controller de la view. */
	public CRUDView(CRUDController control)
	{
		super("CRUD", null, 900, 550, 30);
		this.crud_controller = control;
		this.disableListener(); // on désactive car sinon fillComboBox() déclenche l'event listener
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

	@Override
	public void actionPerformed(ActionEvent event) {

		if (this.actionListener)
		{
			String tableName = (String) this.tableComboBox.getSelectedItem();

			if (tableName != null) 
				this.requestTable(tableName);
		}
	}


	/** Demande au controller d'afficher la table choisie dans la combobox.
	 * @param tableName : nom de la table demandée. */
	private void requestTable(String tableName) 
	{
		// TODO demander la Jtable au controller puis le controller 
		//		appelle tableDisplay(JTable correspondante...) pour mettre a jour la vue
		//		--> les lignes dessous servent juste à tester sans controller


		/* décommentez pour tester -->
		 * 
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>(Arrays.asList("colonne1", "colonne2", "colonne3"));


		for (int i = 1; i< 5; i++)
		{
			Vector<Object> vector = new Vector<Object>(Arrays.asList("field"+i, "field"+i, "field"+i));
			data.add(vector);
		}

		JTable test_table = new JTable(new DefaultTableModel(data, columnNames));

		this.tableDisplay(test_table);
		 */
	}

	@Override
	public boolean isComplete() { // pas d'utilité pour l'instant
		return tableComboBox.getItemCount() > 0;
	}

	/** Instancie, positionne et dimensionne les différents composants de {@code this}. */
	private void handleComponents()	
	{
		this.tableComboBox = new JComboBox<String>();
		this.bindAndAdd(this.tableComboBox);

		this.tableComboBox.addActionListener(this);

		this.tableScrollPane = new JScrollPane();
		this.bindAndAdd(this.tableScrollPane);
		this.tableScrollPane.setVisible(false); // caché au début quand il n'y a pas de sélection
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
		 * decommentez pour tester -->

		this.tableComboBox.addItem("test1");
		this.tableComboBox.addItem("test2");
		 */
		this.tableComboBox.setSelectedItem(null); // aucune sélection par défaut
		this.enableListener();
	}


	/** Affiche une table dans la fenêtre courante.
	 * @param table : table au format {@code JTable} à afficher. */
	private void tableDisplay(JTable table)
	{
		this.tableScrollPane.setViewportView(table); // on attribue la JTable au JScrollPane
		this.tableScrollPane.setVisible(true); // on rend visible la JScrollPane qui n'est désormais plus vide
	}

}
