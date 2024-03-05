package stockage;

import relation.*;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;

public class Stockage {
/// Attributs
    Vector<Relation> tableList; // listes de tous les tables

/// Encapsulation
    public void setTableList(Vector<Relation> tableList) {this.tableList = tableList;}
    public Vector<Relation> getTableList() {return this.tableList;}

/// Constructeur
    public Stockage() {
        this.setTableList(getAllRelationExisted());
        System.out.println("LISTES DES TABLES QUI EXISTENT : ");
        for(int i = 0; i < getTableList().size(); i++) {
            System.out.println("--> " + getTableList().elementAt(i).getNom());
        }
    }

/// Fonctions du classes
    // changer les succession en vecteur string
    public Vector<String> codeToVector(String encodage) {
        String[] division = encodage.split("-");
        Vector<String> resultat = new Vector<String>();
        for(int i = 0; i < division.length; i++) {
            resultat.add(division[i]);
        }
        return resultat;
    }

    // Creer relation a partir d'encodage du fichier
    public Relation createRelation(String encodage) {
        String[] division = encodage.split("/");
        String tabName = division[0];
        Vector<String> colonne = codeToVector(division[1]);
        Vector<Vector<String>> donnees = new Vector<Vector<String>>();
        for(int i = 2; i < division.length; i++) {
            donnees.add(codeToVector(division[i]));
        }
        Relation nouveau = new Relation(tabName, colonne, donnees);
        return nouveau;
    }

    // Generer un relation a partir d'un fichier
    public Relation generateRelation(File source) {
        try {
            FileInputStream reader = new FileInputStream(source);
            int i = 0;
            char[] ensemble = new char[reader.available()];
            int indice = 0;
            while((i = reader.read()) != -1) {
                ensemble[indice] = (char)i;
                indice++;
            }
            String encodage = new String(ensemble);
            Relation relation = createRelation(encodage);
            reader.close();
            return relation;
        } catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // Trouver tous les relations presistantes
    public Vector<Relation> getAllRelationExisted() {
        Vector<Relation> resultat = new Vector<Relation>();
        File stock = new File("Fichier");
        File[] listes = stock.listFiles();
        for(int i = 0; i < listes.length; i++) {
            resultat.add(generateRelation(listes[i]));
        }
        return resultat;
    }

    // Avoir le relation nommer ....
    public Relation getRelation(String nom) throws Exception {
        for(int i = 0; i < tableList.size(); i++) {
            if(tableList.elementAt(i).getNom().toLowerCase().compareTo(nom.toLowerCase()) == 0) {
                return tableList.elementAt(i);
            }
        }
        throw new Exception("La relation " + nom + " que vous demandez n'existe pas...");
    }

    // Ajouter une table dans la listes des tables
    public void addTable(Relation nouveau) {
        tableList.add(nouveau);
        nouveau.saveIntoFile();
    }

    // Voir la listes de tous les tables
    public void viewTableListe() {
        for(int i = 0; i < tableList.size(); i++) {
            System.out.println("--> table " + i + " : " + tableList.elementAt(i).getNom());
        }
    }
}
