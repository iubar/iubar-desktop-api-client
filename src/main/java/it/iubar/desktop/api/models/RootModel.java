package it.iubar.desktop.api.models;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.spi.JsonbProvider;
 
public class RootModel {

	private final static Logger LOGGER = Logger.getLogger(RootModel.class.getName());
	private final static DateFormat FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
	private final static DateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static DateFormat FORMAT3 = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");

//	public String asXml() throws JAXBException {
//		Class c = this.getClass();
//		JAXBContext jContext = JAXBContext.newInstance(c);
//		java.io.StringWriter sw = new StringWriter();
//		Marshaller marshaller = jContext.createMarshaller();
//		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		marshaller.marshal(this, sw);
//		return sw.toString();
//	}

//	public static <T> T fromXml(Source xmlSource, Class<T> c) {
//		T t = null;
//		try {
//			JAXBContext jc = JAXBContext.newInstance(c);
//			Unmarshaller unmarshaller = jc.createUnmarshaller();
//			t = (T) unmarshaller.unmarshal(xmlSource);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//		return t;
//	}

//	public static <T> T fromXml(Reader reader, Class<T> c) {
//		T t = null;
//		try {
//			JAXBContext jc = JAXBContext.newInstance(c);
//			Unmarshaller unmarshaller = jc.createUnmarshaller();
//			t = (T) unmarshaller.unmarshal(reader);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//		return t;
//	}
//
//	public static <T> T fromXml(String xmlString, Class<T> c) {
//		DOMSource xmlSource = toDOMSource(xmlString);
//		return fromXml(xmlSource, c);
//	}

//	public static <T> T fromXml2(String xmlString, Class<T> c) {
//		Reader reader = new StringReader(xmlString);
//		return fromXml(reader, c);
//	}

	 

//	public static StreamSource toStreamSource(String str) {
//		StreamSource source = new StreamSource(new StringReader(str));
//		return source;
//	}

//	public static DOMSource toDOMSource(String str) {
//		DOMSource source = null;
//		Element node = null;
//		try {
//			ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
//			// oppure
//			// InputSource is = new InputSource();
//			// is.setCharacterStream(new StringReader(str));
//			// is.setEncoding("UTF-8");
//			node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
//		source = new DOMSource(node);
//		return source;
//	}

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

//	public static Date toDate(String str) {
//		Date d = null;
//		if (str != null) {
//			try {
//				d = FORMAT1.parse(str);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		return d;
//	}

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
	
	/**
	 * @see http://json-b.net/docs/user-guide.html
	 * @return
	 */
	public String asJson() {	
		  Jsonb builder = JsonbProvider.provider().create().build(); // // da Java 1.8 deve essere cambiato in : JsonbBuilder.create(); 
		  String str = builder.toJson(this);
		  return str;
	}
	
	

}
