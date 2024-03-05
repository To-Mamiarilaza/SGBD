package operation;
import java.util.Vector;
import relation.Relation;

public class Intersection {
    
     // Intersection
     public static Relation intersection(Relation appelant, Relation autre) throws Exception {
        if (autre.getColonnes().size() != appelant.getColonnes().size()) throw new Exception("Les deux relations doit avoir les memes nombres de colonnes"); 
        Relation resultat = new Relation(appelant.getNom(), appelant.getColonnes(), null);
        Vector<Vector<String>> originalData = appelant.getDonnees();
        Vector<Vector<String>> relationData = autre.getDonnees();
        Vector<Vector<String>> newData = new Vector<Vector<String>>();
        for(int i = 0; i < originalData.size(); i++) {
            for(int a = 0; a < relationData.size(); a++) {
                if (OutilOperation.sameRow(originalData.elementAt(i), relationData.elementAt(a))) {
                   newData.add(originalData.elementAt(i));
                }
            }
        }
        resultat.setDonnees(newData);
        OutilOperation.dropDoublon(resultat);
        return resultat;
    }

}
