package requete;
import relation.*;

import java.util.Vector;
import stockage.Stockage;
import condition.Condition;
import operation.*;

/// MODELE UNION : "Union tab1 et tab2"
/// MODELE INTERSECTION : "Intersection tab1 et tab2"
/// MODELE JOINTURE : "Jointure tab1 et tab2 sur col1 = col2"
/// MODELE SELECTION : "Select idFerme, adresse from tab where col = #valeur"
/// MODELE PROJECTION : "Project idFerme, adresse from tab"
/// MODELE INSERTION : "insert into tab values (val1, val2, val3, val4)"
/// MODELE CREATE RELATION : "create table tabname with (col1, col2, col3, col4)"
/// MODELE CREATE DIVISION : "Division tab1 et tab2"
/// MODELE CREATE SOUSTRACTION : "Soustraction tab1 et tab2"

public class ExecuteRequete {
/// Attributs
    String tabName;     // nom de table
    Vector<String> valueUtil;       // les valeurs utilise
    Vector<Condition> conditionUtil;    // Les conditions requis du requete
    int validation; // 1 si la requete est pret a etre executer , 0 si la requete n'est pas encore pret
    int partie;     // indice du division de requete du String[]
    Vector<String> colonneUtil;     // les colonnes utiliser dans la requette
    Relation resultat;      // le relation obtenue
    Vector<Relation> relationUtil;      // les relations utiles dans la requete
    int indice;        // Indice des operations effectuees : 1 => union, 2 => Intersection, 3 => jointure, 4 => selection, 5 => projection, 6 => insertion, 7 => create relation, 8 => division, 9 => soustraction
    Stockage dataStorage;

/// Encapsulation
    public void setTabName(String tabName) {this.tabName = tabName;}
    public String getTabName() {return this.tabName;}

    public void setValueUtil(Vector<String> valueUtil) {this.valueUtil = valueUtil;}
    public Vector<String> getValueUtil() {return this.valueUtil;}

    public void setConditionUtil(Vector<Condition> conditionUtil) {this.conditionUtil = conditionUtil;}
    public Vector<Condition> getConditionUtil() {return this.conditionUtil;}

    public void setValidation(int validation) {this.validation = validation;}
    public int getValidation() {return this.validation;}

    public void setPartie(int partie) {this.partie = partie;}
    public int getPartie() {return this.partie;}

    public void setColonneUtil(Vector<String> colonneUtil) {this.colonneUtil = colonneUtil;}
    public Vector<String> getColonneUtil() {return this.colonneUtil;}

    public void setResultat(Relation resultat) {this.resultat = resultat;}
    public Relation getResultat() {return this.resultat;}

    public void setRelationUtil(Vector<Relation> relationUtil) {this.relationUtil = relationUtil;} 
    public Vector<Relation> getRelationUtil() {return this.relationUtil;}
        
    public void setIndice(int indice) {this.indice = indice;}
    public int getIndice() {return this.indice;}

    public void setDataStorage(Stockage dataStorage) {this.dataStorage = dataStorage;}
    public Stockage getDataStorage() {return this.dataStorage;}

/// Constructeur
    public ExecuteRequete(Stockage dataStorage) {
        setDataStorage(dataStorage);
    }

/// Fonctions de controle de requete             
    public void getNewTabName(String[] division) throws Exception {
        setTabName(division[getPartie()]);
        nextStep(division);
        if(division[getPartie()].toLowerCase().compareTo("with") != 0) erreur("Veuillez suivre le grammaire : la requete doit contenir 'with'");
        nextStep(division);
        if(division[getPartie() - 1].toLowerCase().compareTo("with") == 0 && !checkBeginParenthese(division[getPartie()])) erreur("Vous avez oublier le parenthese");
        getAllValue(division);
    }

    public void rafraichir() {
        setRelationUtil(new Vector<Relation>());
        setColonneUtil(new Vector<String>());
        setConditionUtil(new Vector<Condition>());
        setValueUtil(new Vector<String>());
    }

    public boolean checkFinalParenthese(String donnees) {
        char[] charTab = donnees.toCharArray();
        if(charTab[charTab.length - 1] == ')') return true;
        return false;
    }

     public boolean checkBeginParenthese(String donnees) {
        char[] charTab = donnees.toCharArray();
        if(charTab[0] == '(') return true;
        return false;
    }

    public void getAllValue(String[] division) throws Exception {
        if(division.length - 1 == getPartie()) {
            if(checkFinalParenthese(division[getPartie()]) && !checkComma(division[getPartie()])) {
                getValueUtil().add(formatColonne(division[getPartie()]));
                operating();
            }
            else if(!checkFinalParenthese(division[getPartie()])) erreur("Fermez le parenthese ....");
            else if(checkComma(division[getPartie()])) erreur("Il y a encore un virgule qui traine ....");
        }
        else if(!checkComma(division[getPartie()])) erreur("Vous avez oublier le virgule");
        else {
            getValueUtil().add(formatColonne(division[getPartie()]));
            nextStep(division);
            getAllValue(division);
        }
    }

    public void prepareCondition(String[] division) throws Exception {
        int indiceCol = OutilOperation.getIndiceCol(division[getPartie()], getRelationUtil().elementAt(0));
        boolean test = true;
        nextStep(division);
        if(division[getPartie()].compareTo("=") == 0) test = true;
        if(division[getPartie()].compareTo("!=") == 0) test = false;
        else erreur("Veuillez suivre le grammaire : la requete doit contenir ' = ou != '");
        nextStep(division);
        String valeur = division[getPartie()];
        Condition cond = new Condition(indiceCol, valeur, test);
        getConditionUtil().add(cond);
        if(division.length - 1 == getPartie()) operating();     // Execution si valide
        else erreur("Suivre le grammaire s'il vous plait, vous n'avez droit qu'a un condition pour le moment");
    }

    public void nextStep(String[] division) throws Exception {
        // passer au mot suivant
        setPartie(getPartie() + 1);
        checkRequeteValidation(division);
    }

    public int countParenthese(String chaine, char carac) {
        int nombre = 0;
        char[] division = chaine.toCharArray();
        for(int i = 0; i < division.length; i++) {
            if(division[i] == carac) nombre++;
        }
        return nombre;
    }

    public int[] extractRequete(String[] division) throws Exception {
        int debut = getPartie();        // Le debut du sous requette
        int porte = 0;                  // indice de fermeture du parenthese
        int fin = 0;                    // indice fin du sous requette

        for(int i = debut; i < division.length; i++) {
            porte += countParenthese(division[i], '(');
            porte -= countParenthese(division[i], ')');
            if(porte < 0) erreur("Le nombre d'ouverture du parenthese est inferieur au fermeture");
            if(porte == 0) {
                fin = i;                
                break;
            }
            if (i == division.length - 1) {
                if (porte != 0) erreur("Veuillez bien fermez les parentheses");
            }
            nextStep(division); // cette grammaire saute le sous requette
        }

        int[] resultat = new int[2];
        resultat[0] = debut;
        resultat[1] = fin;

        return resultat;
    }

    public String preparationRequete(String[] division, int[] intervalle) {
        int debut = intervalle[0];
        int fin = intervalle[1];
        String resultat = division[debut];
        for(int i = debut + 1; i <= fin; i++) {
            resultat += " " + division[i];
        }
        return resultat;
    }

    public String formatRequete(String requete) {
        return requete.substring(1, requete.length() - 1);
    }

    public String getSousRequete(String[] division) throws Exception {
        // Verifie si les parentheses ferme bien
        int[] intervalle = extractRequete(division);
        String requete = preparationRequete(division, intervalle);
        requete = formatRequete(requete);
        System.out.println(requete);
        return requete;
    }

    public void getOrigineRelation(String[] division) throws Exception {
        // Prends la relation dans la quel on enleve les colonnes
        if (division[getPartie()].contains("(")) {
            String sousRequete = getSousRequete(division);
            ExecuteRequete executeur = new ExecuteRequete(getDataStorage());
            getRelationUtil().add(executeur.executeRequete(sousRequete));
        }
        else {
            getRelationUtil().add(getDataStorage().getRelation(division[getPartie()]));
        }
        switch(getIndice()) {
            case 4:
                setValidation(1);
                if(division.length - 1 == getPartie() && getValidation() == 1) operating();     // Execution si valide
                else {
                    setValidation(0);
                    nextStep(division);
                    if(division[getPartie()].toLowerCase().compareTo("where") != 0) erreur("Veuillez suivre le grammaire : la requete doit contenir 'where'");
                    else {
                        nextStep(division);
                        prepareCondition(division);
                    }
                }
                break;

            case 5:
                setValidation(1);
                if(division.length - 1 == getPartie() && getValidation() == 1) operating();     // Execution si valide
                else erreur("Suivre le grammaire s'il vous plait, vous ecriver trop !!!");
                break;
            
            case 6:
                getRelationUtil().add(getDataStorage().getRelation(division[getPartie()]));
                nextStep(division);
                if(division[getPartie()].toLowerCase().compareTo("values") != 0) erreur("Veuillez suivre le grammaire : la requete doit contenir 'values'");
                nextStep(division);
                if(division[getPartie() - 1].toLowerCase().compareTo("values") == 0 && !checkBeginParenthese(division[getPartie()])) erreur("Vous avez oublier le parenthese");
                getAllValue(division);
        }
        
    }

    public void checkRequeteValidation(String[] division) throws Exception {
        if(division.length <= getPartie() && getValidation() == 0) erreur("Requete incomplet !"); 
    }

     public String formatColonne(String colonne) {
        // Enlever les virgules et parethese
        char[] division = colonne.toCharArray();

        boolean beginParenth = false;
        boolean finalParenth = false;
        boolean comma = false;
        int enleve = 0;

        if (division[division.length - 1] == ',') {comma = true; enleve++;}
        if(division[0] == '(') {beginParenth = true; enleve++;}
        if(division[division.length - 1] == ')') {finalParenth = true; enleve++;}

        char[] preparation = new char[division.length - enleve];
        int indice = 0;

        for(int i = 0; i < division.length; i++) {
            if(division[i] != ',' && division[i] != ')' && division[i] != '(') {
                preparation[indice] = division[i];
                indice++;
            }
        }

        String resultat = new String(preparation);
        return resultat;
    }

    public boolean checkComma(String colonne) throws Exception {
        char[] division = colonne.toCharArray();
        if(division[division.length - 1] == ',') return true;
        return false;
    }

    public void getUsedColonnes(String[] division) throws Exception {        // Presque meme que getUseddDonnees mais plus efficace
        // Avoir les colonnes   
        if(division[getPartie()].toLowerCase().compareTo("from") == 0) erreur("Vous devez precisez les colonnes a montrer");
        getColonneUtil().add(formatColonne(division[getPartie()]));
        nextStep(division);
        if(division[getPartie()].toLowerCase().compareTo("from") != 0) {
            if(!checkComma(division[getPartie() - 1])) erreur("Vous avez oublier le virgule dans les colonnes");
            getUsedColonnes(division);
        }
        else {
            if(checkComma(division[getPartie() - 1])) erreur("Vous ne devez plus ajouter un virgule dans les colonnes");
            //// Alaina ilay relation akana ireo colonne
            nextStep(division);
            getOrigineRelation(division);
        }
    }

    public void operating() throws Exception {
        // Execution de l'operation demandee
        switch (getIndice()) {
            case 1:
                setResultat(Union.union(getRelationUtil().elementAt(0), getRelationUtil().elementAt(1)));
                break;

            case 2:
                setResultat(Intersection.intersection(getRelationUtil().elementAt(0), getRelationUtil().elementAt(1)));
                break;

            case 3:
                setResultat(Jointure.joindre(getRelationUtil().elementAt(0), getRelationUtil().elementAt(1), getColonneUtil().elementAt(0), getColonneUtil().elementAt(1)));
                break;
            
            case 4:
                setResultat(Selection.selection(getRelationUtil().elementAt(0), getColonneUtil(), getConditionUtil()));
                break;

            case 5:
                setResultat(Projection.projection(getRelationUtil().elementAt(0), getColonneUtil()));
                break;
            
            case 6:
                getRelationUtil().elementAt(0).insert(getValueUtil());
                setResultat(getRelationUtil().elementAt(0));
                break;

            case 7:
                Relation nouveau = new Relation(getTabName(), getValueUtil());
                getDataStorage().addTable(nouveau);
                setResultat(nouveau);
                break;
            
            case 8:
                setResultat(Division.diviser(getRelationUtil().elementAt(0), getRelationUtil().elementAt(1)));
                break;
            
            case 9:
                setResultat(Soustraction.soustraire(getRelationUtil().elementAt(0), getRelationUtil().elementAt(1)));
                break;
        }
    }

    public void verificationRequete(String[] division) throws Exception {
        switch (getIndice()) {
            case 1:
                if (division.length != 4) erreur("Veuillez suivre le grammaire de la requete : respecter le structure!");
                if(division[2].toLowerCase().compareTo("et") != 0) erreur("Veuillez suivre le grammaire de la requete : la requete doit avoir un 'et' !");
                break;

            case 2:
                if (division.length != 4) erreur("Veuillez suivre le grammaire de la requete : respecter le structure!");
                if(division[2].toLowerCase().compareTo("et") != 0) erreur("Veuillez suivre le grammaire de la requete : la requete doit avoir un 'et' !");
                break;

            case 3:
                if (division.length != 8) erreur("Veuillez suivre le grammaire de la requete : respecter le structure!");
                if(division[2].toLowerCase().compareTo("et") != 0) erreur("Veuillez suivre le grammaire de la requete : la requete doit avoir un 'et' !");
                if(division[4].toLowerCase().compareTo("sur") != 0) erreur("Veuillez suivre le grammaire de la requete : la requete doit avoir un 'sur' !");
                if(division[6].toLowerCase().compareTo("=") != 0) erreur("Veuillez suivre le grammaire de la requete : la requete doit avoir un ' = ' !");
                break;

            case 8:
                if (division.length != 4) erreur("Veuillez suivre le grammaire de la requete : respecter le structure!");
                if (division[2].toLowerCase().compareTo("et") != 0) erreur("Veuillez suivre le grammaire de la requete : la requete doit avoir un 'et' !");
                break;
        }
    }

    public void getUsedDonnees(String[] division) throws Exception {
        // prendre tous les relation utiliser
        verificationRequete(division);
        ExecuteRequete executeur = new ExecuteRequete(getDataStorage());
        switch (getIndice()) {      // Savoir les places de nom de table en fonction de l'operation demandee
            case 1:
                if(getDataStorage().getRelation(division[1]) != null && getDataStorage().getRelation(division[3]) != null) {
                    getRelationUtil().add(getDataStorage().getRelation(division[1]));
                    getRelationUtil().add(getDataStorage().getRelation(division[3]));
                }
                else erreur("Les relations que vous demandez n'existe pas !");
                break;

            case 2:
                if(getDataStorage().getRelation(division[1]) != null && getDataStorage().getRelation(division[3]) != null) {
                    getRelationUtil().add(getDataStorage().getRelation(division[1]));
                    getRelationUtil().add(getDataStorage().getRelation(division[3]));
                }
                else erreur("Les relations que vous demandez n'existe pas !");
                break;

            case 3:
                if(getDataStorage().getRelation(division[1]) != null && getDataStorage().getRelation(division[3]) != null) {
                    getRelationUtil().add(getDataStorage().getRelation(division[1]));
                    getRelationUtil().add(getDataStorage().getRelation(division[3]));
                    getColonneUtil().add(division[5]);      // Ajout des colonnes de reference
                    getColonneUtil().add(division[7]);      // Ajout des colonnes de reference
                }
                else erreur("Les relations que vous demandez n'existe pas !");
                break;

            case 8:    
                if(getDataStorage().getRelation(division[1]) != null && getDataStorage().getRelation(division[3]) != null) {
                    getRelationUtil().add(getDataStorage().getRelation(division[1]));
                    getRelationUtil().add(getDataStorage().getRelation(division[3]));
                }
                else erreur("Les relations que vous demandez n'existe pas !");
                break;

            case 9:    
                if(getDataStorage().getRelation(division[1]) != null && getDataStorage().getRelation(division[3]) != null) {
                    getRelationUtil().add(getDataStorage().getRelation(division[1]));
                    getRelationUtil().add(getDataStorage().getRelation(division[3]));
                }
                else erreur("Les relations que vous demandez n'existe pas !");
                break;
        }
    }

    public void erreur(String message) throws Exception {
        // Renvoie une erreur s'il y en a
        throw new Exception(message);
    }

    public String[] removeEspace(String[] division) {
        Vector<String> preResult = new Vector<String>();
        for(int i = 0; i < division.length; i++) {
            if(division[i].compareTo("") != 0) preResult.add(division[i]);
        }
        String[] resultat = new String[preResult.size()];
        for(int i = 0; i < resultat.length; i++) {
            resultat[i] = preResult.elementAt(i);
        }
        return resultat;
    }

    public Relation executeRequete(String requete) throws Exception {
        rafraichir();
        // Premiere etape de l'execution du code 
        String[] division = requete.split(" ");
        division = removeEspace(division);
        setPartie(0);
        setValidation(0);

        switch (division[0].toLowerCase()) {        // Savoir quel operation effectue
            case "union":
                setIndice(1);
                getUsedDonnees(division);       // Trouver les relation utiliser
                operating();
                break;

            case "intersection":
                setIndice(2);
                getUsedDonnees(division);       // Trouver les relation utiliser
                operating();
                break;

            case "jointure":
                setIndice(3);
                getUsedDonnees(division);
                operating();
                break;

            case "select":
                setIndice(4);
                nextStep(division);
                getUsedColonnes(division);
                break;

            case "project":
                setIndice(5);
                nextStep(division);
                getUsedColonnes(division);
                break;

            case "insert":
                setIndice(6);
                nextStep(division);
                if(division[getPartie()].toLowerCase().compareTo("into") != 0) erreur("Veuillez suivre le grammaire : la requete doit contenir 'into'");
                nextStep(division);
                getOrigineRelation(division);
                break;

            case "create":
                setIndice(7);
                nextStep(division);
                if(division[getPartie()].toLowerCase().compareTo("table") != 0) erreur("Veuillez suivre le grammaire : la requete doit contenir 'table'");
                nextStep(division);
                getNewTabName(division);
                break;

            case "division":
                setIndice(8);
                getUsedDonnees(division);       // Trouver les relation utiliser
                operating();
                break;

            case "soustraction":
                setIndice(9);
                getUsedDonnees(division);       // Trouver les relation utiliser
                operating();
                break;

            default:
                erreur("L'operation que vous demandez n'existe pas !");
        }

        return resultat;
    }

    public void regarderUsedRelation() {
        for(int i = 0; i < getRelationUtil().size(); i++) {
            System.out.println("--> " + getRelationUtil().elementAt(i).getNom());
        }
    }

}
