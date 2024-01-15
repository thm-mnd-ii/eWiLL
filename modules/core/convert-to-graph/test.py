# import json
# import networkx as nx
# import matplotlib.pyplot as plt

# def create_graph_from_json(json_file):
#     with open(json_file, 'r') as file:
#         data = json.load(file)

#     G = nx.Graph()

#     for node_data in data:
#         # Extrahiere den Namen des Knotens aus der JSON-Datei
#         node_name_from_json = node_data["attributes"]["name"]
        
#         # Füge den Knoten zum Graphen hinzu
#         G.add_node(node_name_from_json)

#         # Extrahiere weitere Attribute und füge sie als separate Knoten hinzu
#         for key, value in node_data["attributes"].items():
#             if key != "name":
#                 G.add_node(value)
#                 G.add_edge(node_name_from_json, value)

#     return G

# # Beispielaufruf mit einer JSON-Datei namens 'data.json'
# graph = create_graph_from_json('DiagrammExample.json')

# # Zeige den Graphen mit Matplotlib
# nx.draw(graph, with_labels=True, font_weight='bold')
# plt.show()

# my_dict = {'A': ['Hallo'], 'B': ['Welt']}

# key = 'A'
# additional_string = ' OpenAI'

# if key in my_dict:
#     my_dict[key].append(additional_string)

# print(my_dict)

import json
from enum import Enum
import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt
import numpy as np
import re

dictionary = json.load(open("DiagrammExample.json", "r"))
dfNodes = pd.DataFrame.from_dict(dictionary["entities"])
dfEdges = pd.DataFrame.from_dict(dictionary["connections"])
edgesStart = dfEdges["startEntity"].tolist()
edgesEnd = dfEdges["endEntity"].tolist()
edgesStyle = dfEdges["style"].tolist()
# nodeAttributes = dfNodes["attributes"].to_dict()
nodeAttributes = dfNodes["attributes"].to_dict()
edgeAttributesDict = {}

print(edgesStart)
print(edgesEnd)
# print(dfNodes.loc[10, "attributes"])

# # Annahme: df ist dein Pandas DataFrame
# # Beispiel DataFrame:
# data = {'id': [1, 2, 4, 6], 'value': ['A', 'B', 'C', 'D']}
# df = pd.DataFrame(data)

# # Umwandlung in ein Dictionary
# result_dict = dfNodes.set_index('id').to_dict(orient='index')

# # Fehlende Einträge mit leeren Listen auffüllen
# max_id = dfNodes['id'].max()
# for i in range(1, max_id + 1):
#     result_dict.setdefault(i, {})

# print(result_dict)

# # result_dict = dfNodes.set_index('id')['attributes'].to_dict()

# # # Fehlende Einträge mit leeren Listen auffüllen
# # max_id = dfNodes['id'].max()
# # for i in range(1, max_id + 1):
# #     result_dict.setdefault(i, [])

# # print(result_dict)

# # result_attributes_dict = {}

# # for key, attributes_list in result_dict.items():
# #     extracted_values = [item['name'] for item in attributes_list]
# #     result_attributes_dict[key] = extracted_values

# # print(result_attributes_dict)

# # new_dict = {}

# # for key, values in result_attributes_dict.items():
# #     new_values = [f'{value} {key}' for value in values]
# #     new_dict[key] = new_values

# # print(new_dict)

# result_attributes_dict = {}

# for key, attributes_list in result_dict.items():
#     for item in attributes_list:
#         attribute_name = item['name']
#         result_attributes_dict[f'{attribute_name} {key}'] = item

# print(result_attributes_dict)

# i = 0
# new_dict = {}
# new_key = 1
# missing_node = False

# for key, values in result_dict.items():
#     node_id = dfNodes.loc[i, 'id']
#     if node_id == new_key:
#         new_dict[new_key] = [item['name'] for item in values.get('attributes', [])]
#         i += 1
#     else:
#         new_dict[new_key] = []
#         missing_node = True

#     if not missing_node:
#         new_key += 1

#     missing_node = False

# print(new_dict)


# import json
# from enum import Enum
# import pandas as pd
# import networkx as nx
# import matplotlib.pyplot as plt


# dictionary = json.load(open("DiagrammExample.json", "r"))
# dfNodes = pd.DataFrame.from_dict(dictionary["entities"])
# dfEdges = pd.DataFrame.from_dict(dictionary["connections"])
# edgesStart = dfEdges["startEntity"].tolist()
# edgesEnd = dfEdges["endEntity"].tolist()
# edgesStyle = dfEdges["style"].tolist()
# nodeAttributes = dfNodes["attributes"].to_dict()
# edgeAttributesDict = {}

# print(edgesStart)
# print(edgesEnd)

G = nx.DiGraph()
nx.set_edge_attributes(G, edgesStyle, "styles")

result_dict = dfNodes.set_index('id')['attributes'].to_dict()

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

# Zeige alle Attribute der Knoten an
node_data = G.nodes(data=True)
print("Node Attributes:")
for node, data in node_data:
    print(f"Node {node}: {data}")

nx.draw(G, with_labels=True)
plt.savefig("output.png")
