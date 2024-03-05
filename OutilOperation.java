package operation;
import java.util.Vector;
import condition.Condition;
import relation.Relation;

public class OutilOperation {

    // Union de deux listes de colonnes en un
    public static Vector<String> prepareColonnes(Vector<String> col1, Vector<String> col2) {
        Vector<String> resultat = new Vector<String>();
        for(int i = 0; i < col1.size(); i++) {
            resultat.add(col1.elementAt(i));
        }
        for(int i = 0; i < col2.size(); i++) {
            resultat.add(col2.elementAt(i));
        }
        return resultat;
    }

    // Avoir les indices du colonnees
    public static int getIndiceCol(String nomCol, Relation proprietaire) throws Exception {
        Vector<String> listesCol = proprietaire.getColonnes();
        for(int i = 0; i < listesCol.size(); i++) {
            if (listesCol.elementAt(i).toLowerCase().compareTo(nomCol.toLowerCase()) == 0) {
                return i;
            }
        }
        throw new Exception("Le colonne que vous voulez n'existe pas dans " + proprietaire.getNom());
    }

    // Verification des Condition
    public static boolean verifierCondition(Vector<String> ligne, Vector<Condition> cond) throws Exception {
        for(int i = 0; i < cond.size(); i++) {
            if ((ligne.elementAt(cond.elementAt(i).getIndiceCol()).compareTo(cond.elementAt(i).getDonnee()) == 0) != cond.elementAt(i).getEtat()) {
                return false;
            }
        }
        return true;
    }

    // Copier une ligne sans avoir l'adresse
    public static Vector<String> cloneRow(Vector<String> row) {
        Vector<String> resultat = new Vector<String>();
        for(int i = 0; i < row.size(); i++) {
            resultat.add(row.elementAt(i));
        }
        return resultat;
    }

    // Projection (ne prendre que les colonnes requise) 
    public static Vector<Vector<String>> getDonneesInCol(Vector<Integer> indices, Relation proprietaire) {
        Vector<Vector<String>> originalData = proprietaire.getDonnees();
        Vector<Vector<String>> resultat = new Vector<Vector<String>>();
        Vector<String> ligne;
        for(int i = 0; i < originalData.size(); i++) {
            ligne = new Vector<String>();
            for(int a = 0; a < indices.size(); a++) {
               ligne.add(originalData.elementAt(i).elementAt(indices.elementAt(a)));
            }
            resultat.add(ligne);
        }
        return resultat;
    }

    // Verifier si les colonnes demande existe dans la Relation
    public static Vector<Integer> checkListesCol(Vector<String> listesCol, Relation proprietaire) throws Exception {
        Vector<String> origineCol = proprietaire.getColonnes();
        Vector<Integer> indices = new Vector<Integer>(); 
        for(int i = 0; i < listesCol.size(); i++) {
            int meme = 0;
            for(int a = 0; a < origineCol.size(); a++) {
                if (listesCol.elementAt(i).toLowerCase().compareTo(origineCol.elementAt(a).toLowerCase()) == 0) {
                    indices.add(a);
                    meme++;
                }
            }
            if (meme == 0) {
                throw new Exception("La colonne " + listesCol.elementAt(i) + " n'existe pas");
            }
        }
        return indices;
    }

    // Verifier si il y a deux meme ligne
    public static boolean sameRow(Vector<String> ligne1, Vector<String> ligne2) {
        for(int i = 0; i < ligne1.size(); i++) {
            if(ligne1.elementAt(i).compareTo(ligne2.elementAt(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    // Enleve les doublons
    public static void dropDoublon(Relation relation) throws Exception {
        trierRelation(relation, relation.getColonnes().elementAt(0));
        Vector<Vector<String>> donnees = relation.getDonnees();
        for(int i = 1; i < donnees.size(); i++) {
            if(sameRow(donnees.elementAt(i-1), donnees.elementAt(i))) {
                donnees.removeElementAt(i-1);
                i--;
            }
        }
    } 

    // Pour avoir le meme vecteur sans avoir le meme adresse
    public static Vector <Vector <String>> cloneDonnees(Vector <Vector <String>> origine) {
        Vector <Vector <String>> resultat = new Vector<Vector <String>>();
        for(int i = 0; i < origine.size(); i++) {
            resultat.add(origine.elementAt(i)); // seule les lignes sont cloner
        }
        return resultat;
    }

    // Comparer par ordre alphabetique
    public static boolean compareStringSmall(String element1, String element2) {
        char[] div1 = element1.toCharArray();
        char[] div2 = element2.toCharArray();
        for(int i = 0; i < div1.length; i++) {
            if(div1[i] < div2[i]) return true;
            else if(div1[i] > div2[i]) return false;
            else if(i == div2.length - 1) return false; 
        }
        return true;
    }

    // changer place au cours de trie
    public static void permuteVectorElement(Vector<Vector<String>> elements, int indice1, int indice2) {
        Vector<String> temp = elements.elementAt(indice1);
        elements.setElementAt(elements.elementAt(indice2), indice1);
        elements.setElementAt(temp, indice2);
    }

    // trier une relation
    public static void trierRelation(Relation relation, String nomCol) throws Exception {
        int indice = OutilOperation.getIndiceCol(nomCol, relation);
        for(int i = 0; i < relation.getDonnees().size(); i++) {
            for(int a = i; a < relation.getDonnees().size();  a++) {
                if(compareStringSmall(relation.getDonnees().elementAt(a).elementAt(indice), relation.getDonnees().elementAt(i).elementAt(indice))) {
                    permuteVectorElement(relation.getDonnees(), i, a);
                }
            }
        }
    }
}
