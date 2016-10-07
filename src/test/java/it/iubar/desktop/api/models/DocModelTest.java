package it.iubar.desktop.api.models;

import org.junit.BeforeClass;


public class DocModelTest {

    private static DocModel doc;

    @BeforeClass
    public static void before(){
    	DocModelTest.doc = DocModelTest.factory();
    }

	public static DocModel factory() {
		DocModel docModel = new DocModel(1, "BRGNDRXXXXX", 7, 2016);
		docModel.setQnt(200);
		return docModel;
	}
	
//    @Test
//    public void toJson() throws Exception{
//        assertEquals("{\"iddoctype\":1,\"idtitolare\":17512,\"mese\":9,\"anno\":2012}", JSONPrinter.toJson(doc));
//    }
}