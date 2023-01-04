import axios, { responseEncoding } from "axios";
import Diagram from "../model/diagram/Diagram";
import Category from "../model/diagram/Category";
import DiagramType from "../enums/DiagramType";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/diagram/";

class DiagramService {
  getDiagrams(userId: number):Diagram[] {
    
        const diagrams: Diagram[] = [{
          id: 1,
          ownerId: 2,
          name: "name",
          config: {
            id: 1,
            diagramType: DiagramType.SERM,
          },
          entities: [],
          connections: [],
          category: {
            id: 1,
            userId: 2,
            name: "Category1",
          },
        },
        {
          id: 3,
          ownerId: 2,
          name: "Mein Diagramm",
          config: {
            id: 1,
            diagramType: DiagramType.SERM,
          },
          entities: [],
          connections: [],
          category: {
            id: 1,
            userId: 2,
            name: "Category1",
          },
        },];       
        return diagrams;
      };

      getDiagramsTest(userId: number): Promise<Diagram[]> {
        return new Promise((resolve, reject) => {
          const diagrams:Diagram[] = [];
          axios
          .get(API_URL + "user/" + userId, { headers: authHeader()})
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
        axios.post(API_URL, { id:0, ownerId: diagram.ownerId, name: diagram.name, config: diagram.config, entities: diagram.entities, connections: diagram.connections, categoryId: diagram.category.id })
        .then((response) => {
          console.log(response)
        }).catch((error) => {
          console.log(error)
        });          
      }

      putDiagram(diagram: Diagram){
        axios.put(API_URL, {id:diagram.id, ownerId: diagram.ownerId, name: diagram.name, config: diagram.config, entities: diagram.entities, connections: diagram.connections, categoryId: diagram.category.id})
        .then((response) => {
          console.log(response)
        }).catch((error) => {
          console.log(error)
        })
      }

      getCategoriesTest(userId: number):Category[]{
        const categories: Category[] = [];
        const category = {} as Category;
        category.id = 1;
        category.name = "Category1";
        category.userId = 2;
        categories.push(category);
        return categories;
      }

      async getCategories(userId: number):Promise<Category[]>{   
      return new Promise((resolve, reject) => {
        const categories:Category[] = [];
        axios.get("http://localhost:8080/category/user/" + userId, { headers: authHeader()})
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
        return axios.post("http://localhost:8080/category", { name:name, userid: userid }) 
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
