/**
 * Copyright (c) 2003 Columbia University in the City of New York.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package edu.columbia.ais.portal.cms;

import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC_10;
import edu.columbia.ais.filesystem.*;
import edu.columbia.ais.filesystem.impl.*;
import edu.columbia.ais.portal.cms.filters.XmlIncludeFilter;
import edu.columbia.ais.portal.cms.filters.XslTransformFilter;
import edu.columbia.ais.portal.cms.vocabulary.CUCMS;
import org.jasig.portal.services.LogService;
import org.jasig.portal.utils.SmartCache;
import org.jasig.portal.utils.SoftHashMap;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.transform.Templates;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.1 $
 */

public class Project implements ICMSConstants {
// --Recycle Bin START (8/20/03 2:50 PM):
//  // 2 second latency for forcing data reload
//  protected static final long refreshMillis = 2*1000;
// --Recycle Bin STOP (8/20/03 2:50 PM)
    protected static final String wildDir = "*" + SEPARATOR;
    protected final Document def;
    //protected SoftHashMap ifilters;
    //protected SoftHashMap tfilters;
    //protected static final FileCache cacheMaker = new FileCache();
    protected final Map timedFileCache = new SmartCache(300);
    protected final Map fileCache = new SoftHashMap();
    protected final XmlIncludeFilter xifilter;
    protected final XslTransformFilter xtfilter;
    protected final HashMap lookupPaths;
    //protected final HashMap lookupPathsCache = new HashMap();
    protected final HashMap resourcePaths;
    //protected final HashMap resourcePathsCache = new HashMap();
    protected final HashMap pathPatternCache = new HashMap();
    protected final Map includePathCache = new SoftHashMap();
    protected final Map outputPathBuilders = new HashMap();
    protected FileSystem repSystem;
    protected FileSystem preSystem;
    protected FileSystem bldSystem;
    protected FileSystem pubSystem;
    protected IFileSystemManager repManager;
    protected IFileSystemManager preManager;
    protected IFileSystemManager bldManager;
    protected IFileSystemManager pubManager;
    protected String baseBuildURL;
    protected String basePreviewURL;
    protected String basePublishURL;
    protected final String key;
    // --Recycle Bin (8/20/03 2:46 PM): protected static SoftHashMap factories;
    protected String[] requiredFiles;
    protected String[] requiredDirs;
    protected final String[] definedPatterns;
    protected final String[][] definedPatternParts;
    protected final String[] ignoreDirs;
    protected final String[] projectFactoryKeys;
    // --Recycle Bin (8/20/03 2:48 PM): protected IndexWriter indexWriter;
    // --Recycle Bin (8/20/03 2:48 PM): protected IndexSearcher indexSearcher;
    protected String title;
    protected String description;
    protected final Resolver resolver;

    public String getKey() {
        return key;
    }

    public String getPermissionsOwnerToken() 
    {
//        System.err.println("owner_prefix and key : " + PROJECT_OWNER_PREFIX + getKey());
        return PROJECT_OWNER_PREFIX + getKey();
    }

    public Project(File file, String key) throws FileSystemException, CMSException {
        this(DocumentFactory.getDocumentFromStream(new BufferedInputStream(file.getInputStream(), BUFFER_SIZE), Resolver.getCatalogResolver()), key);
        Resource resource = file.getMetaData();
        if(resource.hasProperty(DC_10.title)) {
            title = resource.getProperty(DC_10.title).getString();
        }
        if(resource.hasProperty(DC_10.description)) {
            description = resource.getProperty(DC_10.description).getString();
        }
    }

    public Project(Document definition, String key) throws FileSystemException {
        this.key = key;
        def = definition;
        int id = 1;
        xifilter = new XmlIncludeFilter();
        xtfilter = new XslTransformFilter();
        //fileCache = new SmartCache(300);//new SoftHashMap();
        lookupPaths = new HashMap();
        ArrayList patterns = new ArrayList();
        NodeList xd = def.getElementsByTagName("xml-doc");
        for(int i = 0; i < xd.getLength(); i++) {
            Element e = (Element)xd.item(i);
            String path = e.getAttribute("path");
            lookupPaths.put(path, e);
            patterns.add(path);
            //outputPathBuilders.put(path,new OutputPathBuilder(path));
        }
        NodeList xdt = def.getElementsByTagName("xml-doctype");
        for(int i = 0; i < xdt.getLength(); i++) {
            Element e = (Element)xdt.item(i);
            e.setAttribute(ID_ATTR, String.valueOf(id++));
            String path = e.getAttribute("path");
            lookupPaths.put(path, e);
            patterns.add(path);
            //outputPathBuilders.put(path,new OutputPathBuilder(path));
        }
        resourcePaths = new HashMap();

        NodeList rd = def.getElementsByTagName("resource-directory");
        for(int i = 0; i < rd.getLength(); i++) {
            Element e = (Element)rd.item(i);
            e.setAttribute(ID_ATTR, String.valueOf(id++));
            String path = e.getAttribute("path");
            resourcePaths.put(path, e);
            patterns.add(path);
            //outputPathBuilders.put(path,new OutputPathBuilder(path));
        }
        definedPatterns = (String[])patterns.toArray(new String[0]);
        definedPatternParts = new String[definedPatterns.length][];
        for(int i = 0; i < definedPatterns.length; i++) {
            definedPatternParts[i] = splitPathElements(definedPatterns[i]);
        }

        ArrayList ignores = new ArrayList();
        NodeList idirs = def.getElementsByTagName("ignore-directory");
        for(int i = 0; i < idirs.getLength(); i++) {
            Element e = (Element)idirs.item(i);
            ignores.add(e.getAttribute("path"));
        }
        ignoreDirs = (String[])ignores.toArray(new String[0]);

        NodeList op = def.getElementsByTagName("output");
        for(int i = 0; i < op.getLength(); i++) {
            Element o = (Element)op.item(i);
            o.setAttribute(ID_ATTR, String.valueOf(id++));
        }
        initFileSystems();
        NodeList ef = def.getDocumentElement().getChildNodes();
        ArrayList al = new ArrayList();
        addKeysToList(ef, al);
        projectFactoryKeys = (String[])al.toArray(new String[0]);
        resolver = new Resolver(this);
    }

    protected void initFileSystems() throws FileSystemException {
        NodeList pre = def.getElementsByTagName("preview");
        if(pre.getLength() > 0) {
            Element prev = (Element)pre.item(0);
            preSystem = getFileSystemFromElement(prev);
            basePreviewURL = prev.getAttribute("base-url");
        }

        NodeList pub = def.getElementsByTagName("publish");
        if(pub.getLength() > 0) {
            Element publ = (Element)pub.item(0);
            pubSystem = getFileSystemFromElement(publ);
            basePublishURL = publ.getAttribute("base-url");
        }

        NodeList bld = def.getElementsByTagName("build");
        if(bld.getLength() > 0) {
            Element build = (Element)bld.item(0);
            bldSystem = getFileSystemFromElement(build);
            baseBuildURL = build.getAttribute("base-url");
        }

        NodeList rep = def.getElementsByTagName("repository");
        if(rep.getLength() > 0) {
            repSystem = getFileSystemFromElement((Element)rep.item(0));
        }
    }

    protected static FileSystem getFileSystemFromElement(Element e) throws FileSystemException {
        // given an element, compose a new FileSystem if it has a file-system-def,
        // else retrieve by name (filesystem attribute) from FileSystemFactory
        FileSystem r = null;
        NodeList def = e.getElementsByTagName("filesystem-def");
        if(def.getLength() == 1) {
            r = FileSystemFactory.getFileSystem((Element)def.item(0));
        }
        else {
            String fname = e.getAttribute("filesystem");
            r = FileSystemFactory.getFileSystem(fname);
        }
        return r;
    }

    public void close() {
        if(repManager != null) {
            try {
                repManager.unmount();
                repManager = null;
            }
            catch(Exception e) {
            }
        }
        if(preManager != null) {
            try {
                preManager.unmount();
                preManager = null;
            }
            catch(Exception e) {
            }
        }
        if(pubManager != null) {
            try {
                pubManager.unmount();
                pubManager = null;
            }
            catch(Exception e) {
            }
        }
    }

    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public String getTypeElementID(String path) {
        String id = null;
        Element e = getElementForXmlPath(path);
        if(e != null) {
            id = e.getAttribute(ID_ATTR);
        }
        return id;
    }

    public boolean validXmlPath(String path) {
        if(getElementForXmlPath(path) != null) {
            return true;
        }
        return false;
    }

    protected Element getElementForXmlPath(String path) {
        return (Element)lookupPaths.get(getPattern(path));
        //return getElementForPath(path,lookupPaths,lookupPathsCache);
    }

    protected static int countWildcards(String path) {
        int i = 0;
        while(path.indexOf("*") >= 0) {
            i++;
            path = path.substring(path.indexOf("*") + 1);
        }
        return i;
    }

    public static String[] splitPathElements(String path) {
        ArrayList al = new ArrayList();
        while(path.indexOf(SEPARATOR) >= 0) {
            al.add(path.substring(0, path.indexOf(SEPARATOR) + 1));
            path = path.substring(path.indexOf(SEPARATOR) + 1);
        }
        if(path.length() > 0) {
            al.add(path);
        }
        return (String[])al.toArray(new String[0]);
    }

    protected Element getElementForResourcePath(String path) {
        return (Element)resourcePaths.get(getPattern(path));
        //return getElementForPath(path,resourcePaths,resourcePathsCache);
    }

    public Document getDefinition() {
        return def;
    }

    public String getLabel(String path) throws FileSystemException {
        IFileSystemManager rep = getRepositoryManager();
        if(rep.isDirectory(path)) {
            Resource dr = rep.getMetaData(path);
            if(dr.hasProperty(CUCMS.label)) {
                String label = dr.getProperty(CUCMS.label).getString();
                if(label != null && label.trim().length() > 0) {
                    return label;
                }
            }
        }
        String label = getName(path);
        if(label.endsWith("/")) {
            label = label.substring(0, label.length() - 1);
        }
        return label;
    }

    public String getName(String path) {
        int x = path.lastIndexOf(SEPARATOR) + 1;
        if(x == path.length()) {
            x = (path.substring(0, path.lastIndexOf(SEPARATOR))).lastIndexOf(SEPARATOR) + 1;
        }
        return path.substring(x);
    }

    public final static String join(String[] input, int startpos, String delimiter, int length) {
        StringBuffer buffer = new StringBuffer();
        for(int i = startpos; i < (startpos + length); i++) {
            buffer.append(input[i]);
            if(i < (startpos + length - 1)) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    public String getTitle() {
        if(title == null) {
            String r = null;
            NodeList nl = def.getElementsByTagName("title");
            if(nl.getLength() == 1) {
                Node t = nl.item(0).getFirstChild();
                if(t != null && t instanceof Text) {
                    r = ((Text)t).getData();
                }
            }
            return r;
        }
        return title;
    }

    public String getDescription() {
        if(description == null) {
            String r = null;
            NodeList nl = def.getElementsByTagName("description");
            if(nl.getLength() == 1) {
                Node t = nl.item(0).getFirstChild();
                if(t != null && t instanceof Text) {
                    r = ((Text)t).getData();
                }
            }
            return r;
        }
        return description;
    }

    public FileSystem getRepositoryFileSystem() {
        return repSystem;
    }

    public FileSystem getPreviewFileSystem() {
        return preSystem;
    }

    public FileSystem getBuildFileSystem() {
        return bldSystem;
    }

    public FileSystem getPublishFileSystem() {
        return pubSystem;
    }

    public String makeNewXmlDocument(String path) {
        Element xmldoc = getElementForXmlPath(path);
        StringBuffer sb = new StringBuffer();
        if(xmldoc != null) {
            if((xmldoc.getAttribute("definition") != null) && !(xmldoc.getAttribute("definition").trim().length() == 0)) {
                sb.append("<!DOCTYPE " + xmldoc.getAttribute("root") + " SYSTEM \"" + xmldoc.getAttribute("definition") + "\">\n");
            }
            sb.append("<" + xmldoc.getAttribute("root") + "/>");
            LogService.instance().log(LogService.DEBUG, sb.toString());
        }
        return sb.toString();
    }

    public File getXmlTemplateFile(String path) {
        File f = null;
        Element e = getElementForXmlPath(path);
        String template = e.getAttribute("template");
        if(template != null) {
            try {
                f = getFileHolder(template, null).getFile();
            }
            catch(Exception ee) {
            }
        }
        return f;
    }

    public static File getInMemoryCopy(File file) throws FileSystemException {
        //TimeKeeper.startTask("getInMemoryCopy");
        byte[] bytes = file.getBytes();
        Resource metadata = file.getMetaData();
        File out = new File(file.getPath(), new ByteArrayDataLoader(bytes),
                new ResourceMetaDataLoader(metadata), file.getCreator(), file.getDate(), file.getEdition());
        //TimeKeeper.stopTask("getInMemoryCopy");
        return out;
    }

    private void addKeysToList(NodeList facnodes, ArrayList list) {
        for(int i = 0; i < facnodes.getLength(); i++) {
            try {
                if(facnodes.item(i).getNodeType() == facnodes.item(i).ELEMENT_NODE) {
                    Element e = (Element)facnodes.item(i);
                    if(e.getTagName().equals("editor")) {
                        String key = e.getAttribute("key");
                        addKeyToList(key, list);
                    }
                }
            }
            catch(Exception e) {
            }
        }
    }

    private static void addKeyToList(String key, ArrayList list) {
        if(!list.contains(key)) {
            list.add(key);
        }
    }

    private void addKeysToList(String[] keys, ArrayList list) {
        for(int i = 0; i < keys.length; i++) {
            addKeyToList(keys[i], list);
        }
    }

    public IEditorServantFactory[] getServantFactories(String path) {
        //System.out.println("getting servant factories for "+path);
        Element pe = this.getElementForXmlPath(path);
        ArrayList al = new ArrayList();
        if(pe == null) {
            pe = this.getElementForResourcePath(path);
        }
        if(pe != null) {
            NodeList nl = pe.getElementsByTagName("editor");
            addKeysToList(nl, al);
        }
        if(al.size() == 0) {
            addKeysToList(ContentTypes.getEditorKeys(ContentTypes.getContentType(path)), al);
        }
        addKeysToList(projectFactoryKeys, al);
        // System.out.println("found "+al.size()+" editors for "+path);

        return getServants((String[])al.toArray(new String[0]));
    }

    private static IEditorServantFactory[] getServants(String[] keys) {
        IEditorServantFactory[] servants = new IEditorServantFactory[keys.length];
        for(int i = 0; i < servants.length; i++) {

            try {
                servants[i] = EditorFactoryLibrary.getFactory(keys[i]);
            }
            catch(CMSException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }

        }
        return servants;
    }

    public OutputPathBuilder getOutputPathBuilder(String pattern) {
        OutputPathBuilder builder;
        synchronized(outputPathBuilders) {
            builder = (OutputPathBuilder)outputPathBuilders.get(pattern);
            if(builder == null) {
                builder = new OutputPathBuilder(pattern);
                outputPathBuilders.put(pattern, builder);
            }
        }
        return builder;
    }

    public String[] getOutputPaths(String repositoryPath) throws FileSystemException {
        String pattern = getPattern(repositoryPath);
        return getOutputPathBuilder(pattern).getOutputPaths(repositoryPath);
    }

    public String getRepositoryPath(String outputPath) {
        Element e = getElementForResourcePath(outputPath);
        if(e != null) {
            // resources just get copied, so path is same as repository
            return outputPath;
        }
        String r = null;
        Iterator paths = lookupPaths.keySet().iterator();
        String[] pathParts = splitPathElements(outputPath);
        while(paths.hasNext() && r == null) {
            String key = (String)paths.next();
            Element x = (Element)lookupPaths.get(key);
            NodeList outputs = x.getElementsByTagName("output");
            ArrayList basedirs = new ArrayList();
            for(int i = 0; i < outputs.getLength(); i++) {
                Element output = (Element)outputs.item(i);
                String basedir = output.getAttribute("basedir");
                if((basedir == null) || (basedir.trim().equals(""))) {
                    basedir = SEPARATOR;
                }
                if(!basedirs.contains(basedir)) {
                    basedirs.add(basedir.substring(0, basedir.lastIndexOf(SEPARATOR)));
                }
            }
            Iterator bases = basedirs.iterator();
            while(bases.hasNext() && r == null) {
                String base = (String)bases.next();
                String[] keyParts = splitPathElements(base + key);
                if(keyParts.length == pathParts.length) {
                    boolean match = true;
                    for(int j = 0; j < keyParts.length; j++) {
                        if(!keyParts[j].startsWith("*")
                                && !getFileBase(keyParts[j]).equals(getFileBase(pathParts[j]))) {
                            match = false;
                            break;
                        }
                    }
                    if(match) {
                        r = getFileBase(outputPath.substring(base.length())) + ".xml";
                    }
                }
            }
        }
        return r;
    }

    protected static String getFileBase(String filename) {
        if(filename.indexOf(".") > 0) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        return filename;
    }

    public String[] getDependentFilePaths(String repositoryPath) throws FileSystemException {
        //System.out.println("Getting dependent file paths for "+repositoryPath);
        //TimeKeeper.startTask("Project.getDependentFilePaths()");
        ArrayList dependents = new ArrayList();
        ArrayList dependentKeys = new ArrayList();
        Iterator keys = lookupPaths.keySet().iterator();
        String[] pathParts = splitPathElements(repositoryPath);
        while(keys.hasNext()) {
            String key = (String)keys.next();
            Element e = (Element)lookupPaths.get(key);
            NodeList outputs = e.getElementsByTagName("output");
            for(int i = 0; i < outputs.getLength(); i++) {
                boolean brk = false;
                Element output = (Element)outputs.item(i);
                NodeList includes = output.getElementsByTagName("include");
                for(int j = 0; j < includes.getLength(); j++) {
                    Element include = (Element)includes.item(j);
                    String source = include.getAttribute("source");
                    String absolute = resolveRelativePath(key, source);
                    String[] abParts = splitPathElements(absolute);
                    if(abParts.length == pathParts.length) {
                        boolean match = true;
                        //StringBuffer keyLook = new StringBuffer();
                        for(int k = 0; k < abParts.length; k++) {
                            if(!abParts[k].startsWith("*")
                                    && !abParts[k].equals(pathParts[k])) {
                                match = false;
                                break;
                            }
                        }
                        if(match) {
                            String testme = resolveDependentDocType(key, source, repositoryPath);
                            //System.out.println("Found dependent key match for "+repositoryPath+": "+testme);
                            dependentKeys.add(testme);
                            brk = true;
                            break;
                        }
                    }
                }
                NodeList transforms = output.getElementsByTagName("transform");
                for(int j = 0; j < transforms.getLength(); j++) {
                    Element trans = (Element)transforms.item(j);
                    String source = trans.getAttribute("source");
                    if(repositoryPath.equals(source)) {
                        dependentKeys.add(key);
                        brk = true;
                        break;
                    }
                }
                if(brk) {
                    break;
                }
            }
        }
        Iterator deps = dependentKeys.iterator();
        while(deps.hasNext()) {
            String key = (String)deps.next();
            //System.out.println("Checking for instances of "+key);
            String sp = "/";
            String ep = key.substring(1);
            Stack stack = new Stack();
            stack.push(sp);
            /*System.out.println("Recursively checking for matches with starting path "
              +sp+" and ending path "+ep);
              */
            String[] dfs = getFilePathsRecursive(stack, ep);
            for(int i = 0; i < dfs.length; i++) {
                //System.out.println(repositoryPath+" has dependent "+dfs[i]);
                if(!dependents.contains(dfs[i])) {
                    dependents.add(dfs[i]);
                }
            }

        }
        // TimeKeeper.stopTask("Project.getDependentFilePaths()");
        return (String[])dependents.toArray(new String[0]);
    }

    /*
    * List paths of files which the specified file depends upon
    */
    public String[] getDependencies(FileHolder holder, String outputPath) throws FileSystemException {
        //TimeKeeper.startTask("Project.getDependencies()");
        String repositoryPath = holder.getFile().getPath();
        ArrayList paths = new ArrayList();
        Element output = getOutputElement(repositoryPath, outputPath);
        FileHolder.Include[] specs = getIncludes(holder, outputPath);
        for(int i = 0; i < specs.length; i++) {
            paths.add(specs[i].path);
        }
        if(output != null) {
            NodeList transforms = output.getElementsByTagName("transform");
            if(transforms != null && transforms.getLength() > 0) {
                for(int m = 0; m < transforms.getLength(); m++) {
                    Element e = (Element)transforms.item(m);
                    paths.add(e.getAttribute("source"));
                }
            }
        }
        String[] deps = (String[])paths.toArray(new String[0]);
        // TimeKeeper.stopTask("Project.getDependencies()");
        return deps;
    }

    public Date getLastModifiedDependency(FileHolder holder, String outputPath, String requestID) throws FileSystemException {
        //TimeKeeper.startTask("getLastModifiedDependency");
        String[] deps = getDependencies(holder, outputPath);
        Date last = null;
        for(int j = 0; j < deps.length; j++) {
            Date depDate = getLastModified(deps[j], requestID);
            if((last == null) || depDate.after(last)) {
                last = depDate;
            }
        }
        //TimeKeeper.stopTask("getLastModifiedDependency");
        return last;
    }

    public void processFileHolder(FileHolder holder, String requestID) throws FileSystemException {
        // TimeKeeper.startTask("processFileHolder");
        String rp = holder.getReplacementPath();
        if(rp != null) {
            FileHolder replace = getFileHolder(rp, requestID);
            Date rd = replace.lastModified;
            File rf = replace.getFile();
            File rFile = new File(holder.getFile().getPath(), new FileDataLoader(rf),
                    new FileMetaDataLoader(rf), rf.getCreator(), rf.getDate(), holder.getFile().getEdition());
            holder.setFile(rFile);
            if((holder.lastModified == null) || rd.after(holder.lastModified)) {
                holder.lastModified = rd;
            }
        }
        // TimeKeeper.stopTask("processFileHolder");
    }

    public void setFileHolder(FileHolder holder, String requester) {
        String key = requester + "::" + holder.getFile().getPath();
        timedFileCache.put(key, holder);
    }

    /*
     *  The purpose of this method is to centralize the logic of resolving
     *  a path into a File object. Paths pointing to resources outside the
     *  repository will be resolved first through the catalog resolver,
     *  failing which they will be resolved as standard URLs.
     *
     *
     * @parameter String location
     *    either the absolute path of the file in the repository or a URL
     *    from which to retrieve data.
     *
     *  @return File object
     */
    public FileHolder getFileHolder(String location, String requester) throws FileSystemException {
        //TimeKeeper.startTask("getFileHolder");
        //Map fileCache = (Map)cacheMaker.get();
        IFileSystemManager manager = getRepositoryManager();
        FileHolder holder = new FileHolder(this);
        holder.requester = requester;
        if(requester != null) {
            //TimeKeeper.startTask("getFileHolder.check cache");
            String key = requester + "::" + location;
            FileHolder cached = (FileHolder)timedFileCache.get(key);
            if(cached == null) {
                cached = (FileHolder)fileCache.get(key);
                if(cached == null) {
                    //TimeKeeper.startTask("newFileHolder");
                    fileCache.put(key, holder);
                }
                else {
                    //System.out.println("Using cached fileholder for "+location);
                    holder = cached;
                }
            }
            else {
                holder = cached;
            }
            //TimeKeeper.stopTask("getFileHolder.check cache");
        }

        if(holder.loaded == null) {
            //TimeKeeper.startTask("loadFileHolder");
            try {
                Date lm = manager.getLastModified(location);
                holder.lastModified = lm;
                holder.setFile(manager.getFile(location));
                holder.requester = requester;
                processFileHolder(holder, requester);
            }
            catch(FileSystemException fe) {
                if(!(fe.getType().equals(fe.X_FILE_NOT_FOUND))) {
                    throw fe;
                }
                IDataLoader dataloader = null;
                URL source = resolver.resolveExternalURL(location);
                if(source != null) {
                    long l = 0;
                    try {
                        l = source.openConnection().getLastModified();
                        if(l == 0) {
                            java.io.File iofile = new java.io.File(source.getPath());
                            l = iofile.lastModified();
                        }
                    }
                    catch(Exception e) {
                    }
                    if(l > 0) {
                        holder.lastModified = new Date(l);
                        // System.out.println("URL "+source.toRFC2426String()+" last modified "+holder.lastModified.toRFC2426String());
                    }
                    else {
                        holder.lastModified = new Date();//System.currentTimeMillis() - refreshMillis);
                        //System.out.println("URL "+source.toRFC2426String()+" last modified unknown");
                    }
                }
                else {
                    throw new FileSystemException(FileSystemException.X_FILE_NOT_FOUND, location);
                }
                dataloader = new URLDataLoader(source);
                holder.setFile(new File(location, dataloader, new NewFileMetaDataLoader(location), ""));
            }
            //TimeKeeper.stopTask("loadFileHolder");
        }

        //TimeKeeper.stopTask("getFileHolder");
        return holder;
    }

    public Date getLastModified(String location, String requester) throws FileSystemException {
        //TimeKeeper.startTask("getLastModified");
        Date date = getFileHolder(location, requester).lastModified;
        //TimeKeeper.stopTask("getLastModified");
        return date;
    }

    private Templates getTemplates(String source, String requestID) throws Exception {
        //TimeKeeper.startTask("getTemplates");
        FileHolder holder = getFileHolder(source, requestID);
        synchronized(holder) {
            if(holder.templates == null) {
                InputSource is = new InputSource(holder.getFileInputStream());
                holder.templates = DocumentFactory.getTemplates(is, resolver);
            }
        }
        //TimeKeeper.stopTask("getTemplates");
        return holder.templates;
    }

    public IPublicationFilter[] getPublicationFilters(FileHolder holder, String outputPath, RequestTracker tracker) throws Exception {
        //TimeKeeper.startTask("getPublicationFilters");
        String requestID = tracker.getRequestID();
        ArrayList filters = new ArrayList();
        File input = holder.getFile();
        String repositoryPath = input.getPath();
        Element output = getOutputElement(repositoryPath, outputPath);

        if(output != null) {
            //IFileSystemManager repository = getRepositoryManager();
            //String cachekey = baseURL+":"+key+":"+((Element)output.getParentNode()).getAttribute("path")+":"+output.getAttribute("content-type");
            // process include filter
            //TimeKeeper.startTask("PrepareIncludes");
            XmlIncludeFilter xif = (XmlIncludeFilter)xifilter.clone();
            xif.setProject(this);
            xif.setRequestID(requestID);
            FileHolder.Include[] includes = getIncludes(holder, outputPath);
            if(includes.length > 0) {
                xif.addIncludes(includes);
                //addIncludesToFilter(repositoryPath,includes,xif);
                filters.add(xif);
            }
            NodeList fl = output.getElementsByTagName("filter");
            for(int i = 0; i < fl.getLength(); i++) {
                Element f = (Element)fl.item(i);
                String key = f.getAttribute("key");
                try {
                    IPublicationFilter fi = PublicationFilterFactory.getFilter(key);
                    if(fi != null) {
                        NodeList pn = f.getElementsByTagName("with-param");
                        Map parms = new HashMap();
                        for(int x=0;x<pn.getLength();x++){
                            Element p = (Element)pn.item(x);
                            String name = p.getAttribute("name");
                            if(name!=null && name.trim().length()>0){
                                parms.put(name,p.getAttribute("value"));
                            }
                        }
                        fi.setParameters(parms);
                        filters.add(fi);
                    }
                }
                catch(Exception e) {
                    LogService.log(LogService.ERROR, e);
                }
            }
            //TimeKeeper.stopTask("PrepareIncludes");
            // process xsl filter
            //TimeKeeper.startTask("PrepareXSL");
            NodeList transforms = output.getElementsByTagName("transform");
            if(transforms.getLength() == 1) {
                if(!filters.contains(xif)) {
                    // we want all files to be wrapped for transformation
                    filters.add(xif);
                }
                XslTransformFilter pf = (XslTransformFilter)xtfilter.clone();

                Properties props = new Properties();
                NodeList parms = transforms.item(0).getChildNodes();
                for(int p = 0; p < parms.getLength(); p++) {
                    if(parms.item(p).getNodeType() == parms.item(p).ELEMENT_NODE) {
                        Element e = (Element)parms.item(p);
                        if(e.getTagName().equals("with-param")) {
                            props.setProperty(e.getAttribute("name"), e.getAttribute("value"));
                        }
                        else if(e.getTagName().equals("with-fileproperty")) {
                            props.setProperty("with-fileproperty", e.getAttribute("name"));
                        }
                        else {
                            props.setProperty(e.getTagName(), e.getAttribute("as"));
                        }
                    }
                }
                String source = ((Element)transforms.item(0)).getAttribute("source");
                //Transformer trans = getTransformer(source,requestID);
                //File sf = getRepositoryManager().getFile(source);
                RequestTracker.Errors listener = tracker.getErrorHandler(repositoryPath);
                //InputSource is = new InputSource(new java.io.ByteArrayInputStream(sf.getDataBytes()));
                pf.initialize(getTemplates(source, requestID), resolver, listener, props, getRelativeBasePath(outputPath), output.getAttribute("content-type"), tracker.getMetrics().getMetric(source));

                filters.add(pf);
            }
            //TimeKeeper.stopTask("PrepareXSL");
        }
        //TimeKeeper.stopTask("getPublicationFilters");
        return (IPublicationFilter[])filters.toArray(new IPublicationFilter[0]);
    }

    private static String getRelativeBasePath(String outputPath) {
        StringBuffer rbp = new StringBuffer(".");
        String s = popLastNode(outputPath);
        if(s.length() > 0) {
            rbp.append(".");
            s = popLastNode(s);
        }
        while(s.length() > 0) {
            rbp.append("/..");
            s = popLastNode(s);
        }
        return rbp.toString();
    }

    private static String popLastNode(String path) {
        return path.substring(0, path.lastIndexOf(SEPARATOR));
    }

    private static class IncludeSource {
        String source;
        int dataMode;
        int metadataMode;
    }

    protected String resolveRelativePath(String base, String relativePath) throws FileSystemException {
        //TimeKeeper.startTask("Project.resolveRelativePath()");
        if(!relativePath.startsWith(SEPARATOR)) {
            String orig = getRepositoryManager().getParentDirectory(base);
            while(relativePath.indexOf("../") == 0) {
                relativePath = relativePath.substring(3);
                orig = orig.substring(0, (orig.substring(0, orig.length() - 1)).lastIndexOf(SEPARATOR) + 1);
            }
            relativePath = orig + relativePath;
        }
        //TimeKeeper.stopTask("Project.resolveRelativePath()");
        return relativePath;
    }

    protected static String resolveDependentDocType(String docTypePattern, String includePattern, String includeInstance) {
        if(!includePattern.startsWith(SEPARATOR)) {
            String basedir = docTypePattern.substring(0, docTypePattern.lastIndexOf(SEPARATOR) + 1);
            String filename = docTypePattern.substring(docTypePattern.lastIndexOf(SEPARATOR) + 1);
            String rbase = basedir;
            String remainder = "";
            while(includePattern.indexOf("../") == 0) {
                includePattern = includePattern.substring(3);
                remainder = rbase.substring((rbase.substring(0, rbase.length() - 1)).lastIndexOf(SEPARATOR) + 1) + remainder;
                rbase = rbase.substring(0, (rbase.substring(0, rbase.length() - 1)).lastIndexOf(SEPARATOR) + 1);
            }
            String[] arbase = splitPathElements(rbase);
            String[] inbase = splitPathElements(includeInstance);
            StringBuffer finalBase = new StringBuffer();
            boolean sharedRoot = true;
            for(int i = 0; i < arbase.length; i++) {
                if(i < (inbase.length - 1) && sharedRoot) {
                    if(arbase[i].startsWith("*")) {
                        finalBase.append(inbase[i]);
                    }
                    else if(arbase[i].equals(inbase[i])) {
                        finalBase.append(arbase[i]);
                    }
                    else {
                        sharedRoot = false;
                        finalBase.append(arbase[i]);
                    }
                }
                else {
                    finalBase.append(arbase[i]);
                }
            }
            finalBase.append(remainder).append(filename);
            return finalBase.toString();
        }
        else {
            return docTypePattern;
        }
    }

    protected FileHolder.Include[] getIncludes(FileHolder holder, String outputPath) throws FileSystemException {
        //TimeKeeper.startTask("getIncludes");
        FileHolder.Include[] includes = holder.getIncludes(outputPath);
        if(includes == null) {
            //synchronized(holder){
            //includes = holder.getIncludes(outputPath);
            //if(includes ==null){
            //TimeKeeper.startTask("getIncludes_sourcing");
            HashMap specs = new HashMap();
            ArrayList sources = new ArrayList();
            String repositoryPath = holder.getFile().getPath();

            Element output = getOutputElement(repositoryPath, outputPath);
            if(output != null) {
                NodeList incs = output.getElementsByTagName("include");
                if(incs != null && incs.getLength() > 0) {
                    for(int j = 0; j < incs.getLength(); j++) {
                        Element e = (Element)incs.item(j);
                        String metadata = e.getAttribute("metadata");
                        String data = e.getAttribute("data");
                        int metadataMode = xifilter.WITHOUT_METADATA;
                        int dataMode = xifilter.WITH_DATA;
                        if(metadata.equals("yes")) {
                            metadataMode = xifilter.WITH_METADATA;
                        }
                        if(data.equals("no")) {
                            dataMode = xifilter.WITHOUT_DATA;
                        }
                        String source = e.getAttribute("source");
                        IncludeSource ic = new IncludeSource();
                        ic.source = source;
                        ic.metadataMode = metadataMode;
                        ic.dataMode = dataMode;
                        sources.add(ic);
                    }
                }
            }


            Resource metadata = holder.getMetaData();
            if(metadata.hasProperty(CUCMS.includes)) {
                NodeIterator i = metadata.getProperty(CUCMS.includes).getBag().iterator();
                //boolean use = false;
                while(i.hasNext()) {
                    try {
                        IncludeSource ic = new IncludeSource();
                        Resource include = (Resource)i.nextNode();
                        if(include.hasProperty(CUCMS.location)) {
                            ic.source = include.getProperty(CUCMS.location).getString();
                            ic.dataMode = xifilter.WITHOUT_DATA;
                            ic.metadataMode = xifilter.WITHOUT_METADATA;
                            if(include.hasProperty(CUCMS.data, "yes")) {
                                ic.dataMode = xifilter.WITH_DATA;
                            }
                            if(include.hasProperty(CUCMS.metadata, "yes")) {
                                ic.metadataMode = xifilter.WITH_METADATA;
                            }
                            sources.add(ic);
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //TimeKeeper.stopTask("getIncludes_sourcing");
            //TimeKeeper.startTask("getIncludes_speccing");
            Iterator it = sources.iterator();
            while(it.hasNext()) {
                IncludeSource include = (IncludeSource)it.next();
                String[] incs = new String[0];
                //TimeKeeper.startTask("getIncludes_speccing_paths");
                URL url = resolver.resolveExternalURL(include.source);
                if(url != null) {
                    //System.out.println(include.source+" is a URL: "+url.toExternalForm());
                    incs = new String[]{include.source};
                }
                else {
                    //System.out.println(include.source+" is not a URL");
                    // this is not a URL, so proceed with internal resolution
                    //TimeKeeper.startTask("getIncludes_speccing_paths_resolving");
                    String source = resolveRelativePath(repositoryPath, include.source);
                    //TimeKeeper.stopTask("getIncludes_speccing_paths_resolving");
                    if(source.startsWith(SEPARATOR)) {
                        source = source.substring(1);
                    }
                    String cacheKey = new StringBuffer(source).append(":::").append(holder.requester).toString();
                    incs = (String[])includePathCache.get(cacheKey);
                    if(incs == null) {
                        Stack startPath = new Stack();
                        startPath.push(SEPARATOR);
                        //TimeKeeper.startTask("getIncludes_speccing_paths_recursing");
                        incs = getFilePathsRecursive(startPath, source);
                        //TimeKeeper.stopTask("getIncludes_speccing_paths_recursing");
                        includePathCache.put(cacheKey, incs);
                    }
                    //LogService.instance().log(LogService.DEBUG,"Processing "+paths.length+" includes for file "+file.getPath()+" and source "+source);
                }
                //TimeKeeper.stopTask("getIncludes_speccing_paths");
                for(int k = 0; k < incs.length; k++) {
                    FileHolder.Include spec = (FileHolder.Include)specs.get(incs[k]);
                    if(spec == null) {
                        spec = new FileHolder.Include();
                        spec.dataMode = include.dataMode;
                        spec.metadataMode = include.metadataMode;
                        spec.path = incs[k];
                    }
                    else {
                        if(include.dataMode == XmlIncludeFilter.WITH_DATA) {
                            spec.dataMode = include.dataMode;
                        }
                        if(include.metadataMode == XmlIncludeFilter.WITH_METADATA) {
                            spec.metadataMode = include.metadataMode;
                        }
                    }
                    specs.put(incs[k], spec);
                }
            }

            includes = (FileHolder.Include[])specs.values().toArray(new FileHolder.Include[0]);
            holder.setIncludes(outputPath, includes);
            //TimeKeeper.stopTask("getIncludes_speccing");
            //}
            //}
        }
        //TimeKeeper.stopTask("getIncludes");
        return includes;
    }

    protected String[] getFilePathsRecursive(Stack startpaths, String endpath) throws FileSystemException {
        // look for all files under the starting directories that fulfil the endpath,
        // including wildcards.  Pass in single element stack with "/" to search whole
        // repository.
        ArrayList rfiles = new ArrayList();
        Stack deepStack = new Stack();
        String nextDir = endpath.substring(0, endpath.indexOf(SEPARATOR) + 1);
        String newEndPath = endpath.substring(endpath.indexOf(SEPARATOR) + 1);
        while(startpaths.size() > 0) {
            String path = (String)startpaths.pop();
            if(nextDir.equals("*" + SEPARATOR)) {
                // wildcard directory, add all beneath current path
                String[] npaths = getRepositoryManager().listDirectories(path);
                for(int i = 0; i < npaths.length; i++) {
                    //LogService.instance().log(LogService.DEBUG,"Pushing onto deep stack: "+npaths[i]);
                    deepStack.push(npaths[i]);
                }
            }
            else if(nextDir.length() == 0) {
                if(endpath.startsWith("*")) {
                    // wildcard, grab entire file listing of current directory path
                    String[] fpaths = getRepositoryManager().listFiles(path);
                    for(int i = 0; i < fpaths.length; i++) {
                        rfiles.add(fpaths[i]);
                        //LogService.instance().log(LogService.DEBUG,"returning as include: "+fpaths[i]);
                    }
                }
                else {
                    //endPath is now a file, so check if it exists under the current path
                    // and add it to the return stack if it does
                    if(getRepositoryManager().isFile(path + endpath)) {
                        rfiles.add(path + endpath);
                        //LogService.instance().log(LogService.DEBUG,"returning as include: "+path+endpath);
                    }
                }
            }
            else {
                // nextDir is a named directory, check if it exists
                if(getRepositoryManager().isDirectory(path + nextDir)) {
                    deepStack.push(path + nextDir);
                    //LogService.instance().log(LogService.DEBUG,"Pushing onto deep stack: "+path+nextDir);
                }
            }
        }
        if(deepStack.size() > 0) {
            String[] deepReturn = getFilePathsRecursive(deepStack, newEndPath);
            for(int i = 0; i < deepReturn.length; i++) {
                rfiles.add(deepReturn[i]);
            }
        }
        return (String[])rfiles.toArray(new String[0]);
    }

    public synchronized IFileSystemManager getRepositoryManager() throws FileSystemException {
        if((repManager == null) || (!repManager.isMounted())) {
            repManager = FileSystemFactory.mountFileSystemManager(getRepositoryFileSystem());
            updateRepositoryFileSystem();
        }
        return repManager;
    }

    public synchronized IFileSystemManager getPreviewManager() throws FileSystemException {
        if((preManager == null) || (!preManager.isMounted())) {
            preManager = FileSystemFactory.mountFileSystemManager(getPreviewFileSystem());
        }
        return preManager;
    }

    public synchronized IFileSystemManager getBuildManager() throws FileSystemException {
        if((bldManager == null) || (!bldManager.isMounted())) {
            bldManager = FileSystemFactory.mountFileSystemManager(getBuildFileSystem());
        }
        return bldManager;
    }

    public synchronized IFileSystemManager getPublishManager() throws FileSystemException {
        if((pubManager == null) || (!pubManager.isMounted())) {
            pubManager = FileSystemFactory.mountFileSystemManager(getPublishFileSystem());
        }
        return pubManager;
    }

    public String[] getPreviewURLs(String path) throws FileSystemException {
        return constructURLs(path, getBasePreviewURL());
    }

    public String[] getBuildURLs(String path) throws FileSystemException {
        return constructURLs(path, getBaseBuildURL());
    }

    public String[] getPublishURLs(String path) throws FileSystemException {
        return constructURLs(path, getBasePublishURL());
    }

    public String getBasePreviewURL() {
        return basePreviewURL;
    }

    public String getBaseBuildURL() {
        return baseBuildURL;
    }

    public String getBasePublishURL() {
        return basePublishURL;
    }

    protected String[] constructURLs(String path, String base) throws FileSystemException {
        String[] ot = getOutputPaths(path);
        String[] urls = new String[ot.length];
        for(int i = 0; i < urls.length; i++) {
            String fname = ot[i];
            if(fname.indexOf(SEPARATOR) == 0) {
                fname = fname.substring(SEPARATOR.length());
            }
            urls[i] = base + fname;
        }
        return urls;
    }


    protected NodeList getPathOutputNodes(String path) {
        NodeList output = null;
        Element e = getElementForXmlPath(path);
        if(e != null) {
            output = e.getElementsByTagName("output");
        }
        return output;
    }

    public String getOutputElementID(String repositoryPath, String outputPath) {
        String r = null;
        Element e = getOutputElement(repositoryPath, outputPath);
        if(e != null) {
            r = e.getAttribute(ID_ATTR);
        }
        return r;
    }

    protected Element getOutputElement(String repositoryPath, String contentType, String basedir) {
        //TimeKeeper.startTask("getOutputElement");
        Element output = null;
        //String repositoryBase = repositoryPath.substring(0,repositoryPath.lastIndexOf("."));
        NodeList nl = getPathOutputNodes(repositoryPath);
        if(nl != null) {
            for(int i = 0; i < nl.getLength(); i++) {
                Element temp = (Element)nl.item(i);
                if(ContentTypes.areEquivalent(contentType, temp.getAttribute("content-type"))) {
                    String bd = "/";
                    String obd = temp.getAttribute("basedir");
                    if(obd != null && (obd.trim().length() > 0)) {
                        bd = obd;
                    }
                    if(bd.equals(basedir)) {
                        output = temp;
                        break;
                    }
                }
            }
        }
        //TimeKeeper.stopTask("getOutputElement");
        return output;
    }

    protected Element getOutputElement(String repositoryPath, String outputPath) {
        //TimeKeeper.startTask("getOutputElement");
        Element output = null;
        String contentType = ContentTypes.getContentType(outputPath);
        String repositoryBase = repositoryPath.substring(0, repositoryPath.lastIndexOf("."));
        NodeList nl = getPathOutputNodes(repositoryPath);
        if(nl != null) {
            for(int i = 0; i < nl.getLength(); i++) {
                Element temp = (Element)nl.item(i);
                if(ContentTypes.areEquivalent(contentType, temp.getAttribute("content-type"))) {
                    String baseDir = "";
                    String obd = temp.getAttribute("basedir");
                    if(obd != null && (obd.trim().length() > 0)) {
                        baseDir = obd.substring(0, obd.lastIndexOf(SEPARATOR));
                    }
                    if(outputPath.startsWith(baseDir + repositoryBase)) {
                        output = temp;
                        break;
                    }
                }
            }
        }
        //TimeKeeper.stopTask("getOutputElement");
        return output;
    }

    protected void addRequiredDirectories(IFileSystemManager fsm, String path, ArrayList toList) throws FileSystemException {
        String ppath = fsm.getParentDirectory(path);
        if(!ppath.equals(SEPARATOR)) {
            addRequiredDirectories(fsm, ppath, toList);
        }
        if((path.indexOf("*") == -1) && (path.indexOf(".") == -1)) {
            toList.add(path);
        }
    }

    protected synchronized String[] listRequiredDirectories() throws FileSystemException {
        if(requiredDirs == null) {
            IFileSystemManager ifsm = getRepositoryManager();
            ArrayList list = new ArrayList();
            list.add(SEPARATOR);
            NodeList dt = def.getElementsByTagName("xml-doctype");
            for(int i = 0; i < dt.getLength(); i++) {
                Element e = (Element)dt.item(i);
                String path = e.getAttribute("path");
                addRequiredDirectories(ifsm, path, list);
            }
            NodeList rd = def.getElementsByTagName("resource-directory");
            for(int i = 0; i < rd.getLength(); i++) {
                Element e = (Element)rd.item(i);
                String path = e.getAttribute("path");
                addRequiredDirectories(ifsm, path, list);
            }
            NodeList xd = def.getElementsByTagName("xml-doc");
            for(int i = 0; i < xd.getLength(); i++) {
                Element e = (Element)xd.item(i);
                String fpath = e.getAttribute("path");
                addRequiredDirectories(ifsm, fpath, list);
            }
            requiredDirs = (String[])list.toArray(new String[0]);
        }
        return requiredDirs;
    }

    protected synchronized String[] listRequiredFiles() throws FileSystemException {
        if(requiredFiles == null) {
            //IFileSystemManager ifsm = getRepositoryManager();
            ArrayList list = new ArrayList();
            NodeList xd = def.getElementsByTagName("xml-doc");
            for(int i = 0; i < xd.getLength(); i++) {
                Element e = (Element)xd.item(i);
                String fpath = e.getAttribute("path");
                list.add(fpath);
            }
            requiredFiles = (String[])list.toArray(new String[0]);
        }
        return requiredFiles;
    }

    public boolean isRequired(String path) throws FileSystemException {
        String[] dirs = listRequiredDirectories();
        for(int i = 0; i < dirs.length; i++) {
            if(dirs[i].equals(path)) {
                return true;
            }
        }
        String[] files = listRequiredFiles();
        for(int j = 0; j < files.length; j++) {
            if(files[j].equals(path)) {
                return true;
            }
        }
        return false;
    }

    protected void updateRepositoryFileSystem() throws FileSystemException {

        IFileSystemManager ifsm = getRepositoryManager();
        String[] requiredDirs = listRequiredDirectories();
        for(int i = 0; i < requiredDirs.length; i++) {
            if(!ifsm.exists(requiredDirs[i])) {
                ifsm.newDirectory(requiredDirs[i], getPermissionsOwnerToken());
            }
        }
        String[] requiredFiles = listRequiredFiles();
        for(int j = 0; j < requiredFiles.length; j++) {
            if(!ifsm.exists(requiredFiles[j])) {
                edu.columbia.ais.filesystem.File f = ifsm.newFile(requiredFiles[j], getPermissionsOwnerToken());
                String xdoc = makeNewXmlDocument(requiredFiles[j]);
                ByteArrayInputStream data = new ByteArrayInputStream(xdoc.getBytes());
                ifsm.lock(f.getPath(), getPermissionsOwnerToken(), ifsm.EXCLUSIVE_LOCK);
                ifsm.storeFile(f.getPath(), data, f.getMetaData(), getPermissionsOwnerToken(), true);
                ifsm.unlock(f.getPath(), getPermissionsOwnerToken());
            }
        }

    }

    public String[] getDefinedPatterns() {
        return definedPatterns;
    }

    public String getPatternLabel(String pattern) {
        String label = null;
        Element e = (Element)lookupPaths.get(pattern);
        if(e == null) {
            e = (Element)resourcePaths.get(pattern);
        }
        if(e != null) {
            label = e.getAttribute("label");
        }
        return label;
    }

    public String getPattern(String path) {
        String bestMatch = (String)pathPatternCache.get(path);
        if(bestMatch == null) {
            //old pattern resolver would fail if a resource fulfilled an xml pattern, but had
            // a more specific pattern itself
            String[] pathParts = splitPathElements(path);
            int wildcards = -1;
            int wildcarddepth = -1;
            boolean directory = path.endsWith("/");
            for(int i = 0; i < definedPatterns.length; i++) {
                String pattern = definedPatterns[i];
                String[] patternParts = definedPatternParts[i];
                if((patternParts.length == pathParts.length) || (patternParts.length > pathParts.length && directory)) {
                    boolean success = true;
                    int thisdepth = -1;
                    for(int j = 0; j < pathParts.length; j++) {
                        //LogService.instance().log(LogService.DEBUG,"comparing "+keyParts[i]+" with "+pathParts[i]);
                        if(!(patternParts[j].startsWith("*")) && !(patternParts[j].equals(pathParts[j]))) {
                            success = false;
                            break;
                        }
                        else if(patternParts[j].startsWith("*")) {
                            thisdepth = j;
                        }
                    }
                    if(success) {
                        String match = pattern;
                        if(patternParts.length != pathParts.length) {
                            StringBuffer buf = new StringBuffer();
                            for(int x = 0; x < pathParts.length; x++) {
                                buf.append(patternParts[x]);
                            }
                            match = buf.toString();
                        }
                        int count = countWildcards(match);
                        if((wildcards == -1) || (count < wildcards) || ((count == wildcards) && (thisdepth > wildcarddepth))) {
                            bestMatch = match;
                            wildcards = count;
                            wildcarddepth = thisdepth;
                            if(wildcards == 0) {
                                break;
                            }
                        }
                    }

                }
            }
            pathPatternCache.put(path, bestMatch);
        }

        return bestMatch;
    }

    public String getPatternOld(String path) {
        String r = null;
        Element e = getElementForXmlPath(path);
        if(e == null) {
            e = getElementForResourcePath(path);
        }
        if(e != null) {
            r = e.getAttribute("path");
        }
        else {
            //this could be a directory, so we need to establish a subpattern
            String[] dirParts = splitPathElements(path);
            boolean done = false;
            for(int i = 0; i < definedPatterns.length; i++) {
                String[] patParts = splitPathElements(definedPatterns[i]);
                if(patParts.length >= dirParts.length) {
                    for(int j = 0; j < dirParts.length; j++) {
                        if(patParts[j].equals(dirParts[j]) || patParts[j].equals("*/")) {
                            if(j == (dirParts.length - 1)) {
                                //we have a winner here.  But if this pattern node is a wildcard,
                                // we will want to continue the hunt.
                                StringBuffer buf = new StringBuffer();
                                for(int x = 0; x <= j; x++) {
                                    buf.append(patParts[x]);
                                }
                                r = buf.toString();
                                if(!patParts[j].equals("*/")) {
                                    done = true;
                                }
                            }
                        }
                        else {
                            break;
                        }
                    }
                }
                if(done) {
                    break;
                }
            }
        }
        //System.out.println("Pattern for "+path+" is "+r);
        return r;
    }

    public static boolean fulfilsPattern(String path, String pattern) {
        String[] pathParts = splitPathElements(path);
        String[] patternParts = splitPathElements(pattern);
        if(pathParts.length == patternParts.length) {
            for(int i = 0; i < patternParts.length; i++) {
                if(!pathParts[i].equals(patternParts[i]) && !patternParts[i].startsWith("*")) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    public String[] resolvePattern(String pattern, String directory, int maxDepth) throws FileSystemException {
        ArrayList matches = new ArrayList();
        if(pattern != null) {
            String filename = pattern.substring(pattern.lastIndexOf(SEPARATOR) + 1);
            String dirPattern = pattern.substring(0, pattern.lastIndexOf(SEPARATOR) + 1);
            int maxDirWildcards = 1;
            if(filename.startsWith("*")) {
                maxDirWildcards = 0;
            }
            String[] dirParts = splitPathElements(directory);
            String[] patParts = splitPathElements(dirPattern);
            if(dirParts.length <= patParts.length && (patParts.length <= (dirParts.length + maxDepth) || maxDepth < 0)) {
                String[] dirmatches = resolveDirectoryPatternInstances(dirPattern, directory, maxDirWildcards);
                IFileSystemManager repository = getRepositoryManager();
                for(int i = 0; i < dirmatches.length; i++) {
                    String file = dirmatches[i] + filename;
                    if(!repository.exists(file) && !(matches.contains(file))) {
                        matches.add(file);
                    }
                }
            }
        }
        return (String[])matches.toArray(new String[0]);
    }

    public boolean patternsMatch(String path1, String path2, boolean exact) {
        String pat1 = getPattern(path1);
        String pat2 = getPattern(path2);
        if(exact) {
            return pat1 == null ? false : pat1.equals(pat2);
        }
        String[] pat1parts = splitPathElements(pat1);
        String[] pat2parts = splitPathElements(pat2);
        for(int i = 0; i < pat1parts.length && i < pat2parts.length; i++) {
            if(!pat1parts[i].equals(pat2parts[i])) {
                return false;
            }
        }
        return true;
    }

    protected String[] resolveDirectoryPatternInstances(String dirPattern, String baseDir, int maxWildcards) throws FileSystemException {
        ArrayList matches = new ArrayList();
        String[] patParts = splitPathElements(dirPattern);
        String[] baseParts = splitPathElements(baseDir);
        int i = 0;
        boolean match = true;
        StringBuffer basis = new StringBuffer();
        for(i = 0; i < patParts.length && i < baseParts.length; i++) {
            if(!patParts[i].equals(baseParts[i]) && !patParts[i].startsWith("*")) {
                match = false;
                break;
            }
            else {
                basis.append(baseParts[i]);
            }
        }
        if(match) {
            String bs = basis.toString();
            if(patternsMatch(bs, dirPattern, true)) {
                matches.add(basis.toString());
            }
            else {
                Stack stack = new Stack();
                stack.push(basis.toString());
                IFileSystemManager rep = getRepositoryManager();
                for(int j = i; j < patParts.length; j++) {
                    boolean acceptAll = false;
                    int wildCount = countWildcards(patParts, j);
                    if(wildCount <= maxWildcards) {
                        acceptAll = true;
                    }
                    Stack newStack = new Stack();
                    while(stack.size() > 0) {
                        String dir = (String)stack.pop();
                        //check that the resolved pattern for this dir matches
                        if(patternsMatch(dir, dirPattern, false)) {
                            if(patParts[j].equals(wildDir)) {
                                if(acceptAll) {
                                    matches.add(constructPattern(dir, patParts, j));
                                }
                                try {
                                    String[] dirs = rep.listDirectories(dir);
                                    for(int k = 0; k < dirs.length; k++) {
                                        newStack.push(dirs[k]);
                                        if((wildCount <= (maxWildcards + 1)) && patternsMatch(dirs[k], dirPattern, false)) {
                                            matches.add(constructPattern(dirs[k], patParts, j + 1));
                                        }
                                    }
                                }
                                catch(FileSystemException fe) {
                                }
                            }
                            else {
                                String cd = dir + patParts[j];
                                if(rep.isDirectory(cd) || acceptAll) {
                                    newStack.push(cd);
                                    if(acceptAll && patternsMatch(cd, dirPattern, false)) {
                                        matches.add(constructPattern(cd, patParts, j + 1));
                                    }
                                }
                            }
                        }
                    }
                    stack = newStack;
                }
            }
        }

        return (String[])matches.toArray(new String[0]);
    }

    protected static String constructPattern(String baseDir, String[] pathParts, int index) {
        StringBuffer buf = new StringBuffer(baseDir);
        for(int i = index; i < pathParts.length; i++) {
            buf.append(pathParts[i]);
        }
        return buf.toString();
    }

    protected static int countWildcards(String[] pathParts, int fromIndex) {
        int count = 0;
        for(int i = fromIndex; i < pathParts.length; i++) {
            if(pathParts[i].startsWith("*")) {
                count++;
            }
        }
        return count;
    }


    public boolean hasOutput(String path) throws FileSystemException {
        IFileSystemManager repository = getRepositoryManager();
        boolean rval = false;
        if(repository.isFile(path)) {
            String[] output = getOutputPaths(path);
            if(output.length > 0) {
                rval = true;
            }
        }
        else if(repository.isDirectory(path)) {
            String[] dirParts = splitPathElements(path);
            for(int i = 0; i < definedPatterns.length; i++) {
                String[] patParts = splitPathElements(definedPatterns[i]);
                if(patParts.length > dirParts.length) {
                    boolean match = true;
                    for(int j = 0; j < dirParts.length; j++) {
                        if(!dirParts[j].equals(patParts[j]) && !patParts[j].startsWith("*")) {
                            match = false;
                            break;
                        }
                    }
                    if(match) {
                        Element el = getElementForResourcePath(definedPatterns[i]);
                        if(el != null && el.getAttribute("publish").equals("true")) {
                            return true;
                        }
                        else {
                            el = getElementForXmlPath(definedPatterns[i]);
                            if(el != null && (el.getElementsByTagName("output").getLength() > 0)) {
                                return true;
                            }
                        }
                    }
                }

            }
        }
        else {
            throw new FileSystemException(FileSystemException.X_FILE_NOT_FOUND, path);
        }
        return rval;
    }

    public String[] getContentTypesForPattern(String pattern) {
        ArrayList al = new ArrayList();
        Element el = getElementForResourcePath(pattern);
        if(el != null) {
            NodeList nl = el.getElementsByTagName("content");
            for(int i = 0; i < nl.getLength(); i++) {
                al.add(((Element)nl.item(i)).getAttribute("type"));
            }
        }
        else {
            el = getElementForXmlPath(pattern);
            if(el != null) {
                al.add("text/xml");
            }
        }
        return (String[])al.toArray(new String[0]);
    }

    public boolean ignoreDirectory(String dir) {
        boolean rval = false;
        for(int i = 0; i < ignoreDirs.length; i++) {
            if(ignoreDirs[i].equals(dir)) {
                rval = true;
                break;
            }
        }
        return rval;
    }

    public Resolver getResolver() {
        return resolver;
    }

    private class OutputPathBuilder {
        boolean useRepositoryPath;
        char[][] basedirs = new char[0][];
        char[][] extensions = new char[0][];

        private OutputPathBuilder(String pattern) {
            //TimeKeeper.startTask("Project.OutputPathsBuilder");
            if(pattern != null) {
                NodeList nl = getPathOutputNodes(pattern);
                if(nl != null) {
                    for(int i = 0; i < nl.getLength(); i++) {
                        Element output = (Element)nl.item(i);
                        String baseDir = "";
                        String obd = output.getAttribute("basedir");
                        if((obd != null) && (obd.trim().length() > 0)) {
                            baseDir = obd.substring(0, obd.lastIndexOf(SEPARATOR));
                        }
                        String extension = ContentTypes.getPreferredExtension(output.getAttribute("content-type"));
                        addOutput(baseDir, extension);
                    }
                }
                else {
                    Element e = getElementForResourcePath(pattern);
                    if(e != null && e.getAttribute("publish").equals("true")) {
                        useRepositoryPath = true;
                    }
                }
            }
            //TimeKeeper.stopTask("Project.OutputPathsBuilder");
        }

        private void addOutput(String basedir, String extension) {
            char[][] newbasedirs = new char[basedirs.length + 1][];
            char[][] newextensions = new char[extensions.length + 1][];
            for(int i = 0; i < basedirs.length; i++) {
                newbasedirs[i] = basedirs[i];
                newextensions[i] = extensions[i];
            }
            newbasedirs[basedirs.length] = basedir.toCharArray();
            newextensions[basedirs.length] = extension.toCharArray();
            basedirs = newbasedirs;
            extensions = newextensions;
        }

        protected String[] getOutputPaths(String repositoryPath) {
            if(useRepositoryPath) {
                return new String[]{repositoryPath};
            }
            String[] r = new String[basedirs.length];
            char[] repchars = repositoryPath.substring(0, repositoryPath.lastIndexOf(".") + 1).toCharArray();
            for(int i = 0; i < basedirs.length; i++) {
                char[] ca = new char[basedirs[i].length + repchars.length + extensions[i].length];
                System.arraycopy(basedirs[i], 0, ca, 0, basedirs[i].length);
                System.arraycopy(repchars, 0, ca, basedirs[i].length, repchars.length);
                System.arraycopy(extensions[i], 0, ca, basedirs[i].length + repchars.length, extensions[i].length);
                r[i] = new String(ca);
            }
            return r;
        }
    }

}
