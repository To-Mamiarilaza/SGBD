package operation;
import java.util.Vector;
import relation.Relation;
import operation.OutilOperation;
public class Jointure {
     // Fonctions de selection de colonnes (joindre appelant et appele on rel1.col1 = rel2.col2)
    public static Relation joindre(Relation rel1, Relation rel2, String col1, String col2) throws Exception {
        Relation resultat = new Relation(rel1.getNom() + " et " + rel2.getNom(), null, null);
        Vector<String> colonnes = OutilOperation.prepareColonnes(rel1.getColonnes(), rel2.getColonnes());
        resultat.setColonnes(colonnes);
        resultat.setDonnees(Jointure.prepareJointureData(rel1, rel2, col1, col2)); 
        OutilOperation.dropDoublon(resultat);
        return resultat;
    } 

    public static Relation joindre(Relation rel1, Relation rel2) throws Exception {
        Relation resultat = new Relation(rel1.getNom() + " et " + rel2.getNom(), null, null);
        Vector<String> colonnes = OutilOperation.prepareColonnes(rel1.getColonnes(), rel2.getColonnes());
        resultat.setColonnes(colonnes);
        resultat.setDonnees(Jointure.prepareJointureData(rel1, rel2)); 
        OutilOperation.dropDoublon(resultat);
        return resultat;
    } 

    // Preparation du donnees
    public static Vector<Vector<String>> prepareJointureData(Relation rel1, Relation rel2, String col1, String col2) throws Exception {
        if(OutilOperation.getIndiceCol(col1, rel1) == 0) throw new Exception("La colonne 1 que vous entrer n'existe pas");
        if(OutilOperation.getIndiceCol(col2, rel2) == 0) throw new Exception("La colonne 2 que vous entrer n'existe pas");
        Vector<Vector<String>> resultat = new Vector<Vector<String>>();
        Vector<String> ligne = new Vector<String>();
        OutilOperation.trierRelation(rel1, col1);
        OutilOperation.trierRelation(rel2, col2);
        Vector<Vector<String>> data1 = rel1.getDonnees();
        Vector<Vector<String>> data2 = rel2.getDonnees();
        for(int i = 0; i < data1.size(); i++) {
            for(int a = 0; a < data2.size(); a++) {
                ligne = new Vector<String>();
                if(data1.elementAt(i).elementAt(OutilOperation.getIndiceCol(col1, rel1)).compareTo(data2.elementAt(a).elementAt(OutilOperation.getIndiceCol(col2, rel2))) == 0) {
                    Jointure.ajouterDonnees(ligne, data1.elementAt(i), data2.elementAt(a));
                    resultat.add(ligne);
                }
            }
        }
        return resultat;
    }

    public static Vector<Vector<String>> prepareJointureData(Relation rel1, Relation rel2) throws Exception {
        Vector<Vector<String>> resultat = new Vector<Vector<String>>();
        Vector<String> ligne = new Vector<String>();
        OutilOperation.trierRelation(rel1, rel1.getColonnes().elementAt(0));
        OutilOperation.trierRelation(rel2, rel2.getColonnes().elementAt(0));
        Vector<Vector<String>> data1 = rel1.getDonnees();
        Vector<Vector<String>> data2 = rel2.getDonnees();
        for(int i = 0; i < data1.size(); i++) {
            for(int a = 0; a < data2.size(); a++) {
                ligne = new Vector<String>();
                Jointure.ajouterDonnees(ligne, data1.elementAt(i), data2.elementAt(a));
                resultat.add(ligne);
            }
        }
        return resultat;
    }

    public static void ajouterDonnees(Vector<String> ligne, Vector<String> element1, Vector<String> element2) {
        for(int i = 0; i < element1.size(); i++) {
            ligne.add(element1.elementAt(i));
        }
        for(int a = 0; a < element2.size(); a++) {
            ligne.add(element2.elementAt(a));
        }
    }

   
}

