package relation;

import java.util.Vector;
import operation.RelationOperation;
import java.io.FileOutputStream;

public class Relation {
    // Attributs ---------------
    String nom;
    Vector <String> colonnes;
    Vector <Vector <String>> donnees;
    RelationOperation calculeur = new RelationOperation(this);

    // Encapsulation --------------
    public void setNom(String nom){this.nom = nom;}
    public String getNom(){return this.nom;}
    public void setColonnes(Vector <String> colonnes){this.colonnes = colonnes;}
    public Vector <String> getColonnes(){return this.colonnes;}
    public void setDonnees(Vector <Vector <String>> donnees){this.donnees = donnees;}
    public Vector <Vector <String>> getDonnees(){return this.donnees;}
    public RelationOperation getCalculeur(){return this.calculeur;}

    // Constructeur -------------
     public Relation(String nom, Vector <String> colonnes) {
        this.setNom(nom);
        this.setColonnes(colonnes);
        this.setDonnees(new Vector<Vector<String>>());
    }

    public Relation(String nom, Vector <String> colonnes, Vector <Vector <String>> donnees) {
        this.setNom(nom);
        this.setColonnes(colonnes);
        this.setDonnees(donnees);
    }

    public void insertIntoTable(Vector<String> newLine) {       // Enregistre dans le fichier le nouveau ligne
        try {
            FileOutputStream fichier = new FileOutputStream("./Fichier/" + getNom() + ".tab", true);
            String ligne = "/";
            for(int i = 0; i <  newLine.size(); i++) {
                ligne += newLine.elementAt(i);
                if(i < newLine.size() - 1) ligne += "-";
            }
            byte[] format = ligne.getBytes();
            fichier.write(format);
            fichier.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public String prepareContenue() {      // Prepare l'enchainement des donnees
        String entete = getNom();
        String colonnes = "";
        for(int i = 0; i < getColonnes().size(); i++) {
            if(i == 0) colonnes += "/";
            colonnes += getColonnes().elementAt(i);
            if(i < getColonnes().size() - 1) colonnes += "-";
        }
        String donnees = "";
        Vector<Vector<String>> tableData = getDonnees();
        for(int i = 0; i < tableData.size(); i++) {
            for(int a = 0; a < tableData.elementAt(i).size(); a++) {
                if(a == 0) donnees += "/";
                donnees += tableData.elementAt(i).elementAt(a);
                if(a < tableData.elementAt(i).size() - 1) donnees += "-";
            }
        } 
        return entete + colonnes + donnees;
    }

    public void saveIntoFile() {       // enregistre tous les donnees d'une table dans un fichier
        try {
            FileOutputStream fichier = new FileOutputStream("./Fichier/" + getNom() + ".tab", true);
            String contenue = prepareContenue();
            byte[] format = contenue.getBytes();
            fichier.write(format);
            fichier.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }


    // Inserer dans une relation
    public void insert(Vector<String> newLine) throws Exception {
        if(getColonnes().size() != newLine.size()) throw new Exception("Le nombre de colonne de votre donnees ne correspond pas a cette table");
        getDonnees().add(newLine);
        System.out.println("Insertion ....");
        insertIntoTable(newLine);
    }

    // Avoir les elements dans une colonne
    public Vector<String> getColonnesElements(int indice){
        indice--;
        if(indice > colonnes.size()) return null;
        Vector<String> resultat = new Vector<String>();
        for(int i = 0; i < donnees.size(); i++) {
            resultat.add(donnees.elementAt(i).elementAt(indice));
        }
        return resultat;
    }

    // Afficher relations
    public String colonneAlignement(String text) {
        int colLong = 15;       // longueur en caractere du colonne
        String espace = "";
        for(int i = text.length(); i < colLong; i++) {
            espace += " ";
        }
        return espace;
    }

    public void afficherRelation() {
        System.out.println("Detail du relation : " + getNom());
        System.out.println("---------------");

        // Affichage Colonne
        for(int i = 0; i < colonnes.size(); i++) {
            System.out.print(" | " + colonnes.elementAt(i) + colonneAlignement(colonnes.elementAt(i)) + " | ");
        }
        System.out.println("");

        
        // Affichage Donnees
        for(int i = 0; i < donnees.size(); i++) {
            for(int a = 0; a < donnees.elementAt(i).size(); a++) {
                System.out.print(" | " + donnees.elementAt(i).elementAt(a) + colonneAlignement(donnees.elementAt(i).elementAt(a)) + " | ");
            }
            System.out.println(""); 
        }
    }
}