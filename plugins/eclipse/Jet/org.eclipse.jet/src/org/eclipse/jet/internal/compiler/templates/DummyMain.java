package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;

public class DummyMain implements JET2Template {

    public DummyMain() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;
        out.write("<c:log>This transform in not intended to be run.</c:log>");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
