package charactercreator.demo.util;



import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import charactercreator.demo.dto.CustomCharacterDto;
import charactercreator.demo.entities.Account;
import charactercreator.demo.entities.CustomCharacter;


public class staticMetods {

   
    


    public static JsonObject jsonParser(String list) throws JsonSyntaxException{
        
        JsonObject convertedObject = new Gson().fromJson(list, JsonObject.class);


        return convertedObject;
    }

    public static Integer levelCreator(){
        int level = (int)(Math.random() * 20) + 1;

        return level;
    }

    public static List<String> apiOrganizer(JsonObject raceJson, JsonObject classJson,  JsonObject aligmentJson) {



        //Criação de um index aleatório para cada caracteristica do personagem: 
       int valueRaceIndex = (int)(Math.random() * Integer.parseInt(raceJson.get("count").getAsString())) + 1;
       int valueClassINdex = (int)(Math.random() * Integer.parseInt(classJson.get("count").getAsString())) + 1;
       int valueAligmentIndex= (int)(Math.random() * Integer.parseInt(aligmentJson.get("count").getAsString())) + 1;



       //Seleciona as informaçôes dentro do json de resposta da API
       JsonObject  chosenRace = raceJson.getAsJsonArray("results").get(valueRaceIndex - 1).getAsJsonObject();
       String name = chosenRace.get("name").getAsString();

       JsonObject chosenClass = classJson.getAsJsonArray("results").get(valueClassINdex - 1).getAsJsonObject();
       String Class = chosenClass.get("name").getAsString();

       JsonObject chosenAligment= aligmentJson.getAsJsonArray("results").get(valueAligmentIndex - 1).getAsJsonObject();
       String aligment = chosenAligment.get("name").getAsString();


        //Armazena as respostas em uma lista
        List<String> resultList = Arrays.asList(name,Class,aligment);

        System.out.println(resultList);
        return resultList;
    }


/* 

    //V2 da apiOrganizer
    public static List<String> apiOrganizer(String[] array) {



        //Criação de um index aleatório para cada caracteristica do personagem: 
       int valueRaceIndex = (int)(Math.random() * Integer.parseInt(array.get("count").getAsString())) + 1;
       int valueClassINdex = (int)(Math.random() * Integer.parseInt(classJson.get("count").getAsString())) + 1;
       int valueAligmentIndex= (int)(Math.random() * Integer.parseInt(aligmentJson.get("count").getAsString())) + 1;



       //Seleciona as informaçôes dentro do json de resposta da API
       JsonObject  chosenRace = raceJson.getAsJsonArray("results").get(valueRaceIndex - 1).getAsJsonObject();
       String name = chosenRace.get("name").getAsString();

       JsonObject chosenClass = classJson.getAsJsonArray("results").get(valueClassINdex - 1).getAsJsonObject();
       String Class = chosenClass.get("name").getAsString();

       JsonObject chosenAligment= aligmentJson.getAsJsonArray("results").get(valueAligmentIndex - 1).getAsJsonObject();
       String aligment = chosenAligment.get("name").getAsString();


        //Armazena as respostas em uma lista
        List<String> resultList = Arrays.asList(name,Class,aligment);

        System.out.println(resultList);
        return resultList;
    }
*/
    


    public static CustomCharacter characterGenerator(String name, String sex,  Account account){

        //Chamada da API externa
        RestTemplate restTemplate = new RestTemplate();


        //Faz as chamadas da Api
        String raceUrl = "https://www.dnd5eapi.co/api/races";
        String raceResult = restTemplate.getForObject(raceUrl, String.class);
        String classUrl = "https://www.dnd5eapi.co/api/classes";
        String classResult = restTemplate.getForObject(classUrl, String.class);
        String aligmentUrl = "https://www.dnd5eapi.co/api/alignments";
        String aligmentResult = restTemplate.getForObject(aligmentUrl, String.class);


        //Trata a resposta e seleciona um valor aleatório
        List<String> result = apiOrganizer(jsonParser(raceResult),jsonParser(classResult),jsonParser(aligmentResult));

        CustomCharacter customCharacter = new CustomCharacter(name, sex, result.get(0), result.get(1),levelCreator(),result.get(2),account);


        String chatGPTResquestUrl = "http://localhost:8080/bot/chat/" + customCharacter.toString();
        String chatGPTResult = restTemplate.getForObject(chatGPTResquestUrl, String.class);


        CustomCharacter customCharacterGPT = new CustomCharacter(new CustomCharacterDto(customCharacter) ,account, chatGPTResult);

        
        return customCharacterGPT;
    }

    /*
    //V2 do characterGenerator
    public static CustomCharacter characterGenerator(CustomCharacterDto customCharacterDto,  Account account){

        //Chamada da API externa
        RestTemplate restTemplate = new RestTemplate();


        //Faz as chamadas da Api
        if(customCharacterDto.getRace().isEmpty()){
            String raceUrl = "https://www.dnd5eapi.co/api/races";
            String raceResult = restTemplate.getForObject(raceUrl, String.class);
        }else{
             String raceResult = customCharacterDto.getRace();
        }
       
        if(customCharacterDto.getCharacterClass().isEmpty()){
            String classUrl = "https://www.dnd5eapi.co/api/classes";
             String classResult = restTemplate.getForObject(classUrl, String.class);
        }else{
            String classResult = customCharacterDto.getCharacterClass();

        }
        
 
        if(customCharacterDto.getCharacterClass().isEmpty()){
         String aligmentUrl = "https://www.dnd5eapi.co/api/alignments";
          String aligmentResult = restTemplate.getForObject(aligmentUrl, String.class);

        }else{
            String aligmentResult = customCharacterDto.getAlignments();

        }
        List<String> stringCreator = {raceResult, classResult, aligmentResult};
       
        //Trata a resposta e seleciona um valor aleatório
        List<String> result = apiOrganizer(jsonParser(raceResult),jsonParser(classResult),jsonParser(aligmentResult));

        CustomCharacter customCharacter = new CustomCharacter(customCharacterDto.getName(), customCharacterDto.getSex(), result.get(0), result.get(1),levelCreator(),result.get(2),account);


        
        return customCharacter;
    }
     */
}
