package spaceconquest;

import java.util.ArrayList;

public class Dijkstra {
    private Graphe g; // ne se change pas.
    private int infini;
    private boolean mark[];
    private int pi[];
    private int d[];

    public Dijkstra(Graphe g) {
        this.g = g;
        mark = new boolean[g.getNbSommet()];
        pi = new int[g.getNbSommet()];
        d = new int[g.getNbSommet()]; //On a un tableau d'entier car les distances sont des entiers.

        calculInfini();
        initialisation();
    }

    public int[] calcul(int arg){
        int a = arg-1;
        d[a] = 0;
        while (nonMarque()){
            a = premierSommetMinimum();
            mark[a] = true;

            for (int b = 1; b <= g.getNbSommet(); b++) {
                relachement(a, b);
            }
        }

        return d;
    }

    public int plusCourtChemin(int a){
        int tab[] = calcul(a);
        int min = infini;
        int res = -1;
        for (int i = 0; i < tab.length; i++) {
            if (min > tab[i]){
                min = tab[i];
                res = i;
            }
        }
        return res;
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
                if (poidsMin > d[i]){
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
        if ((d[b] > d[a] + g.getMatrice(a,b)) && g.getMatrice(a, b) != 0){
            d[b] = d[a] + g.getMatrice(a, b);
            pi[b] = a;
        }
    }

}
