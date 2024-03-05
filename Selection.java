package operation;
import java.util.Vector;

import operation.OutilOperation;
import operation.Projection;
import condition.Condition;
import relation.Relation;

public class Selection {
    
     // Fonction pour la selection
     public static Relation selection(Relation appelant, Vector<String> col, Vector<Condition> filtre) throws Exception {
        Relation resultat = new Relation(appelant.getNom(), OutilOperation.cloneRow(appelant.getColonnes()), null);
        Vector<Vector<String>> originalData = appelant.getDonnees();
        Vector<Vector<String>> newData = new Vector<Vector<String>>();
        
        for(int i = 0; i < originalData.size(); i++) {
            if(OutilOperation.verifierCondition(originalData.elementAt(i), filtre)) {   // verifier les condition dans le filtre
                newData.add(OutilOperation.cloneRow(originalData.elementAt(i)));
            }
        }
        resultat.setDonnees(newData);
        OutilOperation.dropDoublon(resultat);
        if(col.elementAt(0).compareTo("*") != 0) resultat = Projection.projection(resultat, col);
        return resultat;
    }

}
