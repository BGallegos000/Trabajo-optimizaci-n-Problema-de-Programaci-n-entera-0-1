package Java;
import java.util.Random;

public class ip_base {// Clase para resolver el problema de programación entera 0-1 utilizando backtracking sin poda
    private double[] w, p;
    private double cap, mejorP = 0;
    private int n;
    public long nodosVisitados = 0;

    public ip_base(double[] weights, double[] profits, double capacity) { // Constructor para inicializar los datos del problema
        this.w = weights; this.p = profits; this.cap = capacity; this.n = weights.length;
    }

    private void backtrack(int nivel, double pesoA, double benefA) { // Método recursivo para explorar todas las combinaciones posibles
        nodosVisitados++;
        if (benefA > mejorP) mejorP = benefA;
        if (nivel == n) return;
        
        if (pesoA + w[nivel] <= cap) {
            backtrack(nivel + 1, pesoA + w[nivel], benefA + p[nivel]);
        }
        backtrack(nivel + 1, pesoA, benefA);
    }

    public void resolver() { // Método para iniciar la resolución del problema y medir el tiempo de ejecución
        long inicio = System.nanoTime();
        backtrack(0, 0, 0);
        long fin = System.nanoTime();
        System.out.printf("N: %-4d | Tiempo: %.6fs | Nodos: %d\n", n, (fin - inicio) / 1e9, nodosVisitados);
    }

    public static void main(String[] args) { // Método principal para generar casos de prueba y ejecutar el solver
        int[] n_pruebas = {10, 15, 20, 25, 30}; 
        Random rnd = new Random(42); // Semilla fija para consistencia

        for (int n_actual : n_pruebas) {// Generación de pesos y beneficios aleatorios para cada prueba
            double[] pesos = new double[n_actual];
            double[] beneficios = new double[n_actual];
            double sumW = 0;

            for (int i = 0; i < n_actual; i++) {// Generación de pesos y beneficios aleatorios
                pesos[i] = 1.0 + rnd.nextDouble() * 9.0;
                beneficios[i] = 10.0 + rnd.nextDouble() * 40.0;
                sumW += pesos[i];
            }
            
            ip_base solver = new ip_base(pesos, beneficios, sumW * 0.5);// Capacidad al 50% de la suma de pesos
            solver.resolver();
        }
    }
}