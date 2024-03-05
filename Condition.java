package condition;

public class Condition {
    int indiceCol;
    String donnee;
    Boolean etat;

    // Encapsulation
    public void setIndiceCol(int col){this.indiceCol = col;}
    public int getIndiceCol(){return this.indiceCol;}
    public void setDonnee(String data){this.donnee = data;}
    public String getDonnee(){return this.donnee;}
    public void setEtat(Boolean valeur){this.etat = valeur;}
    public Boolean getEtat(){return this.etat;}

    // Constructeur
    public Condition(int col, String data, Boolean valeur) {
        this.setIndiceCol(col);
        this.setDonnee(data);
        this.setEtat(valeur);
    }
}
