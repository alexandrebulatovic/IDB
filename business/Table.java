package business;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import manager.sql.SQLManager;



public class Table {
	
	//Attributs
	/** Nom de la table.*/
	private String name;
	
	/** Un ensemble d'attributs (pas de doublons).*/
	private LinkedHashSet<Attribute> attributes;
	
	private ArrayList<Constraint> constraints;
	
	

	//Constructeurs
	/**
	 * Constructeur vide.
	 * Instancie un ensemble vide.
	 */
	protected Table(){
		this.attributes = new LinkedHashSet<Attribute>();
		this.constraints = new ArrayList<Constraint>();
	}
	
	
	
	/**
	 * Construit une table nommée $name.
	 * Recopie les attributs de $attributes dans $this.
	 * 
	 * @param name : le nom de la table.
	 * @param attributes : un ensemble d'attributs, peut être vide.
	 */
	public Table(String name, LinkedHashSet<Attribute> attributes,ArrayList<Constraint> constraint){
		this();
		this.constraints = constraint;
		//TODO copie défensive
		this.name=name;	
		this.copyAttributes(attributes);
	}

	
	/**
	 * Construit une table nommée $name.
	 * Si $cascade est vrai, la table doit être supprimée avec 
	 * l'option CASCADE CONSTRAINT dans le SGBD.
	 * 
	 * @param name : le nom de la table.
	 */
	public Table(String name)
	{
		this();
		this.name = name;
	}
	
	/**
	 * Constructeur par recopie profonde.
	 * 
	 * @param copy : une table à recopier.
	 */
	public Table(Table copy)
	{
		this();
		this.name = copy.name;
		this.copyAttributes(attributes);
	}
	
	
	//Accesseurs
	public  LinkedHashSet<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public  String getName(){
		return this.name;
	}
	
	public List<Constraint> getConstraints(){
		return this.constraints;
	}
	
	
	public void setConstraints(ArrayList<Constraint> constraints){
		this.constraints = constraints;
	}
	
	public boolean addAttribute(Attribute attributes) {
		if (!this.attributes.contains(attributes)){
			return this.attributes.add(attributes);
		}
		return false;
		
	}



	/**
	 * retourne false en cas d'échec
	 * @param attributeToDrop
	 * @return
	 */
	public boolean dropAttribute(Attribute attributeToDrop){
		boolean noError = true;
		for (Constraint c : attributeToDrop.getConstraints()){
			noError = c.dropAttribute(attributeToDrop);
			if (c.getAttributes().size()==0){
				if (!this.constraints.remove(c)){
					return false;
				}
			}
		}
		return true;
	}



	/**
	 * Retourne une chaîne de caractères qui synthétise $this
	 * en une requète SQL de création de table.
	 * 
	 * @return String
	 */
	public List<String> toCreate(){
		List<String> sqls = new ArrayList<String>();
		
		String sql = this.tableNameToSQL() + "\n(\n";
		int i=0;
		for (Attribute att : attributes){
			if (i!=0){
				sql+=",\n";
			}
			sql += att.toString();
			i++;
		}
		sqls.add(sql+"\n)");
		
		for (Constraint constraint : constraints){			
			sqls.add(constraint.toAddConstraintSQL());
		}
		return sqls;
	}
	
	
	
	
	/**
	 * Retourne les requetes SQL qui modifient
	 * La table avec des ALTER TABLE
	 */
	public List<String> toModify(Table tableSource) {
		ArrayList<String> results = new ArrayList<String>();
		
		addAttributes(tableSource, results);
		
		dropAttributes(tableSource, results);
		
		modifyAttributes(tableSource, results);
		
//		modifyName(tableSource, results);
		

	 
		return results;
	}



	/**
	 * Retourne une chaîne de caractères qui synthétise $this
	 * en une requète SQL de suppression de table.
	 * 
	 * @return String
	 */
	public String toDrop()
	{
		return "DROP TABLE " + this.name;
	}



	//Méthodes
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.name + ":\n");
		for (Attribute a : this.attributes) {
			result.append(a.toString());
			result.append('\n');
		}
		return result.toString();
	}

	
	@Override
	public boolean equals(Object o)
	{
		boolean result = 
		(o == null 
				? false 
				: ((Table)o).name.equals(this.name));
		return result;
	}
	
	
	/**
	 * Recherche un attribut d'après son nom et retourne 
	 * true s'il existe
	 * @param attributeName
	 * @return
	 */
	public boolean containsAttributeName(String attributeName){
		for (Attribute att : this.attributes){
			if (att.name.equals(attributeName)){
				return true;
			}
		}
		return false;
	}


	/**
	 * TODO uniquement valable sur Oracle
	 * @param tableSource
	 * @param results
	 */
	private void modifyName(Table tableSource, ArrayList<String> results) {
		if (!tableSource.name.equals(this.name)){
			results.add("RENAME "+tableSource.name+" TO "+this.name);	
		}
	}



	/**
	 * TODO Disperser ce code dans des méthodes métier ou plus génériques
	 * @param tableSource
	 * @param results
	 */
	private void modifyAttributes(Table tableSource, ArrayList<String> results) {
		
		for (Attribute[] attribute : attributesToChange(tableSource)){
			System.out.println(attribute[0]);
			System.out.println(attribute[1]);
//			Attribute attSrc = attribute[0];
//			Attribute attDest = attribute[1];
//			if (attSrc.foreignKey != attDest.foreignKey){
//				StringBuilder sql = new StringBuilder();
//				sql.append("ALTER TABLE ");
//				sql.append(this.name);
//				sql.append("\n");
//				if (attSrc.foreignKey){
//					sql.append("DROP ");
//					sql.append(attSrc.toSQLConstraintName("fk"));
//
//				}else{
//					sql.append("ADD ");
//					sql.append(attDest.toSQLConstraintName("fk"));
//					sql.append(attDest.toSQLConstraintType(" FOREIGN KEY", ""));
//					sql.append(" ");
//					sql.append(attDest.toSQLReferences());
//				}
//				results.add(sql.toString());
//			}
//			if (attSrc.unique != attDest.unique){
//				StringBuilder sql = new StringBuilder();
//				sql.append("ALTER TABLE ");
//				sql.append(this.name);
//				sql.append("\n");
//				if (attSrc.unique){
//					sql.append("DROP ");
//					sql.append(attSrc.toSQLConstraintName("un"));
//				}
//				else{
//					sql.append("ADD ");
//					sql.append(attSrc.toSQLConstraintName("un"));
//					sql.append(attDest.toSQLConstraintType(" UNIQUE",""));
//					sql.append(" ");
//				}
//				results.add(sql.toString());
//			}
//			if (attSrc.primaryKey != attDest.primaryKey){
//				StringBuilder sql = new StringBuilder();
//				if (this.hasPrimaryKey()){
//					sql.append("ALTER TABLE ");
//					sql.append(this.name);
//					sql.append("\nDROP CONSTRAINT pk_");
//					sql.append(this.name);
//					results.add(sql.toString());
//				}
//				
//				if (this.hasPrimaryKey()){
//					sql = new StringBuilder();
//					sql.append("ALTER TABLE ");
//					sql.append(this.name);
//					sql.append("\nADD ");
//					sql.append(this.primaryKeyToSQL());
//					results.add(sql.toString());
//				}
//
//				
//			}
//			if ((attSrc.size != attDest.size) || (!attSrc.type.equals(attDest.type))){
//				StringBuilder sql = new StringBuilder();
//				//ALTER TABLE this.name
//				//ALTER COLUMN attSrc.name
//				sql.append("ALTER TABLE ");
//				sql.append(this.name);
//				sql.append("\nMODIFY ");
//				sql.append(attSrc.name);
//				sql.append(" ");
//				sql.append(attDest.type);
//				if (!attDest.type.equals("DATE")){
//					sql.append(" (");
//					sql.append(attDest.size);
//					sql.append(")");
//				}
//
//				results.add(sql.toString());
//				
//			}
		}
	}



	/**
	 * Ajoute les requettes SQL des tables à supprimer
	 * @param tableSource
	 * @param results
	 */
	private void dropAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute attribute : attributesToDrop(tableSource)){
			results.add(attribute.toDROPSQL());
		}
	}


	/**
	 * Ajoute les requettes SQL des tables à ajouter
	 * @param tableSource
	 * @param results
	 */
	private void addAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute attribute : attributesToAdd(tableSource)){
			results.add(attribute.toADDSQL());
		}
	}
	
	/**
	 * Retourne une liste d'attributs 
	 * qui doivent etre supprimés
	 * @param tableSource
	 * @return
	 */
	private ArrayList<Attribute> attributesToDrop(Table tableSource) {
		ArrayList<Attribute> attributesToDrop = new ArrayList<Attribute>();
		for (Attribute att : this.getAttributes()){
			if (!estContenu(tableSource.getAttributes(),att)){
				attributesToDrop.add(att);
			}
	
		}
		
		return attributesToDrop;
	}

	/**
	 * Retourne une liste d'attributs
	 * qui sont différents et qui doivent
	 * donc être modifié
	 * 
	 * Le Attribute est composé de attribut source et attribut cible<br>
	 * Attribute[0] ==> attribut à obtenir<br>
	 * Attribute[1] ==> attribut actuel<br>
	 * @param tableSource
	 * @return
	 */
	private ArrayList<Attribute[]> attributesToChange(Table tableSource) {
		ArrayList<Attribute[]> attributesToModify = new ArrayList<Attribute[]>();
		for (Attribute attributeSrc : tableSource.getAttributes()){
			for (Attribute attributeDest : this.attributes){
				if (attributeSrc.name.equals(attributeDest.name)){
					if (!attributeSrc.toString().equals(attributeDest.toString())){// on cherche les memes attributs mais différents
						attributesToModify.add(new Attribute[]{
								attributeSrc,
								attributeDest
						});
					}
				}
			}
		}
		return attributesToModify;
	}


	/**
	 * Retourne une liste d'attributs à ajouter
	 * (attribtus qui n'existent pas dans une tableSource)
	 * @return
	 */
	private ArrayList<Attribute> attributesToAdd(Table tableSource) {
		ArrayList<Attribute> attributesToAdd = new ArrayList<Attribute>();

		for (Attribute att : tableSource.getAttributes()){
			if (!estContenu(this.getAttributes(),att)){
				attributesToAdd.add(att);
			}
	
		}
		
		return attributesToAdd;
	}


	/**
	 * Retourne true si l'attribut est contenu dans 
	 * la liste passé en paramètres
	 * @param ListOfAtributes
	 * @param att
	 * @return boolean
	 */
	private boolean estContenu(LinkedHashSet<Attribute> listAttributes, Attribute att) {
		
		for (Attribute attribute : listAttributes){
			if (attribute.name.equals(att.name)){
				return true;
			}
		}
		
		return false;
	}



	/**
	 * Retourne le nombre d'attributs qui
	 * composent la clef primaire de $this.
	 * 
	 * @return int
	 */
	private int countPrimaryKey()
	{
		int result = 0;
		for (Constraint c : this.constraints) {
			if (c instanceof PrimaryKeyConstraint) result++;
		}
		return result;
	}
	
	
	/**
	 * Retourne vrai si et seulement si $this possède
	 * au moins un attribut de clef primaire.
	 * 
	 * @return boolean
	 */
	private boolean hasPrimaryKey()
	{
		return this.countPrimaryKey() != 0;
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui commence
	 * une requète SQL de création de table.
	 * 
	 * La chaîne retournée s'arrête après le nom de la table.
	 * 
	 * @Exemple CREATE TABLE tableTest
	 * 
	 * @return String
	 */
	private String tableNameToSQL()
	{
		return "CREATE TABLE " + this.name;
	}
	
	

	
	
	/**
	 * Recopie chaque attribut contenu dans $attributes,
	 * et place la copie dans l'ensemble d'attributs de $this.
	 * 
	 * @param attributes : un ensemble d'attributs, peut être vide.
	 */
	private void copyAttributes(LinkedHashSet<Attribute> attributes)
	{
		for (Attribute a : attributes) {
			this.attributes.add(new Attribute(a));
		}
	}



	public boolean addConstraint(PrimaryKeyConstraint pk) {
		if (!this.constraints.contains(pk)){
			return this.constraints.add(pk);
		}
		return false;
		
	}


	/**
	 * Supprime tous les attributes et les contraintes
	 * (ou modifie les contraintes si nécessaires)
	 */
	public boolean cleanAll() {
		ArrayList<Constraint> constraintsDeleted;
		for (Attribute a : attributes){
			constraintsDeleted = a.cleanConstraints();
			
			for (Constraint c : constraintsDeleted){
				System.out.println("\n\ncons : "+this.constraints.get(0)+"\n\n");
				System.out.println("\n\ntoDel : "+c+"\n\n");
				System.out.println("============="+String.valueOf(c==this.constraints.get(0))+"=============");
			}
			
			this.constraints.removeAll(constraintsDeleted);
			constraintsDeleted.clear();
		}
		this.attributes.clear();		
		if (this.constraints.size() != 0){
			return false;
		}
		return true;
	}







}

