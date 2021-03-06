package business;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Table {
	
	//Attributs
	/** Nom de la table.*/
	private String name;
	
	/** Un ensemble d'attributs (pas de doublons).*/
	private LinkedHashSet<Attribute> attributes;
	
	private List<Constraint> constraints;
	
	

	//Constructeurs
	/**
	 * Constructeur vide.
	 * Instancie un ensemble vide.
	 */
	protected Table()
	{
		this.name = "";
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
	
	
	/**
	 * @deprecated il est préférable d'ajouter les contraintes 1 à 1 pour les contrôles
	 * @param constraints
	 */
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

		for (Constraint c : attributeToDrop.getConstraints()){
			c.dropAttribute(attributeToDrop);
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
		
		String sql = this.tableNameToCreateSQL() + "\n(\n";
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
	public List<String> toModify(Table newTable) {
		ArrayList<String> results = new ArrayList<String>();
		
		addAttributes(newTable, results);
		
		dropAttributes(newTable, results);
		
		modifyAttributes(newTable, results);
		
		modifyConstraints(newTable, results);
		
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
	@SuppressWarnings("unused")
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
			System.out.println("to modify :"+attribute[0]+"<========>"+attribute[1]);
			Attribute attDest = attribute[0];

			//Attribute attsrc = attribute[1];
			results.add(attDest.toModify(this.name));
		}
		
	}



	private void modifyConstraints(Table tableSource, ArrayList<String> results) {
		for (Constraint c : this.constraints){
			results.add(c.toDropConstraintSQL());
		}
		
		for (Constraint c : tableSource.getConstraints()){
			results.add(c.toAddConstraintSQL());
		}		
		
		
		

	}


	/**
	 * Ajoute les requettes SQL des tables à supprimer
	 * @param tableSource
	 * @param results
	 */
	private void dropAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute attribute : attributesToDrop(tableSource)){
			results.add(attribute.toDROPSQL(this.name));
		}
	}


	/**
	 * Ajoute les requettes SQL des tables à ajouter
	 * @param tableSource
	 * @param results
	 */
	private void addAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute attribute : attributesToAdd(tableSource)){
			results.add(attribute.toADDSQL(this.name));
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
					if (!attributeSrc.toString().equals(attributeDest.toString())){// on cherche les memes attributs mais avec des valeurs différentes
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
	 * Retounre une liste contenant les contraintes portant le meme nom mais différentes<br>
	 * [0] ConstraintDest<br>
	 * [1] ConstraintSrc
	 * @param tableSource
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Constraint[]> constraintsToChange(Table tableSource) {
		ArrayList<Constraint[]> constraintToModify = new ArrayList<Constraint[]>();
		
		
		for(Constraint constraintSrc : tableSource.getConstraints()){
			
			for (Constraint constraintDest : this.constraints){
				System.out.println();
				System.out.println();
				System.out.println(constraintSrc.toAddConstraintSQL("rien")+"\n°°°°°°°°°°°°°°\n"+constraintDest.toAddConstraintSQL("rien"));
				System.out.println();
				System.out.println();
				if (constraintDest.equalsName(constraintSrc)){
					
					if (!constraintDest.toAddConstraintSQL("rien").equals(constraintSrc.toAddConstraintSQL("rien"))){
						constraintToModify.add(new Constraint[] {
								constraintSrc,
								constraintDest
						});
						
					}
				}
			}
		}
		
		return constraintToModify;
		
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
	@SuppressWarnings("unused")
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
	private String tableNameToCreateSQL()
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



	public boolean addConstraint(Constraint c) {
		if (!this.containsConstraint(c)){
			return this.constraints.add(c);
		}
		return false;
		
	}

	/**
	 * 
	 * @param a constraint
	 * @return boolean
	 */
	private boolean containsConstraint(Constraint c) {
		for (Constraint constraint : this.constraints){
			if (constraint.equals(c)){
				return true;
			}
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
			constraintsDeleted = a.cleanAll();
			
			this.constraints.removeAll(constraintsDeleted);
			constraintsDeleted.clear();
		}
		this.attributes.clear();		
		if (this.constraints.size() != 0){
			return false;
		}
		return true;
	}



	public List<UniqueConstraint> getUniques() {
		
		ArrayList<UniqueConstraint> uniques = new ArrayList<UniqueConstraint>();
		for (Constraint c : this.constraints){
			if (c instanceof UniqueConstraint){
				uniques.add((UniqueConstraint) c);
			}
		}
		return uniques;
	}
	
	
	public List<ForeignKeyConstraint> getFks() {
		
		ArrayList<ForeignKeyConstraint> fks = new ArrayList<ForeignKeyConstraint>();
		for (Constraint c : this.constraints){
			if (c instanceof ForeignKeyConstraint){
				fks.add((ForeignKeyConstraint) c);
			}
		}
		return fks;
	}



	public void setName(String name) {
		this.name = name;
		for (Attribute a : this.attributes){
			a.setTableName(name);
		}
	}


	/**
	 * Supprime la contrainte
	 * @param c
	 * @return 
	 */
	public boolean dropConstraint(Constraint c) {
		c.cleanAll();

		for (Attribute att : c.getAttributes()){
			att.getConstraints().remove(c);
		}

		return this.constraints.remove(c);
		
	}



	public PrimaryKeyConstraint getPk() {
		for (Constraint c : this.constraints){
			if (c instanceof PrimaryKeyConstraint){
				return (PrimaryKeyConstraint) c;
			}
		}
		return null;
	}






}

