package sanutils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static position.ICodage.CASES117;
import static sanutils.SANUtils.getFile;
import static sanutils.SANUtils.getRank;

public class SANUtilsTest {

    private final int[] board;

    public SANUtilsTest() {
        board = CASES117;
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetFile() {
        System.out.println("getFile");
        for (int _case : board) {
            int result = getFile(_case);
            System.out.println(_case + ";" + result);
        }

//        assertEquals(expResult, result);
    }

    @Test
    public void testGetRank() {
        System.out.println("getRank");
        for (int _case : board) {
            int result = getRank(_case);
            System.out.println(_case + ";" + result);
        }

    }

}
