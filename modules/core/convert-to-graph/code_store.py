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
# print(nodeAttributes)
# print(nodeAttributes[0][0]["name"])
# print(nodeAttributes[0][5]["name"])
# print(dfNodes["attributes"][0])
# for entry in edgesStart:
    
# for key in nodeAttributes:
#     relevantValues = nodeAttributes[key]
    # relevantValues.todict()
    # print(relevantValues)
# i = 0
# for edge in dfEdges:
#     edges.append(dfEdges["startEntity"][i])
#     edges.append(dfEdges["endEntity"])
# print(edges)
# print(dictionary)
# print(dictionary["connections"])
# print(dictionary["entities"])
# print(dictionary.keys())
# print(dfEdges)
G = nx.DiGraph()
nx.set_edge_attributes(G, edgesStyle, "styles")
# for entity in dfNodes:
#     G.add_nodes_from(dfNodes["id"])
# G.add_nodes_from(dfNodes["id"])

# DAS HIER CODE GUT
# i = 0
# new_dict = {}
# new_key = 1
# missing_node = False
# for key, values in nodeAttributes.items():
#     if missing_node == True:
#         # new_key += 1
#         i -= 1
#         missing_node = False
#     node_id = dfNodes.loc[i, 'id']
#     if node_id == new_key:
#         new_dict[new_key] = [item['name'] for item in values]
#     else:
#         new_dict[new_key] = []
#         missing_node = True
#     i += 1
#     new_key += 1
# print(new_dict)

result_dict = dfNodes.set_index('id')['attributes'].to_dict()

# Fehlende Einträge mit leeren Listen auffüllen
max_id = dfNodes['id'].max()
for i in range(1, max_id + 1):
    result_dict.setdefault(i, [])

print(result_dict)

result_attributes_dict = {}

for key, attributes_list in result_dict.items():
    extracted_values = [item['name'] for item in attributes_list]
    result_attributes_dict[key] = extracted_values

print(result_attributes_dict)




# i = 0
# new_dict = {}
# new_key = 1
# missing_node = False
# for key, values in nodeAttributes.items():
#     if missing_node == True:
#         # new_key += 1
#         i -= 1
#         missing_node = False
#     node_id = dfNodes.loc[i, 'id']
#     if node_id == new_key:
#         new_dict[new_key] = [item['name'] for item in values]
#     else:
#         new_dict[new_key] = []
#         missing_node = True
#     i += 1
#     new_key += 1
# print(new_dict)

# i = 0
# new_dict = {}
# new_key = 1

# while new_key <= len(dfNodes):
#     node_id = dfNodes.loc[i, 'id']

#     if node_id == new_key:
#         new_dict[new_key] = [item['name'] for item in nodeAttributes.get(new_key, [])]
#         i += 1
#     else:
#         new_dict[new_key] = []

#     new_key += 1

# print(new_dict)


# i = 0
# new_dict = {}
# new_key = 1
# missing_node = False

# for _ in range(len(nodeAttributes)):
#     node_id = dfNodes.loc[i, 'id']

#     if node_id == new_key:
#         new_dict[new_key] = [item['name'] for item in nodeAttributes.get(new_key, [])]
#         i += 1
#         new_key += 1
#     else:
#         new_dict[new_key] = []
#         missing_node = True
#         new_key += 1

#     if not missing_node:
#         i += 1

#     missing_node = False

# print(new_dict)




# Füge Kanten basierend auf edgesStart und edgesEnd hinzu
for i in range(len(edgesStart)):
    # if i < 8:
    G.add_edge(edgesStart[i], edgesEnd[i])

# Füge Kanten basierend auf dem neuen Dictionary hinzu
# for key, values in new_dict.items():
for key, values in result_attributes_dict.items():
    for value in values:
        # if key < 8:
        G.add_edge(key, value)

i = 0
for entry in edgesStyle:
    start = edgesStart[i]
    end = edgesEnd[i]
    style = edgesStyle[i]
    # G.edges[edgesStart[i], edgesEnd[i]]["styles"]= edgesStyle[i]
    if i < 8:
        G.edges[start, end]["styles"]= style
        i += 1

edgeAttributes = nx.get_edge_attributes(G, "styles")
print(edgeAttributes)

nx.draw(G, with_labels=True)
plt.savefig("output.png")


# for entry in edgesStart:
#     G.add_edge(edgesStart[i], edgesEnd[i])
#     key = edgesStart[i]
#     j = 0
#     G.add_edge(key, new_dict[key])
#     # if key in nodeAttributes:
#     #     while j < len(nodeAttributes[key]):
#     #         node_values = nodeAttributes[key]
#     #         key = edgesStart[i]
#     #         # print(node_values)
#     #         if key not in new_dict:
#     #             # new_dict[key] = [nodeAttributes[key][j]["name"]]
#     #             new_dict[key] = []
#     #         new_dict[key].append(nodeAttributes[key][j]["name"])
#             # elif key in new_dict:
#             #     # additional_string = node_values[j]["name"]
#             #     new_dict[key].append(node_values[key][j]["name"])
#             # # new_dict = {key: node_values[j]["name"]}
#             # # if(edgesStart[i] == new_dict[key]):
#             # G.add_edge(edgesStart[i], new_dict[key])
#             # print(new_dict)
#             # j += 1
#     # for key2 in new_dict:
#         # G.add_edges_from(new_dict[1])
#     # G.add_edge(edgesStart[i], nodeAttributes)
#     # G.edges[edgesStart[i], edgesEnd[i]]["styles"]= edgesStyle[i]
#     i += 1
# # print(new_dict)

# i = 0
# new_dict = {}
# for entry in edgesStart:
#     G.add_edge(edgesStart[i], edgesEnd[i])
#     key = edgesStart[i]
    
#     if key in nodeAttributes:
#         # Überprüfe, ob der Schlüssel bereits im neuen Dictionary existiert
#         if key not in new_dict:
#             new_dict[key] = {}

#         # Füge die Attribute des aktuellen Knotens zum neuen Dictionary hinzu
#         for attribute in nodeAttributes[key]:
#             attribute_name = attribute.get("name")
#             attribute_value = attribute["name"]
            
#             # Erweitere den Wert des Schlüssels im neuen Dictionary um das aktuelle Attribut
#             if attribute_name in new_dict[key]:
#                 # Wenn der Schlüssel bereits existiert, füge das Attribut zu einer Liste hinzu
#                 # new_dict[key][attribute_name].append(attribute_value)
#                 new_dict[key][attribute_name].append(attribute_name)
#             else:
#                 # Wenn der Schlüssel noch nicht existiert, erstelle eine neue Liste mit dem Attribut
#                 # new_dict[key][attribute_name] = [attribute_value]
#                 new_dict[key][attribute_name] = [attribute_name]
            
#     i += 1

# print(new_dict)



# i = 0
# new_dict = {}

# for entry in edgesStart:
#     g.add_edge(edgesStart[i], edgesEnd[i])
#     key = edgesStart[i]
    
#     if key in nodeAttributes:
#         # Überprüfe, ob der Schlüssel bereits im neuen Dictionary existiert
#         if key not in new_dict:
#             new_dict[key] = {}

#         # Füge die Attribute des aktuellen Knotens zum neuen Dictionary hinzu
#         for attribute in nodeAttributes[key]:
#             attribute_name = attribute.get("name")
#             print(attribute_name)
#             new_dict[key][attribute_name] = attribute_name
#             g.add_edge(edgesStart[i], attribute_name)
            
#     i += 1

# print(new_dict)



# g.add_edges_from(nodeAttributes)
# i = 0
# j = 0

# helpDict = {}
# for value in edgesStart:
#     if(i < len(nodeAttributes)): 
#         j=0
#         for key, value in nodeAttributes.items():
#         # if (j<len(nodeAttributes[i])):
#             if(j<len(nodeAttributes[i])):
#                 # helpDict[i] = nodeAttributes[i][j]["name"]
#                 # helpDict[i] = i
#                 helpDict.update(nodeAttributes[i][j], nodeAttributes[i][j]["name"])
#             j+=1
#     i+=1

# print(helpDict)
# for node in g.nodes:
#     # helpArray = np.asarray(nodeAttributes[i])
#     for attributes in nodeAttributes[i]:
#         # helpArray = len(nodeAttributes[i])
#         # helpArray[i][j] = nodeAttributes[i][j]
#         g.add_edge(edgesStart[i], helpArray[i])
#         j+=1
#     i+=1
# for nodes in edgesStart:
#     # helpList = [[],[]]
#     # helpList = nodeAttributes[i]
#     # print(helpList)
#     # print(helpList)
#     # i += 1
#     for attributes in nodeAttributes[i]:
#         # helpString = str(nodeAttributes[i])
#         # print(helpString[0])
#         # g.add_edge(edgesStart[i], helpList[[i],[j]])
#         # g.add_edge(edgesStart[i], helpString)
#         # print(helpList[1])
#         j += 1
#     i += 1
#     g.add_edge(dfEdges["startEntity"], dfEdges["endEntity"])
# g.add_edges_from(dfEdges)
# g.add_node(1)
# g.add_node(2)
# g.add_edge(1,2)


# file = pd.read_json("DiagrammExample.json", lines=True)
# print(file)

# with open("DiagrammExample.json", "r") as f:
#     data = json.load(f)
# df = pd.DataFrame(data)
# print(df)
# g = nx.DiGraph(dictionary)
# nx.draw(g)








# # Füge Kanten basierend auf edgesStart und edgesEnd hinzu
# for i in range(len(edgesStart)):
#     G.add_edge(edgesStart[i], edgesEnd[i])

# # Füge Kanten basierend auf dem neuen Dictionary hinzu
# for key, values in new_dict.items():
#     for value in values:
#         G.add_edge(key, value)

















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
# node_attributes = df_nodes["attributes"].to_dict()
# edge_attributes_dict = {}

# print(edges_start)
# print(edges_end)
# print(df_nodes)
G = nx.DiGraph()
# nx.set_edge_attributes(G, edges_types, "styles")


result_dict = df_nodes.set_index('id')['attributes'].to_dict()

# # Fehlende Einträge mit leeren Listen auffüllen
# max_id = df_nodes['id'].max()
# for i in range(1, max_id + 1):
#     result_dict.setdefault(i, [])

# print(result_dict)

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
    
    # # Füge Kante zum Graphen hinzu und weise Attribute zu
    # G.add_edge(start, end, style=edge_style)

    # # Weise das Attribut zum Graphen hinzu
    # G[start][end]['style'] = edge_style

    # Weise das Attribut zum Graphen hinzu (wenn die Kante existiert) oder füge die Kante mit Attribut hinzu (wenn sie nicht existiert)
    if G.has_edge(start, end):
        G[start][end]['style'] = edge_style
    else:
        G.add_edge(start, end, style=edge_style)

# Ausgabe des Graphen mit Attributen
print("Graph with Attributes:")
print(G.edges(data=True))

# print(edges_types)
# i = 0
# for entry in edges_types:
#     start = edges_start[i]
#     end = edges_end[i]
#     style = edges_types[i]
#     G.edges[start, end]["styles"]= style
#     i += 1

# edgeAttributes = nx.get_edge_attributes(G, "styles")
# print(edgeAttributes)

nx.draw(G, with_labels=True)
plt.savefig("output.png")