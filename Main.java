package main;

import java.util.Vector;
import relation.Relation;
import condition.Condition;
import requete.*;
import stockage.Stockage;
import operation.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Relation Etudiant ------------------------------------
        // Conception de la colonne
        // Vector<String> colonnes = new Vector<String>();
        // colonnes.add("nom");
        // colonnes.add("prenom");
        // colonnes.add("produit");
        // colonnes.add("location");

        // // Conception des donnees
        // Vector<Vector<String>> donnees = new Vector<Vector<String>>();
        // // Chaque ligne
        // Vector <String> ligne1 = new Vector<String>();
        // ligne1.add("1");
        // ligne1.add("To");
        // ligne1.add("bonbon");
        // ligne1.add("Iavoloha");
        // donnees.add(ligne1);

        // Vector <String> ligne2 = new Vector<String>();
        // ligne2.add("1");
        // ligne2.add("To");
        // ligne2.add("banane");
        // ligne2.add("Iavoloha");
        // donnees.add(ligne2);

        // Vector <String> ligne3 = new Vector<String>();
        // ligne3.add("2");
        // ligne3.add("Niavo");
        // ligne3.add("bonbon");
        // ligne3.add("Iavoloha");
        // donnees.add(ligne3);

        // Vector <String> ligne4 = new Vector<String>();
        // ligne4.add("2");
        // ligne4.add("Niavo");
        // ligne4.add("banane");
        // ligne4.add("Iavoloha");
        // donnees.add(ligne4);

        // Vector <String> test = new Vector<String>();
        // test.add("3");
        // test.add("Mimi");
        // test.add("biscuit");
        // test.add("tsiroanomandidy");

        // // Creation du relation
        // Relation etudiant = new Relation("Etudiant", colonnes, donnees);
        // etudiant.insert(test);

        // // Relation Ferme ----------------------------------------
        // // Conception de la colonne
        // Vector<String> colonnes1 = new Vector<String>();
        // colonnes1.add("produit");
        // colonnes1.add("location");

        // // Conception des donnees
        // Vector<Vector<String>> donnees1 = new Vector<Vector<String>>();
        // // Chaque ligne
        // Vector <String> ligne12 = new Vector<String>();
        // ligne12.add("bonbon");
        // ligne12.add("Iavoloha");
        // donnees1.add(ligne12);

        //  Vector <String> ligne13 = new Vector<String>();
        // ligne13.add("banane");
        // ligne13.add("Iavoloha");
        // donnees1.add(ligne13);

        // Relation ferme = new Relation("Ferme", colonnes1, donnees1);
        // // ==================================================================================== 
        // ferme.afficherRelation();
        // etudiant.afficherRelation();
        // /// TEST A PROPOS DES RELATION
        // try {
        //     Division.diviser(etudiant, ferme).afficherRelation();
        // } catch (Exception e) {
        //     System.out.println(e);
        // }

        Stockage stock = new Stockage();
        ExecuteRequete executeur = new ExecuteRequete(stock);
        execution(executeur);
    }

    public static void execution(ExecuteRequete executeur) {
        try {
            String requete = "";
            while(true) {
                    Scanner input = new Scanner(System.in);
                    System.out.print("Requete ==> ");
                    requete = input.nextLine();
                    executeur.executeRequete(requete).afficherRelation();
                }
        } catch(Exception e) {
            System.out.println(e);
            execution(executeur);
        }
    }
}