package org.eclipse.jet.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Xml {

	private String name;
	private String relation;

	/** 路径 */
	private String path;

	private Group parent;

	public Xml(String name, String relation, String path) {
		super();
		this.name = name;
		this.relation = relation;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Group getParent() {
		return parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}

	public Document getDocument()
	{
		Document document = null;
		try
		{
			// 创建Document对象
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();	

			Element element = document.createElement("xml");
			document.appendChild(element);
			element.setAttribute("name", name);
			element.setAttribute("relation", relation);
			Document xmlDocument = importDocument(path);
			Node xmlNode = xmlDocument.getDocumentElement();
			Node importNode = document.importNode(xmlNode, true);
			element.appendChild(importNode);
		}catch (Exception e){
			
		} 
		return document;
	}

	private Document importDocument(String path) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);//命名空间支持
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new EntityResolver()
	    {
	      public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
	      {
	        return new InputSource(new ByteArrayInputStream(systemId.getBytes())); 
	      }
	    });
		String uri = path;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(path));
		if(resource != null){
			uri = resource.getLocation().toOSString();
		}
		return builder.parse(uri);
	}

}
