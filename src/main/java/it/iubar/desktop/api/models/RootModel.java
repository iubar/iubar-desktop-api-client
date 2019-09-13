package it.iubar.desktop.api.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class RootModel {

	private final static Logger LOGGER = Logger.getLogger(RootModel.class.getName());
	private final static DateFormat FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
	private final static DateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static DateFormat FORMAT3 = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");

	public String asXml() throws JAXBException {
		Class c = this.getClass();
		JAXBContext jContext = JAXBContext.newInstance(c);
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

	public String asJson() {
		String jsonString = null;
		ObjectMapper mapper = getMapper();
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Jackson could not convert the object correctly.", JsonProcessingException.class);
		}
		return jsonString;
	}

	public static <T> String asJson(List<T> list) {
		String jsonString = null;
		ObjectMapper mapper = getMapper();
		try {
			jsonString = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Jackson could not convert the object correctly.", JsonProcessingException.class);
		}
		return jsonString;
	}

	public String asPrettyJson() {
		String jsonPrettyString = null;
		ObjectMapper mapper = getMapper();
		try {
			jsonPrettyString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonPrettyString;
	}

	private String asJsonFiltered() {
		String jsonPrettyString = null;
		ObjectMapper mapper = getMapper();
		try {
			jsonPrettyString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonPrettyString;
	}

	public static <T> T fromJson(String json, Class<T> c) {
		T t = null;
		ObjectMapper mapper = getMapper();
		try {
			t = mapper.readValue(json, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(FORMAT1);

		// By default all fields without explicit view definition are included.
		// If enabled, non-annotated properties will be included;
		// when disabled, they will be excluded.
		// Feature that determines whether properties that have no view annotations are included in JSON serialization views 
		mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		// Feature that determines what happens when no accessors are found for a type
		// (and there are no annotations to indicate it is meant to be serialized).
		// If enabled (default), an exception is thrown to indicate these as
		// non-serializable types;
		// if disabled, they are serialized as empty Objects, i.e. without any properties.
		// Note that empty types that this feature has only effect on those "empty"
		// beans that do not have any recognized annotations (like @JsonSerialize): ones
		// that do have annotations do not result in an exception being thrown.
		// Feature is enabled by default.
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		// SerializationInclusion: ovvero gestione di null, stringhe vuote e collection
		// vuote !!!
		mapper.setSerializationInclusion(Include.ALWAYS); 	// Includo null e stringhe vuote // Value that indicates that
															// property is to be always included, independent of value
															// of the property.
		// mapper.setSerializationInclusion(Include.NON_EMPTY);
		// equivale a @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
		// mapper.setSerializationInclusion(Include.NON_NULL);
		// equivale a @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
		// If you need both non-null and non-empty, use
		// mapper.setSerializationInclusion(Include.NON_DEFAULT) or
		// @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)

		// Add support for both Jaxb and Jackson annotations
		AnnotationIntrospector aiJackson = new JacksonAnnotationIntrospector();
		AnnotationIntrospector aiJaxb = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		// First Jaxb, second Jackson annotations
		mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(aiJaxb, aiJackson));

		// Il seguente statement equivale all'annotazione (di classe)
		// @JsonIgnoreProperties(ignoreUnknown = true)
		// Feature that determines whether encountering of unknown properties (ones that
		// do not map to a property, and there is no "any setter" or handler that can
		// handle it) should result in a failure (by throwing a JsonMappingException) or
		// not.
		// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public static StreamSource toStreamSource(String str) {
		StreamSource source = new StreamSource(new StringReader(str));
		return source;
	}

	public static DOMSource toDOMSource(String str) {
		DOMSource source = null;
		Element node = null;
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
			// oppure
			// InputSource is = new InputSource();
			// is.setCharacterStream(new StringReader(str));
			// is.setEncoding("UTF-8");
			node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement();
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

	public static String toString(Date date) {
		String formatted = null;
		if (date != null) {
			formatted = FORMAT1.format(date);
		}
		return formatted;
	}

	public static String toString(GregorianCalendar cal) {
		if (cal != null) {
			return toString(cal.getTime());
		}
		return null;
	}

	public static Date toDate(String str) {
		Date d = null;
		if (str != null) {
			try {
				d = FORMAT1.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	protected static Date toDate(GregorianCalendar cal) {
		Date d = null;
		if (cal != null) {
			d = cal.getTime();
		}
		return d;
	}

	protected static GregorianCalendar toCal(Date d) {
		GregorianCalendar cal = new GregorianCalendar(); 
		cal.setTime(d);
		return cal;
	}

	protected static String toString(BigDecimal bd) {
		String str = null;
		if (bd != null) {
			str = bd.toString();
		}
		return str;
	}

}
