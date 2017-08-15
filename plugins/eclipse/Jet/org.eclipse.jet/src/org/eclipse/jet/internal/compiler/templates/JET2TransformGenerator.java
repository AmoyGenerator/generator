/*
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  *
 * Contributors:
 *   IBM - Initial API and implementation
 */
package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.eclipse.jet.internal.JavaUtil;
import org.eclipse.jet.internal.compiler.LoaderGenerationParameters;

public class JET2TransformGenerator implements JET2Template {

    public JET2TransformGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

LoaderGenerationParameters parms = (LoaderGenerationParameters)context.getVariable("args"); //$NON-NLS-1$
SortedMap templateMap = new TreeMap(parms.getTemplateMap());
boolean useJava5 = context.hasVariable("useJava5") ? Boolean.valueOf((String)context.getVariable("useJava5")).booleanValue() : false; //$NON-NLS-1$  //$NON-NLS-2$  

        out.write("package ");  //$NON-NLS-1$        
        out.write( parms.getPackageName() );
        out.write(";");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("import java.util.HashMap;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import java.util.Map;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("import org.eclipse.jet.JET2Template;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import org.eclipse.jet.JET2TemplateLoader;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import org.eclipse.jet.JET2TemplateLoaderExtension;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("public class ");  //$NON-NLS-1$        
        out.write( parms.getClassName() );
        out.write(" implements JET2TemplateLoader,");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        JET2TemplateLoaderExtension {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("    private JET2TemplateLoader delegate = null;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
 if(useJava5){ 
        out.write("    private final static Map<String, Integer> pathToTemplateOrdinalMap = new HashMap<String,Integer>(");  //$NON-NLS-1$        
        out.write(templateMap.size());
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
 } else { 
        out.write("    private final static Map pathToTemplateOrdinalMap = new HashMap(");  //$NON-NLS-1$        
        out.write(templateMap.size());
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
 } 
        out.write("    static {");  //$NON-NLS-1$        
        out.write(NL);         
 int ordinal = 0;
   for(Iterator i=templateMap.keySet().iterator(); i.hasNext(); ordinal++) { 
       String templatePath = (String)i.next(); 
        out.write("        pathToTemplateOrdinalMap.put(");  //$NON-NLS-1$        
        out.write( JavaUtil.asJavaQuotedString(templatePath) );
        out.write(", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
 if(useJava5) { 
        out.write("                Integer.valueOf(");  //$NON-NLS-1$        
        out.write( ordinal );
        out.write("));");  //$NON-NLS-1$        
        out.write(NL);         
 } else { 
        out.write("                new Integer(");  //$NON-NLS-1$        
        out.write( ordinal );
        out.write("));");  //$NON-NLS-1$        
        out.write(NL);         
 } 
 } 
        out.write("    }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("    /*");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * (non-Javadoc)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * ");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * @see org.eclipse.jet.JET2TemplateLoader#getTemplate(java.lang.String)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     */");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    public JET2Template getTemplate(final String templatePath) {");  //$NON-NLS-1$        
        out.write(NL);         
 if(useJava5) { 
        out.write("        final Integer ordinal = pathToTemplateOrdinalMap.get(templatePath);");  //$NON-NLS-1$        
        out.write(NL);         
 } else { 
        out.write("        final Integer ordinal = (Integer)pathToTemplateOrdinalMap.get(templatePath);");  //$NON-NLS-1$        
        out.write(NL);         
 } 
        out.write("        if(ordinal != null) {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("           switch (ordinal.intValue()) {");  //$NON-NLS-1$        
        out.write(NL);         
 ordinal = 0;
   for(Iterator i=templateMap.entrySet().iterator(); i.hasNext(); ordinal++) { 
       Map.Entry entry = (Map.Entry)i.next(); 
        out.write("            case ");  //$NON-NLS-1$        
        out.write( ordinal );
        out.write(": // ");  //$NON-NLS-1$        
        out.write( entry.getKey() );
        out.write(NL);         
        out.write("                return new ");  //$NON-NLS-1$        
        out.write( entry.getValue() );
        out.write("();");  //$NON-NLS-1$        
        out.write(NL);         
 } 
        out.write("            default:");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("                break;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        return this.delegate != null ? this.delegate.getTemplate(templatePath) : null;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("    /*");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * (non-Javadoc)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * ");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * @see org.eclipse.jet.JET2TemplateLoaderExtension#getDelegateLoader()");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     */");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    public JET2TemplateLoader getDelegateLoader() {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        return this.delegate;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("    /*");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * (non-Javadoc)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * ");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * @see");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * org.eclipse.jet.JET2TemplateLoaderExtension#setDelegateLoader(org.eclipse");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     * .jet.JET2TemplateLoader)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("     */");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    public void setDelegateLoader(final JET2TemplateLoader loader) {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        this.delegate = loader;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("}");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
