package it.iubar.desktop.api.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CcnlTest {

    static Ccnl ccnl;

    @BeforeClass
    public static void before(){
        ccnl = new Ccnl(123);
    }

    @Test
    public void testToJson() throws JsonProcessingException, CloneNotSupportedException {
        String str = "{\n" +
                "  \"idccnl\" : 123\n" +
                "}";
        assertEquals(str, JSONPrinter.toJson(ccnl));
    }

}