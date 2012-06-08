package hsplet.compiler;

//import org.objectweb.asm.KLabel;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * @author Kejardon
 * Intended parser for handling subsubmethods from Compiler.java.
 */
public class SSMAdapter extends MethodAdapter {
    protected final KLabel startLabel;
    protected final KLabel endLabel;
    protected KLabel replaceSL;
    protected KLabel replaceEL;
    public SSMAdapter(MethodVisitor mv) {
        super(mv);
        startLabel=null;
        endLabel=null;
    }
    public SSMAdapter(MethodVisitor mv, KLabel sl, KLabel el) {
        super(mv);
        startLabel=sl;
        if(startLabel!=null) {
            replaceSL=new KLabel();
            mv.visitLabel(replaceSL);
        }
        endLabel=el;
        if(endLabel!=null)
            replaceEL=new KLabel();
    }
    protected class MyOpcode {
        public static final int LABEL=-1;
        public int opcode;
        public int intData;
        public Object otherData;
        public MyOpcode(int o){
            opcode=o;
        }
        public MyOpcode(int o, int i){
            opcode=o;
            intData=i;
        }
        public MyOpcode(int o, Object d){
            opcode=o;
            otherData=d;
        }
        public MyOpcode(int o, int i, Object d){
            opcode=o;
            intData=i;
            otherData=d;
        }
    }
    protected void visit(MyOpcode o) {
        switch(o.opcode) {
            case MyOpcode.LABEL: {
                KLabel L=(KLabel)o.otherData;
                if(L==startLabel) L=replaceSL;
                else if(L==endLabel) L=replaceEL;
                mv.visitLabel(L);
                break;
            }
            case Opcodes.IINC:
                mv.visitIincInsn(o.intData, ((Integer)o.otherData).intValue());
                break;
            case Opcodes.AASTORE:
            case Opcodes.ARETURN:
            case Opcodes.POP:
            case Opcodes.DUP:
            case Opcodes.IADD:
            case Opcodes.IMUL:
            case Opcodes.AALOAD:
            case Opcodes.SWAP:
            case Opcodes.ACONST_NULL:
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
                mv.visitInsn(o.opcode);
                break;
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEVIRTUAL: {
                String[] data=(String[])o.otherData;
                mv.visitMethodInsn(o.opcode, data[0], data[1], data[2]);
                break;
            }
            case Opcodes.GETSTATIC:
            case Opcodes.PUTFIELD:
            case Opcodes.GETFIELD: {
                String[] data=(String[])o.otherData;
                mv.visitFieldInsn(o.opcode, data[0], data[1], data[2]);
                break;
            }
            case Opcodes.TABLESWITCH: {
                Object[] data=(Object[])o.otherData;
                for(int i=0;i<((KLabel[])data[2]).length;i++) {
                    KLabel L=((KLabel[])data[2])[i];
                    if(L==startLabel) ((KLabel[])data[2])[i]=replaceSL;
                    else if(L==endLabel) ((KLabel[])data[2])[i]=replaceEL;
                }
                KLabel L=(KLabel)data[1];
                if(L==startLabel) L=replaceSL;
                else if(L==endLabel) L=replaceEL;
                mv.visitTableSwitchInsn(((Integer)data[0]).intValue(), o.intData, L, (KLabel[])data[2]);
                break;
            }
            case Opcodes.ISTORE:
            case Opcodes.ALOAD:
            case Opcodes.ILOAD:
                mv.visitVarInsn(o.opcode, o.intData);
                break;
            case Opcodes.IFNULL:
            case Opcodes.IFLE:
            case Opcodes.IFNE:
            case Opcodes.IFEQ:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ICMPGE:
            case Opcodes.GOTO: {
                KLabel L=(KLabel)o.otherData;
                if(L==startLabel) L=replaceSL;
                else if(L==endLabel) L=replaceEL;
                mv.visitJumpInsn(o.opcode, L);
                break;
            }
            case Opcodes.LDC:
                mv.visitLdcInsn(o.otherData);
                break;
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                mv.visitIntInsn(o.opcode, o.intData);
                break;
            case Opcodes.ANEWARRAY:
                mv.visitTypeInsn(o.opcode, ((String)o.otherData).substring(1, ((String)o.otherData).length()-1));
                break;
            default:
                throw new UnsupportedOperationException("Opcode value for write: "+o.opcode);
        }
    }
}