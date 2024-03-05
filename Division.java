package operation;
import relation.Relation;
import java.util.Vector;

public class Division {
    public static void checkDivisionValability(Relation appelant, Relation autre) throws Exception {        // tester si la division est possible pour les relations
        Vector<String> colonneAppelant = appelant.getColonnes();
        Vector<String> colonneAutre = autre.getColonnes();
        for(int i = colonneAutre.size() - 1; i >= 0; i--) {
            if (colonneAutre.elementAt(i).compareTo(colonneAppelant.elementAt((colonneAppelant.size() - (colonneAutre.size() - i)))) != 0) {
                throw new Exception("Division invalide , le colonne diviseur n'appartient pas au colonne a divise");
            }
        }
    }

    public static Vector<String> getAllResultColonnes(Relation appelant, Relation autre) {        // avoir les colonnes de retour
        Vector<String> colonneAppelant = appelant.getColonnes();
        Vector<String> colonneAutre = autre.getColonnes();
        Vector<String> resultColonnes = new Vector<String>();
        for(int i = 0; i < colonneAppelant.size() - colonneAutre.size(); i++) {
            resultColonnes.add(colonneAppelant.elementAt(i));
        }
        return resultColonnes;
    }

    public static Relation diviser(Relation appelant, Relation autre) throws Exception {      // Effectuer une division
        Division.checkDivisionValability(appelant, autre);
        Vector<String> resultCol = Division.getAllResultColonnes(appelant, autre);
        Relation possiblesLink = Jointure.joindre(Projection.projection(appelant, resultCol), autre);
        Relation missedLink = Soustraction.soustraire(possiblesLink, appelant);
        Relation linkEffectued = Projection.projection(appelant, resultCol);
        Relation linkMissed = Projection.projection(missedLink, resultCol);
        Relation division = Soustraction.soustraire(linkEffectued, linkMissed);
        OutilOperation.dropDoublon(division);
        return division;
    }
}
