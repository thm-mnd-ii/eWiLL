import axios, { responseEncoding } from "axios";
import Diagram from "../model/diagram/Diagram";
import Category from "../model/diagram/Category";
import DiagramType from "../enums/DiagramType";
import authHeader from "./auth-header";
import { DiagnosticCategory } from "typescript";

class DiagramService {
  
  getDiagramsByUserId(userId: number): Promise<Diagram[]> {
    return new Promise((resolve, reject) => {
      const diagrams:Diagram[] = [];
      axios
      .get("/api/diagram/user/" + userId, { headers: authHeader()})
      .then((response) => {
        for(const entry of response.data){
          const tmpDiagram = {} as Diagram
          tmpDiagram.id = entry.id;
          tmpDiagram.ownerId = entry.ownerId;
          tmpDiagram.name = entry.name;
          tmpDiagram.config = entry.conig;
          tmpDiagram.connections = entry.connections;
          tmpDiagram.entities = entry.entities;
          const tmpCategory = {} as Category;
          tmpCategory.id = entry.categoryId
          tmpCategory.userId = entry.ownerId;
          tmpDiagram.category = tmpCategory;
          diagrams.push(tmpDiagram) 
        } 
        resolve(diagrams)
    }).catch((error) => {
      reject(error)
    });
    });
  }

  postDiagram(diagram: Diagram){
    return axios.post("/api/diagram/", { id:0, ownerId: diagram.ownerId, name: diagram.name, config: diagram.config, entities: diagram.entities, connections: diagram.connections, categoryId: diagram.category.id })        
  }

  putDiagram(diagram: Diagram){
    return axios.put("/api/diagram/", {id:diagram.id, ownerId: diagram.ownerId, name: diagram.name, config: diagram.config, entities: diagram.entities, connections: diagram.connections, categoryId: diagram.category.id})
    .then((response) => {
      console.log(response)
    }).catch((error) => {
      console.log(error)
    })
  }

  deleteDiagram(diagram: Diagram){
    return axios.delete("/api/diagram/" + diagram.id, { headers: authHeader()})
  }

  async getCategories(userId: number):Promise<Category[]>{   
    return new Promise((resolve, reject) => {
      const categories:Category[] = [];
      axios.get("/category/user/" + userId, { headers: authHeader()})
      .then((response) => {
        for(const entry of response.data){
          const tmpCategory = {} as Category
          tmpCategory.id = entry.id;
          tmpCategory.userId = entry.userId
          tmpCategory.name = entry.name;
          categories.push(tmpCategory)
        } 
        resolve(categories)       
      }).catch((error) => {
        reject(error)
      })    
    })
  };
     

  postCategory(name: string, userid: number){
    return axios.post("/category", { name:name, userid: userid }) 
  }

  deleteCategory(category: Category) {
    return axios.delete("/category/" + category.id, { headers: authHeader()})
  }

  getDiagramsWithCategory(categories:Category[], diagrams:Diagram[]): Map<string, Diagram[]>{
    const map: Map<string, Diagram[]> = new Map();

    categories.forEach(category => {
      const diagramsSameCategory: Diagram[] = []; 
      diagrams.forEach(diagram => {
        if(category.id == diagram.category.id){
          diagram.category.name = category.name;
          diagramsSameCategory.push(diagram);
        }
      })
    map.set(category.name, diagramsSameCategory)
    });
    return map;
  }
}

export default new DiagramService();
