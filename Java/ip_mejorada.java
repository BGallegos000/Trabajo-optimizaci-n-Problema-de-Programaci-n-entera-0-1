package Java;
import java.util.Arrays;
import java.util.Random;

public class ip_mejorada { // Clase para resolver el problema de programación entera 0-1 utilizando backtracking con poda basada en cota superior
    static class Item implements Comparable<Item> {// Clase interna para representar un ítem con peso, beneficio y razón beneficio/peso
        double w, p, r;
        Item(double w, double p) { this.w = w; this.p = p; this.r = p/w; }
        public int compareTo(Item o) { return Double.compare(o.r, this.r); }
    }

    private Item[] items;  
    private double cap, mejorP = 0;
    private int n;
    public long nodosVisitados = 0;

    public ip_mejorada(double[] w, double[] p, double c) { // Constructor para inicializar los datos del problema y ordenar los ítems por razón beneficio/peso
        this.n = w.length; this.cap = c; this.items = new Item[n];
        for (int i=0; i<n; i++) items[i] = new Item(w[i], p[i]);
        Arrays.sort(items);
    }

    private double cota(int niv, double pA, double bA) { // Método para calcular la cota superior utilizando el método de la mochila fraccionaria
        double res = bA, pT = pA;
        for (int i=niv; i<n; i++) {
            if (pT + items[i].w <= cap) { pT += items[i].w; res += items[i].p; }
            else { res += items[i].p * ((cap - pT) / items[i].w); break; }
        }
        return res;
    }

    private void bnb(int niv, double pA, double bA) { // Método recursivo para explorar el espacio de soluciones utilizando backtracking con poda
        nodosVisitados++;
        if (bA > mejorP) mejorP = bA;
        if (niv == n || cota(niv, pA, bA) <= mejorP) return;
        
        if (pA + items[niv].w <= cap) bnb(niv + 1, pA + items[niv].w, bA + items[niv].p);
        bnb(niv + 1, pA, bA);
    }

    public void resolver() { // Método para iniciar la resolución del problema y medir el tiempo de ejecución
        long inicio = System.nanoTime();
        bnb(0, 0, 0);
        long fin = System.nanoTime();
        System.out.printf("N: %-4d | Tiempo: %.6fs | Nodos: %d\n", n, (fin - inicio) / 1e9, nodosVisitados);
    }

    public static void main(String[] args) { // Método principal para generar casos de prueba y ejecutar el solver
        int[] n_pruebas = {20, 40, 60, 100, 1000}; 
        Random rnd = new Random(42);

        for (int n_actual : n_pruebas) { // Generación de pesos y beneficios aleatorios para cada prueba
            double[] pesos = new double[n_actual];
            double[] beneficios = new double[n_actual];
            double sumW = 0;

            for (int i = 0; i < n_actual; i++) { // Generación de pesos y beneficios aleatorios
                pesos[i] = 10.0 + rnd.nextDouble() * 40.0;
                beneficios[i] = 50.0 + rnd.nextDouble() * 50.0;
                sumW += pesos[i];
            }

            ip_mejorada solver = new ip_mejorada(pesos, beneficios, sumW * 0.5); // Capacidad al 50% de la suma de pesos
            solver.resolver();
        }
    }
}