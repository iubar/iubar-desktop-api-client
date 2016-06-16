package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatoreTest {

    static Datore datore;

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
        String string = "{\n" +
                "  \"cf\" : \"AIHDUAWHDOU\",\n" +
                "  \"piva\" : \"obaojwdboawdb\",\n" +
                "  \"denom\" : \"AWDBAWJDBJLA\",\n" +
                "  \"sub\" : \"ADWADAWD\",\n" +
                "  \"para\" : \"EBOLA\",\n" +
                "  \"email\" : \"awdnaowdj@gmail.com\",\n" +
                "  \"tel\" : \"0129300823\",\n" +
                "  \"idcomune\" : 2,\n" +
                "  \"idprovincia\" : 4\n" +
                "}";
        Assert.assertEquals(string, JSONPrinter.toJson(datore));
    }

}