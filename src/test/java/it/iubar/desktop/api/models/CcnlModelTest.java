package it.iubar.desktop.api.models;

import org.junit.jupiter.api.BeforeAll;

public class CcnlModelTest {

    private static CcnlModel ccnl;

    @BeforeAll
    public static void before(){
    	CcnlModelTest.ccnl =  CcnlModelTest.factory();
    }
    
	public static CcnlModel factory() {
		CcnlModel contratto = new CcnlModel();
		contratto.setIdccnl("123");
		return contratto;
	}    

//    @Test
//    public void testToJson() throws JsonProcessingException, CloneNotSupportedException {
//        String str = "{\"idccnl\":123}";
//        assertEquals(str, JSONPrinter.toJson(ccnl));
//    }


}