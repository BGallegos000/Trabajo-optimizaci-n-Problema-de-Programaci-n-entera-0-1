#include <iostream>
#include <vector>
#include <chrono>
#include <iomanip>
#include <random>

using namespace std;

class SolverIPBase {
    int n;
    double mejor_beneficio;
    const vector<double>& w;
    const vector<double>& p;
    double cap;
    unsigned long long nodos_visitados; // Contador real de nodos

    void backtrack(int nivel, double peso_cur, double benef_cur) {
        nodos_visitados++; // Incremento por cada nodo visitado
        if (benef_cur > mejor_beneficio) mejor_beneficio = benef_cur;
        if (nivel == n) return;
        if (peso_cur + w[nivel] <= cap) {
            backtrack(nivel + 1, peso_cur + w[nivel], benef_cur + p[nivel]);
        }
        backtrack(nivel + 1, peso_cur, benef_cur);
    }
public:
    SolverIPBase(const vector<double>& pesos, const vector<double>& beneficios, double capacidad)
        : n(pesos.size()), mejor_beneficio(0.0), w(pesos), p(beneficios), cap(capacidad), nodos_visitados(0) {}

    void resolver() {
        auto inicio = chrono::high_resolution_clock::now();
        backtrack(0, 0.0, 0.0);
        auto fin = chrono::high_resolution_clock::now();
        chrono::duration<double> transcurrido = fin - inicio;
        cout << "N: " << left << setw(3) << n << " | Tiempo: " 
             << fixed << setprecision(6) << transcurrido.count() << "s | Nodos visitados: " 
             << nodos_visitados << endl;
    }
};

int main() {
    vector<int> n_pruebas = {10, 15, 25, 30}; // Ajusta hasta 40 si quieres dejarlo corriendo
    mt19937 rng(42); 
    uniform_real_distribution<double> dist_w(1.0, 10.0);
    uniform_real_distribution<double> dist_p(10.0, 50.0);

    for (int n_actual : n_pruebas) {
        vector<double> pesos, beneficios;
        double suma_pesos = 0;
        for (int i = 0; i < n_actual; ++i) {
            pesos.push_back(dist_w(rng));
            beneficios.push_back(dist_p(rng));
            suma_pesos += pesos.back();
        }
        SolverIPBase solver(pesos, beneficios, suma_pesos * 0.5);
        solver.resolver();
    }
    return 0;
}