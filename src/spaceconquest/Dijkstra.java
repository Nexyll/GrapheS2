package spaceconquest;


import java.util.ArrayList;

public class Dijkstra {
    private Graphe g;
    private int infini;
    private boolean mark[];
    private int pi[];
    private int d[];

    public Dijkstra(Graphe g) {
        this.g = g;
        mark = new boolean[g.getNbSommet()];
        pi = new int[g.getNbSommet()];
        d = new int[g.getNbSommet()];                       //On a un tableau d'entier car les distances sont des entiers.

        calculInfini();
        initialisation();
    }

    private void calcul(int sommetDepart){
        int a = sommetDepart-1;
        d[a] = 0;
        while (nonMarque()){
            a = premierSommetMinimum();
            mark[a] = true;
            for (int b = 0; b < g.getNbSommet(); b++) {
                relachement(a, b);
            }
        }
    }

    /**
     *
     * @param sommetDepart Sommet de départ pour le calcul de Dijkstra.
     * @return Liste des distances par rapport au sommet principal (indexé par les sommets).
     */
    public int[] tableauDistance(int sommetDepart){
        calcul(sommetDepart);
        return d;
    }

    /**
     *
     * @param sommetDepart Sommet de départ pour le calcul de Dijkstra.
     * @param cible Sommet à atteindre
     * @param contrainte Contrainte sur le poids maximal du déplacement.
     * @return numéro du sommet qui convient aux contraintes
     */
    public int sommetIntermediaire(int sommetDepart, int cible, int contrainte){
        calcul(sommetDepart);

        ArrayList<Integer> chemin = new ArrayList<>();

        int pred = pi[cible-1];
        chemin.add(cible-1);
        chemin.add(pred);

        while(pred != -1){                                  // Tant qu'on est pas arrivé
            pred = pi[pred];
            if (pred!=-1)
                chemin.add(pred);
        }

        for (int sommet : chemin){
            if (d[sommet] <= contrainte ){                  // On retourne le sommet le plus loin possible
                return sommet;
            }
        }
        return sommetDepart;                                // Si jamais aucun sommet n'a été trouvé, on ne bouge pas.
    }

    /**
     *
     * @param sommetDepart Sommet de départ pour le calcul de Dijkstra.
     * @param contrainte Poids maximal pour qu'un sommet soit accessible.
     * @return Liste de sommets accessibles.
     */
    public ArrayList<Integer> sommetsAccessibles(int sommetDepart, int contrainte){
        calcul(sommetDepart);
        ArrayList<Integer> listeSommetsAccessibles = new ArrayList<>();

        for (int sommet = 0; sommet < d.length; sommet++) {
            if (d[sommet] <= contrainte)                    // On ajoute le sommet seulement si le poids est inférieur à la contrainte
                listeSommetsAccessibles.add(sommet);
        }

        listeSommetsAccessibles.remove(sommetDepart);       //On supprime le sommet de départ.
        return listeSommetsAccessibles;
    }


    private boolean nonMarque(){
        for (boolean res : mark){
            if (!res) return true;
        }
        return false;
    }

    private int premierSommetMinimum(){
        int poidsMin = infini;
        int res = -1;
        for (int i = 0 ; i < d.length ; i++){
            if (!mark[i]){
                if (poidsMin >= d[i]){
                    res = i;
                    poidsMin = d[i];
                }
            }
        }
        return res;
    }

    private void calculInfini(){
        int somme = 0;
        for (int i = 1; i <= g.getNbSommet(); i++) {
            for (int j = 1; j <= g.getNbSommet() ; j++) {
                somme += g.getMatrice(i, j);
            }
        }
        infini = somme;
    }

    private void initialisation(){
        for (int i = 0; i < g.getNbSommet(); i++) {
            mark[i] = false;
            pi[i] = -1;
            d[i] = infini;
        }
    }

    private void relachement(int a, int b){
        if ((d[b] > d[a] + g.getMatrice(a+1,b+1)) && g.getMatrice(a+1, b+1) != 0){
            d[b] = d[a] + g.getMatrice(a+1, b+1);
            pi[b] = a;
        }
    }

}
