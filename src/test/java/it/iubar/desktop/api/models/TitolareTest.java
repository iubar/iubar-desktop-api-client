package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TitolareTest {

    Titolare titolare;

    @Before
    public void before(){
        titolare = new Titolare(123, 0);
        titolare.setIdtipo(0);
        titolare.setCf("123123123");
        titolare.setPiva("qweasdzxc");
        titolare.setDenom("Studio Eccol");
        titolare.setCognome("Tommaso");
        titolare.setIndirizzo("Via Sant'Antonio");
        titolare.setEmail("");
        titolare.setTel("4674567567");
        titolare.setDatori(123);
        titolare.setLavoratori(123);
        titolare.setInfo_added("12307799987934");
        titolare.setInfo_updated("1273559797681729539");
        titolare.setChiave_pubblica("");
    }

    @Test
    public void testToJSon() throws Exception{
        String str = "{\n" +
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
        Assert.assertEquals(str, JSONPrinter.toJson(titolare));
    }

}