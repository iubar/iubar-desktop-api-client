package it.iubar.desktopApi.DBClasses;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TitolareTest {

    Titolare titolare;

    @Before
    public void before(){
        titolare = new Titolare(11072, 0, "SQLMRC59M30G999Z", "SQLMRC59M30", "Studio Squilloni", "Marco", "Via Lorenzo il Magnifico, 28/a", "", "0558798780", 5, 13, 100004, 0, "2012-05-08 15:20:16", "2012-05-08 15:20:16", "");
    }

    @Test
    public void testToJSon() throws Exception{
        String str = "{\"idtitolare\":11072,\"idtipo\":0,\"cf\":\"SQLMRC59M30G999Z\",\"piva\":\"SQLMRC59M30\",\"denom\":\"Studio Squilloni\",\"cognome\":\"Marco\",\"indirizzo\":\"Via Lorenzo il Magnifico, 28/a\",\"email\":\"\",\"tel\":\"0558798780\",\"datori\":5,\"lavoratori\":13,\"idcomune\":100004,\"idprovincia\":0,\"info_added\":\"2012-05-08 15:20:16\",\"info_updated\":\"2012-05-08 15:20:16\",\"chiave_pubblica\":\"\"}";
        assertEquals(str, titolare.toJson());
    }

}