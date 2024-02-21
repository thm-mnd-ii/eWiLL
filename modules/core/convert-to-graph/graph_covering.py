import networkx as nx
from networkx.algorithms import isomorphism
from graph_conversion_service import convert_to_graph

# Initialisierung der Graphen
abgabe_datei = "ReferenceSolution.json"
loesung_datei = "StudentSolution1.json"
abgabe_graph = convert_to_graph(abgabe_datei) # Initialisieren des Graphen für die Abgabe
musterloesung_graph = convert_to_graph(loesung_datei) # Initialisieren des Graphen für die Musterlösung

# Ursprungsgraphen
anzahl_knoten_abgabe = abgabe_graph.number_of_nodes()
anzahl_kanten_abgabe = abgabe_graph.number_of_edges()
anzahl_knoten_musterloesung = musterloesung_graph.number_of_nodes()
anzahl_kanten_musterloesung = musterloesung_graph.number_of_edges()


# Hilfsfunktion
def finde_groessten_match(startknoten, besuchte_knoten, besuchte_kanten, aktueller_graph, is_in_musterloesung):
    besuchte_knoten.add(startknoten)
    groesster_graph = nx.DiGraph(aktueller_graph)

    for nachbar in abgabe_graph.successors(startknoten):
        if aktueller_graph.has_edge(startknoten, nachbar) or (startknoten, nachbar) in besuchte_kanten:
            continue

        aktueller_graph.add_edge(startknoten, nachbar)
        besuchte_kanten.add((startknoten, nachbar))

        if is_in_musterloesung == (nachbar in musterloesung_graph.successors(startknoten)):
            temp_graph = finde_groessten_match(nachbar, besuchte_knoten, besuchte_kanten, aktueller_graph, is_in_musterloesung)
            if temp_graph.number_of_edges() > groesster_graph.number_of_edges():
                groesster_graph = temp_graph


    return groesster_graph

# # Erzeuge zwei identische gerichtete Graphen (für Testzwecke)
# abgabe_graph = nx.DiGraph()
# musterloesung_graph = nx.DiGraph()

# abgabe_graph.add_edges_from([(1, 2), (2, 3), (2, 1), (3, 1), (3, 4), (4, 5)])
# musterloesung_graph.add_edges_from([(1, 2), (2, 3), (2, 1), (3, 1), (3, 4), (4, 5)])

# Ursprungsgraphen
anzahl_knoten_abgabe = abgabe_graph.number_of_nodes()
anzahl_kanten_abgabe = abgabe_graph.number_of_edges()
anzahl_knoten_musterloesung = musterloesung_graph.number_of_nodes()
anzahl_kanten_musterloesung = musterloesung_graph.number_of_edges()

# Matching-Prozess
matched = []  # Array für gematchte Teilgraphen
nicht_gematcht = []  # Array für nicht gematchte Teilgraphen
besuchte_knoten = set()
besuchte_kanten = set()

summe_knoten_gematcht = 0
summe_kanten_gematcht = 0
summe_knoten_nicht_gematcht = 0
summe_kanten_nicht_gematcht = 0

for knoten in abgabe_graph.nodes():
    if knoten not in besuchte_knoten:
        if knoten in musterloesung_graph.nodes():
            ergebnis = finde_groessten_match(knoten, besuchte_knoten, besuchte_kanten, nx.DiGraph(), True)
            if ergebnis.edges():
                matched.append(ergebnis)
                summe_knoten_gematcht += ergebnis.number_of_nodes()
                summe_kanten_gematcht += ergebnis.number_of_edges()
        else:
            ergebnis = finde_groessten_match(knoten, besuchte_knoten, besuchte_kanten, nx.DiGraph(), False)
            if ergebnis.edges():
                nicht_gematcht.append(ergebnis)
                summe_knoten_nicht_gematcht += ergebnis.number_of_nodes()
                summe_kanten_nicht_gematcht += ergebnis.number_of_edges()


# Ausgabe der Ergebnisse
print("Ursprungsgraph abgabe_graph:")
print(f"Anzahl der Knoten: {anzahl_knoten_abgabe}, Anzahl der Kanten: {anzahl_kanten_abgabe}")

print("\nUrsprungsgraph musterloesung_graph:")
print(f"Anzahl der Knoten: {anzahl_knoten_musterloesung}, Anzahl der Kanten: {anzahl_kanten_musterloesung}")

print("\nGematchte Teilgraphen:")
for i, graph in enumerate(matched):
    anzahl_knoten_matched = graph.number_of_nodes()
    anzahl_kanten_matched = graph.number_of_edges()
    print(f"\nTeilgraph {i + 1}:")
    print(f"Anzahl der gematchten Knoten: {anzahl_knoten_matched}, Anzahl der gematchten Kanten: {anzahl_kanten_matched}")
    for edge in graph.edges():
        startknoten, endknoten = edge
        print(f"  - Kante: ({startknoten}, {endknoten}), Bezeichnungen: {startknoten} -> {endknoten}")

print("\nNicht gematchte Teilgraphen:")
for i, graph in enumerate(nicht_gematcht):
    anzahl_knoten_nicht_gematcht = graph.number_of_nodes()
    anzahl_kanten_nicht_gematcht = graph.number_of_edges()
    print(f"\nTeilgraph {i + 1}:")
    print(f"Anzahl der nicht gematchten Knoten: {anzahl_knoten_nicht_gematcht}, Anzahl der nicht gematchten Kanten: {anzahl_kanten_nicht_gematcht}")
    for edge in graph.edges():
        startknoten, endknoten = edge
        print(f"  - Kante: ({startknoten}, {endknoten}), Bezeichnungen: {startknoten} -> {endknoten}")

# Gesamtsummen ausgeben
gesamt_summe_knoten_gematcht = summe_knoten_gematcht
gesamt_summe_kanten_gematcht = summe_kanten_gematcht + summe_kanten_nicht_gematcht

print("\nGesamtsummen:")
print(f"Summe aller gematchten Knoten: {gesamt_summe_knoten_gematcht}")
print(f"Summe aller gematchten Kanten: {gesamt_summe_kanten_gematcht}")
