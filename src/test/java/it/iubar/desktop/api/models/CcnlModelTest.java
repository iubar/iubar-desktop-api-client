package it.iubar.desktop.api.models;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.iubar.desktop.api.MasterClientTest;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CcnlModelTest {

    private static CcnlModel ccnl;

    @BeforeClass
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