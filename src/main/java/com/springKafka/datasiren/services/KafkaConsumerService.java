package com.springKafka.datasiren.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
	double[][] lastValue = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
	String[] names = { "Teresa", "Zé", "João" };

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	private DataStoreService DatabaseManageService;

	@KafkaListener(topics = "${kafka.topic}")
	public void consume(@Payload String message) throws JsonMappingException, JsonProcessingException {
                    DatabaseManageService.UpdateFirefighterGroup(message);

					ObjectMapper mapper = new ObjectMapper();
					JsonNode actualObj = mapper.readTree(message);
					ArrayNode datasetArray = (ArrayNode)actualObj.get("firefighters");
					ObjectNode jsonn = mapper.valueToTree(actualObj);
			
					for(int i = 0; i < 3; i++){
			
						int coValue = Integer.parseInt(datasetArray.get(i).get("CO").asText());
						double hrValue = Double.parseDouble(datasetArray.get(i).get("hr").asText());
						int batteryValue = Integer.parseInt(datasetArray.get(i).get("bat").asText());
						if(coValue != lastValue[i][0]){
							if (coValue > 250 || coValue <= 800){
								lastValue[i][0] = coValue;
								ArrayNode alertsArray = (ArrayNode)actualObj.get("alerts");
								ObjectNode alert = mapper.createObjectNode();
								alert.put("id", ""+i);
								alert.put("alert", "Firefighter "+names[i]+" is located in ("+datasetArray.get(i).get("lat").asText()+", "+datasetArray.get(i).get("long").asText()+") and has entered a dangerous environment!");
								alertsArray.add(alert);
								jsonn.putArray("alerts").addAll(alertsArray);
								System.out.println("ALERT::::::::: "+ jsonn);	
							}
							if (coValue > 800){
								lastValue[i][0] = coValue;
								ArrayNode alertsArray = (ArrayNode)actualObj.get("alerts");
								ObjectNode alert = mapper.createObjectNode();
								alert.put("id", ""+i);
								alert.put("alert", "Firefighter "+names[i]+" is located in ("+datasetArray.get(i).get("lat").asText()+", "+datasetArray.get(i).get("long").asText()+") and has entered VERY dangerous environment!");
								alertsArray.add(alert);
								jsonn.putArray("alerts").addAll(alertsArray);
								System.out.println("ALERT::::::::: "+ jsonn);				
							}
						}
						if(hrValue != lastValue[i][1]){
							if (hrValue < 60 || hrValue <= 800){
								lastValue[i][1] = hrValue;
								ArrayNode alertsArray = (ArrayNode)actualObj.get("alerts");
								ObjectNode alert = mapper.createObjectNode();
								alert.put("id", ""+i);
								alert.put("alert", "Firefighter "+names[i]+" is located in ("+datasetArray.get(i).get("lat").asText()+", "+datasetArray.get(i).get("long").asText()+") and is probably injured or unconscious!");
								alertsArray.add(alert);
								jsonn.putArray("alerts").addAll(alertsArray);
								System.out.println("ALERT::::::::: "+ jsonn);	
							}
						}
						if(batteryValue != lastValue[i][2]){
							if (batteryValue < 10){ 
								lastValue[i][2] = batteryValue;
								ArrayNode alertsArray = (ArrayNode)actualObj.get("alerts");
								ObjectNode alert = mapper.createObjectNode();
								alert.put("id", ""+i);
								alert.put("alert", "Firefighter "+names[i]+" is located in ("+datasetArray.get(i).get("lat").asText()+", "+datasetArray.get(i).get("long").asText()+"), replacement battery needed!");
								alertsArray.add(alert);
								jsonn.putArray("alerts").addAll(alertsArray);
								System.out.println("ALERT::::::::: "+ jsonn);	
							}
						}
					}
			
					
					System.out.println("MESSAGE: " + message);
					String newmessage = prettyPrintJsonString(actualObj);
					template.convertAndSend("/topic/esp24-data", newmessage);
			
					System.out.println("NEW MESSAGE: " + newmessage);
		
		
	}
	public String prettyPrintJsonString(JsonNode jsonNode) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(jsonNode.toString(), Object.class);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (Exception e) {
			return "Sorry, conversion error";
		}
	}
	public  boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    @SuppressWarnings("unused")
		double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
}
