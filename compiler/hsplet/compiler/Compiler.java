/*
 * $Id: Compiler.java,v 1.11.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.compiler;

import com.thoughtworks.xstream.XStream;
import hsplet.Context;
import hsplet.RunnableCode;
import hsplet.compiler.ByteCode.Code;
import hsplet.compiler.ByteCode.Function;
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
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.ASMifierClassVisitor;

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
public class Compiler implements Opcodes, Serializable {

    /** このクラスを含むソースファイルのバージョン文字列。 */
    private static final String fileVersionID = "$Id: Compiler.java,v 1.11.2.1 2006/08/02 12:13:06 Yuki Exp $";
    /** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
    private static final long serialVersionUID = 8668239863505235428L;
    /** デバッグ出力をするかどうか。 */
    private static final boolean DEBUG_ENABLED = false;
    private static final boolean STORE_ENABLED = true;

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

                    final String className = generateClassName(packFile.getName());

                    final JarEntry je = new JarEntry(className + ".class");
                    je.setMethod(JarEntry.DEFLATED);
                    jar.putNextEntry(je);
                    try {
                        final Compiler c = new Compiler(new ByteCode(new FileInputStream(packFile)),
                                packFile.getName(), libraryLoader);
                        c.compile(className, jar);
                    } finally {

                        jar.closeEntry();
                    }

                } else {

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
            jar.close();
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
    private static final String contextDesc = Type.getDescriptor(Context.class);
    private static final String contextIName = Type.getInternalName(Context.class);
    private static final String opeDesc = Type.getDescriptor(Operand.class);
    private static final String opeIName = Type.getInternalName(Operand.class);
    private static final String varDesc = Type.getDescriptor(Variable.class);
    private static final String varIName = Type.getInternalName(Variable.class);
    private static final String literalDesc = Type.getDescriptor(Scalar.class);
    private static final String literalIName = Type.getInternalName(Scalar.class);
    private static final String parentDesc = Type.getDescriptor(RunnableCode.class);
    private static final String parentIName = Type.getInternalName(RunnableCode.class);
    private static final String typeArrayOfScalar = "[L" + literalIName + ";";
    private static final int thisIndex = 0;
    private static final int jumpLabelIndex = 1; // int
    private static final int contextIndex = 2; // contextType
    private static final int assignOffsetIndex = 3; // int
    private static final int literalsIndex = 4; // Scalar[]
    private static final int zeroIndex = 5; // Scalar
    private static final int zeroLengthStringIndex = 6; // literals[3]    
    private static final int localsStart = 7;
    private List<CommonObjectContainer> commonObjectsList = new ArrayList<CommonObjectContainer>();
    private ClassVisitor cw, superClassWriter;
    private String inputName;
    private String className;
    private String classIName;
    private String superClassName;
    private String superClassIName;
    private ByteCode ax;
    private int codeIndex;
    private List literals;
    private Stack<Label> loopStarts;
    private boolean enableVariableOptimization;
    private RuntimeInfo runtime;
    private List<Class> instancedLibraries;
    private boolean useSuperClassConstants = true;
    private static final boolean useLocalVariableForLiterals = true;
    private static final boolean optimizeWithLocalVariables = true;
    private int[] varsStats;
    private int[] paramsStats;
    private static final boolean commonVarsInLocals = false;

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
    public void compile(final String className, final OutputStream out) throws IOException {

        this.className = className;
        this.classIName = className.replace('.', '/');
        this.superClassName = className + "Super";
        this.superClassIName = className + "Super";

        ClassVisitor output;

        //final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        output = writer;

        ClassWriter superWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //final ClassWriter writer = new ClassWriter(true);
        ClassNode bufferClassNode = new ClassNode();

        if (STORE_ENABLED) {
            output = bufferClassNode;
        }

        // フィールドの初期化。
        if (DEBUG_ENABLED) {
            cw = new ClassDebugger(output);
        } else {
            cw = output;
        }

        literals = new ArrayList();
        loopStarts = new Stack<Label>();
        submethodStartEnds = new ArrayList();
        instancedLibraries = new ArrayList<Class>();
        codeIndex = 0;
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

        createRun();
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

        // 出力
        out.write(writer.toByteArray());
	((JarOutputStream) out).putNextEntry(new JarEntry(classIName + "Super.class"));
        out.write(superWriter.toByteArray());

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
    private final boolean collectStats = true;

    private void collectLiterals() {

        // よく使う。
        literals.add(new Integer(0));
        literals.add(new Double(0.0));
        literals.add(new String(""));

        int idx = localsStart;
        commonObjectsList.add(new CommonObjectContainer(new Integer(1), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(2), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(3), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(7), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(255), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(38), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(8), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(10), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(4), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(20), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(9), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(-1), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(5), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(29), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(6), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(13), idx++));
        commonObjectsList.add(new CommonObjectContainer(new Integer(100), idx++));

        for (CommonObjectContainer o : commonObjectsList) {
            literals.add(o.o);
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

        // 使用する引数を用意する。
        for (int i = 0; i < ax.parameters.length; ++i) {

            cw.visitField(ACC_PRIVATE | ACC_FINAL, "p" + i, opeDesc, null, null);
        }

        // 定数を用意する、毎回作っていたら遅い。
        for (int i = 0; i < literals.size(); ++i) {
            if (!useSuperClassConstants) {
                cw.visitField(ACC_PRIVATE | ACC_FINAL, "c" + i, literalDesc, null, null);
            } else {
                superClassWriter.visitField(ACC_PROTECTED | ACC_FINAL, "c" + i, literalDesc, null, null);
            }
        }
        if (useSuperClassConstants) {
            for (int i = 0; i < 3; i++) {
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
            if (commonVarsInLocals) {
                if (commonVariable(i)) {
                    mv.visitInsn(DUP);
                    mv.visitVarInsn(ASTORE, getVariableLocalIndex(i));
                }
            }
            mv.visitFieldInsn(PUTFIELD, classIName, "v" + i, varDesc);
        }

        // 定数を用意する、毎回作っていたら遅い。
        for (int i = 0; i < literals.size(); ++i) {

            final Object value = literals.get(i);

            if (!useSuperClassConstants) {
                mv.visitVarInsn(ALOAD, thisIndex);

                mv.visitLdcInsn(value);

                mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                        + Type.getDescriptor(value instanceof Integer ? Integer.TYPE
                        : value instanceof Double ? Double.TYPE : String.class) + ")" + literalDesc);

                mv.visitFieldInsn(PUTFIELD, classIName, "c" + i, literalDesc);
            }
        }

        // Get the 3 magic constants, 0, 0.0, and ""
        if (useSuperClassConstants) {
            for (int i = 0; i < 3; ++i) {

                final Object value = literals.get(i);

                mv.visitVarInsn(ALOAD, thisIndex);

                mv.visitLdcInsn(value);

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

            } catch (Exception e) {

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
    private Map<Integer, Label> labels;

    private void createRun() {

        final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_FINAL, "run", "(I)" + opeDesc, null, new String[0]);

        compileLocalVariables(mv);

        prepareLabels();

        final Label start_try = new Label();
        mv.visitLabel(start_try);

        compileLabelJumpTable(mv);

        compileCodes(mv);

        final Label end_try = new Label();
        mv.visitLabel(end_try);

        final Label after_try = new Label();
        mv.visitJumpInsn(GOTO, after_try);

        final Label try_handler = new Label();
        mv.visitLabel(try_handler);

        mv.visitTryCatchBlock(start_try, end_try, try_handler, Type.getInternalName(GotoException.class));

        mv.visitFieldInsn(GETFIELD, Type.getInternalName(GotoException.class), "label", "I");

        mv.visitVarInsn(ISTORE, jumpLabelIndex);
        mv.visitJumpInsn(GOTO, start_try);

        mv.visitLabel(after_try);

        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

    }

    private void prepareLabels() {

        labels = new HashMap<Integer, Label>();

        // 先頭のラベル
        labels.put(new Integer(ax.codes[0].offset), new Label());

        for (int i = 0; i < ax.labels.length; ++i) {

            labels.put(new Integer(ax.labels[i]), new Label());
        }

    }

    private Label getLabel(final int index) {

        return labels.get(new Integer(ax.labels[index]));
    }

    /**
     * 必要なローカル変数を用意。
     *
     * @param mv
     */
    private void compileLocalVariables(final MethodVisitor mv) {

        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitFieldInsn(GETFIELD, classIName, "context", contextDesc);
        mv.visitVarInsn(ASTORE, contextIndex);
        if (useLocalVariableForLiterals) {
            // cLiterals in locals[literalsIndex]
            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitFieldInsn(GETFIELD, superClassIName, "cLiterals", typeArrayOfScalar);
            mv.visitVarInsn(ASTORE, literalsIndex);

            // 0 in locals[zeroLiteralIndex]
            mv.visitVarInsn(ALOAD, thisIndex);
            mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(new Integer(0)), literalDesc);
            mv.visitVarInsn(ASTORE, zeroIndex);

            mv.visitVarInsn(ALOAD, literalsIndex);
            mv.visitInsn(ICONST_3);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ASTORE, zeroLengthStringIndex);

            for (CommonObjectContainer obj : commonObjectsList) {
                mv.visitVarInsn(ALOAD, thisIndex);
                mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(obj.o), literalDesc);
                mv.visitVarInsn(ASTORE, obj.localIndex);
            }
        }

    }

    private void compileLabelJumpTable(final MethodVisitor mv) {

        mv.visitVarInsn(ILOAD, jumpLabelIndex);
        mv.visitInsn(ICONST_M1);
        mv.visitVarInsn(ISTORE, jumpLabelIndex);

        final Label[] labels = new Label[ax.labels.length];

        for (int i = 0; i < labels.length; ++i) {
            labels[i] = getLabel(i);
        }

        mv.visitTableSwitchInsn(0, ax.labels.length - 1, (Label) this.labels.get(new Integer(ax.codes[0].offset)),
                labels);

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

    class MangledMethodSignature {

        public MangledMethodSignature(String name) {
            this(name, "()V");
        }

        public MangledMethodSignature(String name, String signature) {
            this.name = name;
            this.signature = signature;
        }
        private String name, signature;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
    private int mergedMethodCount = 0;
    /* Two or more consecutive calls are merged with this method visitor. */
    private Map<Integer, List<MangledMethodSignature>> map = new TreeMap<Integer, List<MangledMethodSignature>>();

    private void mergeMethodCalls(final MethodVisitor mv, List<MangledMethodSignature> methods) {
        // Call this.mm####
        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, classIName, "mm" + mergedMethodCount, "()V");

        map.put(new Integer(mergedMethodCount), methods);

        mergedMethodCount++;
    }

    private void createMergedMethodCalls(ClassVisitor cv) {
        for (Integer index : map.keySet()) {
            MethodVisitor subroutine = cv.visitMethod(ACC_PUBLIC, "mm" + index.toString(), "()V", null, null);

            List<MangledMethodSignature> methods = map.get(index);

            // And each of these methods will be called in series:
            for (MangledMethodSignature method : methods) {
                subroutine.visitVarInsn(ALOAD, thisIndex);
                subroutine.visitMethodInsn(INVOKEVIRTUAL, classIName, method.getName(), method.getSignature());
            }

            subroutine.visitInsn(RETURN);
            subroutine.visitMaxs(0, 0);
            subroutine.visitEnd();
        }
    }
    private ArrayList<String> methodNames = new ArrayList<String>();

    private void compileCodes(final MethodVisitor mv) {

        int nextBlock = 1;

        final Set<Label> unusedLabels = new HashSet<Label>(labels.values());

        boolean lastLabelUsed = true;

        methodNames.clear();

        while (codeIndex < ax.codes.length) {

            final Label label = labels.get(new Integer(ax.codes[codeIndex].offset));

            boolean labelUsed = label != null;

            if (labelUsed) {
                mv.visitLabel(label);
                unusedLabels.remove(label);
                //XStream xs = new XStream();
                //xs.toXML(methodNames, System.out);
                methodNames.clear();
            }

            // If we're at a location which is within a block, then we may
            // compile it into a method.
            if (codeIndex >= nextBlock) {
                // Java の制限で長すぎるコードはコンパイルできないことが判明・・・orz
                // メソッドに切り出せるものは切り出すのが吉。
                // ラベルを含まない、goto/return を含まない範囲が切り出せる。
                final int nextLabelIndex = Math.min(codeIndex + 10000, findNextLabelGotoReturn());

                if (nextLabelIndex != codeIndex) {

                    // 終わっていない if/else/repeat/foreach、始まっていない
                    // else/loop/break/continue/foreachCheck は除外しなければいけない。
                    final int blockEnd = findBlockEnd(codeIndex, nextLabelIndex);
                    final int blockSize = blockEnd - codeIndex;
                    // The following appears to affect the number of submethods compiled.  Smaller numbers appear to reduce the number of methods.
                    if (blockSize >= 1) {

                        submethodStartEnds.add(new int[]{codeIndex, blockEnd});

                        compileSeparatedMethod(new EmptyVisitor(), blockEnd);

                        mv.visitVarInsn(ALOAD, thisIndex);
                        mv.visitMethodInsn(INVOKEVIRTUAL, classIName, "m" + (submethodStartEnds.size() - 1), "()V");

                        if (!lastLabelUsed) {
                            methodNames.add("m" + (submethodStartEnds.size() - 1));
                        }

                        continue;
                    } else {
                        nextBlock = blockEnd;
                    }
                }
            }

            compileStatement(mv);
            lastLabelUsed = labelUsed;
        }

        // 使用されなかったラベルも使ってやらないとエラーになる
        for (final Iterator<Label> i = unusedLabels.iterator(); i.hasNext();) {
            final Label label = i.next();
            mv.visitLabel(label);
        }
    }
    private List submethodStartEnds = new ArrayList();

    private void compileSeparatedMethod(final MethodVisitor mv, final int endIndex) {

        compileLocalVariables(mv);

        while (codeIndex < endIndex) {

            final Label label = (Label) labels.get(new Integer(ax.codes[codeIndex].offset));

            if (label != null) {
                mv.visitLabel(label);
            }

            compileStatement(mv);
        }

        if (codeIndex < ax.codes.length) {
            final Label label = (Label) labels.get(new Integer(ax.codes[codeIndex].offset));

            if (label != null) {
                mv.visitLabel(label);
            }
        }

    }

    private int findNextLabelGotoReturn() {

        for (int i = codeIndex; i < ax.codes.length; ++i) {

            // ラベル
            if (i != codeIndex && labels.get(new Integer(ax.codes[i].offset)) != null) {
                if (!isLoopLabel(ax.codes[i].offset)) {
                    return i;
                }
            }

            if (ax.codes[i].type == ByteCode.Code.Type.ProgCmd) {

                // goto か return か exgoto か on
                if (ax.codes[i].newLine
                        && (ax.codes[i].value == 0 || ax.codes[i].value == 2 || ax.codes[i].value == 0x18 || ax.codes[i].value == 0x19)) {
                    return i;
                }

            }
        }

        return ax.codes.length;
    }

    private boolean isLoopLabel(int offset) {

        for (int i = 0; i < ax.codes.length; ++i) {
            final Code code = ax.codes[i];

            if (code.type == Code.Type.CmpCmd) {

                if (code.value == 0 || code.value == 1) {
                    // if のターゲット?
                    if (ax.codes[i + 1].value + ax.codes[i + 2].offset == offset) {
                        return false;
                    }
                }

            } else if (code.type == ByteCode.Code.Type.ProgCmd) {

                if (code.value == 3 || code.value == 4 || code.value == 6 || code.value == 0x0B || code.value == 0x0C) {
                    ++i;
                }
            } else if (code.type == ByteCode.Code.Type.Label) {

                if (ax.labels[code.value] == offset) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * else/loop/break/continue/foreachCheck
     *
     * @param endIndex
     * @return
     */
    private int findBlockEnd(final int startIndex, final int endIndex) {

        for (int i = startIndex; i < endIndex;) {

            if (ax.codes[i].type == ByteCode.Code.Type.CmpCmd) {

                if (ax.codes[i].value == 1) {
                    // else
                    return i;
                } else {

                    final int ifEndOffset = ax.codes[i + 2].offset + ax.codes[i + 1].value;

                    final int ifEnd = findCodeForOffset(i, endIndex, ifEndOffset);

                    if (ifEnd < 0) {
                        return i;
                    }

                    final int ifEnd2 = findBlockEnd(i + 2, ifEnd);

                    if (ifEnd2 == ifEnd) {
                        // if の最後までいけた

                        i = ifEnd;
                    } else if (ifEnd2 == ifEnd - 2 && ax.codes[ifEnd - 1].type == Code.Type.JumpOffset) {
                        // else まで行けた

                        final int elseEndOffset = ax.codes[ifEnd].offset + ax.codes[ifEnd - 1].value;

                        final int elseEnd = findCodeForOffset(ifEnd, endIndex, elseEndOffset);

                        if (elseEnd < 0) {
                            return i;
                        }

                        final int elseEnd2 = findBlockEnd(ifEnd, elseEnd);

                        if (elseEnd2 == elseEnd) {
                            i = elseEnd;
                        } else {
                            return i;
                        }
                    } else {
                        return i;
                    }
                }
            } else if (ax.codes[i].type == ByteCode.Code.Type.ProgCmd) {

                if (ax.codes[i].value == 3 || ax.codes[i].value == 5 || ax.codes[i].value == 6
                        || ax.codes[i].value == 0x0C) {
                    // break/loop/continue/foreachCheck
                    return i;
                } else if (ax.codes[i].value == 4 || ax.codes[i].value == 0x0B) {
                    // repeat/foreach

                    final int loopEndOffset = ax.labels[ax.codes[i + 1].value];

                    final int loopEnd = findCodeForOffset(i, endIndex, loopEndOffset);

                    if (loopEnd < 0) {
                        // このループはブロックに入りきらない
                        return i;
                    }

                    i = loopEnd;
                } else {
                    ++i;
                }
            } else {
                ++i;
            }

        }

        return endIndex;
    }

    private boolean containsOuterRepeatLoop(int startIndex, int endIndex, int blockEnd) {

        for (int i = startIndex; i < endIndex; ++i) {

            if (ax.codes[i].type == ByteCode.Code.Type.ProgCmd
                    && (ax.codes[i].value == 3 || ax.codes[i].value == 4 || ax.codes[i].value == 5
                    || ax.codes[i].value == 6 || ax.codes[i].value == 0x0B || ax.codes[i].value == 0x0C)) {
                return true;
            }
        }

        return false;
    }

    private int findCodeForOffset(int startIndex, final int endIndex, final int offset) {

        for (int i = startIndex; i < ax.codes.length && i <= endIndex; ++i) {

            if (ax.codes[i].offset == offset) {
                return i;
            }
        }
        return -1;
    }

    private void compileStatement(final MethodVisitor mv) {

        // ステートメントは変数・パラメータへの代入、または命令で始まると決まっている。

        switch (ax.codes[codeIndex].type) {
            case ByteCode.Code.Type.Var:
            case ByteCode.Code.Type.Struct:
                compileAssignment(mv);
                break;
            case ByteCode.Code.Type.IntCmd:
            case ByteCode.Code.Type.ExtCmd:
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
    }
    private static final String[] assignOperators = new String[]{"assignAdd", "assignSub", "assignMul", "assignDiv",
        "assignMod", "assignAnd", "assignOr", "assignXor", "assign", "assignNe", "assignGt", "assignLt", "assignGtEq", "assignLtEq", "assignSr",
        "assignSl"};
    private static final String[] unaryOperators = new String[]{"inc", "dec"};

    private void compileAssignment(final MethodVisitor mv) {

        final boolean prevEnableVariableOptimization = enableVariableOptimization;

        // 演算子を先読みして最適化を有効にする。
        // {
        final int index = codeIndex;

        if (ax.codes[codeIndex].type == Code.Type.Var) {
            compileVariable(new EmptyVisitor());
        } else {
            compileParameter(new EmptyVisitor());
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

            mv.visitVarInsn(ISTORE, assignOffsetIndex);

            do {

                mv.visitInsn(DUP);

                mv.visitVarInsn(ILOAD, assignOffsetIndex);

                mv.visitIincInsn(assignOffsetIndex, 1);

                compileExpression(mv);

                mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, name, "(I" + opeDesc + "I)V");

            } while (codeIndex < ax.codes.length && !ax.codes[codeIndex].newLine);

            mv.visitInsn(POP);

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

        compileArrayIndex(mv);

    }

    private void compileArrayIndex(final MethodVisitor mv) {

        int paramCount = 0;
        if (codeIndex < ax.codes.length && ax.codes[codeIndex].type == Code.Type.Mark
                && ax.codes[codeIndex].value == '(') {

            ++codeIndex;

            final int index = codeIndex;

            compileExpression(new EmptyVisitor());

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
    }

    private void compileExpression(final MethodVisitor mv) {

        // 先読みして最適化を有効化、トークンが二つ以上ある（演算される）時は有効。
        // {

        final boolean prevEnableVariableOptimization = enableVariableOptimization;

        final int index = codeIndex;

        compileToken(new EmptyVisitor());

        enableVariableOptimization |= (codeIndex < ax.codes.length
                && !(ax.codes[codeIndex].type == Code.Type.Mark && (ax.codes[codeIndex].value == ')' || ax.codes[codeIndex].value == '?')) && !(ax.codes[codeIndex].newLine | ax.codes[codeIndex].comma));

        codeIndex = index;

        // }

        do {

            compileToken(mv);

        } while (codeIndex < ax.codes.length
                && !(ax.codes[codeIndex].type == Code.Type.Mark && (ax.codes[codeIndex].value == ')' || ax.codes[codeIndex].value == '?'))
                && !(ax.codes[codeIndex].newLine | ax.codes[codeIndex].comma));

        enableVariableOptimization = prevEnableVariableOptimization;

    }

    private void compileToken(final MethodVisitor mv) {

        // トークンはリテラル・変数・演算子・関数呼び出しと決まっている。

        switch (ax.codes[codeIndex].type) {
            case ByteCode.Code.Type.Mark:
                compileOperator(mv);
                break;
            case ByteCode.Code.Type.String:
            case ByteCode.Code.Type.DNum:
            case ByteCode.Code.Type.INum:
                compileLiteral(mv);
                break;
            case ByteCode.Code.Type.Struct:
                compileParameter(mv);
                break;
            case ByteCode.Code.Type.Label:
                compileLabel(mv);
                break;
            case ByteCode.Code.Type.Var:
                compileVariable(mv);
                break;
            case ByteCode.Code.Type.ExtSysVar:
                compileGuiSystmVariable(mv);
                break;
            case ByteCode.Code.Type.ModCmd:
                compileModuleCommand(mv, true);
                break;
            case ByteCode.Code.Type.IntFunc:
                compileFunction(mv);
                break;
            case ByteCode.Code.Type.SysVar:
                compileSystemVariable(mv);
                break;
            case ByteCode.Code.Type.ProgCmd:
                compileProgramCommand(mv);
                break;
            case ByteCode.Code.Type.DllFunc:
                compileDllFunction(mv);
                break;
            case ByteCode.Code.Type.DllCtrl:
                compileDllFunction(mv);
                break;

            default:
                throw new RuntimeException("命令コード " + ax.codes[codeIndex].type + " は解釈できません。");
        }
    }

    private void compileLabel(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        mv.visitLdcInsn(new Integer(code.value));
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
        if (useSuperClassConstants) {
            // Type of Scalar[]
            if (useLocalVariableForLiterals) {
                for (CommonObjectContainer obj : commonObjectsList) {
                    if (obj.o.equals(o)) {
                        mv.visitVarInsn(ALOAD, obj.localIndex);
                        mv.visitInsn(ICONST_0);
                        return;
                    }
                }
                if (zero.equals(o)) {
                    mv.visitVarInsn(ALOAD, zeroIndex);
                    mv.visitInsn(ICONST_0);
                    return;
                } else if ("".equals(o)) { // 3rd index in cLiterals is cached as locals[literal3index]
                    mv.visitVarInsn(ALOAD, zeroLengthStringIndex);
                    mv.visitInsn(ICONST_0);
                    return;
                } else {
                    // locals[literalsIndex] = this.cLiterals
                    // cLiterals[
                    mv.visitVarInsn(ALOAD, literalsIndex);
                }
            } else {
                // this.
                mv.visitVarInsn(ALOAD, thisIndex);
                // this.array[i]
                mv.visitFieldInsn(GETFIELD, superClassIName, "cLiterals", typeArrayOfScalar);
                // push the array's index on the stack
            }

            if (collectStats) {
                literalsStatsAaLoad[index] += 1;
            }

            switch (index) {
                case 0:
                    mv.visitInsn(ICONST_0);
                    break;
                case 1:
                    mv.visitInsn(ICONST_1);
                    break;
                case 2:
                    mv.visitInsn(ICONST_2);
                    break;
                case 3:
                    mv.visitInsn(ICONST_3);
                    break;
                case 4:
                    mv.visitInsn(ICONST_4);
                    break;
                case 5:
                    mv.visitInsn(ICONST_5);
                    break;
                default:
                    if (index < 128) {
                        mv.visitIntInsn(BIPUSH, index);
                    } else if (index < 32768) {
                        mv.visitIntInsn(SIPUSH, index);
                    } else {
                        mv.visitIntInsn(SIPUSH, 32767);
                        mv.visitIntInsn(SIPUSH, index - 32767);
                        mv.visitInsn(IADD);
                    }
            }
            mv.visitInsn(AALOAD);

        } else {
            mv.visitVarInsn(ALOAD, thisIndex);

            mv.visitFieldInsn(GETFIELD, classIName, "c" + index, literalDesc);
        }

        mv.visitInsn(ICONST_0);
    }

    private void compileParameter(final MethodVisitor mv) {

        final Code code = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, thisIndex);

        if (collectStats) {
            paramsStats[code.value]++;
        }

        mv.visitFieldInsn(GETFIELD, classIName, "p" + code.value, opeDesc);

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

        if (!Modifier.isStatic(method.getModifiers())) {

            if (!instancedLibraries.contains(libraryClass)) {
                instancedLibraries.add(libraryClass);
            }

            mv.visitIntInsn(ALOAD, thisIndex);
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
        mv.visitMethodInsn(Modifier.isStatic(method.getModifiers()) ? INVOKESTATIC : INVOKEVIRTUAL, Type.getInternalName(libraryClass), method.getName(), methodDesc);

        if (hasresult) {

            // 関数のときは戻り値が必要。

            if (method.getReturnType().equals(Void.TYPE)) {

                if (useLocalVariableForLiterals) {
                    mv.visitVarInsn(ALOAD, zeroIndex);
                } else {
                    mv.visitVarInsn(ALOAD, thisIndex);
                    mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(new Integer(0)), literalDesc);
                }
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

            final Class type = method.getParameterTypes()[paramIndex];

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
                    compileExpression(mv);

                    if (type.equals(Operand.class)) {

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

            compileExpression(new EmptyVisitor());

        }
    }

    private void compileModuleCommand(final MethodVisitor mv, final boolean hasresult) {

        final Code code = ax.codes[codeIndex++];
        final Method method = runtime.getMethodFor(ax, code);
        final String methodDesc = Type.getMethodDescriptor(method);

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

        mv.visitLdcInsn(new Integer(ax.functions[code.value].otindex));

        mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(method.getDeclaringClass()), method.getName(),
                methodDesc);

        if (hasresult) {

            // 関数のときは戻り値が必要。

            if (!function.isFunction()) {

                if (useLocalVariableForLiterals) {
                    mv.visitVarInsn(ALOAD, zeroIndex);
                } else {
                    mv.visitVarInsn(ALOAD, thisIndex);
                    mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(new Integer(0)), literalDesc);
                }

            } else {

                mv.visitInsn(ICONST_0);

            }

        } else {

            // 命令のときは戻り値は stat に代入

            mv.visitInsn(DUP);

            final Label noassign = new Label();
            mv.visitJumpInsn(IFNULL, noassign);

            mv.visitVarInsn(ALOAD, contextIndex);
            mv.visitFieldInsn(GETFIELD, contextIName, "stat", Type.getDescriptor(IntScalar.class));

            mv.visitInsn(SWAP);

            mv.visitInsn(ICONST_0);

            mv.visitInsn(SWAP);

            mv.visitInsn(ICONST_0);

            mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "assign", "(I" + opeDesc + "I)V");

            final Label end = new Label();
            mv.visitJumpInsn(GOTO, end);
            mv.visitLabel(noassign);
            mv.visitInsn(POP);
            mv.visitLabel(end);
        }

    }

    private void compileModuleParameters(final MethodVisitor mv, final Function function) {

        boolean firstParam = true;

        for (int paramIndex = 0; paramIndex < function.prmmax; ++paramIndex) {

            final ByteCode.Parameter param = ax.parameters[function.prmindex + paramIndex];
            final int type = param.mptype;

            mv.visitVarInsn(ALOAD, thisIndex);

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
                        if (useLocalVariableForLiterals) {
                            mv.visitVarInsn(ALOAD, zeroLengthStringIndex);
                        } else {
                            mv.visitVarInsn(ALOAD, thisIndex);
                            mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(""), literalDesc);
                        }

                    }
                    break;
                    case 3: // dnum
                    {
                        mv.visitVarInsn(ALOAD, thisIndex);
                        mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(new Double(0.0)), literalDesc);
                    }
                    break;
                    case 4: // inum
                    {
                        if (useLocalVariableForLiterals) {
                            mv.visitVarInsn(ALOAD, zeroIndex);
                        } else {
                            mv.visitVarInsn(ALOAD, thisIndex);
                            mv.visitFieldInsn(GETFIELD, classIName, "c" + literals.indexOf(new Integer(0)), literalDesc);
                        }
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

            mv.visitFieldInsn(PUTFIELD, classIName, "p" + (function.prmindex + paramIndex), opeDesc);

        }

        // いらない引数は読み飛ばす。
        while (codeIndex < ax.codes.length && !(ax.codes[codeIndex].newLine)
                && !(ax.codes[codeIndex].type == Code.Type.Mark && ax.codes[codeIndex].value == ')')) {

            compileExpression(new EmptyVisitor());

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

    private void compileProgramCommand(final MethodVisitor mv) {

        switch (ax.codes[codeIndex].value) {
            case 0x00: // goto
                compileGoto(mv);
                break;
            case 0x02: // return
                compileReturn(mv);
                break;
            case 0x03: // break
                compileBreak(mv);
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
            case 0x01: // gosub
            case 0x07: // wait
            case 0x08: // await
            case 0x09: // dim
            case 0x0A: // sdim
            case 0x0D: // dimtype
            case 0x0E: // dup
            case 0x0F: // dupptr
            case 0x10: // end
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
            default:
                throw new UnsupportedOperationException("プログラム制御命令 " + ax.codes[codeIndex].value + " はサポートされていません。");
        }
    }

    private void compileGoto(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        mv.visitJumpInsn(GOTO, getLabel(label.value));

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

        mv.visitInsn(ARETURN);

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

        mv.visitJumpInsn(IFEQ, getLabel(label.value));

        final Label startLabel = new Label();

        mv.visitLabel(startLabel);

        loopStarts.add(startLabel);

    }

    private void compileBreak(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];
        final Code label = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, contextIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "endLoop", "()V");

        mv.visitJumpInsn(GOTO, getLabel(label.value));

    }

    private void compileLoop(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        mv.visitVarInsn(ALOAD, contextIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, contextIName, "nextLoop", "()Z");

        mv.visitJumpInsn(IFNE, (Label) loopStarts.pop());

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

        mv.visitJumpInsn(IFNE, (Label) loopStarts.peek());

        mv.visitJumpInsn(GOTO, getLabel(label.value));

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

        mv.visitJumpInsn(IFEQ, getLabel(label.value));

        final Label startLabel = new Label();

        mv.visitLabel(startLabel);

        loopStarts.add(startLabel);
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

        mv.visitJumpInsn(IFEQ, getLabel(label.value));

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

        final Label negative = new Label();
        mv.visitJumpInsn(IFLE, negative);

        final Label nojump = new Label();

        mv.visitJumpInsn(IF_ICMPGE, getLabel(label.value));

        mv.visitJumpInsn(GOTO, nojump);

        mv.visitLabel(negative);

        mv.visitJumpInsn(IF_ICMPLE, getLabel(label.value));

        mv.visitLabel(nojump);
    }

    private void compileOn(MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        // 変数の値
        compileExpression(mv);
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

            final Label nojump = new Label();
            mv.visitTableSwitchInsn(0, labels.size() - 1, nojump, (Label[]) labels.toArray(new Label[0]));

            mv.visitLabel(nojump);

        } else {

            final Method method = runtime.getMethodFor(ax, statement);
            final String methodDesc = Type.getMethodDescriptor(method);

            final Label[] pushLabels = new Label[labels.size()];
            for (int i = 0; i < pushLabels.length; ++i) {
                pushLabels[i] = new Label();
            }

            final Label endSwitch = new Label();
            final Label nojump = new Label();

            mv.visitTableSwitchInsn(0, labels.size() - 1, nojump, pushLabels);

            for (int i = 0; i < pushLabels.length; ++i) {
                final Label label = pushLabels[i];

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

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        // offset は、条件式の先頭からの移動量
        final int offset = ax.codes[codeIndex++].value;
        final int base = ax.codes[codeIndex].offset;

        compileExpression(mv);

        mv.visitMethodInsn(INVOKEVIRTUAL, opeIName, "toInt", "(I)I");

        final Label existingLabel = (Label) labels.get(new Integer(base + offset));
        if (existingLabel != null) {
            mv.visitJumpInsn(IFEQ, existingLabel);
        } else {
            final Label label = new Label();
            labels.put(new Integer(base + offset), label);

            mv.visitJumpInsn(IFEQ, label);

        }

    }

    private void compileElse(final MethodVisitor mv) {

        //@SuppressWarnings("unused")
        final Code code = ax.codes[codeIndex++];

        // offset は、次の命令からの移動量
        final int offset = ax.codes[codeIndex++].value;
        final int base = ax.codes[codeIndex].offset;

        final Label existingLabel = (Label) labels.get(new Integer(base + offset));
        if (existingLabel != null) {
            mv.visitJumpInsn(GOTO, existingLabel);
        } else {
            final Label label = new Label();
            labels.put(new Integer(base + offset), label);
            mv.visitJumpInsn(GOTO, label);

        }

    }

    private void createSubMethods() {

        int totalSize = 0;

        for (int i = 0; i < submethodStartEnds.size(); ++i) {

            totalSize += ((int[]) submethodStartEnds.get(i))[1] - ((int[]) submethodStartEnds.get(i))[0];

            prepareLabels();

            codeIndex = ((int[]) submethodStartEnds.get(i))[0];

            final MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "m" + i, "()V", null, new String[0]);

            compileSeparatedMethod(mv, ((int[]) submethodStartEnds.get(i))[1]);

            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        if (DEBUG_ENABLED) {
            System.out.println("" + totalSize + "/" + ax.codes.length);
        }
    }

    /* */
    private ClassNode createSuperClassVisitor() {
        ClassNode node = new ClassNode();
        return node;
    }

    private void createArrayFieldInSuperClass() {

        // Type of Scalar[]
        final String typeArrayOfScalar = "[L" + literalIName + ";";
        // Create constant literals field
        FieldVisitor fv = superClassWriter.visitField(ACC_PUBLIC, "cLiterals", typeArrayOfScalar, null, null);
        fv.visitEnd();

    }

    private void createSuperClassCtor() {

        // Type of Scalar[]


        final MethodVisitor mv = superClassWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);

        // Call super()
        mv.visitVarInsn(ALOAD, thisIndex);
        mv.visitMethodInsn(INVOKESPECIAL, parentIName, "<init>", "()V");

        // Create array of literal Scalars by:

        // get this
        mv.visitVarInsn(ALOAD, thisIndex);
        // push the size
        mv.visitIntInsn(SIPUSH, literals.size());

        // make the array of type Scalar, popping size
        mv.visitTypeInsn(ANEWARRAY, literalIName);

        // Store this field in this class
        mv.visitFieldInsn(PUTFIELD, superClassIName, "cLiterals", typeArrayOfScalar);

        // Populate the array
        for (int i = 0; i < literals.size(); ++i) {

            final Object value = literals.get(i);

            // push this on the stack
            mv.visitVarInsn(ALOAD, thisIndex);

            // pop this, push the array
            mv.visitFieldInsn(GETFIELD, superClassIName, "cLiterals", typeArrayOfScalar);

            // push the array's index on the stack
            switch (i) {
                case 0:
                    mv.visitInsn(ICONST_0);
                    break;
                case 1:
                    mv.visitInsn(ICONST_1);
                    break;
                case 2:
                    mv.visitInsn(ICONST_2);
                    break;
                case 3:
                    mv.visitInsn(ICONST_3);
                    break;
                case 4:
                    mv.visitInsn(ICONST_4);
                    break;
                case 5:
                    mv.visitInsn(ICONST_5);
                    break;
                default:
                    if (i < 128) {
                        mv.visitIntInsn(BIPUSH, i);
                    } else if (i < 32768) {
                        mv.visitIntInsn(SIPUSH, i);
                    } else {
                        mv.visitIntInsn(SIPUSH, 32767);
                        mv.visitIntInsn(SIPUSH, i - 32767);
                        mv.visitInsn(IADD);
                    }
            }


            // push the load-constant instruction on the stack
            mv.visitLdcInsn(value);

            // call Scalar.fromValue on the constant to get a Scalar
            // pop the constant off, but push a Scalar on
            mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
                    + Type.getDescriptor(value instanceof Integer ? Integer.TYPE
                    : value instanceof Double ? Double.TYPE : String.class) + ")" + literalDesc);

            // populate the c# fields for constants
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(SWAP);
            mv.visitFieldInsn(PUTFIELD, superClassIName, "c" + i, literalDesc);


            // put the Scalar onto array[i]
            // pop off the Scalar, the index, the array.

            mv.visitInsn(DUP);
	    mv.visitVarInsn(ALOAD, thisIndex);
	    mv.visitInsn(SWAP);
	    mv.visitFieldInsn(PUTFIELD, superClassIName, "c" + i, literalDesc);
            mv.visitInsn(AASTORE);
        }
        /*
        
        // Create literals
        // 定数を用意する、毎回作っていたら遅い。
        for (int i = 0; i < literals.size(); ++i) {
        
        final Object value = literals.get(i);
        
        mv.visitVarInsn(ALOAD, thisIndex);
        
        mv.visitLdcInsn(value);
        
        mv.visitMethodInsn(INVOKESTATIC, literalIName, "fromValue", "("
        + Type.getDescriptor(value instanceof Integer ? Integer.TYPE
        : value instanceof Double ? Double.TYPE : String.class) + ")" + literalDesc);
        
        mv.visitFieldInsn(PUTFIELD, classIName, "c" + i, literalDesc);
        }
        
         */

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
}

class CommonObjectContainer {

    public Object o;
    public int localIndex;

    public CommonObjectContainer(Object o, int localIndex) {
        this.o = o;
        this.localIndex = localIndex;
    }
}
