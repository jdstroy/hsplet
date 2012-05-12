public class basic extends hsplet.RunnableCode{
public final hsplet.variable.Operand run(int);
  Code:
   0:	aload_0
   1:	getfield	#10; //Field context:Lhsplet/Context;
   4:	astore_2
   5:	iload_1
   6:	iconst_m1
   7:	istore_1
   8:	tableswitch{ //0 to 2
		0: 59;
		1: 52;
		2: 137;
		default: 36 }
   36:	aload_2
   37:	aload_0
   38:	getfield	#14; //Field c3:Lhsplet/variable/Scalar;
   41:	iconst_0
   42:	invokevirtual	#20; //Method hsplet/variable/Operand.toString:(I)Ljava/lang/String;
   45:	invokestatic	#26; //Method hsplet/function/GuiCommand.mes:(Lhsplet/Context;Ljava/lang/String;)V
   48:	aload_0
   49:	invokevirtual	#30; //Method m0:()V
   52:	aload_2
   53:	invokestatic	#36; //Method hsplet/function/ProgramCommand.stop:(Lhsplet/Context;)V
   56:	goto	52
   59:	aload_2
   60:	aload_0
   61:	getfield	#39; //Field c28:Lhsplet/variable/Scalar;
   64:	iconst_0
   65:	aload_0
   66:	getfield	#43; //Field v0:Lhsplet/variable/Variable;
   69:	getfield	#49; //Field hsplet/variable/Variable.value:Lhsplet/variable/Operand;
   72:	iconst_0
   73:	invokevirtual	#53; //Method hsplet/variable/Operand.add:(ILhsplet/variable/Operand;I)Lhsplet/variable/Operand;
   76:	iconst_0
   77:	aload_0
   78:	getfield	#56; //Field c29:Lhsplet/variable/Scalar;
   81:	iconst_0
   82:	invokevirtual	#53; //Method hsplet/variable/Operand.add:(ILhsplet/variable/Operand;I)Lhsplet/variable/Operand;
   85:	iconst_0
   86:	invokevirtual	#20; //Method hsplet/variable/Operand.toString:(I)Ljava/lang/String;
   89:	aload_0
   90:	getfield	#59; //Field c0:Lhsplet/variable/Scalar;
   93:	iconst_0
   94:	invokevirtual	#63; //Method hsplet/variable/Operand.toInt:(I)I
   97:	aload_0
   98:	getfield	#66; //Field c30:Lhsplet/variable/Scalar;
   101:	iconst_0
   102:	invokevirtual	#20; //Method hsplet/variable/Operand.toString:(I)Ljava/lang/String;
   105:	invokestatic	#70; //Method hsplet/function/GuiCommand.dialog:(Lhsplet/Context;Ljava/lang/String;ILjava/lang/String;)V
   108:	aload_2
   109:	iconst_0
   110:	invokestatic	#74; //Method hsplet/function/GuiCommand.cls:(Lhsplet/Context;I)V
   113:	aload_2
   114:	aload_0
   115:	getfield	#77; //Field c31:Lhsplet/variable/Scalar;
   118:	iconst_0
   119:	invokevirtual	#20; //Method hsplet/variable/Operand.toString:(I)Ljava/lang/String;
   122:	invokestatic	#26; //Method hsplet/function/GuiCommand.mes:(Lhsplet/Context;Ljava/lang/String;)V
   125:	aload_2
   126:	aload_0
   127:	getfield	#80; //Field c32:Lhsplet/variable/Scalar;
   130:	iconst_0
   131:	invokevirtual	#20; //Method hsplet/variable/Operand.toString:(I)Ljava/lang/String;
   134:	invokestatic	#26; //Method hsplet/function/GuiCommand.mes:(Lhsplet/Context;Ljava/lang/String;)V
   137:	aload_2
   138:	invokestatic	#36; //Method hsplet/function/ProgramCommand.stop:(Lhsplet/Context;)V
   141:	goto	151
   144:	getfield	#86; //Field hsplet/function/GotoException.label:I
   147:	istore_1
   148:	goto	5
   151:	aconst_null
   152:	areturn
  Exception table:
   from   to  target type
     5   141   144   Class hsplet/function/GotoException


public basic(hsplet.Context);
  Code:
   0:	aload_0
   1:	invokespecial	#209; //Method hsplet/RunnableCode."<init>":()V
   4:	aload_0
   5:	aload_1
   6:	putfield	#10; //Field context:Lhsplet/Context;
   9:	aload_0
   10:	new	#45; //class hsplet/variable/Variable
   13:	dup
   14:	invokespecial	#210; //Method hsplet/variable/Variable."<init>":()V
   17:	putfield	#43; //Field v0:Lhsplet/variable/Variable;
   20:	aload_0
   21:	ldc	#191; //int 0
   23:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   26:	putfield	#59; //Field c0:Lhsplet/variable/Scalar;
   29:	aload_0
   30:	ldc2_w	#213; //double 0.0d
   33:	invokestatic	#154; //Method hsplet/variable/Scalar.fromValue:(D)Lhsplet/variable/Scalar;
   36:	putfield	#217; //Field c1:Lhsplet/variable/Scalar;
   39:	aload_0
   40:	ldc	#219; //String 
   42:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   45:	putfield	#102; //Field c2:Lhsplet/variable/Scalar;
   48:	aload_0
   49:	ldc	#224; //String ????? HSPLet ??????????????
   51:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   54:	putfield	#14; //Field c3:Lhsplet/variable/Scalar;
   57:	aload_0
   58:	ldc	#225; //int 100
   60:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   63:	putfield	#89; //Field c4:Lhsplet/variable/Scalar;
   66:	aload_0
   67:	ldc	#227; //String ?????????????????HSPLet ???????????????
   69:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   72:	putfield	#96; //Field c5:Lhsplet/variable/Scalar;
   75:	aload_0
   76:	ldc	#228; //int 200
   78:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   81:	putfield	#99; //Field c6:Lhsplet/variable/Scalar;
   84:	aload_0
   85:	ldc	#230; //String ???????...
   87:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   90:	putfield	#105; //Field c7:Lhsplet/variable/Scalar;
   93:	aload_0
   94:	ldc	#232; //String 1. basic.hsp ? HSP3 ??????????????????????????
   96:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   99:	putfield	#108; //Field c8:Lhsplet/variable/Scalar;
   102:	aload_0
   103:	ldc	#234; //String   ?
   105:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   108:	putfield	#111; //Field c9:Lhsplet/variable/Scalar;
   111:	aload_0
   112:	ldc	#236; //String 2. compiler.exe ????? basic.ax ?????????????
   114:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   117:	putfield	#114; //Field c10:Lhsplet/variable/Scalar;
   120:	aload_0
   121:	ldc	#238; //String 3. basic.html ???????
   123:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   126:	putfield	#117; //Field c11:Lhsplet/variable/Scalar;
   129:	aload_0
   130:	ldc	#239; //int 500
   132:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   135:	putfield	#120; //Field c12:Lhsplet/variable/Scalar;
   138:	aload_0
   139:	ldc	#241; //String ??????...
   141:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   144:	putfield	#123; //Field c13:Lhsplet/variable/Scalar;
   147:	aload_0
   148:	ldc	#243; //String  ??: 1+2 = 
   150:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   153:	putfield	#126; //Field c14:Lhsplet/variable/Scalar;
   156:	aload_0
   157:	ldc	#244; //int 1
   159:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   162:	putfield	#129; //Field c15:Lhsplet/variable/Scalar;
   165:	aload_0
   166:	ldc	#245; //int 2
   168:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   171:	putfield	#132; //Field c16:Lhsplet/variable/Scalar;
   174:	aload_0
   175:	ldc	#247; //String  ??: sqrt(85720) = 
   177:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   180:	putfield	#135; //Field c17:Lhsplet/variable/Scalar;
   183:	aload_0
   184:	ldc	#248; //int 85720
   186:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   189:	putfield	#138; //Field c18:Lhsplet/variable/Scalar;
   192:	aload_0
   193:	ldc	#249; //int 255
   195:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   198:	putfield	#157; //Field c19:Lhsplet/variable/Scalar;
   201:	aload_0
   202:	ldc	#251; //String  ?????????????
   204:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   207:	putfield	#164; //Field c20:Lhsplet/variable/Scalar;
   210:	aload_0
   211:	ldc	#253; //String  ?????????
   213:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   216:	putfield	#167; //Field c21:Lhsplet/variable/Scalar;
   219:	aload_0
   220:	ldc	#254; //int 240
   222:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   225:	putfield	#170; //Field c22:Lhsplet/variable/Scalar;
   228:	aload_0
   229:	ldc	#255; //int 50
   231:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   234:	putfield	#177; //Field c23:Lhsplet/variable/Scalar;
   237:	aload_0
   238:	ldc_w	#256; //int 220
   241:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   244:	putfield	#180; //Field c24:Lhsplet/variable/Scalar;
   247:	aload_0
   248:	ldc_w	#257; //int 280
   251:	invokestatic	#212; //Method hsplet/variable/Scalar.fromValue:(I)Lhsplet/variable/Scalar;
   254:	putfield	#183; //Field c25:Lhsplet/variable/Scalar;
   257:	aload_0
   258:	ldc_w	#259; //String ???
   261:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   264:	putfield	#190; //Field c26:Lhsplet/variable/Scalar;
   267:	aload_0
   268:	ldc_w	#261; //String ????????
   271:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   274:	putfield	#202; //Field c27:Lhsplet/variable/Scalar;
   277:	aload_0
   278:	ldc_w	#263; //String ??????????\r\n???????????
   281:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   284:	putfield	#39; //Field c28:Lhsplet/variable/Scalar;
   287:	aload_0
   288:	ldc_w	#265; //String ???
   291:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   294:	putfield	#56; //Field c29:Lhsplet/variable/Scalar;
   297:	aload_0
   298:	ldc_w	#267; //String HSPLet ????
   301:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   304:	putfield	#66; //Field c30:Lhsplet/variable/Scalar;
   307:	aload_0
   308:	ldc_w	#269; //String ??????????????
   311:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   314:	putfield	#77; //Field c31:Lhsplet/variable/Scalar;
   317:	aload_0
   318:	ldc_w	#271; //String ??????????????????????????????
   321:	invokestatic	#222; //Method hsplet/variable/Scalar.fromValue:(Ljava/lang/String;)Lhsplet/variable/Scalar;
   324:	putfield	#80; //Field c32:Lhsplet/variable/Scalar;
   327:	return

}

