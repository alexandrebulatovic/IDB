package controller;

import facade.CRUDFacade;
import gui.CRUDGUI;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import useful.ResponseData;

public class CRUDController 
{
	/* ATTRIBUTS */

	/** IHM du CRUD.*/
	private CRUDGUI crud_view;

	/** Facade pour le CRUD des données.*/
	private CRUDFacade facade;

	/**
	 * Constructeur commun.
	 * 
	 * @param facade : null interdit.
	 */
	public CRUDController(CRUDFacade facade)
	{
		this.facade = facade;
	}


	/* METHODES */
	/**
	 * Ouvre l'IHM de CRUD si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openCRUDGUI() {
		if (this.crud_view == null) {
			this.crud_view = new CRUDGUI(this);
		}
		else{
			showGUI(this.crud_view);
		}
	}

	/**
	 * Affiche une fenêtre au premier plan.
	 * 
	 * @param gui : une IHM, null interdit.
	 */
	private static void showGUI(JFrame gui)
	{
		gui.setVisible(true);
		gui.toFront();
	}

	/**
	 * Retourne une réponse personnalisée contenant le nom des tables
	 * de la base, si et seulement si (ces dernières existent et 
	 * il n'y a pas eu d'exceptions).
	 * 
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getTables()
	{
		return this.facade.getTables();
	}

	/**
	 * Demande au {@code SQLManager} un modèle de {@code JTable} avec les données correspondant 
	 * au nom de la table demandée.
	 * @param tableName : nom de la table demandée.
	 * @return un objet {@code DefaultTableModel} contenant les données de la table.
	 */
	public DefaultTableModel requestTable(String tableName) 
	{
		return this.facade.requestTable(tableName);
	}

	/**
	 * Supprime le tuple situé à {@code index} de la base de données.
	 * @param index : position du tuple à supprimer.
	 * @return "OK" si la suppression a réussie, un message d'erreur sinon.
	 */
	public String deleteRow(int index) {
		return (this.facade.deleteRow(index));
	}

	/**
	 * Insère dans la base de données le tuple situé à {@code index} dans la {@code JTable}.
	 * @param index : position du tuple à ajouter. 
	 * @param tableName : nom de la table concernée.
	 * @return "OK" si la modification a réussie, un message d'erreur sinon.
	 */
	public String addRow(int index, String tableName) 
	{
		Vector dataVector = this.crud_view.getTableModel().getDataVector();
		Vector<Object> row_to_add =  (Vector<Object>) dataVector.elementAt(index); // on récupere la ligne concernée

		return (this.facade.addTuple(row_to_add));
	}

	/** Met à jour une valeur d'un tuple dans la base de données.
	 * @param index : position du tuple.
	 * @param column : colonne de la valeur.
	 * @param updateBuffer : nouvelle valeur.
	 * @return "OK" si la modification a réussie, un message d'erreur sinon.
	 */
	public String updateRow(int index, int column, Object updateBuffer)
	{
		return this.facade.updateRow(index, column, updateBuffer);
	}
}