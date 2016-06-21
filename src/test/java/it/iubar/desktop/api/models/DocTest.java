package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class DocTest {
    private static Doc doc;

    @BeforeClass
    public static void before(){
        doc = new Doc(1, 17512, 9, 2012);
    }

    @Test
    public void toJson() throws Exception{
        assertEquals("{\"iddoctype\":1,\"idtitolare\":17512,\"mese\":9,\"anno\":2012}", JSONPrinter.toJson(doc));
    }
}