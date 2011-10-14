package au.org.theark.lims.web.component.panel.applet;

import net.sf.json.JSONObject;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.value.IValueMap;
import org.apache.wicket.util.value.ValueMap;

/**
 * <p>Wicket component to add the 
 * <a href="http://java.sun.com/javase/6/docs/technotes/guides/jweb/deployment_advice.html">Sun's Deployment Toolkit</a>
 * Javascript.
 * The markup can be defined as applet. Attributes defined for the applet are
 * copied to the Javascript. The markup can also be another element, for example
 * a div element.</p>
 * <p>
 * Suppose we have the following applet markup:
 * <pre>
 * &lt;applet wicket:id="applet" width=200 height=120 code="SignatureApplet.class"
 *   archive="codesign.jar"&gt;&lt;/applet&gt;
 * </pre>
 * In a Wicket page we can create this component and add it to the page:
 * <pre>
 * final JavaDeploy javaDeploy = new JavaDeploy("applet");
 * add(javaDeploy);
 * </pre>
 * We get the following output:
 * <pre>
 * &lt;html&gt;
 *   &lt;head&gt;
 *     &lt;script type="text/Javascript" src="http://java.com/js/deployJava.js"&gt;&lt;/script&gt;
 *   &lt;/head&gt;
 *   &lt;body&gt;
 *     &lt;script&gt;
 *     var attributes = { "width":200,"height":120,"code":"SignatureApplet.class","archive":"codesign.jar"};
 *     var parameters = {};
 *     var version = null;
 *     deployJava.runApplet(attributes, parameters, version);
 *     &lt;/script&gt;
 *   &lt;/body&gt;
 * &lt;/html&gt;
 * </pre>
 * </p>
 */
public class DeployJava extends WebComponent implements IHeaderContributor {
    private static final long serialVersionUID = 1L;

    /**
     * Javascript URL on Sun's website for deployJava Javascript. (={@value})
     */
    private static final String JAVASCRIPT_URL = "http://java.com/js/deployJava.js";

    /**
     * Attribute to set the name of the applet. (={@value})
     */
    private static final String ATTRIBUTE_NAME = "name";
    
    /**
     * Attribute to set the width of the applet. (={@value})
     */
    private static final String ATTRIBUTE_WIDTH = "width";

    /**
     * Attribute to set the height of the applet. (={@value})
     */
    private static final String ATTRIBUTE_HEIGHT = "height";

    /**
     * Attribute to set the applet classname. (={@value})
     */
    private static final String ATTRIBUTE_CODE = "code";

    /**
     * Attribute to set the codebase for the applet. (={@value})
     */
    private static final String ATTRIBUTE_CODEBASE = "codebase";

    /**
     * Attribute to set the archive neede by the applet. (={@value})
     */
    private static final String ATTRIBUTE_ARCHIVE = "archive";

    /**
     * Minimal Java version needed for the applet.
     */
    private String minimalVersion;

    /**
     * If true we use a local resource otherwise the URL from the Sun site.
     * For the local resource we must add the file deployJava.js next to 
     * this class in our package structure.
     */
    private boolean useJavascriptResource;

    /**
     * Attributes for the javaDeploy.runApplet Javascript method.
     */
    private IValueMap appletAttributes = new ValueMap();

    /**
     * Parameters for the javaDeploy.runApplet Javascript method.
     */
    private IValueMap appletParameters = new ValueMap();

    /**
     * Default constructor with markup id.
     *
     * @param id Markup id for applet.
     */
    public DeployJava(String id) {
        super(id);
    }

    /**
     * Minimal Java version for the applet. E.g. Java 1.6 is "1.6".
     *
     * @param version Minimal Java version needed by the applet.
     */
    public void setMinimalVersion(final String version) {
        this.minimalVersion = version;
    }

    /**
     * Width of the applet.
     *
     * @param width Width of the applet on screen.
     */
    public void setWidth(final Integer width) {
        appletAttributes.put(ATTRIBUTE_WIDTH, width);
    }

    /**
     * Height of the applet.
     *
     * @param height Height of the applet on screen.
     */
    public void setHeight(final Integer height) {
        appletAttributes.put(ATTRIBUTE_HEIGHT, height);
    }

    /**
     * Applet classname.
     *
     * @param code Applet classname.
     */
    public void setCode(final String code) {
        appletAttributes.put(ATTRIBUTE_CODE, code);
    }

    /**
     * Codebase for the applet code.
     *
     * @param codebase Codebase for the applet code.
     */
    public void setCodebase(final String codebase) {
        appletAttributes.put(ATTRIBUTE_CODEBASE, codebase);
    }

    /**
     * Archive path for the applet.
     *
     * @param archive Archive location for the applet.
     */
    public void setArchive(final String archive) {
        appletAttributes.put(ATTRIBUTE_ARCHIVE, archive);
    }
    
    /**
     * Name for the applet.
     *
     * @param name Name for the applet.
     */
    public void setName(final String name) {
        appletAttributes.put(ATTRIBUTE_NAME, name);
    }

    /**
     * Add a parameter to the applet.
     *
     * @param key Name of the parameter.
     * @param value Value for the parameter.
     */
    public void addParameter(final String key, final Object value) {
        appletParameters.put(key, value);
    }

    /**
     * Indicate if deployJava Javascript must be loaded from the Sun site or as local resource.
     *
     * @param useResource True local resource is used, otherwise Sun site.
     */
    public void setUseJavascriptResource(final boolean useResource) {
        this.useJavascriptResource = useResource;
    }

    /**
     * Get the applet attributes already set and assign them to the attribute
     * list for the Javascript code. And we change the tag name to "script".
     *
     * @param tag The current tag which is replaced.
     */
    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        if ("applet".equalsIgnoreCase(tag.getName())) {
            final IValueMap tagAttributes = tag.getAttributes();
            // Save wicket:id so we can add again to the list of attributes.
            final String wicketId = tagAttributes.getString("wicket:id");
            appletAttributes.putAll(tagAttributes);
            tagAttributes.clear();
            tagAttributes.put("wicket:id", wicketId);
            tagAttributes.put("type", "text/javascript");
        }
        tag.setName("script");
    }

    /**
     * Create Javascript for deployJava.runApplet.
     *
     * @param markupStream MarkupStream to be replaced.
     * @param openTag Tag we are replacing.
     */
    @Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        final StringBuilder script = new StringBuilder();
        if (appletAttributes.size() > 0) {
            final JSONObject jsonAttributes = JSONObject.fromObject(appletAttributes);
            script.append("var attributes = " + jsonAttributes + ";\n");
        } else {
            script.append("var attributes = {};\n");
        }
        if (appletParameters.size() > 0) {
            final JSONObject jsonParameters = JSONObject.fromObject(appletParameters);
            script.append("var parameters = " + jsonParameters + ";\n");
        } else {
            script.append("var parameters = {};\n");
        }
        if (minimalVersion != null) {
            script.append("var version = \"" + minimalVersion + "\";\n");
        } else {
            script.append("var version = null;\n");
        }
        script.append("deployJava.runApplet(attributes, parameters, version);\n");
        replaceComponentTagBody(markupStream, openTag, script.toString());
    }

    /**
     * Add Javascript src reference in the HTML head section of the web page.
     *
     * @param response Header response.
     */
    public void renderHead(IHeaderResponse response) {
        if (useJavascriptResource) {
            response.renderJavaScriptReference(new PackageResourceReference(DeployJava.class, "deployJava.js"));
        } else {
            response.renderJavaScriptReference(JAVASCRIPT_URL);
        }
    }
}
