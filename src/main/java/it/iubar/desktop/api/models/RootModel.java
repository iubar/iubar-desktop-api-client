package it.iubar.desktop.api.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RootModel {
    
	private final static Logger LOGGER = Logger.getLogger(RootModel.class.getName());
	
	public String asXml() throws JAXBException {
		JAXBContext jContext = JAXBContext.newInstance(this.getClass());
		java.io.StringWriter sw = new StringWriter();			
		Marshaller marshaller = jContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, sw);			
		return sw.toString();
	}
	
    public static <T> T fromXml(Source xmlSource, Class<T> c) {
    	T t = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(c);
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        t = (T) unmarshaller.unmarshal(xmlSource);				
		} catch (JAXBException e) {				
			e.printStackTrace();
		}	 
        return t;
    }
    
    public static <T> T fromXml(Reader reader, Class<T> c) {
    	T t = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(c);
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        t = (T) unmarshaller.unmarshal(reader);				
		} catch (JAXBException e) {				
			e.printStackTrace();
		}	 
        return t;
    }
    
    public static <T> T fromXml(String xmlString, Class<T> c) {
    	DOMSource xmlSource = toDOMSource(xmlString);
        return fromXml(xmlSource, c);
    }
    
    public static <T> T fromXml2(String xmlString, Class<T> c) {
    	Reader reader = new StringReader(xmlString);
        return fromXml(reader, c);
    }
    
	public String asJson(){
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);		
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Jackson could not convert the object correctly.", JsonProcessingException.class);
			throw new RuntimeException("Jackson could not convert the object correctly.");	
		}
		return jsonString;
	}
	
	public String asPrettyJson(){
		String jsonPrettyString = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonPrettyString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonPrettyString;
	}		
	
	public String asJsonFiltered(){
		String jsonPrettyString = null;
		ObjectMapper mapper = new ObjectMapper();
		//By default all fields without explicit view definition are included, disable this
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);	
//	    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
//	    mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
//	    mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
//	    mapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
//	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//	    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);			
		try {
			// String jsonString = mapper.writeValueAsString(this);
			jsonPrettyString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonPrettyString;
	}
	
	public static <T> T fromJson(String json, Class<T> c) {
		T t = null;
		ObjectMapper mapper = new ObjectMapper();			
		try {
			t = mapper.readValue(json, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static StreamSource toStreamSource(String str){
		StreamSource source = new StreamSource(new StringReader(str));
		return source;
	}
	
	public static DOMSource toDOMSource(String str){
		DOMSource source = null;
		Element node = null;
		try {
			// Java 7
			//ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
			// Java 6
			ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));	
			// oppure
			// InputSource is = new InputSource();
			// is.setCharacterStream(new StringReader(str));
			// is.setEncoding("UTF-8");
			node = DocumentBuilderFactory
				    .newInstance()
				    .newDocumentBuilder()
				    .parse(is)
				    .getDocumentElement();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		source = new DOMSource(node);
		return source;
	}

	
	
	
}
