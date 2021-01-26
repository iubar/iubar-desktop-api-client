package it.iubar.desktop.api.json;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.commons.io.IOUtils;
 
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
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
 
public static String jsonFormat(JsonStructure json, String... options) {
    StringWriter stringWriter = new StringWriter();
    Map<String, Boolean> config = buildConfig(options);
    JsonWriterFactory writerFactory = Json.createWriterFactory(config);
    JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

    jsonWriter.write(json);
    jsonWriter.close();

    return stringWriter.toString();
}

private static Map<String, Boolean> buildConfig(String... options) {
    Map<String, Boolean> config = new HashMap<String, Boolean>();

    if (options != null) {
        for (String option : options) {
            config.put(option, true);
        }
    }

    return config;
}

public static String prettyPrint(JsonStructure json) {
    return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
}

public static String prettyPrint(String str) {
	JsonObject json = fromString(str);
	return prettyPrint(json);
}

public static JsonObject fromString(String str) {
	Charset charset = java.nio.charset.StandardCharsets.UTF_8;
	InputStream is = IOUtils.toInputStream(str, charset);
	JsonReader reader = Json.createReader(is);
	JsonObject root = reader.readObject();
	try {
		is.close();
	} catch (IOException e) {
		e.printStackTrace();
	}    	
	return root;
}

/**
 * Usage:
 * JsonObject jo = ...;
 * JsonObjectBuilder builder = JsonJavaxUtils.jsonObjectToBuilder(jo);
 * builder.add("numberProperty", 42);
 * builder.add("stringProperty", "Hello");
 * jo = builder.build();
 *
 * Usage:
 * JsonObject jo = ...;
 * jo = jsonObjectToBuilder(jo).add("numberProperty", 42).build();
 * 
 */
public static JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
    JsonObjectBuilder job = Json.createObjectBuilder();
    for (Entry<String, JsonValue> entry : jo.entrySet()) {
        job.add(entry.getKey(), entry.getValue());
    }
    return job;
}

public static void main(String[] args) throws Exception {
	String content = "{\"cliente\": { \"nome\": \"Mario\", \"cognome\": \"Rossi\" } }";
	
	InputStream is = IOUtils.toInputStream(content);    	
	JsonReader reader = Json.createReader(is);
	JsonObject root = reader.readObject();
	
	JsonObjectBuilder builder = JsonUtils.jsonObjectToBuilder(root);
	builder.add("indirizzo", "Via Roma");
	
	JsonObjectBuilder cliente2 = JsonUtils.jsonObjectToBuilder(root.getJsonObject("cliente"));
	cliente2.add("nome", "Giovanni");
	
	builder.add("cliente", cliente2);
	
	JsonObject jsonObject = builder.build();
	System.out.println(jsonObject.toString());		
}

/**
 * FileWriter is meant for writing streams of characters. For writing streams of raw bytes, consider using a FileOutputStream.
 * @see https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html 
 */
public static void saveToFile2(JsonObjectBuilder objectBuilder, String fileName) throws IOException {
	FileWriter fileWriter = new FileWriter(fileName);
	String json = prettyPrint(objectBuilder.build());
	fileWriter.write(json);
	// fileWriter.flush(); // To flush a stream manually, invoke its flush method. The flush method is valid on any output stream, but has no effect unless the stream is buffered.
	fileWriter.close(); 
}

/**
 * FileWriter is meant for writing streams of characters. For writing streams of raw bytes, consider using a FileOutputStream.
 * @see https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html 
 */
public static void saveToFile(JsonObjectBuilder objectBuilder, String fileName) throws IOException {
	Writer fstream = null;
	BufferedWriter out = null;
	FileOutputStream fos = new FileOutputStream(fileName);
    fstream = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
    out = new BufferedWriter(fstream);
    String json = prettyPrint(objectBuilder.build());
    out.write(json);
    out.flush();
    out.close();
}


}
