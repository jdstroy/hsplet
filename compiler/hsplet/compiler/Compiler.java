/*
 * $Id: Compiler.java,v 1.11.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.compiler;

import hsplet.FlagObject;
import hsplet.Context;
import hsplet.RunnableCode;
import hsplet.compiler.ByteCode.Code;
import hsplet.compiler.ByteCode.Function;
import hsplet.compiler.util.OpcodeIndexAsLineClassAdapter;
import hsplet.function.GotoException;
import hsplet.function.JumpStatement;
import hsplet.variable.ByteString;
import hsplet.variable.IntScalar;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
//import org.objectweb.asm.KLabel;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

/**
 * axファイルをコンパイルするクラス。
 * <p>
 * ASM を用いて Java のバイトコードを生成する。
 * </p>
 * <ul>
 * <li>変数・定数・パラメータはすべてフィールドにコンパイルする。</li>
 * <li>プログラムはエントリポイントとなる run メソッド内に実装するが、 ある程度の単位で区切ってサブメソッド m?? を呼び出すようにする。
 * そうすることでエントリポイントメソッドのサイズを抑えることが出来る。 </li>
 * <li>コマンドはメソッド呼び出しにコンパイルする。</li>
 * <li>ラベルはラベルとしてコンパイルする。</li>
 * <li>gosub、モジュールコマンド呼び出しは自分自身のエントリポイントをラベルを指定して呼び出すことで実現する。</li>
 * </ul>
 * 
 * @author Yuki
 * @version $Revision: 1.11.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
//Unused main labels in Elona: 2570, 2627, 2628, 2685, 2768, 2900, 3032, 3035, 3214, 3421, 4222, 4249, 4254, 4382, 4457, 4625, 5353, 5382, 5706
public class Compiler implements Opcodes, Serializable {

    /** このクラスを含むソースファイルのバージョン文字列。 */
    private static final String fileVersionID = "$Id: Compiler.java,v 1.11.2.1 2006/08/02 12:13:06 Yuki Exp $";
    /** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
    private static final long serialVersionUID = 8668239863505235428L;
    /** デバッグ出力をするかどうか。 */
    private static final boolean DEBUG_ENABLED = false;
    /** Show opcode index as line numbers in debug output */
    private static final boolean LINENUMS_ENABLED = false;
    /** Stores the results in an ASM tree.  This is slow and memory hungry;
     * you should use this only if you're debugging/poking around.  This 
     * shouldn't be used in production, but it is useful to find out 
     * how much of the constant pool is used.*/
    private static final boolean STORE_ENABLED = false;

    /**
     * ax ファイルをコンパイルする。
     * <p>
     * コンパイル後の jar と表示するための HTML が生成される。
     * </p>
     *
     * @param args
     *            実行時の引数。
     *            <ol>
     *            <li>--jar=生成するJARファイル名</li>
     *            <li>--html=生成するHTMLファイル名</li>
     *            <li>--template=使用するHTMLテンプレートファイル名</li>
     *            <li>--startClass=開始クラス名（オブジェクトファイルから.axを除いたもの）</li>
     *            <li>--lib=拡張ライブラリアーカイブ名</li>
     *            <li>--libdir=拡張ライブラリ検索ディレクトリ名</li>
     *            <li>--pack=アーカイブに入れるファイル</li>
     *            <li>--width=横幅</li>
     *            <li>--height=高さ</li>
     *            <li>--title=ページタイトル</li>
     *            </ol>
     * @throws IOException
     *             入出力エラーが発生したとき。
     */
    public static void main(final String[] args) throws IOException {

        int width = 640;
        int height = 480;
        Set<String> libs = new HashSet<String>();
        Set<String> libdirs = new HashSet<String>();
        Set<String> packs = new HashSet<String>();
        String title = "HSPLet";
        File jarFile = null;
        File htmlFile = null;
        File templateFile = null;
        String startClass = "start";

        for (int i = 0; i < args.length; ++i) {

            final String arg = args[i];
            if (arg.startsWith("--jar=")) {
                jarFile = new File(arg.substring("--jar=".length()));
            } else if (arg.startsWith("--html=")) {
                htmlFile = new File(arg.substring("--html=".length()));
            } else if (arg.startsWith("--template=")) {
                templateFile = new File(arg.substring("--template=".length()));
            } else if (arg.startsWith("--startClass=")) {
                startClass = arg.substring("--startClass=".length());
            } else if (arg.startsWith("--lib=")) {
                libs.add(arg.substring("--lib=".length()));
            } else if (arg.startsWith("--libdir=")) {
                libdirs.add(arg.substring("--libdir=".length()));
            } else if (arg.startsWith("--pack=")) {
                packs.add(arg.substring("--pack=".length()));
            } else if (arg.startsWith("--title=")) {
                title = arg.substring("--title=".length());

            } else if (arg.startsWith("--width=")) {
                width = Integer.parseInt(arg.substring("--width=".length()));

            } else if (arg.startsWith("--height=")) {
                height = Integer.parseInt(arg.substring("--height=".length()));

            }
        }

        if (jarFile == null) {
            throw new RuntimeException("出力ファイル名が指定されていません。");
        }

        build(jarFile, htmlFile, templateFile, startClass, title, packs, libs, libdirs, width, height);
    }

    public static String generateClassName(final String fileName) {
        String result = fileName.split("\\.")[0].replaceAll("[^a-zA-Z0-9_]", "");
        if (result.length() == 0) {
            return "_start";
        }
        if (Character.isDigit(result.charAt(0))) {
            return "_" + result;
        }

        return result;
    }

    public static void build(final File jarFile, final File htmlFile, final File templateFile, final String startClass,
            final String title, final Collection<String> packs, final Collection<String> libs, final Collection<String> libdirs, int w, int h)
            throws IOException {

        final LibraryLoader libraryLoader = new LibraryLoader((String[]) libs.toArray(new String[0]),
                (String[]) libdirs.toArray(new String[0]), Compiler.class.getClassLoader());

        final JarOutputStream jar = new JarOutputStream(new FileOutputStream(jarFile));
        try {

            if (jarFile.getName().equalsIgnoreCase("hsplet.jar")) {
                throw new IllegalArgumentException(jarFile.getName() + " という名前は使用できません。");
            }
            for (final Iterator<String> i = libs.iterator(); i.hasNext();) {
                final String extLib = i.next();

                if (jarFile.getName().equalsIgnoreCase(new File(extLib).getName())) {
                    throw new IllegalArgumentException(jarFile.getName() + " という名前はすでに拡張ライブラリで使用されています。");
                }
            }

            for (final Iterator<String> i = packs.iterator(); i.hasNext();) {
                final String pack = i.next();
                final File packFile = new File(pack);

                if (packFile.getName().toLowerCase().endsWith(".ax")) {
                    /* If it's an .ax file, it's probably an HSP bytecode file, 
                     * so compile it into a .class */

                    final String className = generateClassName(packFile.getName());

                    try {
                        final Compiler c = new Compiler(new ByteCode(new FileInputStream(packFile)),
                                packFile.getName(), libraryLoader);
                        c.compile(className, jar);
                    } catch (Exception ex) {
                        Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        jar.closeEntry();
                    }

                } else {
                    // Copy the file if it is not an .ax file

                    final JarEntry je = new JarEntry(packFile.getName());
                    je.setMethod(JarEntry.DEFLATED);
                    jar.putNextEntry(je);

                    final InputStream in = new FileInputStream(packFile);
                    try {
                        connectStream(in, jar);

                    } finally {
                        in.close();
                        jar.closeEntry();
                    }
                }
            }

        } finally {
            try {
                jar.close();
            }
            catch(Exception E){}
        }

        if (htmlFile != null) {

            final Set<String> libNames = new HashSet<String>();
            deploy(htmlFile.getAbsoluteFile().getParentFile(), jarFile);
            libNames.add(jarFile.getName());

            deploy(htmlFile.getAbsoluteFile().getParentFile(), new File("hsplet.jar"));
            libNames.add("hsplet.jar");

            for (final Iterator<String> i = libraryLoader.getUsedLibs().iterator(); i.hasNext();) {
                final String extLib = i.next();
                deploy(htmlFile.getAbsoluteFile().getParentFile(), new File(extLib));
                libNames.add(new File(extLib).getName());
            }

            final OutputStream html = new FileOutputStream(htmlFile);
            try {
                new HtmlGenerator(generateClassName(startClass), title, libNames, w, h, templateFile).generate(html);
            } finally {
                html.close();
            }
        }

    }

    private static void deploy(final File destDir, final File file) throws IOException {

        if (file.getAbsoluteFile().getParentFile().equals(destDir)) {
            return;
        }

        final FileInputStream in = new FileInputStream(file);
        try {

            final FileOutputStream out = new FileOutputStream(new File(destDir, file.getName()));
            try {
                connectStream(in, out);
            } finally {
                out.close();
            }

        } finally {
            in.close();
        }
    }

    private static void connectStream(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[1024];
        for (int length = in.read(buffer); length >= 0; length = in.read(buffer)) {
            out.write(buffer, 0, length);
        }
    }
    public static final String ODesc = Type.getDescriptor(Object.class);
    public static final String OIName = Type.getInternalName(Object.class);
    public static final String FODesc = Type.getDescriptor(FlagObject.class);
    public static final String FOIName = Type.getInternalName(FlagObject.class);
    public static final String contextDesc = Type.getDescriptor(Context.class);
    public static final String contextIName = Type.getInternalName(Context.class);
    public static final String opeDesc = Type.getDescriptor(Operand.class);
    public static final String opeIName = Type.getInternalName(Operand.class);
    public static final String varDesc = Type.getDescriptor(Variable.class);
    public static final String varIName = Type.getInternalName(Variable.class);
    public static final String literalDesc = Type.getDescriptor(Scalar.class);
    public static final String literalIName = Type.getInternalName(Scalar.class);
    public static final String parentDesc = Type.getDescriptor(RunnableCode.class);
    public static final String parentIName = Type.getInternalName(RunnableCode.class);
    public static final String typeArrayOfScalar = "[L" + literalIName + ";";
    private static final int thisIndex = 0;
    private static final int jumpLabelIndex = 1; // int
    private static final int contextIndex = 2; // contextType
    private static final int assignOffsetIndex = 3; // int
    private static final int literalsIndex = 4; // Scalar[]
    private static final int localsStart = 5;
    private List<CommonObjectContainer> commonLiteralsList = new ArrayList<CommonObjectContainer>();
    private ClassVisitor cw, superClassWriter;
    private String inputName;
    public String className;
    public String classIName;
    private String superClassName;
    private String superClassIName;
    private ByteCode ax;
    private int codeIndex;
    private List<Object> literals;
    private Stack<KLabel> loopStarts;
    private boolean enableVariableOptimization;
    private RuntimeInfo runtime;
    private List<Class> instancedLibraries;
    private final boolean useSuperClassConstants = true;
    private final boolean useLocalVariableForLiterals = true;
    private int[] varsStats;
    private int[] paramsStats;
    private final boolean commonVarsInLocals = true;
    /* The work for this isn't complete yet. This should be allow us to take pretty much the same code path as the original HSPlet.
     * Leave this set to true for now.
     */
    private final boolean useLiteralsInArray = true;

    /**
     * 入力バイトコードを指定してオブジェクトを構築する。
     *
     * @param ax
     *            バイトコード。
     * @param inputName
     *            入力ファイル名。
     * @param libraryLoader 拡張ライブラリローダ。
     */
    public Compiler(final ByteCode ax, final String inputName, final ClassLoader libraryLoader) {

        this.ax = ax;
        this.inputName = inputName;

        this.runtime = new DefaultRuntimeInfo(libraryLoader);

    }

    /**
     * データをコンパイルする。
     *
     * @param className
     *            コンパイル後のクラス名。
     * @param out
     *            出力先。
     * @throws IOException
     *             入出力エラーが発生したとき。
     */
    public void compile(final String className, final JarOutputStream out) throws IOException {

        this.className = className;
        this.classIName = className.replace('.', '/');
        this.superClassName = className + "Super";
        this.superClassIName = className + "Super";

        ClassVisitor output;

        //final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        output = writer;
        
        if (LINENUMS_ENABLED) {
            output = new OpcodeIndexAsLineClassAdapter(writer);
        }
        ClassWriter superWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //final ClassWriter writer = new ClassWriter(true);
        ClassNode bufferClassNode = new ClassNode();

        if (STORE_ENABLED) {
            output = bufferClassNode;
        }

    //output = new CheckClassAdapter(output);

        // フィールドの初期化。
        if (DEBUG_ENABLED) {
            cw = new ClassDebugger(output);
        } else {
            cw = output;
        }

        literals = new ArrayList<Object>();
        loopStarts = new Stack<KLabel>();
        instancedLibraries = new ArrayList<Class>();
        codeIndex = 0;

        // Original must be = false.  Whoops.
        enableVariableOptimization = false;

        // we will need to change parentIName to classIName + "Super"
        // クラス生成

        cw.visit(V1_4, ACC_PUBLIC, classIName, null, useSuperClassConstants ? classIName + "Super" : parentIName, new String[0]);

        collectLiterals();

        // Contains the superclass of the one that we'll be creating
        ClassNode superClassNode = createSuperClassVisitor();

        superClassWriter = superClassNode;
        superClassWriter.visit(V1_4, ACC_ABSTRACT | ACC_PUBLIC, classIName + "Super", null, parentIName, new String[0]);

        final int[] runLiteralStats, runParamStats, runVarStats;

        prepareLabels();

        firstScan(new ScanOneVisitor(labels, ax.labels));
        codeIndex = 0;
        secondScan(new ScanTwoVisitor());
        codeIndex = 0;
        int unusedStatements = thirdScan(new ScanThreeVisitor());
        codeIndex = 0;

        int numTargets = restructureLabels();    //numMethods is just labelGroups.length;

        if (DEBUG_ENABLED) {
            doScanAnalysis();
            System.out.println("Unused Statements: " + unusedStatements);
        }

        createRun(numTargets);
        if (collectStats) {
            runLiteralStats = new int[literalsStats.length];
            System.arraycopy(literalsStats, 0, runLiteralStats, 0, literalsStats.length);

            runParamStats = new int[paramsStats.length];
            System.arraycopy(paramsStats, 0, runParamStats, 0, paramsStats.length);

            runVarStats = new int[varsStats.length];
            System.arraycopy(varsStats, 0, runVarStats, 0, paramsStats.length);
        }
        createSubMethods();

        createConstructor();
        createSuperClassCtor();

        createArrayFieldInSuperClass();

        createFields();

        cw.visitEnd();

        superClassWriter.visitEnd();
        if (STORE_ENABLED) {
            //ASMifierClassVisitor asmifier = new ASMifierClassVisitor(new PrintWriter("J:\\HSPletDebug.java"));
            //bufferClassNode.accept(asmifier);
            bufferClassNode.accept(writer);
        }

        superClassNode.accept(superWriter);


        final JarEntry je = new JarEntry(className + ".class");
        je.setMethod(JarEntry.DEFLATED);
        out.putNextEntry(je);
        // 出力
        out.write(writer.toByteArray());
        out.closeEntry();

        final JarEntry jes = new JarEntry(superClassName + ".class");
        jes.setMethod(JarEntry.DEFLATED);
        out.putNextEntry(jes);
        out.write(superWriter.toByteArray());
        out.closeEntry();

        if (collectStats) {
            System.err.println("literals:");
            System.err.println("index\ttotal\tarray_load\trun\ttoString");
            for (int i = 0; i < literalsStats.length; i++) {
                System.err.format("%d\t%d\t%d\t%d\t%s\n", i, literalsStats[i], literalsStatsAaLoad[i], runLiteralStats[i], literals.get(i).toString());
            }

            System.err.println("vars:");
            System.err.println("index\ttotal\trun");
            for (int i = 0; i < varsStats.length; i++) {
                System.err.format("%d\t%d\t%d\n", i, varsStats[i], runVarStats[i]);
            }

            System.err.println("params:");
            System.err.println("index\ttotal\trun");
            for (int i = 0; i < paramsStats.length; i++) {
                System.err.format("%d\t%d\t%d\n", i, paramsStats[i], runParamStats[i]);
            }
        }
    }
    private int[] literalsStats;
    private int[] literalsStatsAaLoad;
    private final boolean collectStats = false;
    private int[] conversionArray;
    private int argsToOldLabel = 0;

    private int restructureLabels() {
        //Identify all main labels by their old index
        for (int i = 0; i < ax.labels.length; i++) {
            KLabel label = labels.get(ax.labels[i]);
            if (label.isMainLabel) {
                if (label.extra == null) {
                    label.extra = Integer.valueOf(i);
                    continue;
                } else if (label.extra instanceof Integer) {
                    Integer old = (Integer) label.extra;
                    label.extra = new ArrayList<Integer>();
                    ((ArrayList) label.extra).add(old);
                }
                ((ArrayList) label.extra).add(Integer.valueOf(i));
            }
        }
        //Create the conversionArray to convert call/gosub/on*/button values during compiling
        conversionArray = new int[ax.labels.length];
        int mainCount = 0;
        for (MyTreeThing labelGroup : labelGroups) {
            for (Integer I : labelGroup.mainLabels(allLabels)) {
                KLabel label = allLabels[I.intValue()];
                if (label.extra == null) {
                    //Default label and it does not get called elsewhere.
                    label.branchesToHere = -1;
                    continue;
                }
                label.branchesToHere = mainCount;
                if (label.extra instanceof Integer) {
                    Integer J = (Integer) label.extra;
                    conversionArray[J.intValue()] = mainCount;
                } else {
                    for (Integer J : ((ArrayList<Integer>) label.extra)) {
                        conversionArray[J.intValue()] = mainCount;
                    }
                }
                mainCount++;
            }
        }
        //I forget why I planned this..
        for (Integer I : allLabelOffsets) {
            labels.get(I.intValue()).extra = I;
        }
        return mainCount;
    }
    //This is only called if DEBUG_ENABLED

    private void doScanAnalysis() {
        StringBuilder str = new StringBuilder();
        {
            TreeSet<Integer> tree = allLabelOffsets;
            System.out.println("Labels");
            System.out.println("HSP Address:LabelIndex");
            for (Iterator<Integer> iter = tree.iterator(); iter.hasNext();) {
                Integer I = iter.next();
                KLabel L = labels.get(I);
                str.append(I).append(":").append(L.myIndex);
                if (L.isUsed) {
                    str.append(" Targetted:").append(L.branchesToHere);
                    if (L.isMainLabel) {
                        str.append(" (Main)");
                    }
                } else {
                    if (L.branchesToHere > 0) {
                        str.append(" Not used but targetted??").append(L.branchesToHere);
                    } else {
                        str.append(" Not used.");
                    }
                }
                System.out.println(str.toString());
                str.setLength(0);
            }
        }

        System.out.println("Tree groups start, " + labelGroups.length + " groups formed.");
        for (TreeSet<Integer> tree : labelGroups) {
            int mainCount = 0;
            int firstMainIndex = Integer.MAX_VALUE;
            int lastMainIndex = -1;
            boolean entry = (tree.first().intValue() == 0);
            if (tree.size() > 1000) {
                str.append("(").append(tree.size()).append(" labels, ").append(tree.first()).append("-").append(tree.last()).append(") ");
                for (Iterator<Integer> iter = tree.iterator(); iter.hasNext();) {
                    KLabel label = allLabels[iter.next().intValue()];
                    if (label.isMainLabel) {
                        mainCount++;
                        for (int i = 0; i < ax.labels.length; i++) {
                            if (labels.get(ax.labels[i]) == label) {
                                if (firstMainIndex > i) {
                                    firstMainIndex = i;
                                }
                                if (lastMainIndex < i) {
                                    lastMainIndex = i;
                                }
                            }
                        }
                    }
                }
            } else {
                Iterator<Integer> iter = tree.iterator();
                KLabel label = allLabels[iter.next().intValue()];
                str.append(label.myIndex);
                if (label.isMainLabel) {
                    mainCount++;
                    for (int i = 0; i < ax.labels.length; i++) {
                        if (labels.get(ax.labels[i]) == label) {
                            if (firstMainIndex > i) {
                                firstMainIndex = i;
                            }
                            if (lastMainIndex < i) {
                                lastMainIndex = i;
                            }
                        }
                    }
                }
                while (iter.hasNext()) {
                    label = allLabels[iter.next().intValue()];
                    str.append(", ").append(label.myIndex);
                    if (label.isMainLabel) {
                        mainCount++;
                        for (int i = 0; i < ax.labels.length; i++) {
                            if (labels.get(ax.labels[i]) == label) {
                                if (firstMainIndex > i) {
                                    firstMainIndex = i;
                                }
                                if (lastMainIndex < i) {
                                    lastMainIndex = i;
                                }
                            }
                        }
                    }
                }
                str.append(". ");
            }
            if ((mainCount == 0) && (!entry)) {
                System.out.println("Unused group!");
            } else {
                if (mainCount == 0) {
                    str.append("Entry only.");
                } else {
                    if (entry) {
                        str.append("Entry group. ");
                    }
                    str.append(mainCount).append(" main labels: ").append(firstMainIndex);
                    if (mainCount > 1) {
                        str.append(" to ").append(lastMainIndex);
                    }
                }
            }
            System.out.println(str.toString());
            str.setLength(0);
        }

        System.out.println("Loop Exceptions start, " + loopExceptions.size() + " exceptions found.");
        {
            TreeSet<Integer> tree = new TreeSet<Integer>(loopExceptions.keySet());
            for (Iterator<Integer> iter = tree.iterator(); iter.hasNext();) {
                Integer address = iter.next();
                str.append(address).append(" ").append(loopExceptions.get(address).myIndex);
                System.out.println(str.toString());
                str.setLength(0);
            }
        }

        System.out.println("False main labels:");
        {
            int falseMains = 0;
            int unusedMains = 0;
            for (int i = 0; i < ax.labels.length; i++) {
                KLabel L = labels.get(ax.labels[i]);
                if (L.isMainLabel) {
                    continue;
                }
                System.out.print(i + ", ");
                falseMains++;
                if (!L.isUsed) {
                    unusedMains++;
                    str.append(i).append(", ");
                }
            }
            System.out.print("\r\n");
            System.out.println(falseMains + " false main labels.");
            System.out.println(str.toString());
            str.setLength(0);
            System.out.println(unusedMains + " unused false main labels.");
        }
    }

    private void collectLiterals() {

        // よく使う。
        literals.add(new Integer(0));
        literals.add(new Double(0.0));
        literals.add("");
        literals.add(new String(""));

        // These are given local variables in all methods.
        int idx = localsStart;
        commonLiteralsList.add(new CommonObjectContainer(new Integer(0), idx++));
        commonLiteralsList.add(new CommonObjectContainer("", idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(1), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(2), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(3), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(7), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(255), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(38), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(8), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(10), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(4), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(20), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(9), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(-1), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(5), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(29), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(6), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(13), idx++));
        commonLiteralsList.add(new CommonObjectContainer(new Integer(100), idx++));

        for (CommonObjectContainer o : commonLiteralsList) {
            if (!literals.contains(o.o)) {
                literals.add(o.o);
            }
        }

        for (int i = 0; i < ax.codes.length; ++i) {

            final Object o = literalValueOf(ax.codes[i]);

            if (o != null) {
                if (literals.indexOf(o) < 0) {
                    literals.add(o);
                }
            }
        }

        if (collectStats) {

            literalsStats = new int[literals.size()];
            literalsStatsAaLoad = new int[literals.size()];
            varsStats = new int[ax.header.variableCount];
            paramsStats = new int[ax.parameters.length];
        }
    }

    private Object literalValueOf(final Code code) {

        switch (code.type) {
            case ByteCode.Code.Type.String:
                return new ByteString(ax.datas, code.value, false).toString();

            case ByteCode.Code.Type.DNum:
                final byte[] b = ax.datas;
                final int o = code.value;
                long bits = 0;
                for (int i = 0; i < 8; ++i) {
                    bits |= (b[o + i] & 0xFFL) << 8 * i;
                }
                return new Double(Double.longBitsToDouble(bits));

            case ByteCode.Code.Type.INum:
                return new Integer(code.value);
            default:
                return null;
        }

    }

    private void createFields() {

        cw.visitField(ACC_PRIVATE | ACC_FINAL, "context", contextDesc, null, null);

        // 使用する変数を用意する。
        for (int i = 0; i < ax.header.variableCount; ++i) {

            cw.visitField(ACC_PRIVATE | ACC_FINAL, "v" + i, varDesc, null, null);
        }

        // 定数を用意する、毎回作っていたら遅い。
        for (int i = 0; i < literals.size(); ++i) {
            if (!useSuperClassConstants && !useLiteralsInArray) {
                cw.visitField(ACC_PRIVATE | ACC_FINAL, "c" + i, literalDesc, null, null);
            }
        }

        // インスタンスが必要なライブラリを用意する。
        for (int i = 0; i < instancedLibraries.size(); ++i) {

            cw.visitField(ACC_PRIVATE | ACC_FINAL, "l" + i, Type.getDescriptor((Class) instancedLibraries.get(i)),
                    null, null);
        }
    }

    private void createConstructor() {

        final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + contextDesc + ")V", null, new String[0]);

        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitMethodInsn(INVOKESPECIAL, useSuperClassConstants ? superClassIName : parentIName, "<init>", "()V");

        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, classIName, "context", contextDesc);

        // 使用する変数を用意する。
        for (int i = 0; i < ax.header.variableCount; ++i) {

            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitTypeInsn(NEW, varIName);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, varIName, "<init>", "()V");
            mv.visitFieldInsn(PUTFIELD, classIName, "v" + i, varDesc);
        }

        // 定数を用意する、毎回作っていたら遅い。
        for (int i = 0; i < literals.size(); ++i) {

            final Object value = literals.get(i);

            if (!useSuperClassConstants) {
                mv.visitVarInsn(ALOAD, thisIndex);

                efficientLDC(mv, value);

                mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                        + Type.getDescriptor(value instanceof Integer ? Integer.TYPE
                        : value instanceof Double ? Double.TYPE : String.class) + ")" + literalDesc);

                mv.visitFieldInsn(PUTFIELD, classIName, "c" + i, literalDesc);
            }
        }

        // インスタンス化が必要なライブラリを用意する。
        for (int i = 0; i < instancedLibraries.size(); ++i) {

            final Class clazz = (Class) instancedLibraries.get(i);

            mv.visitVarInsn(ALOAD, thisIndex);
            try {
                // Context を受け取るコンストラクタがあるなら。
                clazz.getConstructor(new Class[]{Context.class});

                mv.visitTypeInsn(NEW, Type.getInternalName(clazz));
                mv.visitInsn(DUP);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(clazz), "<init>", "(" + contextDesc + ")V");

            } catch (NoSuchMethodException e) {

                try {
                    // デフォルトコンストラクタがあるなら。
                    clazz.getConstructor(new Class[]{});

                    mv.visitTypeInsn(NEW, Type.getInternalName(clazz));
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(clazz), "<init>", "()V");
                } catch (Exception e1) {

                    mv.visitInsn(ACONST_NULL);
                }
            }

            mv.visitFieldInsn(PUTFIELD, classIName, "l" + i, Type.getDescriptor(clazz));
        }

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

    }
    private Map<Integer, KLabel> labels;    //labels from ax code (pregenned) or if/else statements(genned on the fly), keys are ax offsets.
    private KLabel[] allLabels;

    private void createRun(int numTargets) {

        final MethodVisitor originalVisitor = cw.visitMethod(ACC_PUBLIC | ACC_FINAL, "run", "(I)" + opeDesc, null, new String[0]);
        final MethodVisitor mv = originalVisitor;
        
        mv.visitCode();
        if (numTargets == 0) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, classIName, "m0", "(I)" + FODesc);
            mv.visitFieldInsn(GETFIELD, FOIName, "returnObject", opeDesc);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
            return;
        }

        final KLabel start_try = new KLabel();
        final KLabel handle_return = new KLabel();
        final KLabel start_run = new KLabel();
        final KLabel[] switchTargets = new KLabel[numTargets];
        final KLabel end_try = new KLabel();
        final KLabel try_handler = new KLabel();
        
        for (int i = 0; i < switchTargets.length; i++) {
            switchTargets[i] = new KLabel();
        }
        mv.visitTryCatchBlock(start_try, end_try, try_handler, Type.getInternalName(GotoException.class));
        mv.visitLabel(start_try);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ILOAD, 1);    //this, int, int
        mv.visitTableSwitchInsn(0, numTargets - 1, start_run, switchTargets);    //this, int
        int visitedGroups = 0;
        mv.visitLabel(start_run);
        for (MyTreeThing labelGroup : labelGroups) {
            for (Integer I : labelGroup.mainLabels(allLabels)) {
                int i = allLabels[I.intValue()].branchesToHere;
                if (i != -1) {
                    mv.visitLabel(switchTargets[i]);
                }
            }
            mv.visitMethodInsn(INVOKEVIRTUAL, classIName, "m" + visitedGroups++, "(I)" + FODesc);    //FO
            mv.visitJumpInsn(GOTO, handle_return);
        }
        mv.visitLabel(handle_return);
        mv.visitInsn(DUP);    //FO, FO
        mv.visitFieldInsn(GETFIELD, FOIName, "returnNow", "Z");    //FO, Z
        final KLabel do_return = new KLabel();
        mv.visitJumpInsn(IFNE, do_return);    //FO
        mv.visitFieldInsn(GETFIELD, FOIName, "newTarget", "I");    //I
        mv.visitVarInsn(ISTORE, 1);    //Empty stack
        mv.visitJumpInsn(GOTO, start_try);
        mv.visitLabel(do_return);
        mv.visitFieldInsn(GETFIELD, FOIName, "returnObject", opeDesc);    //Operand
        mv.visitInsn(ARETURN);

        mv.visitLabel(end_try);

        
        mv.visitLabel(try_handler);

        

        mv.visitFieldInsn(GETFIELD, Type.getInternalName(GotoException.class), "label", "I");

        mv.visitVarInsn(ISTORE, 1);
        mv.visitJumpInsn(GOTO, start_try);

        mv.visitMaxs(0, 0);
        mv.visitEnd();

        //System.out.format("run#statsVisitor.getCount():%d\n", statsVisitor.getCount());
    }
    // Collect all CmpCmd labels, setup allLabels, mark mainLabels from gosubs
    TreeSet<Integer> allLabelOffsets;

    private void firstScan(ScanOneVisitor mv) {
        while (codeIndex < ax.codes.length) {
            compileStatement(mv);
        }
        allLabels = new KLabel[labels.size()];
        allLabelOffsets = new TreeSet<Integer>(labels.keySet());
        int i = 0;
        for (Iterator<Integer> iter = allLabelOffsets.iterator(); iter.hasNext(); i++) {
            KLabel L = labels.get(iter.next());
            L.myIndex = i;
            allLabels[i] = L;
        }
    }
    // Analyze how all labels interact to group together for methods, and identify unused labels
    private MyTreeThing[] labelGroups = null;
    private KLabel currentLabel = null;
    private HashMap<Integer, KLabel> loopExceptions = null;

    private void secondScan(ScanTwoVisitor mv) {
        mv.labelTree.addBase(0);
        for (int i : ax.labels) {
            mv.labelTree.addBase(labels.get(i).myIndex);
        }
        currentLabel = null;
        while (codeIndex < ax.codes.length) {
            mv.currentStatementAddress = ax.codes[codeIndex].offset;
            final KLabel label = labels.get(new Integer(mv.currentStatementAddress));
            if (label != null) {
                //NOTE/THOUGHT: This does not necessarily need to combine labels just because they run into eachother.
                if ((currentLabel != null) && (currentLabel != label)) {
                    mv.labelTree.combine(label.myIndex, currentLabel.myIndex);
                    label.relyOn(currentLabel);
                }
                currentLabel = label;
            }
            compileStatement(mv);
        }
        HashSet<KLabel> recursionCheck = new HashSet<KLabel>();
        for (KLabel L : allLabels) {
            recursionCheck.clear();
            resolveLabel(L, recursionCheck, true);
            if (!L.isUsed) {
                mv.labelTree.remove(L.myIndex);
            }
            //Below two should theoretically never happen, comment out if confident enough about that.
            /*
            if(L.extra!=null) throw new RuntimeException("Error in label logic! .extra not cleared after use logic.");
            if((L.isUsed)&&(!mv.labelTree.contains(L.myIndex))) {
                //throw new RuntimeException("Error in label logic! Used label not in tree.");
                System.out.println("KLabel "+L.myIndex+" used but not in tree.");
            }
            */
        }
        labelGroups = mv.labelTree.labelSets();
        loopExceptions = mv.getLoopExceptions();
    }

    private boolean resolveLabel(KLabel label, HashSet<KLabel> recursionCheck, boolean isSure) {
        if (recursionCheck.contains(label)) {
            return false;
        }
        if (label.extra == null) {
            return label.isUsed;
        }
        //if(label.extra==Boolean.TRUE) { label.isUsed=true; label.extra=null; return true; }
        if (label.extra instanceof KLabel) {
            KLabel other = (KLabel) label.extra;
            recursionCheck.add(label);
            label.isUsed = resolveLabel(other, recursionCheck, isSure);
            if (isSure || other.extra == null) {
                label.extra = null;
            }
            return label.isUsed;
        }
        if (label.extra instanceof HashSet) {
            HashSet<KLabel> myArray = (HashSet<KLabel>) label.extra;
            recursionCheck.add(label);
            for (KLabel other : myArray.toArray(new KLabel[0])) {
                if (resolveLabel(other, recursionCheck, false)) {
                    label.isUsed = true;
                    label.extra = null;
                    return true;
                } else if (other.extra == null) {
                    myArray.remove(other);
                    if (myArray.size() == 0) {
                        label.extra = null;
                    }
                }
            }
            if (isSure) {
                label.extra = null;
            }
        }
        return label.isUsed;
    }
    //This simply counts REAL branches to labels

    private int thirdScan(ScanThreeVisitor mv) {
        MethodVisitor empty = EmptyVisitor.mv;
        while (codeIndex < ax.codes.length) {
            mv.currentStatementAddress = ax.codes[codeIndex].offset;
            final KLabel label = labels.get(mv.currentStatementAddress);
            if ((label != null) && (label.isUsed)) {
                currentLabel = label;
            }
            if (currentLabel == null) {
                currentLabel = loopExceptions.get(mv.currentStatementAddress);
                //Main compile loop will have more in here for mv stuff.
            }
            if (currentLabel == null) {
                compileStatement(empty);
                mv.unusedStatements++;
            } else {
                compileStatement(mv);
            }
        }
        return mv.unusedStatements;
    }

    int[] functionParamFix;
    private void prepareLabels() {

        labels = new HashMap<Integer, KLabel>();

        // 先頭のラベル
        KLabel L;

        for (int i = 0; i < ax.labels.length; ++i) {
            L = new KLabel();
            //L.isMainLabel=true;
            //L.isUsed=true;
            labels.put(new Integer(ax.labels[i]), L);
        }

        L = new KLabel();
        L.isMainLabel = true;
        L.isUsed = true;
        labels.put(new Integer(ax.codes[0].offset), L);

        int maxFunctionParm=0;
        for (ByteCode.Function function : ax.functions) {
            if(function.prmmax>0 && (function.prmmax+function.prmindex>maxFunctionParm))
                maxFunctionParm=function.prmmax+function.prmindex;
            if(function.index>=0) continue;
            L = labels.get(ax.labels[function.otindex]);
            L.isMainLabel = true;
            L.isUsed = true;
        }
        functionParamFix=new int[maxFunctionParm];
        for (ByteCode.Function function : ax.functions) {
            for(int i=1;i<function.prmmax;i++) {
                functionParamFix[function.prmindex+i]=i;
            }
        }
    }

    //Get a label from the original code's list of labels (aka only pregenned labels, no compiler-generated labels for loops and such)
    private KLabel getLabel(final int index) {

        return labels.get(new Integer(ax.labels[index]));
    }

    private void getLiteralByIndex(final MethodVisitor mv, final int index) {
        getLiteralByIndex(mv, index, true);
    }

    private int getCommonLiteralLocalVariableIndexByIndex(int index) {
        Object target = literals.get(index);
        for (CommonObjectContainer container : commonLiteralsList) {
            if (container.o.equals(target)) {
                return container.localIndex;
            }
        }
        return -1;
    }

    private void getLiteralByIndex(final MethodVisitor mv, final int index, final boolean useLocals) {
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(" Literal "+index);
        final int lvIndex = (useLocals && useLocalVariableForLiterals) ? getCommonLiteralLocalVariableIndexByIndex(index) : -1;
        if (lvIndex > -1) {
            mv.visitVarInsn(ALOAD, lvIndex);
        } else {
            if (useLiteralsInArray) {
                if (useLocalVariableForLiterals) {
                    mv.visitVarInsn(ALOAD, literalsIndex);
                } else {
                    mv.visitVarInsn(ALOAD, thisIndex);
                    mv.visitFieldInsn(GETFIELD, useSuperClassConstants ? superClassIName : classIName, "cLiterals", typeArrayOfScalar);
                }

                pushInteger(index, mv);

                if (collectStats) {
                    literalsStatsAaLoad[index] += 1;
                }
                mv.visitInsn(AALOAD);
            } else {
                mv.visitVarInsn(ALOAD, thisIndex);
                mv.visitFieldInsn(GETFIELD, useSuperClassConstants ? superClassIName : classIName, "c" + index, typeArrayOfScalar);
            }
        }
    }

    private int numExtraMethods=0;
    public int getExtraMVNum() {
        return numExtraMethods++;
    }
    public MethodVisitor getExtraMV(String name, String signature) {
        MethodVisitor mv=cw.visitMethod(ACC_PRIVATE, name, signature, null, new String[0]);
        return mv;
    }
    
    /**
     * 必要なローカル変数を用意。
     *
     * @param mv
     */
    public void compileLocalVariables(final MethodVisitor mv) {

        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitFieldInsn(GETFIELD, classIName, "context", contextDesc);
        mv.visitVarInsn(ASTORE, contextIndex);
        if (useLocalVariableForLiterals) {
            // cLiterals in locals[literalsIndex]
            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitFieldInsn(GETFIELD, superClassIName, "cLiterals", typeArrayOfScalar);
            mv.visitVarInsn(ASTORE, literalsIndex);

            for (CommonObjectContainer obj : commonLiteralsList) {
                getLiteralByIndex(mv, literals.indexOf(obj.o), false);
                mv.visitVarInsn(ASTORE, obj.localIndex);
            }

            for (int i = 0; i < ax.header.variableCount; ++i) {
                if (commonVarsInLocals && commonVariable(i)) {
                    mv.visitVarInsn(ALOAD, thisIndex);
                    mv.visitFieldInsn(GETFIELD, classIName, "v" + i, varDesc);
                    mv.visitVarInsn(ASTORE, getVariableLocalIndex(i));
                }
            }

        }

    }

    private boolean commonVariable(int varIndex) {
        switch (varIndex) {
            case 504:
            case 502:
            case 57:
            case 243:
            case 343:
            case 99:
            case 60:
            case 58:
            case 240:
            case 241:
            case 623:
                return true;
            default:
                return false;
        }
    }

    private int getVariableLocalIndex(int varIndex) {

        switch (varIndex) {
            case 504:
                return 254;
            case 502:
                return 253;
            case 57:
                return 252;
            case 243:
                return 251;
            case 343:
                return 250;
            case 99:
                return 249;
            case 60:
                return 248;
            case 58:
                return 247;
            case 240:
                return 246;
            case 241:
                return 245;
            case 623:
                return 244;
            default:
                throw new UnsupportedOperationException("Not implemented");
        }
    }
    private final String[] byteCodeCodeTypes = new String[]{
        "Mark ",
        "Var ",
        "String ",
        "DNum ",
        "INum ",
        "Struct ",
        "XLabel ",
        "Label ",
        "IntCmd ",
        "ExtCmd ",
        "ExtSysVar ",
        "CmpCmd ",
        "ModCmd ",
        "IntFunc ",
        "SysVar ",
        "ProgCmd ",
        "DllFunc ",
        "DllCtrl ",
        "UserDef ",
        "JumpOffset "
    };

    private void compileStatement(final MethodVisitor mv) {

        // ステートメントは変数・パラメータへの代入、または命令で始まると決まっている。

        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(ax.codes[codeIndex].offset+" "+byteCodeCodeTypes[ax.codes[codeIndex].type]);
        switch (ax.codes[codeIndex].type) {
            case ByteCode.Code.Type.Var:
            case ByteCode.Code.Type.Struct:
                compileAssignment(mv);
                break;
            case ByteCode.Code.Type.ExtCmd:
                if ((mv instanceof ScanOneVisitor) && (ax.codes[codeIndex].value == 0)) {
                    ((ScanOneVisitor) mv).setArgCount(1);
                } else if ((conversionArray != null) && (ax.codes[codeIndex].value == 0)) {
                    argsToOldLabel = 1;
                }
                compileCommand(mv);
                break;
            case ByteCode.Code.Type.IntCmd:
                if(mv instanceof ScanOneVisitor) {
                    if(ax.codes[codeIndex].value<=4) {
                        ((ScanOneVisitor)mv).setArgCount(1);
                    }
                } else if(conversionArray!=null) {
                    if(ax.codes[codeIndex].value<=4) {
                        argsToOldLabel=1;
                    }
                }
                compileCommand(mv);
                break;
            case ByteCode.Code.Type.CmpCmd:
                compileCompareCommand(mv);
                break;
            case ByteCode.Code.Type.ModCmd:
                compileModuleCommand(mv, false);
                break;
            case ByteCode.Code.Type.ProgCmd:
                compileProgramCommand(mv);
                break;
            case ByteCode.Code.Type.DllFunc:
                compileDllCommand(mv);
                break;
            case ByteCode.Code.Type.DllCtrl:
                compileCommand(mv);
                break;
            default:
                throw new RuntimeException("命令コード " + ax.codes[codeIndex].type + " は解釈できません。");
        }
            //if(mv instanceof ScanOneVisitor)
            //    System.out.print("\r\n");
    }
    private static final String[] assignOperators = new String[]{"assignAdd", "assignSub", "assignMul", "assignDiv",
        "assignMod", "assignAnd", "assignOr", "assignXor", "assign", "assignNe", "assignGt", "assignLt", "assignGtEq", "assignLtEq", "assignSr",
        "assignSl"};
    private static final String[] unaryOperators = new String[]{"inc", "dec"};

    private void compileAssignment(final MethodVisitor mv) {

        final boolean prevEnableVariableOptimization = enableVariableOptimization;

        // 演算子を先読みして最適化を有効にする。
        // {
        int index = codeIndex;

        if (ax.codes[codeIndex].type == Code.Type.Var) {
            compileVariable(EmptyVisitor.mv);
        } else {
            compileParameter(EmptyVisitor.mv);
        }

        enableVariableOptimization |= ax.codes[codeIndex].value != 8;

        codeIndex = index;

        // }

        if (ax.codes[codeIndex].type == Code.Type.Var) {
            compileVariable(mv);
        } else {
            compileParameter(mv);
        }

        // 代入の右辺は常に最適化が有効
        enableVariableOptimization = true;

        final Code code = ax.codes[codeIndex++];

        if (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine) {

            // 代入演算子

            String name = assignOperators[code.value];

            index = codeIndex;
            compileExpression(EmptyVisitor.mv);
            boolean lastCommand = (codeIndex >= ax.codes.length || ax.codes[codeIndex].newLine);
            codeIndex = index;

            if(lastCommand) {
                compileExpression(mv);
                mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, name, "(I" + opeDesc + "I)V");
            } else {
                mv.visitVarInsn(ISTORE, assignOffsetIndex);
    
                do {
    
                    mv.visitInsn(DUP);
                    mv.visitVarInsn(ILOAD, assignOffsetIndex);
                    mv.visitIincInsn(assignOffsetIndex, 1);
                    compileExpression(mv);
                    mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, name, "(I" + opeDesc + "I)V");
    
                    index = codeIndex;
                    compileExpression(EmptyVisitor.mv);
                    lastCommand = (codeIndex >= ax.codes.length || ax.codes[codeIndex].newLine);
                    codeIndex = index;
    
                } while (!lastCommand);
    
                mv.visitVarInsn(ILOAD, assignOffsetIndex);
                compileExpression(mv);
                mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, name, "(I" + opeDesc + "I)V");
            }
    
        } else {
    
            // 単項演算子
    
            String name = unaryOperators[code.value];
    
            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, name, "(I)V");
    
        }
    
        enableVariableOptimization = prevEnableVariableOptimization;
    
    }

    private void compileVariableFastLoad(final MethodVisitor mv, final int varIndex) {
        if (commonVariable(varIndex)) {
            mv.visitVarInsn(ALOAD, getVariableLocalIndex(varIndex));
        }
    }

    private void compileVariable(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        if (collectStats) {
            varsStats[code.value]++;
        }

        if (commonVarsInLocals && commonVariable(code.value)) {
            compileVariableFastLoad(mv, code.value);
        } else {
            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitFieldInsn(GETFIELD, classIName, "v" + code.value, varDesc);
        }

        if (enableVariableOptimization) {
            mv.visitFieldInsn(GETFIELD, varIName, "value", opeDesc);
        }
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(" compVar "+code.value);

        compileArrayIndex(mv);

    }

    private void compileArrayIndex(final MethodVisitor mv) {

        int paramCount = 0;
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print("[");
        if (codeIndex < ax.codes.length && ax.codes[codeIndex].type == Code.Type.Mark
                && ax.codes[codeIndex].value == '(') {

            ++codeIndex;

            final int index = codeIndex;

            compileExpression(EmptyVisitor.mv);

            final boolean moreThan2Dim = (codeIndex < ax.codes.length && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')'));

            codeIndex = index;

            if (!moreThan2Dim) {

                compileExpression(mv);
                mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

            } else {
                mv.visitInsn(DUP);

                // 配列要素アクセス

                do {
                    ++paramCount;

                    compileExpression(mv);
                    mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

                } while (codeIndex < ax.codes.length
                        && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')'));

                mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "getIndex", "(" + "IIII".substring(0, paramCount) + ")I");

            }

            ++codeIndex;

        } else {

            // 配列アクセスじゃないときはインデックスは 0 固定。
            mv.visitInsn(ICONST_0);
        }
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print("]");
    }

    private boolean maySkipLabel=false;
    private int compileExpression(final MethodVisitor mv) {

        // 先読みして最適化を有効化、トークンが二つ以上ある（演算される）時は有効。
        // {

        final boolean prevEnableVariableOptimization = enableVariableOptimization;

        final int index = codeIndex;

        compileToken(EmptyVisitor.mv);

        enableVariableOptimization |= (codeIndex < ax.codes.length
                && !(ax.codes[codeIndex].type == Code.Type.Mark && (ax.codes[codeIndex].value == ')' || ax.codes[codeIndex].value == '?')) && !(ax.codes[codeIndex].newLine | ax.codes[codeIndex].comma));

        codeIndex = index;

        // }

        int i=0;
        do {

            i+=compileToken(mv);

        } while (codeIndex < ax.codes.length
                && !(ax.codes[codeIndex].type == Code.Type.Mark && (ax.codes[codeIndex].value == ')' || ax.codes[codeIndex].value == '?'))
                && !(ax.codes[codeIndex].newLine | ax.codes[codeIndex].comma));

        enableVariableOptimization = prevEnableVariableOptimization;

        return i;
    }

    private int compileToken(final MethodVisitor mv) {

        // トークンはリテラル・変数・演算子・関数呼び出しと決まっている。
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(" "+byteCodeCodeTypes[ax.codes[codeIndex].type]);

        int added=0;
        switch (ax.codes[codeIndex].type) {
            case ByteCode.Code.Type.Mark:
                compileOperator(mv);
                added=-1;
                break;
            case ByteCode.Code.Type.INum:
                if(mv instanceof ScanOneVisitor) {
                    ((ScanOneVisitor)mv).setArgCount(0);
                } else if((argsToOldLabel>0)&&(!(mv instanceof EmptyVisitor))) {
                    argsToOldLabel=0;
                }
            case ByteCode.Code.Type.String:
            case ByteCode.Code.Type.DNum:
                compileLiteral(mv);
                added=1;
                break;
            case ByteCode.Code.Type.Struct:
                compileParameter(mv);
                added=1;
                break;
            case ByteCode.Code.Type.Label:
                compileLabel(mv);
                added=1;
                break;
            case ByteCode.Code.Type.Var:
                compileVariable(mv);
                added=1;
                break;
            case ByteCode.Code.Type.ExtSysVar:
                compileGuiSystmVariable(mv);
                added=1;
                break;
            case ByteCode.Code.Type.ModCmd:
                compileModuleCommand(mv, true);
                added=1;
                break;
            case ByteCode.Code.Type.IntFunc:
                compileFunction(mv);
                added=1;
                break;
            case ByteCode.Code.Type.SysVar:
                compileSystemVariable(mv);
                added=1;
                break;
            case ByteCode.Code.Type.ProgCmd:
                compileProgramCommand(mv);
                //added=0;
                break;
            case ByteCode.Code.Type.DllFunc:
                compileDllFunction(mv);
                added=1;
                break;
            case ByteCode.Code.Type.DllCtrl:
                compileDllFunction(mv);
                added=1;
                break;

            default:
                throw new RuntimeException("命令コード " + ax.codes[codeIndex].type + " は解釈できません。");
        }
        return added;
    }
    private boolean skipToInt = false;

    private void compileLabel(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        if(mv instanceof ScanOneVisitor) {
            ((ScanOneVisitor)mv).decArgCount(code.value);
        }
        if((argsToOldLabel>0)&&(!(mv instanceof EmptyVisitor))) {
            argsToOldLabel--;
            if(argsToOldLabel==0) {
                assert (code.value < 32768);
                pushInteger(conversionArray[code.value], mv);
            }
        }
        else
            efficientLDC(mv, Integer.valueOf(code.value));
        if(maySkipLabel) {
            skipToInt=true;
            return;
        }
        mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromLabel", "(I)" + literalDesc);

        mv.visitInsn(ICONST_0);
    }
    private static final String[] binaryOperators = new String[]{"add", "sub", "mul", "div", "mod", "and", "or",
        "xor", "eq", "ne", "gt", "lt", "ge", "le", "sr", "sl"};

    private void compileOperator(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        final String name = binaryOperators[code.value];

        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, name, "(I" + opeDesc + "I)" + opeDesc);

        mv.visitInsn(ICONST_0);
    }

    private void compileLiteral(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        compileLiteral(mv, literalValueOf(code));
    }
    private final Integer zero = new Integer(0);

    private void compileLiteral(final MethodVisitor mv, final Object o) {

        int index = literals.indexOf(o);

        if (collectStats) {
            literalsStats[index] += 1;
        }
        getLiteralByIndex(mv, index);

        mv.visitInsn(ICONST_0);
    }

    private void compileParameter(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        if (collectStats) {
            paramsStats[code.value]++;
        }

        mv.visitVarInsn(ALOAD, contextIndex);
        pushInteger(functionParamFix[code.value], mv);
        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "getArgument", "(I)"+opeDesc);

        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(" Parameter "+code.value);

        compileArrayIndex(mv);

    }

    private void compileGuiSystmVariable(final MethodVisitor mv) {

        compileInvocation(mv, true, true);
    }

    private void compileInvocation(final MethodVisitor mv, boolean bracket, final boolean hasresult) {

        final Code code = ax.codes[codeIndex++];

        final Class libraryClass = runtime.getClassFor(ax, code);
        final Method method = runtime.getMethodFor(ax, code);
        final String methodDesc = Type.getMethodDescriptor(method);
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(" "+method.getName()+methodDesc);

        if (!Modifier.isStatic(method.getModifiers())) {

            if (!instancedLibraries.contains(libraryClass)) {
                instancedLibraries.add(libraryClass);
            }

            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitFieldInsn(GETFIELD, classIName, "l" + instancedLibraries.indexOf(libraryClass), Type.getDescriptor(libraryClass));
        }

        final boolean noeatparam;
        if (bracket && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == '(')) {
            // パラメータ無しシステム変数
            noeatparam = true;
            bracket = false;
        } else {
            noeatparam = false;
        }

        if (bracket) {
            ++codeIndex; // ( を読み飛ばす。
        }

        compileInvocationParameters(mv, method, noeatparam);

        if (bracket) {

            // ')' 見つからず
            if (!(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')')) {
                throw new RuntimeException("対応する ) が見つかりません。");
            }

            ++codeIndex; // ) を読み飛ばす。
        }

        // 呼び出す。
        /* if(method.getName().equals("gosub")) {
            mv.visitInsn(SWAP);
            mv.visitInsn(POP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(SWAP);
            mv.visitMethodInsn(INVOKEVIRTUAL, classIName, "run", "(I)"+opeDesc);
            mv.visitInsn(POP);
        }
        else */
            mv.visitMethodInsn(Modifier.isStatic(method.getModifiers()) ? INVOKESTATIC : INVOKEVIRTUAL, Type.getInternalName(libraryClass), method.getName(), methodDesc);

        if (hasresult) {

            // 関数のときは戻り値が必要。

            if (method.getReturnType().equals(Void.TYPE)) {
                getLiteralByIndex(mv, literals.indexOf(new Integer(0)));
            } else if (method.getReturnType().equals(Operand.class)) {
                // 何もする必要なし
            } else {

                mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                        + Type.getDescriptor(method.getReturnType()) + ")" + literalDesc);

            }

            mv.visitInsn(ICONST_0);

        } else {

            // 命令のときは戻り値は stat に代入

            if (method.getReturnType().equals(Integer.TYPE)) {

                mv.visitVarInsn(ALOAD, contextIndex);
                mv.visitFieldInsn(GETFIELD, contextIName, "stat", Type.getDescriptor(IntScalar.class));

                mv.visitInsn(SWAP);

                mv.visitFieldInsn(PUTFIELD, Type.getInternalName(IntScalar.class), "value", "I");

            } else if (!method.getReturnType().equals(Void.TYPE)) {
                // int 以外は無視
                mv.visitInsn(POP);
            }
        }

    }

    private void compileInvocationParameters(final MethodVisitor mv, final Method method, final boolean noeatparam) {

        boolean firstParam = true;

        for (int paramIndex = 0; paramIndex < method.getParameterTypes().length; ++paramIndex) {

            Class type = method.getParameterTypes()[paramIndex];

            if (type.equals(Context.class)) {

                mv.visitVarInsn(ALOAD, contextIndex);

            } else if (type.equals(JumpStatement.class)) {

                if (!noeatparam && codeIndex < ax.codes.length && !(ax.codes[codeIndex].newLine)
                        && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')')
                        && (ax.codes[codeIndex].type == Code.Type.ProgCmd)) {

                    mv.visitFieldInsn(GETSTATIC, Type.getInternalName(JumpStatement.class),
                            ax.codes[codeIndex].value == 0 ? "Goto" : "Gosub", Type.getDescriptor(JumpStatement.class));

                    ++codeIndex;
                } else {
                    mv.visitInsn(ACONST_NULL);
                }

            } else {

                boolean omitted;

                final Code code = ax.codes[codeIndex];

                if (!noeatparam && codeIndex < ax.codes.length && !(code.newLine)
                        && !(code.type == Code.Type.Mark && code.value == ')')) {

                    if (firstParam && code.comma) {
                        omitted = true;
                    } else if (code.type == Code.Type.Mark && code.value == '?') {
                        omitted = true;
                        ++codeIndex;
                    } else {
                        omitted = false;
                    }
                } else {
                    omitted = true;
                }

                firstParam = false;

                if (!omitted) {
                    maySkipLabel=type.equals(Integer.TYPE);
                    int numVals=compileExpression(mv);
                    
                    while(true) {
                        if (skipToInt) {
                            skipToInt = false;
                        } else if (type.equals(Operand.class)) {
    
                            ++paramIndex; // 次の int は読み飛ばす
    
                            // 次は必ず int
                            if (!method.getParameterTypes()[paramIndex].equals(Integer.TYPE)) {
                                throw new RuntimeException(
                                        "拡張ライブラリの引数に Operand を受け取ったときはかならずその次の引数で配列インデックスを受け取らなければいけません。");
                            }
    
                        } else if (type.equals(Integer.TYPE)) {
    
                            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");
    
                        } else if (type.equals(Double.TYPE)) {
    
                            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toDouble", "(I)D");
    
                        } else if (type.equals(ByteString.class)) {
    
                            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toByteString", "(I)" + Type.getDescriptor(type));
    
                        } else if (type.equals(String.class)) {
    
                            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toString", "(I)"
                                    + Type.getDescriptor(String.class));
    
                        } else {
                            throw new UnsupportedOperationException("拡張ライブラリの引数型 " + type + " はサポートされていません。");
                        }
                        
                        if(--numVals<=0)
                            break;
                        
                        paramIndex++;
                        type = method.getParameterTypes()[paramIndex];
                    }
                    maySkipLabel=false;

                } else {

                    if (type.equals(Operand.class)) {

                        mv.visitInsn(ACONST_NULL);
                        mv.visitInsn(ICONST_0);

                        ++paramIndex; // 次の int は読み飛ばす

                        // 次は必ず int
                        if (!method.getParameterTypes()[paramIndex].equals(Integer.TYPE)) {
                            throw new RuntimeException(
                                    "拡張ライブラリの引数に Operand を受け取ったときはかならずその次の引数で配列インデックスを受け取らなければいけません。");
                        }

                    } else if (type.equals(Integer.TYPE)) {

                        mv.visitInsn(ICONST_0);

                    } else if (type.equals(Double.TYPE)) {

                        mv.visitInsn(DCONST_0);

                    } else {

                        mv.visitInsn(ACONST_NULL);

                    }
                }
            }

        }

        // いらない引数は読み飛ばす。
        while (!noeatparam && codeIndex < ax.codes.length && !(ax.codes[codeIndex].newLine)
                && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')')) {

            compileExpression(EmptyVisitor.mv);

        }
    }

    private void compileModuleCommand(final MethodVisitor mv, final boolean hasresult) {

        final Code code = ax.codes[codeIndex++];
        final Method method = runtime.getMethodFor(ax, code);
        final String methodDesc = Type.getMethodDescriptor(method);
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print(" "+method.getName()+methodDesc);

        final ByteCode.Function function = ax.functions[code.value];

        // '(' を読み飛ばす
        if (function.isFunction()) {
            ++codeIndex;
        }

        compileModuleParameters(mv, function);

        // ')' を読み飛ばす。
        if (function.isFunction()) {
            ++codeIndex;
        }

        mv.visitVarInsn(ALOAD, contextIndex);

        Integer labelInt;
        if (conversionArray != null) {
            labelInt = new Integer(conversionArray[ax.functions[code.value].otindex]);
        } else {
            labelInt = new Integer(ax.functions[code.value].otindex);
        }
        efficientLDC(mv, labelInt);

        pushInteger(function.prmindex, mv);
        mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(method.getDeclaringClass()), method.getName(),
                methodDesc);

        if (hasresult) {

            // 関数のときは戻り値が必要。

            if (!function.isFunction()) {

                getLiteralByIndex(mv, literals.indexOf(new Integer(0)));

            } else {

                mv.visitInsn(ICONST_0);

            }

        } else {

            // 命令のときは戻り値は stat に代入

            mv.visitInsn(DUP);

            final KLabel noassign = new KLabel();
            noassign.branchesToHere=1;
            mv.visitJumpInsn(IFNULL, noassign);

            mv.visitVarInsn(ALOAD, contextIndex);
            mv.visitFieldInsn(GETFIELD, contextIName, "stat", Type.getDescriptor(IntScalar.class));

            mv.visitInsn(SWAP);

            mv.visitInsn(ICONST_0);

            mv.visitInsn(SWAP);

            mv.visitInsn(ICONST_0);

            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "assign", "(I" + opeDesc + "I)V");

            final KLabel end = new KLabel();
            end.branchesToHere=1;
            mv.visitJumpInsn(GOTO, end);
            mv.visitLabel(noassign);
            mv.visitInsn(POP);
            mv.visitLabel(end);
        }

    }

    private void compileModuleParameters(final MethodVisitor mv, final Function function) {

        boolean firstParam = true;

        pushInteger(function.prmmax, mv);
        mv.visitTypeInsn(ANEWARRAY, opeIName);

        for (int paramIndex = 0; paramIndex < function.prmmax; ++paramIndex) {

            final ByteCode.Parameter param = ax.parameters[function.prmindex + paramIndex];
            final int type = param.mptype;

            mv.visitInsn(DUP);
            pushInteger(paramIndex, mv);

            boolean omitted;

            final Code code = ax.codes[codeIndex];

            if (codeIndex < ax.codes.length && !(code.newLine) && !(code.type == Code.Type.Mark && code.value == ')')) {

                if (firstParam && code.comma) {
                    omitted = true;
                } else if (code.type == Code.Type.Mark && code.value == '?') {
                    omitted = true;
                    ++codeIndex;
                } else {
                    omitted = false;
                }
            } else {
                omitted = true;
            }

            firstParam = false;

            if (!omitted) {

                compileExpression(mv);

                switch (type) {
                    case 1: // var
                    case -1: // local variable
                    case -3: // single variable
                    {
                        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "ref", "(I)" + opeDesc);
                    }
                    break;
                    case -2: // array variable
                    {
                        mv.visitInsn(POP);
                        mv.visitInsn(ICONST_0);
                        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "ref", "(I)" + opeDesc);
                    }
                    break;
                    case -6: // local string
                    case 2: // string
                    {

                        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toByteString", "(I)"
                                + Type.getDescriptor(ByteString.class));

                        mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                                + Type.getDescriptor(ByteString.class) + ")" + literalDesc);
                    }
                    break;
                    case 3: // dnum
                    {
                        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toDouble", "(I)D");

                        mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "(D)" + literalDesc);
                    }
                    break;
                    case 4: // inum
                    {
                        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

                        mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "(I)" + literalDesc);

                    }
                    break;
                    case 5: // struct
                        throw new UnsupportedOperationException("ユーザ定義命令の引数型 struct はサポートされていません。");
                    case 6: // label
                        throw new UnsupportedOperationException("ユーザ定義命令の引数型 label はサポートされていません。");
                    default:
                        throw new UnsupportedOperationException("ユーザ定義命令の引数型 " + type + " はサポートされていません。");
                }

            } else {

                switch (type) {
                    case 1: // var
                    case -1: // local variable
                    case -2: // array variable
                    case -3: // single variable
                        throw new RuntimeException("ユーザ定義命令の引数の変数は省略できません。");
                    case -6: // local string
                    case 2: // string
                    {
                        getLiteralByIndex(mv, literals.indexOf(""));
                    }
                    break;
                    case 3: // dnum
                    {
                        getLiteralByIndex(mv, literals.indexOf(new Double(0.0)));
                    }
                    break;
                    case 4: // inum
                    {
                        getLiteralByIndex(mv, literals.indexOf(new Integer(0)));
                    }
                    break;
                    case 5: // struct
                        throw new UnsupportedOperationException("ユーザ定義命令の引数型 struct はサポートされていません。");
                    case 6: // label
                        throw new UnsupportedOperationException("ユーザ定義命令の引数型 label はサポートされていません。");
                    default:
                        throw new UnsupportedOperationException("ユーザ定義命令の引数型 " + type + " はサポートされていません。");
                }
            }

            mv.visitInsn(AASTORE);

        }

        // いらない引数は読み飛ばす。
        while (codeIndex < ax.codes.length && !(ax.codes[codeIndex].newLine)
                && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')')) {

            compileExpression(EmptyVisitor.mv);

        }
    }

    private void compileFunction(final MethodVisitor mv) {

        compileInvocation(mv, true, true);
    }
    private static final String[] systemVariables = new String[]{"system", "hspstat", "hspver", "stat", "cnt", "err",
        "strsize", "looplev", "sublev", "iparam", "wparam", "lparam", "refstr", "refdval"};

    private void compileSystemVariable(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        // context をプッシュしておく。
        mv.visitVarInsn(ALOAD, contextIndex);

        final Field field;
        try {
            field = Context.class.getField(systemVariables[code.value]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mv.visitFieldInsn(GETFIELD, contextIName, field.getName(), Type.getDescriptor(field.getType()));

        mv.visitInsn(ICONST_0);
    }
    private final String[] progCmdCodeTypes = new String[]{
        "goto",
        "gosub",
        "return",
        "break",
        "repeat",
        "loop",
        "continue",
        "wait",
        "await",
        "dim",
        "sdim",
        "foreach",
        "foreachcheck",
        "dimtype",
        "dup",
        "dupptr",
        "end",
        "stop",
        "newmod",
        "setmod",
        "delmod",
        "alloc",
        "mref",
        "run",
        "exgoto",
        "on",
        "mcall",
        "assert",
        "logmes"
    };

    private void compileProgramCommand(final MethodVisitor mv) {
        //if((mv instanceof ScanOneVisitor)&&(ax.codes[codeIndex].value<0x1D))
        //    System.out.print(" "+progCmdCodeTypes[ax.codes[codeIndex].value]);

        switch (ax.codes[codeIndex].value) {
            case 0x00: // goto
                compileGoto(mv);
                currentLabel = null;
                break;
            case 0x02: // return
                compileReturn(mv);
                currentLabel = null;
                break;
            case 0x03: // break
                compileBreak(mv);
                //currentLabel = null;
                break;
            case 0x04: // repeat
                compileRepeat(mv);
                break;
            case 0x05: // loop
                compileLoop(mv);
                break;
            case 0x06: // continue
                compileContinue(mv);
                break;
            case 0x0B: // foreach
                compileForeach(mv);
                break;
            case 0x0C: // foreachcheck
                compileForeachcheck(mv);
                break;
            case 0x18: // exgoto
                compileExgoto(mv);
                break;
            case 0x19: // on
                compileOn(mv);
                break;
            case 0x10: // end
                currentLabel = null;
            case 0x07: // wait
            case 0x08: // await
            case 0x09: // dim
            case 0x0A: // sdim
            case 0x0D: // dimtype
            case 0x0E: // dup
            case 0x0F: // dupptr
            case 0x11: // stop
            case 0x12: // newmod
            case 0x13: // setmod
            case 0x14: // delmod
            case 0x15: // alloc
            case 0x16: // mref
            case 0x17: // run
            case 0x1A: // mcall
            case 0x1B: // assert
            case 0x1C: // logmes
                compileCommand(mv);
                break;
            case 0x01: // gosub
                if (mv instanceof ScanOneVisitor) {
                    ((ScanOneVisitor) mv).setArgCount(1);
                } else if (conversionArray != null) {
                    argsToOldLabel = 1;
                }
                compileCommand(mv);
                break;
            default:
                throw new UnsupportedOperationException("プログラム制御命令 " + ax.codes[codeIndex].value + " はサポートされていません。");
        }
    }

    private void compileGoto(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        KLabel L = getLabel(label.value);
        mv.visitJumpInsn(GOTO, L);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
        } else if(mv instanceof ScanThreeVisitor) {
            if((currentLabel!=null)&&(currentLabel.isUsed)) L.branchesToHere++;
        //    System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" goto to "+ax.labels[label.value]);
        }
        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);

    }

    private void compileReturn(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        if (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine) {

            compileExpression(mv);

            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "dup", "(I)" + opeDesc);

        } else {

            mv.visitInsn(ACONST_NULL);

        }
        mv.visitMethodInsn(INVOKESTATIC, FOIName, "getFO", "(" + opeDesc + ")" + FODesc);

        mv.visitInsn(ARETURN);
        //if(conversionArray!=null)
        //    System.out.println("AReturn");

    }

    private void compileRepeat(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, contextIndex);

        // loop count
        if (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine) {

            compileExpression(mv);

            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        } else {

            mv.visitInsn(ICONST_M1);
        }

        // initial cnt
        if (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine) {

            compileExpression(mv);

            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        } else {
            mv.visitInsn(ICONST_0);
        }

        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "startLoop", "(II)Z");

        KLabel L = getLabel(label.value);
        mv.visitJumpInsn(IFEQ, L);
        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);

        final KLabel startLabel = new KLabel();
        startLabel.branchesToHere=1;

        mv.visitLabel(startLabel);

        loopStarts.add(startLabel);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
            ((ScanTwoVisitor) mv).addLoopLabel(startLabel, currentLabel);
        } else if (mv instanceof ScanThreeVisitor) {
            if ((currentLabel != null) && (currentLabel.isUsed)) {
                L.branchesToHere++;
        }
            //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Repeat-Skip to "+ax.labels[label.value]);
        }

    }

    private void compileBreak(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, contextIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "endLoop", "()V");

        KLabel L = getLabel(label.value);
        mv.visitJumpInsn(GOTO, L);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
        } else if (mv instanceof ScanThreeVisitor) {
            if ((currentLabel != null) && (currentLabel.isUsed)) {
                L.branchesToHere++;
        }
            //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Break to "+ax.labels[label.value]);
        }
        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);

    }

    private void compileLoop(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, contextIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "nextLoop", "()Z");

        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).loopReliesOn(loopStarts.peek(), currentLabel);
            }
        }
        mv.visitJumpInsn(IFNE, (KLabel) loopStarts.pop());

    }

    private void compileContinue(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, contextIndex);

        // next cnt
        if (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine) {

            compileExpression(mv);

            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

            mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "nextLoop", "(I)Z");

        } else {

            mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "nextLoop", "()Z");

        }

        KLabel L = getLabel(label.value);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).loopReliesOn(loopStarts.peek(), currentLabel);
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
        } else if (mv instanceof ScanThreeVisitor) {
            if ((currentLabel != null) && (currentLabel.isUsed)) {
                L.branchesToHere++;
            }
            //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Continue to "+ax.labels[label.value]);
        }
        loopStarts.peek().branchesToHere++;
        mv.visitJumpInsn(IFNE, (KLabel) loopStarts.peek());

        mv.visitJumpInsn(GOTO, L);
        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);
    }

    private void compileForeach(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        // 初期値0,回数無制限の repeat と同じ扱いとする。

        mv.visitVarInsn(ALOAD, contextIndex);

        mv.visitInsn(ICONST_M1);
        mv.visitInsn(ICONST_0);

        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "startLoop", "(II)Z");

        KLabel L = getLabel(label.value);
        mv.visitJumpInsn(IFEQ, L);

        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);

        final KLabel startLabel = new KLabel();
        startLabel.branchesToHere=1;

        mv.visitLabel(startLabel);

        loopStarts.add(startLabel);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
            ((ScanTwoVisitor) mv).addLoopLabel(startLabel, currentLabel);
        } else if (mv instanceof ScanThreeVisitor) {
            if ((currentLabel != null) && (currentLabel.isUsed)) {
                L.branchesToHere++;
            }
            //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Foreach-skip to "+ax.labels[label.value]);
        }

    }

    private void compileForeachcheck(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        // cnt が範囲を超えていたら抜ける。

        mv.visitVarInsn(ALOAD, contextIndex);

        compileExpression(mv);

        // 配列要素は無視
        mv.visitInsn(POP);

        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "checkForeach", "(" + opeDesc + ")Z");

        KLabel L = getLabel(label.value);
        mv.visitJumpInsn(IFEQ, L);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
        } else if (mv instanceof ScanThreeVisitor) {
            if ((currentLabel != null) && (currentLabel.isUsed)) {
                L.branchesToHere++;
            }
            //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Foreach-check to "+ax.labels[label.value]);
        }
        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);

    }

    private void compileExgoto(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        // 変数の値
        compileExpression(mv);
        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        // モード
        compileExpression(mv);
        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        // 基準値
        compileExpression(mv);
        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        mv.visitInsn(SWAP);

        final Code label = ax.codes[codeIndex++];

        final KLabel negative = new KLabel();
        negative.branchesToHere=1;
        mv.visitJumpInsn(IFLE, negative);

        final KLabel nojump = new KLabel();
        nojump.branchesToHere=1;

        KLabel L = getLabel(label.value);
        mv.visitJumpInsn(IF_ICMPGE, L);
        if (mv instanceof ScanTwoVisitor) {
            if (currentLabel != null) {
                ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, L.myIndex);
                L.relyOn(currentLabel);
            }
        } else if (mv instanceof ScanThreeVisitor) {
            if ((currentLabel != null) && (currentLabel.isUsed)) {
                L.branchesToHere++;
        }
            //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Exgoto to "+ax.labels[label.value]);
        }
        //if(conversionArray!=null)
        //    System.out.print(" to "+ax.labels[label.value]);

        mv.visitJumpInsn(GOTO, nojump);

        mv.visitLabel(negative);

        mv.visitJumpInsn(IF_ICMPLE, L);

        mv.visitLabel(nojump);
    }

    private void compileOn(MethodVisitor mv) {

        throw new UnsupportedOperationException("'ProgCmd on' currently unsupported.");
        //Else case not properly scanned yet - looks like it gets a ProgramCommand method:
        //gosub, end, and alloc are valid targets according to the arguments.
        //Technically call and goto_ too but those are unreachable.

        //PROBABLY should mark all targetted labels as main labels that rely on currentLabel?
        //If so, need a later check to unmark any main labels without isUsed
        /*
        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        
        // 変数の値
        compileExpression(mv);
        if(skipToInt)
            skipToInt=false;
        else
            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");
        
        final Code statement = ax.codes[codeIndex++];
        
        final List<Label> labels = new ArrayList<Label>();
        final List<Integer> labelIndexs = new ArrayList<Integer>();
        
        while (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine) {
        
            final Code label = ax.codes[codeIndex++];
        
            labels.add(getLabel(label.value));
            labelIndexs.add(new Integer(label.value));
        
        }
        
        if (statement.value == 0) {
            // goto
        
            if(mv instanceof ScanTwoVisitor) {
                for(KLabel label : labels) {
                    ((ScanTwoVisitor)mv).labelTree.combine(currentLabel.myIndex, label.myIndex);
                    label.relyOn(currentLabel);
                }
            }
            else if(mv instanceof ScanThreeVisitor) {
                if((currentLabel!=null)&&(currentLabel.isUsed))
                for(KLabel label : labels) {
                    label.branchesToHere++;
                }
            }
            final KLabel nojump = new KLabel();
            mv.visitTableSwitchInsn(0, labels.size() - 1, nojump, (KLabel[]) labels.toArray(new KLabel[0]));
        
            mv.visitLabel(nojump);
        
        } else {
        
            final Method method = runtime.getMethodFor(ax, statement);
            final String methodDesc = Type.getMethodDescriptor(method);
        
            final KLabel[] pushLabels = new KLabel[labels.size()];
            for (int i = 0; i < pushLabels.length; ++i) {
                pushLabels[i] = new KLabel();
            }
        
            final KLabel endSwitch = new KLabel();
            final KLabel nojump = new KLabel();
        
            mv.visitTableSwitchInsn(0, labels.size() - 1, nojump, pushLabels);
        
            for (int i = 0; i < pushLabels.length; ++i) {
                final KLabel label = pushLabels[i];
        
                mv.visitLabel(label);
                mv.visitLdcInsn(labelIndexs.get(i));
                mv.visitJumpInsn(GOTO, endSwitch);
            }
            mv.visitLabel(endSwitch);
        
            mv.visitVarInsn(ALOAD, contextIndex);
        
            mv.visitInsn(SWAP);
        
            mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(method.getDeclaringClass()), method.getName(),
                    methodDesc);
        
            if (!method.getReturnType().equals(Void.TYPE)) {
                mv.visitInsn(POP);
            }
        
            mv.visitLabel(nojump);
        }
        */
    }

    private void compileCommand(final MethodVisitor mv) {

        compileInvocation(mv, false, false);

    }

    private void compileDllFunction(final MethodVisitor mv) {

        compileInvocation(mv, true, true);

    }

    private void compileDllCommand(final MethodVisitor mv) {

        mv.visitVarInsn(ALOAD, contextIndex);
        mv.visitFieldInsn(GETFIELD, contextIName, "stat", Type.getDescriptor(IntScalar.class));

        mv.visitInsn(ICONST_0);

        compileInvocation(mv, false, true);

        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "assign", "(I" + opeDesc + "I)V");

    }

    private void compileCompareCommand(final MethodVisitor mv) {

        switch (ax.codes[codeIndex].value) {
            case 0: // if
                compileIf(mv);
                break;
            case 1: // else
                compileElse(mv);
                break;
            default:
                throw new UnsupportedOperationException("条件分岐命令 " + ax.codes[codeIndex].value + " はサポートされていません。");
        }

    }

    private void compileIf(final MethodVisitor mv) {
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print("If");

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        // offset は、条件式の先頭からの移動量
        final int offset = ax.codes[codeIndex++].value;
        final int base = ax.codes[codeIndex].offset;

        compileExpression(mv);

        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        final KLabel existingLabel = (KLabel) labels.get(new Integer(base + offset));
        if (existingLabel != null) {
            mv.visitJumpInsn(IFEQ, existingLabel);
            if (mv instanceof ScanTwoVisitor) {
                if (currentLabel != null) {
                    ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, existingLabel.myIndex);
                    existingLabel.relyOn(currentLabel);
                }
            } else if (mv instanceof ScanThreeVisitor) {
                if ((currentLabel != null) && (currentLabel.isUsed)) {
                    existingLabel.branchesToHere++;
                }
                //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" If to "+(base + offset));
            }
            //System.out.println("Existing "+base+" "+(base + offset));
        } else {
            final KLabel label = new KLabel();
            labels.put(new Integer(base + offset), label);
            mv.visitJumpInsn(IFEQ, label);
        }
        //if(mv instanceof CodeOpcodeCounter)
        //    System.out.print(" to "+(base + offset));

    }

    private void compileElse(final MethodVisitor mv) {
        //if(mv instanceof ScanOneVisitor)
        //    System.out.print("Else");
        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        // offset は、次の命令からの移動量
        final int offset = ax.codes[codeIndex++].value;
        final int base = ax.codes[codeIndex].offset;

        final KLabel existingLabel = (KLabel) labels.get(new Integer(base + offset));
        if (existingLabel != null) {
            mv.visitJumpInsn(GOTO, existingLabel);
            if (mv instanceof ScanTwoVisitor) {
                if (currentLabel != null) {
                    ((ScanTwoVisitor) mv).labelTree.combine(currentLabel.myIndex, existingLabel.myIndex);
                    existingLabel.relyOn(currentLabel);
                }
            } else if (mv instanceof ScanThreeVisitor) {
                if ((currentLabel != null) && (currentLabel.isUsed)) {
                    existingLabel.branchesToHere++;
                }
                //System.out.println(((ScanThreeVisitor)mv).currentStatementAddress+" Else to "+(base + offset));
            }
            //System.out.println("Existing "+base+" "+(base + offset));
        } else {
            final KLabel label = new KLabel();
            labels.put(new Integer(base + offset), label);
            mv.visitJumpInsn(GOTO, label);
        }
        //if(mv instanceof CodeOpcodeCounter)
        //    System.out.print(" to "+(base + offset));

    }

    private void createSubMethods() {

        int numMethods=0;
        int numMains=0;
        for(MyTreeThing labelGroup : labelGroups) {
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "m" + numMethods++, "(I)"+FODesc, null, new String[0]);

            compileLocalVariables(mv);

            mv=new SubMethodAdapter(this, mv);

            //NOTE: Hack for Elona to reduce method count. Reduce this number if it causes ClassFormatError: Invalid method Code length
            if(numMethods==1)
                ((SubMethodAdapter)mv).maxSize=73000;

            Integer[] mainLabels=labelGroup.mainLabels(allLabels);
            int numTableLabels=mainLabels.length;
            if(numTableLabels > 1) {
                boolean dummyMainLabel=(allLabels[mainLabels[0].intValue()].branchesToHere==-1);
                if(dummyMainLabel) numTableLabels--;
                KLabel defaultLabel=new KLabel();
                defaultLabel.branchesToHere=1;
                KLabel[] tableLabels=new KLabel[numTableLabels];
                if(dummyMainLabel)
                    for(int i=0;i<numTableLabels;i++)
                        tableLabels[i]=allLabels[mainLabels[i+1].intValue()];
                else
                    for(int i=0;i<numTableLabels;i++)
                        tableLabels[i]=allLabels[mainLabels[i].intValue()];
                mv.visitVarInsn(ILOAD, 1);
                mv.visitTableSwitchInsn(numMains, numMains + numTableLabels - 1, defaultLabel, tableLabels);
                mv.visitLabel(defaultLabel);
            }
            //A thought. There should be an else here for if the first mainLabel is not the first label.
            //If so, mv.visitJumpInsn(GOTO, allLabels[mainLabels[0].intValue()]); ?
            numMains += numTableLabels;
            codeIndex = 0;
            int nextLabelStart;
            int numStatements = 0;
            KLabel nextLabel;
            Iterator<Integer> iter = labelGroup.iterator();
            currentLabel = allLabels[iter.next().intValue()];
            while (currentLabel != null) {
                if (iter.hasNext()) {
                    nextLabel = allLabels[iter.next().intValue()];
                    nextLabelStart = ((Integer) nextLabel.extra).intValue();
                } else {
                    nextLabelStart = -1;
                    nextLabel = null;
                }
                int startAddress = ((Integer) currentLabel.extra).intValue();
                while (ax.codes[codeIndex].offset < startAddress) {
                    codeIndex++;    //A smarter seek method would be nice
                }
                mv.visitLabel(currentLabel);
                while ((codeIndex < ax.codes.length) && ((currentLabel != null) || (ax.codes[codeIndex].offset == nextLabelStart))) {
                    if (ax.codes[codeIndex].offset == nextLabelStart) {
                        currentLabel = nextLabel;
                        mv.visitLabel(nextLabel);
                        if (iter.hasNext()) {
                            nextLabel = allLabels[iter.next().intValue()];
                            nextLabelStart = ((Integer) nextLabel.extra).intValue();
                        } else {
                            nextLabelStart = -1;
                            nextLabel = null;
                        }
                    }
                    compileStatement(mv);
                }
                currentLabel = nextLabel;
            }
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
    }

    private ClassNode createSuperClassVisitor() {
        ClassNode node = new ClassNode();
        return node;
    }

    private void createArrayFieldInSuperClass() {
        // Create constant literals field
        FieldVisitor fv = superClassWriter.visitField(ACC_PUBLIC, "cLiterals", typeArrayOfScalar, null, null);
        fv.visitEnd();

    }

    /**
     * Guaranteed equivalent-or-better option for LDC.
     * Should slightly improve constantpool size and bytecode size.
     */
    private void efficientLDC(MethodVisitor mv, Object O) {
        if(O instanceof Integer) {
            if(pushByte(((Integer)O).intValue(), mv)) {
                return;
            }
        } else if(O instanceof Double) {
            double value = ((Double)O).doubleValue();
            if (value == 0.0) {
                mv.visitInsn(DCONST_0);
                return;
            } else if (value == 1.0) {
                mv.visitInsn(DCONST_1);
                return;
            }
        }
        mv.visitLdcInsn(O);
    }

    /**
     * Pushes i onto the stack for mv for values from -128 to 127.  Returns true 
     * if i was successfully pushed on the stack, false otherwise.
     * 
     * This implementation never produces more than 1 opcode per call.
     * 
     * @param i The integer to push on to the stack for mv; must be within 0 to
     * 32768 inclusive.
     * @param mv The MethodVisitor to add an opcode to push an integer on the stack.
     * @return true if mv changed due to this method call; false otherwise.
     */
    private boolean pushByte(int i, MethodVisitor mv) {
        if(!(i >= -128 && i <= 127)) return false;
        switch(i) {
            case -1:
                mv.visitInsn(ICONST_M1);
                break;
            case 0: case 1: case 2:
            case 3: case 4: case 5:
                mv.visitInsn(ICONST_INSNS[i]);
                break;
            default:
                mv.visitIntInsn(BIPUSH, i);
                break;
        }
        return true;
    }

    private final int[] ICONST_INSNS = new int[]{ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5};
    /**
     * Pushes i onto the stack for mv for values from 0 to 32767.  Returns true 
     * if i was successfully pushed on the stack, false otherwise.
     * 
     * This implementation never produces more than 1 opcode per call.
     * 
     * @param i The integer to push on to the stack for mv; must be within 0 to
     * 32768 inclusive.
     * @param mv The MethodVisitor to add an opcode to push an integer on the stack.
     * @return true if mv changed due to this method call; false otherwise.
     */
    private boolean pushSmall(int i, MethodVisitor mv) {
        if (i < 0) {
            return false;
        } else if (i < ICONST_INSNS.length) {
            mv.visitInsn(ICONST_INSNS[i]);
        } else if (i < 128) {
            mv.visitIntInsn(BIPUSH, i);
        } else if (i < 32768) {
            mv.visitIntInsn(SIPUSH, i);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Pushes a positive integer on the stack for the given method without using 
     * the ldc instruction; integers are inlined in the byte-code.  
     * 
     * This method may generate one or more opcodes per call.
     * 
     * @param i The integer to push.  Must be greater than 0, but cannot exceed 
     * Integer.MAX_VALUE
     * @param mv The MethodVisitor to which opcodes should be added.
     * @see pushSmall
     */
    private void pushInteger(int i, MethodVisitor mv) {
        if (i < 0) {
            throw new UnsupportedOperationException("Unimplemented: negative input for pushInteger");
        } else if (i < ICONST_INSNS.length) {
            mv.visitInsn(ICONST_INSNS[i]);
        } else if (i < 128) {
            mv.visitIntInsn(BIPUSH, i);
        } else if (i < 32768) {
            mv.visitIntInsn(SIPUSH, i);
        } else if (i < 65535) {
            mv.visitIntInsn(SIPUSH, 32767); // 0x7fff
            mv.visitIntInsn(SIPUSH, i - 32767);
            mv.visitInsn(IADD);
        } else {
            assert (i > 32767);
            int iterations = (i / 32767);
            pushInteger(32767, mv);
            if(iterations > 1) {
                pushInteger(iterations, mv);
                mv.visitInsn(IMUL);
            }
            /*
            for(int j = 1; j < iterations; j++) {
                mv.visitInsn(DUP);
                mv.visitInsn(IADD);
            }
            */
            assert( i - (32767 * iterations) < 32767 );
            pushInteger(i - (32767 * iterations), mv);
            mv.visitInsn(IADD);
        }
    }

    private void initializeConstants(final MethodVisitor mv, final int start, final int stop) {

        if (useLiteralsInArray) {

            // push this on the stack
            mv.visitVarInsn(ALOAD, thisIndex);

            // pop this, push the array
            mv.visitFieldInsn(GETFIELD, superClassIName, "cLiterals", typeArrayOfScalar);

            mv.visitVarInsn(ASTORE, literalsIndex);

            // Populate the array
            for (int i = start; i < stop; ++i) {

                mv.visitVarInsn(ALOAD, literalsIndex);
                final Object value = literals.get(i);

                // push the array's index on the stack
                pushInteger(i, mv);

                // push the constant on the stack
                efficientLDC(mv, value);

                // call Scalar.fromValue on the constant to get a Scalar
                // pop the constant off, but push a Scalar on
                mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                        + Type.getDescriptor(value instanceof Integer ? Integer.TYPE
                        : value instanceof Double ? Double.TYPE : String.class) + ")" + literalDesc);


                // put the Scalar onto array[i]
                // pop off the Scalar, the index, the array.
                mv.visitInsn(AASTORE);
            }

        } else {
            for (int i = start; i < stop; ++i) {

                final Object value = literals.get(i);

                // push the constant on the stack
                efficientLDC(mv, value);

                // call Scalar.fromValue on the constant to get a Scalar
                // pop the constant off, but push a Scalar on
                mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                        + Type.getDescriptor(value instanceof Integer ? Integer.TYPE
                        : value instanceof Double ? Double.TYPE : String.class) + ")" + literalDesc);

                // Get "this"
                mv.visitVarInsn(ALOAD, 0);
                // populate the c# fields for constants
                mv.visitFieldInsn(PUTFIELD, superClassIName, "c" + i, literalDesc);
            }

        }
    }

    private void createSuperClassCtor() {

        
        int subMethods = createSuperClassCtorSubMethods();
        // Type of Scalar[]

        //final MethodVisitor mv = superClassWriter.visitMethod(ACC_PUBLIC, "<init>", "(" + contextDesc + ")V", null, null);
        final MethodVisitor mv = superClassWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);


        // Literals array initialization
        
        if (useLiteralsInArray) {

            // Create array of literal Scalars by:

            // get this
            mv.visitVarInsn(ALOAD, thisIndex);
            // push the size
            mv.visitIntInsn(SIPUSH, literals.size());

            // make the array of type Scalar, popping size
            mv.visitTypeInsn(ANEWARRAY, literalIName);

            // Store this field in this class
            mv.visitFieldInsn(PUTFIELD, superClassIName, "cLiterals", typeArrayOfScalar);

        }
        // Call super()
        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitMethodInsn(INVOKESPECIAL, parentIName, "<init>", "()V");

        // Call ctor+i
        for (int i = 0; i < subMethods; i++) {
            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitMethodInsn(INVOKESPECIAL, superClassIName, "ctor" + i, "()V");
        }

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private int createSuperClassCtorSubMethods() {
        // Number of methods created
        int count = 0;

        // Number of constants per method
        int size = 5000;

        /* Walk the range of literals from 0 to literals.size().
         * Starting from 0, walk up to the lesser of 5000 constant literals or 
         * what remains.
         */
        for (int start = 0, stop = (size < literals.size()) ? size : literals.size();
             stop <= literals.size();
             start = stop, stop += ((stop + size) < literals.size()) ? size : literals.size() % size, count++) {
            final MethodVisitor mv = superClassWriter.visitMethod(ACC_PRIVATE, "ctor" + count, "()V", null, null);
            initializeConstants(mv, start, stop);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        return count;
    }
}

