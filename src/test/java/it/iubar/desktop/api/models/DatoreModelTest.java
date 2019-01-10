package it.iubar.desktop.api.models;

import it.iubar.desktop.api.MasterClientTest;
import org.junit.jupiter.api.BeforeAll;

public class DatoreModelTest {

    private static DatoreModel datore;

    @BeforeAll
    public static void before(){
    	DatoreModelTest.datore = DatoreModelTest.factory();
    }

	public static DatoreModel factory() {
		DatoreModel datore = new DatoreModel();
		datore.setIdapp(MasterClientTest.ID_APP_PAGHEOPEN);
        datore.setIdcomune(null);
        datore.setPiva("12345678901");
        datore.setDenom("AWDBAWJDBJLA");
        datore.setSub(3);
        datore.setPara(7);
        datore.setCf(ClientModelTest.CF);
        datore.setEmail("awdnaowdj@gmail.com");
        datore.setTel("0129300823");
        return datore;
	}
	
//    @Test
//    public void testToJson() throws Exception{
//        String string = "{\"cf\":\"AIHDUAWHDOU\",\"piva\":\"12345678901\",\"denom\":\"AWDBAWJDBJLA\",\"sub\":\"ADWADAWD\",\"para\":\"EBOLA\",\"email\":\"awdnaowdj@gmail.com\",\"tel\":\"0129300823\",\"idcomune\":2,\"idprovincia\":4}";
//        Assert.assertEquals(string, JSONPrinter.toJson(datore));
//    }

}