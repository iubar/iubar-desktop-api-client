package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TitolareTest {

    static Titolare titolare;

    @BeforeClass
    public static void before(){
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
        String str = "{\"idtipo\":0,\"cf\":\"123123123\",\"piva\":\"qweasdzxc\",\"denom\":\"Studio Eccol\",\"cognome\":\"Tommaso\",\"indirizzo\":\"Via Sant'Antonio\",\"email\":\"\",\"tel\":\"4674567567\",\"datori\":123,\"lavoratori\":123,\"idcomune\":123,\"idprovincia\":0,\"info_added\":\"12307799987934\",\"info_updated\":\"1273559797681729539\",\"chiave_pubblica\":\"\"}";
        assertEquals(str, JSONPrinter.toJson(titolare));
    }

}