package eu.amidst.huginlink.converters;


import COM.hugin.HAPI.DefaultClassParseListener;
import COM.hugin.HAPI.Domain;
import COM.hugin.HAPI.ExceptionHugin;
import COM.hugin.HAPI.ParseListener;
import eu.amidst.core.io.BayesianNetworkWriter;
import eu.amidst.core.models.*;
import eu.amidst.huginlink.io.BNWriterToHugin;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by afa on 19/11/14.
 */
public class BNConverterToAmidstTest {

    @Before
    public void setUp() throws ExceptionHugin, IOException {

        ParseListener parseListener = new DefaultClassParseListener();
        Domain huginBN = new Domain ("networks/huginNetworkFromAMIDST.net", parseListener);
        System.out.println("\n\nConverting the Hugin network into AMIDST format ...");
        BayesianNetwork amidstBN = BNConverterToAMIDST.convertToAmidst(huginBN);
        System.out.println("\nAMIDST network object created.");

        Domain huginBN2 = new Domain ("networks/asia.net", parseListener);
        BayesianNetwork amidstBN2 = BNConverterToAMIDST.convertToAmidst(huginBN2);
        System.out.println("\nAMIDST network object created.");

        BNWriterToHugin.saveToHuginFile(amidstBN2, "networks/asia.bn");



        parseListener = new DefaultClassParseListener();
        huginBN = new Domain ("networks/IS.net", parseListener);
        BayesianNetworkWriter.saveToFile(BNConverterToAMIDST.convertToAmidst(huginBN), "networks/IS.bn");

    }

    @Test
    public void test() throws ExceptionHugin {


    }


}
