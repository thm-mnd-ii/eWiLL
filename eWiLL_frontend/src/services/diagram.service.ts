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
          ownerId: 1,
          name: "name",
          config: {
            id: 1,
            diagramType: DiagramType.SERM,
          },
          entities: [],
          connections: [],
          category: {
            id: 1,
            userId: 1,
            name: "Category1",
          },
        },
        {
          id: 2,
          ownerId: 2,
          name: "name2",
          config: {
            id: 2,
            diagramType: DiagramType.SERM,
          },
          entities: [],
          connections: [],
          category: {
            id: 2,
            userId: 1,
            name: "Category2",
          },
        },{
          id: 3,
          ownerId: 1,
          name: "Mein Diagramm",
          config: {
            id: 1,
            diagramType: DiagramType.SERM,
          },
          entities: [],
          connections: [],
          category: {
            id: 1,
            userId: 1,
            name: "Category1",
          },
        },{
          id: 4,
          ownerId: 1,
          name: "Mein Diagramm2",
          config: {
            id: 1,
            diagramType: DiagramType.SERM,
          },
          entities: [],
          connections: [],
          category: {
            id: 2,
            userId: 1,
            name: "Category2",
          }},{
            id: 5,
            ownerId: 1,
            name: "Dia ohne Kategorie",
            config: {
              id: 1,
              diagramType: DiagramType.SERM,
            },
            entities: [],
            connections: [],
            category: {
              id: 0,
              userId: 1,
              name: "Keine Kategorie",
            }}];
        
        return diagrams;
      };

      getDiagramsTest(userId: number): Diagram[] {
        //TODO: API CALL
        const diagrams: Diagram[] = [];
        return diagrams;
      }

      saveDiagram(diagram: Diagram){
        axios.post(API_URL, {
          diagram
        }).then((response) => {
          console.log(response)
        }
        )
      }

      getCategories(userId: number):Category[] {
    
        const categories: Category[] = [{
          id: 0,
          userId: 1,
          name: "Keine Kategorie",
        },{
          id: 1,
          userId: 1,
          name: "Category1",
        },
        {
          id: 2,
          userId: 2,
          name: "Category2",
        }];
       
        return categories;
      };

      getCategoriesTest(userId: number):Category[]{
      //TODO: FIX API CALL
      const categories:Category[] = [];
      axios
      .get("http://localhost:8080/category/user/" + userId, { headers: authHeader()}) 
      .then((response) => {      
        for(const entry of response.data){
          const tmpCategory = {} as Category
          tmpCategory.id = entry.id;
          tmpCategory.userId = entry.userId
          tmpCategory.name = entry.name;
          categories.push(tmpCategory)
        }  
      });
      return categories;
      };
  //{ headers: authHeader() } {withCredentials: true}
      getDiagramsWithCategory(userId: number): Map<string, Diagram[]>{
        const map: Map<string, Diagram[]> = new Map();
        const allCategories = this.getCategories(1);
        const allDiagrams = this.getDiagrams(1);
        allCategories.forEach(category => {
          const diagramsSameCategory: Diagram[] = []; 
          allDiagrams.forEach(diagram => {
            if(category.name == diagram.category.name){
              diagramsSameCategory.push(diagram);
            }
          })
          map.set(category.name, diagramsSameCategory)
        }) 
        return map;
      }
}

export default new DiagramService();
