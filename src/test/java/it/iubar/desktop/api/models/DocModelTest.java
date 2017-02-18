package it.iubar.desktop.api.models;

import org.junit.BeforeClass;

import it.iubar.desktop.api.MasterClientTest;


public class DocModelTest {

    private static DocModel doc;

    @BeforeClass
    public static void before(){
    	DocModelTest.doc = DocModelTest.factory();
    }

	public static DocModel factory() {
		DocModel docModel = new DocModel(DocType.CEDOLINO, "BRGNDRXXXXX", 7, 2016);
		return docModel;
	}

}