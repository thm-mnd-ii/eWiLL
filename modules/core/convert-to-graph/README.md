Erklärung der einzelnen Dateien:

graph_conversion_service.py:
Hier wird aus einer json-Datei Daten für Kanten und Knoten ausgelesen. Die Datei und der Pfad sind in dieser Version noch hardcoded. 
Die ausgelesenen Daten werden daraufhin genutzt, um einen Graphen mit der Bibliothek "networkx" zu erstellen.
Die Kanten des Graphen sind gerichtet. Attribute eines Knoten werden als eigene Knoten behandelt.
Jede Kante zwischen Knoten (die selbst kein Attribut sind) haben einen type als Eigenschaft, welcher die Beziehung zwischen den Knoten widerspiegelt.
Nicht-Attributs-Knoten haben außerdem einen Entitätstyp. Attributsknoten können die Eigenschaft "PK: true" haben, wenn das Attribut ein primary key ist.

DiagrammExample.json:
Daten, die zur Erstellung des Graphen ausgelesen werden.

output.png:
Bildliche Darstellung des resultierenden Graphen aus "graph_conversion_service.py". (Wird nach jedem Ausführen des Services überschrieben)

test.py:
Wurde zum testen kleinerer Codeschnipsel und neuer Ansätze benutzt, um die Funktionalität zu testen, bevor sie in den eigentlichen Service übernommen wurden.

code_store.py:
Sammlung von nicht oder teilweise erfolgreichen Quellcode-Lösungsansätzen. Spiegelt teilweise die Zwischenstände im Entwicklungsverlauf wider.