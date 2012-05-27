/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.variable;

/**
 *
 * @author jdstroy
 */
public class LazyByteString extends ByteString {

    private boolean delegate = false;

    public String toString() {
        if (delegate) {
            return target.toString();
        } else {
            return super.toString();
        }
    }

    public ByteString substring(int index, int count) {
        if (delegate) {
            return target.substring(index, count);
        } else {
            return this.substring(index, count);
        }
    }

    public void set(int index, byte b) {
        __lazyConstruct();
        target.set(index, b);
    }

    public void replace(int index, int length, ByteString string) {
        __lazyConstruct();
        target.replace(index, length, string);
    }

    public int nextLineIndex(int index) {
        if (delegate) {
            return target.nextLineIndex(index);
        } else {
            return super.nextLineIndex(index);
        }
    }

    public int lineIndex(int line) {
        if (delegate) {
            return target.lineIndex(line);
        } else {
            return super.lineIndex(line);
        }
    }

    public int lineCount() {
        if (delegate) {
            return target.lineCount();
        } else {
            return super.lineCount();
        }
    }

    public int length() {
        if (delegate) {
            return target.length();
        } else {
            return super.length();
        }
    }

    public int indexOf(ByteString sub, int index) {
        if (delegate) {
            return target.indexOf(sub, index);
        } else {
            return super.indexOf(sub, index);
        }
    }

    public String getLine(int index) {
        if (delegate) {
            return target.getLine(index);
        } else {
            return super.getLine(index);
        }
    }

    public byte get(int index) {
        if (delegate) {
            return target.get(index);
        } else {
            return super.get(index);
        }
    }

    public boolean equals(Object obj) {
        if (delegate) {
            return target.equals(obj);
        } else {
            return super.equals(obj);
        }
    }

    public int compareTo(ByteString rhs) {
        if (delegate) {
            return target.compareTo(rhs);
        } else {
            return super.compareTo(rhs);
        }
    }

    public int compareSub(int index, ByteString rhs) {
        if (delegate) {
            return target.compareSub(index, rhs);
        } else {
            return super.compareSub(index, rhs);
        }
    }

    public void assign(ByteString string) {
        __lazyConstruct();
        target.assign(string);
    }

    public void assign(String string) {
        __lazyConstruct();
        target.assign(string);
    }

    public void append(ByteString rhs) {
        __lazyConstruct();
        target.append(rhs);
    }
    private ByteString target;
    private static final byte[] nullArray = new byte[256];

    private void __lazyConstruct() {
        if (!delegate) {
            target = new ByteString(new byte[256], 0, 0, false);
            delegate = true;
        }
    }

    public LazyByteString() {
        super(nullArray, 0, 0, false);
    }

    public LazyByteString(ByteString str, boolean uniqueBuffer) {
        super(str, uniqueBuffer);
    }

    public LazyByteString(byte[] bytes, int offset, int length, boolean uniqueBuffer) {
        super(bytes, offset, length, uniqueBuffer);
    }

    public LazyByteString(byte[] bytes, int offset, boolean uniqueBuffer) {
        super(bytes, offset, uniqueBuffer);
    }

    public LazyByteString(String text) {
        super(text);
    }
}
