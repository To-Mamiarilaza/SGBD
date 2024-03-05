package operation;
import java.util.Vector;
import relation.Relation;

public class Union {

    // Operations Union
    public static Relation union(Relation appelant, Relation autre) throws Exception {
        if (autre.getColonnes().size() == appelant.getColonnes().size()) {
            Relation result = new Relation(appelant.getNom(), appelant.getColonnes(), OutilOperation.cloneDonnees(appelant.getDonnees()));
            for(int i = 0; i < autre.getDonnees().size(); i++) {
                result.getDonnees().add(autre.getDonnees().elementAt(i));
            }
            OutilOperation.dropDoublon(result);
            return result;
        }
        else {
            System.out.println("L'operation union n'est pas valide , le nombre de colonne est different");
        }
        return null;
    }
}
