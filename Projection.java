package operation;
import java.util.Vector;
import relation.Relation;

public class Projection {
    
     // Fonctions de selection de colonnes
     public static Relation projection(Relation appelant, Vector<String> listesCol) throws Exception {
        Vector<Integer> indices = OutilOperation.checkListesCol(listesCol, appelant);
        Relation resultat = new Relation(appelant.getNom(), listesCol, null);
        resultat.setDonnees(OutilOperation.getDonneesInCol(indices, appelant));
        OutilOperation.dropDoublon(resultat);
        return resultat;
    } 

}
