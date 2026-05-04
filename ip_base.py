import time
import random

class IPBase:
    def __init__(self, pesos, beneficios, capacidad):
        self.pesos = pesos
        self.beneficios = beneficios
        self.capacidad = capacidad
        self.n = len(pesos)
        self.mejor_beneficio = 0.0

    def backtracking(self, nivel, peso_actual, beneficio_actual):
        if beneficio_actual > self.mejor_beneficio:
            self.mejor_beneficio = beneficio_actual
        if nivel == self.n:
            return
        if peso_actual + self.pesos[nivel] <= self.capacidad:
            self.backtracking(nivel + 1, peso_actual + self.pesos[nivel], beneficio_actual + self.beneficios[nivel])
        self.backtracking(nivel + 1, peso_actual, beneficio_actual)

def generar_instancia(n):
    random.seed(42) # Semilla fija para reproducibilidad
    pesos = [random.uniform(5, 25) for _ in range(n)]
    beneficios = [random.uniform(10, 50) for _ in range(n)]
    capacidad = sum(pesos) * 0.5 
    return pesos, beneficios, capacidad

if __name__ == "__main__":
    for n_test in [10, 15, 20, 25, 30, 40]: # Reducimos el tamaño para evitar tiempos excesivos
        w, p, cap = generar_instancia(n_test)
        solucionador = IPBase(w, p, cap)
        inicio = time.perf_counter()
        solucionador.backtracking(0, 0.0, 0.0)
        fin = time.perf_counter()
        print(f"N: {n_test} | Beneficio: {solucionador.mejor_beneficio:.2f} | Tiempo: {fin - inicio:.6f}s")