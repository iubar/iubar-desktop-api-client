package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatoreTest {

    private static Datore datore;

    @BeforeClass
    public static void before(){
        datore = new Datore(2, 4);
        datore.setCf("AIHDUAWHDOU");
        datore.setPiva("obaojwdboawdb");
        datore.setDenom("AWDBAWJDBJLA");
        datore.setSub("ADWADAWD");
        datore.setPara("EBOLA");
        datore.setEmail("awdnaowdj@gmail.com");
        datore.setTel("0129300823");
    }

    @Test
    public void testToJson() throws Exception{
        String string = "{\"cf\":\"AIHDUAWHDOU\",\"piva\":\"obaojwdboawdb\",\"denom\":\"AWDBAWJDBJLA\",\"sub\":\"ADWADAWD\",\"para\":\"EBOLA\",\"email\":\"awdnaowdj@gmail.com\",\"tel\":\"0129300823\",\"idcomune\":2,\"idprovincia\":4}";
        Assert.assertEquals(string, JSONPrinter.toJson(datore));
    }

}