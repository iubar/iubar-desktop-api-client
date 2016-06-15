package it.iubar.desktopApi.DBClasses;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TitolareTest {

    Titolare titolare;

    @Before
    public void before(){
        titolare = new Titolare(123123, 0, "123123123", "qweasdzxc", "Studio Eccol", "Tommaso", "Via Sant'Antonio", "", "4674567567", 123, 123, 123, 0, "12307799987934", "1273559797681729539", "");
    }

    @Test
    public void testToJSon() throws Exception{
        String str = "{\n" +
                "  \"idtitolare\" : 123123,\n" +
                "  \"idtipo\" : 0,\n" +
                "  \"cf\" : \"123123123\",\n" +
                "  \"piva\" : \"qweasdzxc\",\n" +
                "  \"denom\" : \"Studio Eccol\",\n" +
                "  \"cognome\" : \"Tommaso\",\n" +
                "  \"indirizzo\" : \"Via Sant'Antonio\",\n" +
                "  \"email\" : \"\",\n" +
                "  \"tel\" : \"4674567567\",\n" +
                "  \"datori\" : 123,\n" +
                "  \"lavoratori\" : 123,\n" +
                "  \"idcomune\" : 123,\n" +
                "  \"idprovincia\" : 0,\n" +
                "  \"info_added\" : \"12307799987934\",\n" +
                "  \"info_updated\" : \"1273559797681729539\",\n" +
                "  \"chiave_pubblica\" : \"\"\n" +
                "}";
        assertEquals(str, JSONPrinter.toJson(titolare));
    }

}