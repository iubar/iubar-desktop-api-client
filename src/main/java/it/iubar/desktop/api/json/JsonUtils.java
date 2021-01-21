package it.iubar.desktop.api.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
 
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

public class JsonUtils {

	private static final Logger LOGGER = Logger.getLogger(JsonUtils.class.getName());

	public static JsonObject parseJsonString(String jsonString) {
		JsonReader reader = Json.createReader(new StringReader(jsonString));
		JsonObject jsonObject = reader.readObject();
		return jsonObject;
	}

	public static String toString(JsonObject jsonObject) throws IOException {
		String jsonString = null;
		try(Writer writer = new StringWriter()) {
		    Json.createWriter(writer).write(jsonObject);
		    jsonString = writer.toString();
		}
		return jsonString;
	}
	
	public static String prettyPrintFormat(JsonObject jsonObject) throws IOException {
		String jsonString = null; 
		Map<String, Boolean> config = new HashMap<>();
		config.put(JsonGenerator.PRETTY_PRINTING, true);		        
		JsonWriterFactory writerFactory = Json.createWriterFactory(config);		        	 
		try(Writer writer = new StringWriter()) {
		    writerFactory.createWriter(writer).write(jsonObject);
		    jsonString = writer.toString();
		}
		return jsonString;
	}
	
	public static String prettyPrintFormatOld(JsonObject jsonObj) {
 
		String jsonPrettyString = null;
		 ObjectMapper mapper = new ObjectMapper();
		 // mapper.enable(SerializationFeature.INDENT_OUTPUT);
 		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
 		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 		
 		JsonParser jsonParser;
		try {
			jsonParser = new JsonFactory().createParser(jsonObj.toString());
			  Object jsonParsedFromString = mapper.readValue(jsonParser, Object.class);
			jsonPrettyString = mapper.writeValueAsString(jsonParsedFromString);
			System.out.println(jsonPrettyString); 
			System.out.println("-----------------------------------------"); 
			jsonPrettyString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonParsedFromString);
			System.out.println(jsonPrettyString); 
			System.out.println("-----------------------------------------"); 
		} catch (JsonParseException e2) {
			LOGGER.log(Level.SEVERE, e2.getMessage());
		} catch (IOException e2) {
			LOGGER.log(Level.SEVERE, e2.getMessage());
		}
 
		return jsonPrettyString;
	}
	
}
