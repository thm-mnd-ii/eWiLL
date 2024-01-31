import json
import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

dictionary = json.load(open("DiagrammExample.json", "r"))
df_nodes = pd.DataFrame.from_dict(dictionary["entities"])
df_edges = pd.DataFrame.from_dict(dictionary["connections"])
edges_start = df_edges["startEntity"].tolist()
edges_end = df_edges["endEntity"].tolist()
edges_types = df_edges["style"].tolist()

G = nx.DiGraph()

result_dict = df_nodes.set_index('id')['attributes'].to_dict()

result_attributes_dict = {}

for key, attributes_list in result_dict.items():
    extracted_values = [item['name'] for item in attributes_list]
    result_attributes_dict[key] = extracted_values

print(result_attributes_dict)

new_dict = {}

for key, values in result_attributes_dict.items():
    new_values = [f'{value} {key}' for value in values]
    new_dict[key] = new_values

print(new_dict)

# Füge Knoten zum Graphen hinzu
for key, values in new_dict.items():
    G.add_node(key)
    for value in values:
        G.add_node(value)

# Füge Kanten zum Graphen hinzu
for key, values in new_dict.items():
    for value in values:
        G.add_edge(key, value)

        # Überprüfe, ob 'pkey' für das Attribut True ist und füge 'PK' als Attribut zum Attributknoten hinzu
        attribute_name = value.split()[0]  # Extrahiere den Attributnamen (z.B., 'ID' aus 'ID 1')
        if any(attribute['name'] == attribute_name and attribute.get('pkey', False) for attribute in result_dict[key]):
            G.nodes[value]['PK'] = True


# Mapping der Werte in der "type"-Spalte
type_mapping = {0: 'ENTITY', 1: 'ENTITYRELATIONSHIP', 2: 'RELATIONSHIP'}

# Ändere die Werte in der "type"-Spalte entsprechend dem Mapping
df_nodes['type'] = df_nodes['type'].map(type_mapping)

# Erstelle ein Dictionary aus den Spalten "id" und "type"
id_type_dict = df_nodes.set_index('id')['type'].to_dict()
# Ausgabe des Dictionarys
print("ID_Type Dictionary:")
print(id_type_dict)

# Füge 'Type'-Attribute für die Knoten im Graphen hinzu
for node, data in G.nodes(data=True):
    if node in id_type_dict:
        data['type'] = id_type_dict[node]

# Ausgabe des Graphen mit den 'Type'-Attributen
print("Graph with Type Attributes:")
print(G.nodes(data=True))

# Zeige alle Attribute der Knoten an
node_data = G.nodes(data=True)
print("Node Attributes:")
for node, data in node_data:
    print(f"Node {node}: {data}")


# Mapping der Werte für Kantentyp zwischen Knoten
style_mapping = {0: 'ZERO_TO_ONE', 1: 'ZERO_TO_MANY', 2: 'ONE_TO_MANY', 3: 'ONE_TO_ONE'}

# Füge Kanten zum Graphen hinzu und weise Attribute zu
for start, end, edge_type in zip(edges_start, edges_end, edges_types):
    # Ersetze den Attributwert gemäß dem Mapping
    edge_style = style_mapping.get(edge_type, edge_type)
    
    # Weise das Attribut zum Graphen hinzu (wenn die Kante existiert) oder füge die Kante mit Attribut hinzu (wenn sie nicht existiert)
    if G.has_edge(start, end):
        G[start][end]['style'] = edge_style
    else:
        G.add_edge(start, end, style=edge_style)

# Ausgabe des Graphen mit Attributen
print("Graph with Attributes:")
print(G.edges(data=True))

nx.draw(G, with_labels=True)
plt.savefig("output.png")