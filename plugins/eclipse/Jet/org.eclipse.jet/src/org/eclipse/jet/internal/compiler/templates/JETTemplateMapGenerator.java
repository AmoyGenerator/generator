package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.jet.internal.compiler.PreferenceValueUtil;

public class JETTemplateMapGenerator implements JET2Template {

    public JETTemplateMapGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

Map templateMap = (Map)context.getVariable("args"); //$NON-NLS-1$
List templatePaths = new ArrayList(templateMap.keySet());
Collections.sort(templatePaths);

 
    for(Iterator i = templatePaths.iterator(); i.hasNext();){ 
        String key = (String)i.next();
        String value = (String)templateMap.get(key);

        out.write( PreferenceValueUtil.encode(key) );
        out.write(" = ");  //$NON-NLS-1$        
        out.write( PreferenceValueUtil.encode(value) );
        out.write(NL);         
 } 
    }
}
