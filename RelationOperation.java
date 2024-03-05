package operation;
import java.util.Vector;

import javax.lang.model.element.Element;

import condition.Condition;
import relation.Relation;

public class RelationOperation {
    // Attributs ------------------
    Relation proprietaire;

    // Encapsulation --------------
    public void setProprietaire(Relation relation){this.proprietaire = relation;}
    public Relation getProprietaire(){return this.proprietaire;}

    // Constructeur ---------------
    public RelationOperation(Relation propr) {
        this.proprietaire = propr;
    }

    // Fonctions ------------------

    // Intersection
    public Relation intersection(Relation relation) {
        Relation resultat = new Relation(this.proprietaire.getNom(), this.proprietaire.getColonnes(), null);
        Vector<Vector<String>> originalData = this.proprietaire.getDonnees();
        Vector<Vector<String>> relationData = relation.getDonnees();
        Vector<Vector<String>> newData = new Vector<Vector<String>>();
        for(int i = 0; i < originalData.size(); i++) {
            for(int a = 0; a < relationData.size(); a++) {
                if (this.sameRow(originalData.elementAt(i), relationData.elementAt(a))) {
                   newData.add(originalData.elementAt(i));
                }
            }
        }
        resultat.setDonnees(newData);
        dropDoublon(resultat);
        return resultat;
    }

    // Avoir les indices du colonnees
    public int getIndiceCol(String nomCol) {
        Vector<String> listesCol = this.proprietaire.getColonnes();
        for(int i = 0; i < listesCol.size(); i++) {
            if (listesCol.elementAt(i).toLowerCase().compareTo(nomCol.toLowerCase()) == 0) {
                return i;
            }
        }
        return 0;
    }

    // Verification des Condition
    boolean verifierCondition(Vector<String> ligne, Vector<Condition> cond) {
        for(int i = 0; i < cond.size(); i++) {
            if ((ligne.elementAt(cond.elementAt(i).getIndiceCol()).compareTo(cond.elementAt(i).getDonnee()) == 0) != cond.elementAt(i).getEtat()) {
                return false;
            }
        }
        return true;
    }

    public Vector<String> cloneRow(Vector<String> row) {
        Vector<String> resultat = new Vector<String>();
        for(int i = 0; i < row.size(); i++) {
            resultat.add(row.elementAt(i));
        }
        return resultat;
    }

    // Fonction pour la selection
    public Relation selection(Vector<String> col, Vector<Condition> filtre) {
        Relation resultat = new Relation(this.proprietaire.getNom(), this.cloneRow(this.proprietaire.getColonnes()), null);
        Vector<Vector<String>> originalData = this.proprietaire.getDonnees();
        Vector<Vector<String>> newData = new Vector<Vector<String>>();
        for(int i = 0; i < originalData.size(); i++) {
            if(verifierCondition(originalData.elementAt(i), filtre)) { // verifier les condition dans le filtre
                newData.add(cloneRow(originalData.elementAt(i)));
            }
        }
        resultat.setDonnees(newData);
        resultat = resultat.getCalculeur().projection(col);
        return resultat;
    }

    // remplir seulement les colonnes indiquees 
    public Vector<Vector<String>> getDonneesInCol(Vector<Integer> indices) {
        Vector<Vector<String>> originalData = this.proprietaire.getDonnees();
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
    public Vector<Integer> checkListesCol(Vector<String> listesCol) {
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
                listesCol.removeElementAt(i);
                i--;
            }
        }
        return indices;
    }

    // Fonctions de selection de colonnes
    public Relation projection(Vector<String> listesCol) {
        Vector<Integer> indices = checkListesCol(listesCol);
        Relation resultat = new Relation(this.proprietaire.getNom(), listesCol, null);
        resultat.setDonnees(getDonneesInCol(indices));
        return resultat;
    } 

    public boolean sameRow(Vector<String> ligne1, Vector<String> ligne2) {
        for(int i = 0; i < ligne1.size(); i++) {
            if(ligne1.elementAt(i).compareTo(ligne2.elementAt(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    // Enleve les doublons
    void dropDoublon(Relation relation){
        trierRelation(relation);
        Vector<Vector<String>> donnees = relation.getDonnees();
        for(int i = 1; i < donnees.size(); i++) {
            if(sameRow(donnees.elementAt(i-1), donnees.elementAt(i))) {
                donnees.removeElementAt(i-1);
                i--;
            }
        }
    }

    // Arranger par ordre alphabetic
    char[] dropFirstElement(char[] original) {
        char[] resultat = new char[original.length - 1];
        for(int i = 0; i < resultat.length; i++) {
            resultat[i] = original[i+1];
        }
        return resultat;
    }

    public boolean compareStringSmall(String element1, String element2) {
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
    public void permuteVectorElement(Vector<Vector<String>> elements, int indice1, int indice2) {
        Vector<String> temp = elements.elementAt(indice1);
        elements.setElementAt(elements.elementAt(indice2), indice1);
        elements.setElementAt(temp, indice2);
    }

    // trier une relation
    public void trierRelation(Relation relation) {
        for(int i = 0; i < relation.getDonnees().size(); i++) {
            for(int a = i; a < relation.getDonnees().size();  a++) {
                if(this.compareStringSmall(relation.getDonnees().elementAt(a).elementAt(0), relation.getDonnees().elementAt(i).elementAt(0))) {
                    this.permuteVectorElement(relation.getDonnees(), i, a);
                }
            }
        }
    }

    // Pour avoir le meme vecteur sans avoir le meme adresse
    public Vector <Vector <String>> cloneDonnees(Vector <Vector <String>> origine) {
        Vector <Vector <String>> resultat = new Vector<Vector <String>>();
        for(int i = 0; i < origine.size(); i++) {
            resultat.add(origine.elementAt(i)); // seule les lignes sont cloner
        }
        return resultat;
    }

    // Operations Union
    public Relation union(Relation relation) {
        if (relation.getColonnes().size() == this.proprietaire.getColonnes().size()) {
            Relation result = new Relation(this.proprietaire.getNom(), this.proprietaire.getColonnes(), cloneDonnees(this.proprietaire.getDonnees()));
            for(int i = 0; i < relation.getDonnees().size(); i++) {
                result.getDonnees().add(relation.getDonnees().elementAt(i));
            }
            dropDoublon(result);
            return result;
        }
        else {
            System.out.println("L'operation union n'est pas valide , le nombre de colonne est different");
        }
        return null;
    }
}