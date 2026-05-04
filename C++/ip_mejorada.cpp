#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <iomanip>
#include <random>

using namespace std;

struct Elemento { double w, p, r; };
bool comp(Elemento a, Elemento b) { return a.r > b.r; }

class SolverIPMejorado {
    int n;
    double mejor_p, cap;
    vector<Elemento> items;
    unsigned long long nodos_visitados; // Contador real de nodos

    double cota(int nivel, double peso_a, double benef_a) {
        double res = benef_a, p_acc = peso_a;
        for (int i = nivel; i < n; ++i) {
            if (p_acc + items[i].w <= cap) { p_acc += items[i].w; res += items[i].p; }
            else { res += items[i].p * ((cap - p_acc) / items[i].w); break; }
        }
        return res;
    }

    void bnb(int nivel, double peso_a, double benef_a) {
        nodos_visitados++; // Incremento por cada nodo evaluado
        if (benef_a > mejor_p) mejor_p = benef_a;
        if (nivel == n || cota(nivel, peso_a, benef_a) <= mejor_p) return;
        if (peso_a + items[nivel].w <= cap) bnb(nivel + 1, peso_a + items[nivel].w, benef_a + items[nivel].p);
        bnb(nivel + 1, peso_a, benef_a);
    }
public:
    SolverIPMejorado(const vector<double>& w_in, const vector<double>& p_in, double c) 
        : mejor_p(0.0), cap(c), nodos_visitados(0) {
        n = w_in.size();
        for (int i=0; i<n; ++i) items.push_back({w_in[i], p_in[i], p_in[i]/w_in[i]});
        sort(items.begin(), items.end(), comp);
    }
    void resolver() {
        auto inicio = chrono::high_resolution_clock::now();
        bnb(0, 0, 0);
        auto fin = chrono::high_resolution_clock::now();
        chrono::duration<double> transcurrido = fin - inicio;
        cout << "N: " << left << setw(4) << n << " | Tiempo: " 
             << fixed << setprecision(15) << transcurrido.count() << "s | Nodos: " 
             << nodos_visitados << endl;
    }
};

int main() {
    vector<int> n_pruebas = {20, 40, 60, 100, 1000}; 
    mt19937 rng(42); 
    uniform_real_distribution<double> dist_w(10.0, 50.0);
    uniform_real_distribution<double> dist_p(50.0, 100.0);

    for (int n_actual : n_pruebas) {
        vector<double> pesos, beneficios;
        double suma_pesos = 0;
        for (int i = 0; i < n_actual; ++i) {
            pesos.push_back(dist_w(rng));
            beneficios.push_back(dist_p(rng));
            suma_pesos += pesos.back();
        }
        SolverIPMejorado solver(pesos, beneficios, suma_pesos * 0.5);
        solver.resolver();
    }
    return 0;
}