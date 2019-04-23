package it.iubar.desktop.api.models;

public class TitolareModelTest {

	public static TitolareModel factory() {
		TitolareModel titolare = new TitolareModel();
        titolare.setIdtipo(0);
        titolare.setPiva(ClientModelTest.PIVA);
        titolare.setDenom("Studio Eccol");
        titolare.setCognome("Tommaso");
        titolare.setIndirizzo("Via Sant'Antonio");
        titolare.setEmail(null);
        titolare.setTel("4674567567");
        titolare.setDatori(123);
        titolare.setLavoratori(123);
        titolare.setChiave_pubblica(null);
        titolare.setCf(ClientModelTest.CF);
        return titolare;
	}    
	
	

//    @Test
//    public void testToJSon() throws Exception{
//        String str = "{\"idtipo\":0,\"cf\":\"123123123\",\"piva\":\"12345678901\",\"denom\":\"Studio Eccol\",\"cognome\":\"Tommaso\",\"indirizzo\":\"Via Sant'Antonio\",\"email\":\"\",\"tel\":\"4674567567\",\"datori\":123,\"lavoratori\":123,\"idcomune\":123,\"idprovincia\":0,\"info_added\":\"12307799987934\",\"info_updated\":\"1273559797681729539\",\"chiave_pubblica\":\"\"}";
//        assertEquals(str, JSONPrinter.toJson(titolare));
//    }

}