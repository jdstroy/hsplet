code: compiler/hsplet/compiler/*.java
	javac -classpath compiler;runtime;lib;lib/asm-debug-all-3.3.jar;lib/xstream-1.4.2.jar;lib/BrowserLauncher2-10rc4.jar;lib/hb16.jar;lib/jl1.0.jar;./hsplet.jar ./compiler/hsplet/compiler/*.java

test:
	java -Xmx512m -classpath compiler;runtime;lib;lib/asm-debug-all-3.3.jar;lib/xstream-1.4.2.jar;lib/BrowserLauncher2-10rc4.jar;lib/hb16.jar;lib/jl1.0.jar;./hsplet.jar hsplet.compiler.Compiler --jar=r:\out\out.jar --html=r:\out\out.html --pack=demo\net-ball.ax --template=sample-template.html --startClass=net-ball

run:
	java -Xmx800m -classpath compiler;runtime;lib;lib/asm-debug-all-3.3.jar;lib/xstream-1.4.2.jar;lib/BrowserLauncher2-10rc4.jar;lib/hb16.jar;lib/jl1.0.jar;./hsplet.jar; hsplet.compiler.GuiFrontEnd

extext:
	javac -classpath ext;compiler;runtime;lib;lib/asm-debug-all-3.3.jar;lib/xstream-1.4.2.jar;lib/BrowserLauncher2-10rc4.jar;lib/hb16.jar;lib/jl1.0.jar;./hsplet.jar;ext/plugin.jar ext/*.java
