/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.function;

import hsplet.Context;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.StringArray;
import hsplet.variable.Variable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jdstroy
 */
public class BasicCommandTest {
    
    public BasicCommandTest() {
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

    /**
     * Test of onexit method, of class BasicCommand.
     */
    @Test
    public void testOnexit() {
        System.out.println("onexit");
        Context context = null;
        JumpStatement jump = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.onexit(context, jump, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onerror method, of class BasicCommand.
     */
    @Test
    public void testOnerror() {
        System.out.println("onerror");
        Context context = null;
        JumpStatement jump = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.onerror(context, jump, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onkey method, of class BasicCommand.
     */
    @Test
    public void testOnkey() {
        System.out.println("onkey");
        Context context = null;
        JumpStatement jump = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.onkey(context, jump, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onclick method, of class BasicCommand.
     */
    @Test
    public void testOnclick() {
        System.out.println("onclick");
        Context context = null;
        JumpStatement jump = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.onclick(context, jump, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of oncmd method, of class BasicCommand.
     */
    @Test
    public void testOncmd() {
        System.out.println("oncmd");
        Context context = null;
        JumpStatement jump = null;
        Operand v = null;
        int vi = 0;
        int message = 0;
        BasicCommand.oncmd(context, jump, v, vi, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exist method, of class BasicCommand.
     */
    @Test
    public void testExist() {
        System.out.println("exist");
        Context context = null;
        String fileName = "";
        BasicCommand.exist(context, fileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class BasicCommand.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Context context = null;
        String fileName = "";
        BasicCommand.delete(context, fileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mkdir method, of class BasicCommand.
     */
    @Test
    public void testMkdir() {
        System.out.println("mkdir");
        Context context = null;
        String fileName = "";
        BasicCommand.mkdir(context, fileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chdir method, of class BasicCommand.
     */
    @Test
    public void testChdir() {
        System.out.println("chdir");
        Context context = null;
        String dirName = "";
        BasicCommand.chdir(context, dirName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dirlist method, of class BasicCommand.
     */
    @Test
    public void testDirlist() {
        System.out.println("dirlist");
        Context context = null;
        ByteString result_2 = null;
        String mask = "";
        int mode = 0;
        BasicCommand.dirlist(context, result_2, mask, mode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of bload method, of class BasicCommand.
     */
    @Test
    public void testBload() {
        System.out.println("bload");
        Context context = null;
        String fileName = "";
        Operand v = null;
        int vi = 0;
        Operand sizev = null;
        int sizevi = 0;
        int offset = 0;
        BasicCommand.bload(context, fileName, v, vi, sizev, sizevi, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of bsave method, of class BasicCommand.
     */
    @Test
    public void testBsave() {
        System.out.println("bsave");
        Context context = null;
        String fileName = "";
        Operand v = null;
        int vi = 0;
        Operand sizev = null;
        int sizevi = 0;
        int offset = 0;
        BasicCommand.bsave(context, fileName, v, vi, sizev, sizevi, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of bcopy method, of class BasicCommand.
     */
    @Test
    public void testBcopy() {
        System.out.println("bcopy");
        Context context = null;
        String fileName = "";
        String target = "";
        BasicCommand.bcopy(context, fileName, target);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of memfile method, of class BasicCommand.
     */
    @Test
    public void testMemfile() {
        System.out.println("memfile");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int base = 0;
        int size = 0;
        BasicCommand.memfile(context, v, vi, base, size);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of poke method, of class BasicCommand.
     */
    @Test
    public void testPoke() {
        System.out.println("poke");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int index = 0;
        Operand sv = null;
        int svi = 0;
        BasicCommand.poke(context, v, vi, index, sv, svi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of wpoke method, of class BasicCommand.
     */
    @Test
    public void testWpoke() {
        System.out.println("wpoke");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int index = 0;
        int word = 0;
        BasicCommand.wpoke(context, v, vi, index, word);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lpoke method, of class BasicCommand.
     */
    @Test
    public void testLpoke() {
        System.out.println("lpoke");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int index = 0;
        int dword = 0;
        BasicCommand.lpoke(context, v, vi, index, dword);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getstr method, of class BasicCommand.
     */
    @Test
    public void testGetstr() {
        System.out.println("getstr");
        Context context = new Context();
        Operand v = new Variable();
        int vi = 0;
        Operand strOp = new Variable();
        int stri = 0;
        strOp.assign(Scalar.fromValue("foo/"), stri);
        int offset = 0;
        int separator = '/';
        BasicCommand.getstr(context, v, vi, strOp, stri, offset, separator);
        assertEquals("foo", v.toString(vi));
        assertEquals(4, context.strsize.toInt());
        
        strOp.assign(Scalar.fromValue("foo"), stri);
        BasicCommand.getstr(context, v, vi, strOp, stri, offset, separator);
        assertEquals("foo", v.toString(vi));
        assertEquals(3, context.strsize.toInt());
        
        strOp.assign(Scalar.fromValue("/"), stri);
        BasicCommand.getstr(context, v, vi, strOp, stri, offset, separator);
        assertEquals("", v.toString(vi));
        assertEquals(1, context.strsize.toInt());
        
        strOp.assign(Scalar.fromValue("foo\r\nbar\r\nbaz"), stri);
        BasicCommand.getstr(context, v, vi, strOp, stri, offset, separator);
        assertEquals("foo", v.toString(vi));
        assertEquals(5, context.strsize.toInt());
    }

    /**
     * Test of chdpm method, of class BasicCommand.
     */
    @Test
    public void testChdpm() {
        System.out.println("chdpm");
        Context context = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.chdpm(context, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of memexpand method, of class BasicCommand.
     */
    @Test
    public void testMemexpand() {
        System.out.println("memexpand");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int newSize = 0;
        BasicCommand.memexpand(context, v, vi, newSize);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of memcpy method, of class BasicCommand.
     */
    @Test
    public void testMemcpy() {
        System.out.println("memcpy");
        Context context = null;
        Operand dv = null;
        int dvi = 0;
        Operand sv = null;
        int svi = 0;
        int size = 0;
        int doff = 0;
        int soff = 0;
        BasicCommand.memcpy(context, dv, dvi, sv, svi, size, doff, soff);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of memset method, of class BasicCommand.
     */
    @Test
    public void testMemset() {
        System.out.println("memset");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int s = 0;
        int size = 0;
        int offset = 0;
        BasicCommand.memset(context, v, vi, s, size, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of notesel method, of class BasicCommand.
     */
    @Test
    public void testNotesel() {
        System.out.println("notesel");
        Context context = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.notesel(context, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noteadd method, of class BasicCommand.
     */
    @Test
    public void testNoteadd() {
        System.out.println("noteadd");
        Context context = null;
        ByteString str = null;
        Operand linev = null;
        int linei = 0;
        int overwrite = 0;
        BasicCommand.noteadd(context, str, linev, linei, overwrite);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of notedel method, of class BasicCommand.
     */
    @Test
    public void testNotedel() {
        System.out.println("notedel");
        Context context = null;
        int line = 0;
        BasicCommand.notedel(context, line);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noteload method, of class BasicCommand.
     */
    @Test
    public void testNoteload() {
        System.out.println("noteload");
        Context context = null;
        String fileName = "";
        Operand sizev = null;
        int sizevi = 0;
        BasicCommand.noteload(context, fileName, sizev, sizevi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of notesave method, of class BasicCommand.
     */
    @Test
    public void testNotesave() {
        System.out.println("notesave");
        Context context = null;
        String filename = "";
        BasicCommand.notesave(context, filename);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of randomize method, of class BasicCommand.
     */
    @Test
    public void testRandomize() {
        System.out.println("randomize");
        Context context = null;
        Operand v = null;
        int vi = 0;
        BasicCommand.randomize(context, v, vi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noteunsel method, of class BasicCommand.
     */
    @Test
    public void testNoteunsel() {
        System.out.println("noteunsel");
        Context context = null;
        BasicCommand.noteunsel(context);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noteget method, of class BasicCommand.
     */
    @Test
    public void testNoteget() {
        System.out.println("noteget");
        Context context = null;
        Operand v = null;
        int vi = 0;
        int line = 0;
        BasicCommand.noteget(context, v, vi, line);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
