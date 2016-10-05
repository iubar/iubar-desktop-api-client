package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatoreModelTest {

    private static DatoreModel datore;

    @BeforeClass
    public static void before(){
        datore = DatoreModelTest.factory();
    }

	public static DatoreModel factory() {
		DatoreModel datore = new DatoreModel();
		datore.setCf("AIHDUAWHDOU");
        datore.setIdcomune(null);
        datore.setPiva("obaojwdboawdb");
        datore.setDenom("AWDBAWJDBJLA");
        datore.setSub(3);
        datore.setPara(7);
        datore.setEmail("awdnaowdj@gmail.com");
        datore.setTel("0129300823");
        return datore;
	}
	
//    @Test
//    public void testToJson() throws Exception{
//        String string = "{\"cf\":\"AIHDUAWHDOU\",\"piva\":\"obaojwdboawdb\",\"denom\":\"AWDBAWJDBJLA\",\"sub\":\"ADWADAWD\",\"para\":\"EBOLA\",\"email\":\"awdnaowdj@gmail.com\",\"tel\":\"0129300823\",\"idcomune\":2,\"idprovincia\":4}";
//        Assert.assertEquals(string, JSONPrinter.toJson(datore));
//    }

}