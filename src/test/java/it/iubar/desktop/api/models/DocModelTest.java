package it.iubar.desktop.api.models;

import org.junit.jupiter.api.BeforeAll;


public class DocModelTest {

    private static DocModel doc;

    @BeforeAll
    public static void before(){
    	DocModelTest.doc = DocModelTest.factory();
    }

	public static DocModel factory() {
		DocModel docModel = new DocModel(DocType.CEDOLINO, 7, ClientModelTest.CF, 2016);
		return docModel;
	}

}