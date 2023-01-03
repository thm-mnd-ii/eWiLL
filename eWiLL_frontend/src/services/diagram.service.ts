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

      getDiagramsTest(userId: number): Diagram[] {
        //TODO: API CALL
        const diagrams: Diagram[] = [];
        axios
        .get(API_URL + "User/" + userId, { headers: authHeader()})
        .then((response) => {

        })
        return diagrams;
      }

      postDiagram(diagram: Diagram){
        axios.post(API_URL, { id:0, ownerId: diagram.ownerId, name: diagram.name, config: diagram.config, entities: diagram.entities, connections: diagram.connections, categoryId: diagram.category.id })
        .then((response) => {
          console.log(response)
        }
        )
      }

      putDiagram(diagram: Diagram){
        axios.put(API_URL, {id:diagram.id, ownerId: diagram.ownerId, name: diagram.name, config: diagram.config, entities: diagram.entities, connections: diagram.connections, categoryId: diagram.category.id})
        .then((response) => {
          console.log(response)
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

      getCategories(userId: number):Category[]{
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
      }) 
      return categories;
      };

      postCategory(name: string, userid: number){
        axios.post("http://localhost:8080/category", { name:name, userid: userid }) 
      }

      getDiagramsWithCategory(userId: number): Map<string, Diagram[]>{
        const map: Map<string, Diagram[]> = new Map();
        const allCategories = this.getCategoriesTest(userId);
        const allDiagrams = this.getDiagrams(userId);

        allCategories.forEach(category => {
          const diagramsSameCategory: Diagram[] = []; 
          allDiagrams.forEach(diagram => {
            if(category.name == diagram.category.name){
              diagramsSameCategory.push(diagram);
            }
          })
        map.set(category.name, diagramsSameCategory)
        });
        return map;
      }
}

export default new DiagramService();
