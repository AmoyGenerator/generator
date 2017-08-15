package org.eclipse.jet.internal.compiler.templates;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2TemplateLoader;
import org.eclipse.jet.JET2TemplateLoaderExtension;

public class CodeGenTemplateLoader implements JET2TemplateLoader,
        JET2TemplateLoaderExtension {

    private JET2TemplateLoader delegate = null;

    private final static Map pathToTemplateOrdinalMap = new HashMap(11);
    static {
        pathToTemplateOrdinalMap.put("templates/backup/jet2java.jet", //$NON-NLS-1$
                new Integer(0));
        pathToTemplateOrdinalMap.put("templates/backup/tagDataDeclarations.jet", //$NON-NLS-1$
                new Integer(1));
        pathToTemplateOrdinalMap.put("templates/backup/writeTextElement.jet", //$NON-NLS-1$
                new Integer(2));
        pathToTemplateOrdinalMap.put("templates/backup/xmlEnd.jet", //$NON-NLS-1$
                new Integer(3));
        pathToTemplateOrdinalMap.put("templates/jet2transform.jet", //$NON-NLS-1$
                new Integer(4));
        pathToTemplateOrdinalMap.put("templates/jetTemplateMap.properties.jet", //$NON-NLS-1$
                new Integer(5));
        pathToTemplateOrdinalMap.put("templates/main.jet", //$NON-NLS-1$
                new Integer(6));
        pathToTemplateOrdinalMap.put("templates/v1/jet2java.jet", //$NON-NLS-1$
                new Integer(7));
        pathToTemplateOrdinalMap.put("templates/v2/jet2java.jet", //$NON-NLS-1$
                new Integer(8));
        pathToTemplateOrdinalMap.put("templates/writeJavaExpression.jet", //$NON-NLS-1$
                new Integer(9));
        pathToTemplateOrdinalMap.put("templates/xmlStart.jet", //$NON-NLS-1$
                new Integer(10));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jet.JET2TemplateLoader#getTemplate(java.lang.String)
     */
    public JET2Template getTemplate(final String templatePath) {
        final Integer ordinal = (Integer)pathToTemplateOrdinalMap.get(templatePath);
        if(ordinal != null) {
           switch (ordinal.intValue()) {
            case 0: // templates/backup/jet2java.jet
                return new org.eclipse.jet.internal.compiler.templates.JET2JavaGenerator();
            case 1: // templates/backup/tagDataDeclarations.jet
                return new org.eclipse.jet.internal.compiler.templates.TagDataDeclGenerator();
            case 2: // templates/backup/writeTextElement.jet
                return new org.eclipse.jet.internal.compiler.templates.WriteTextElementTemplate();
            case 3: // templates/backup/xmlEnd.jet
                return new org.eclipse.jet.internal.compiler.templates.XMLEndGenerator();
            case 4: // templates/jet2transform.jet
                return new org.eclipse.jet.internal.compiler.templates.JET2TransformGenerator();
            case 5: // templates/jetTemplateMap.properties.jet
                return new org.eclipse.jet.internal.compiler.templates.JETTemplateMapGenerator();
            case 6: // templates/main.jet
                return new org.eclipse.jet.internal.compiler.templates.DummyMain();
            case 7: // templates/v1/jet2java.jet
                return new org.eclipse.jet.internal.compiler.templates.v1.JET1JavaGenerator();
            case 8: // templates/v2/jet2java.jet
                return new org.eclipse.jet.internal.compiler.templates.v2.JET2JavaGeneratorNew();
            case 9: // templates/writeJavaExpression.jet
                return new org.eclipse.jet.internal.compiler.templates.WriteJavaExpressionTemplate();
            case 10: // templates/xmlStart.jet
                return new org.eclipse.jet.internal.compiler.templates.XMLStartGenerator();
            default:
                break;
            }
        }
        return this.delegate != null ? this.delegate.getTemplate(templatePath) : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jet.JET2TemplateLoaderExtension#getDelegateLoader()
     */
    public JET2TemplateLoader getDelegateLoader() {
        return this.delegate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jet.JET2TemplateLoaderExtension#setDelegateLoader(org.eclipse
     * .jet.JET2TemplateLoader)
     */
    public void setDelegateLoader(final JET2TemplateLoader loader) {
        this.delegate = loader;
    }
}
