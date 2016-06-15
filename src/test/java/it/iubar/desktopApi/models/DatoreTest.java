package it.iubar.desktopApi.models;

import it.iubar.desktopApi.services.JSONPrinter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatoreTest {

    Datore datore;

    @Before
    public void before(){
        datore = new Datore(2, "AIHDUAWHDOU", "obaojwdboawdb", "AWDBAWJDBJLA", "ADWADAWD", "EBOLA", "awdnaowdj@gmail.com", "0129300823", 2, 4);
    }

    @Test
    public void testToJson() throws Exception{
        String string = "{\n" +
                "  \"iddatore\" : 2,\n" +
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
        assertEquals(string, JSONPrinter.toJson(datore));
    }

}