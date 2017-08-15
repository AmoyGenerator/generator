/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.jet.internal.xpath.inspectors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

/**
 * Factory for XPath wrappers on attributes of IResource.
 *
 */
public class ResourceAttributeFactory {
	
	private static final class TeamPrivateMemberAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "teamPrivateMember"; //$NON-NLS-1$

		private TeamPrivateMemberAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().isTeamPrivateMember());
		}
	}

	private static final class ReadOnlyAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "readOnly"; //$NON-NLS-1$

		private ReadOnlyAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().isPhantom());
		}
	}

	private static final class PhantomAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "phantom"; //$NON-NLS-1$

		private PhantomAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().isPhantom());
		}
	}

	private static final class LinkedAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "linked"; //$NON-NLS-1$

		private LinkedAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().isLinked());
		}
	}

	private static final class DerivedAttributed extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "derived"; //$NON-NLS-1$

		private DerivedAttributed(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().isDerived());
		}
	}

	private static final class RawLocationURIAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "rawLocationURI"; //$NON-NLS-1$

		private RawLocationURIAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getRawLocationURI() == null ? getResource()
					.getLocationURI().toString()
					: getResource().getRawLocationURI().toString();
		}
	}

	private static final class RawLocationAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "rawLocation"; //$NON-NLS-1$

		private RawLocationAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getRawLocation() == null ? getResource()
					.getLocation().toString() : getResource()
					.getRawLocation().toString();
		}
	}

	private static final class ProjectRelativePathAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "projectRelativePath"; //$NON-NLS-1$

		private ProjectRelativePathAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getProjectRelativePath().toString();
		}
	}

	private static final class ProjectAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "project"; //$NON-NLS-1$

		private ProjectAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			IProject containingProject = getResource().getProject();
            return containingProject != null ? containingProject.getName() : ""; //$NON-NLS-1$
		}
	}

	private static final class NameAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

		private NameAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getName();
		}
	}

	private static final class ModificationStampAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "modificationStamp"; //$NON-NLS-1$

		private ModificationStampAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().getModificationStamp());
		}
	}

	private static final class LocationURIAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "locationURI"; //$NON-NLS-1$

		private LocationURIAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getLocationURI().toString();
		}
	}

	private static final class LocationAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "location"; //$NON-NLS-1$

		private LocationAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getLocation().toString();
		}
	}

	private static final class LocalTimeStampAttribute extends AbstractResourceAttribute {
		private static final String ATTRIBUTE_NAME = "localTimeStamp"; //$NON-NLS-1$

		private LocalTimeStampAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return String.valueOf(getResource().getLocalTimeStamp());
		}
	}

	private static final class FullPathAttribute extends AbstractResourceAttribute {
		public static final String ATTRIBUTE_NAME = "fullPath"; //$NON-NLS-1$

		private FullPathAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			return getResource().getFullPath().toString();
		}
	}

	private static final class FileExtensionAttribute extends AbstractResourceAttribute {
		
		public static final String ATTRIBUTE_NAME = "fileExtension"; //$NON-NLS-1$
		
		private FileExtensionAttribute(IResource parent) {
			super(parent, ATTRIBUTE_NAME);
		}

		public String getStringValue() {
			String fileExtension = getResource().getFileExtension();
      return fileExtension == null ? "" : fileExtension; //$NON-NLS-1$
		}
	}

	public static final int FILE_EXTENSION = 1;
	public static final int FULL_PATH = 2;
	public static final int LOCAL_TIMESTAMP = 3;
	public static final int LOCATION = 4;
	public static final int LOCATION_URI = 5;
	public static final int MODIFICATION_STAMP = 6;
	public static final int NAME = 7;
	public static final int PROJECT = 8;
	public static final int PROJECT_RELATIVE_PATH = 9;
	public static final int RAW_LOCATION = 10;
	public static final int RAW_LOCATION_URI = 11;
	public static final int DERIVED = 12;
	public static final int LINKED = 13;
	public static final int PHANTOM = 14;
	public static final int READ_ONLY = 15;
	public static final int TEAM_PRIVATE_MEMBER = 16;
	
	private static final Map attributeKindByName = new HashMap();
	static {
		attributeKindByName.put(FileExtensionAttribute.ATTRIBUTE_NAME, new Integer(FILE_EXTENSION)); 
		attributeKindByName.put(FullPathAttribute.ATTRIBUTE_NAME, new Integer(FULL_PATH)); 
		attributeKindByName.put(LocalTimeStampAttribute.ATTRIBUTE_NAME, new Integer(LOCAL_TIMESTAMP)); 
		attributeKindByName.put(LocationAttribute.ATTRIBUTE_NAME, new Integer(LOCATION)); 
		attributeKindByName.put(LocationURIAttribute.ATTRIBUTE_NAME, new Integer(LOCATION_URI)); 
		attributeKindByName.put(ModificationStampAttribute.ATTRIBUTE_NAME, new Integer(MODIFICATION_STAMP)); 
		attributeKindByName.put(NameAttribute.ATTRIBUTE_NAME, new Integer(NAME)); 
		attributeKindByName.put(ProjectAttribute.ATTRIBUTE_NAME, new Integer(PROJECT)); 
		attributeKindByName.put(ProjectRelativePathAttribute.ATTRIBUTE_NAME, new Integer(PROJECT_RELATIVE_PATH)); 
		attributeKindByName.put(RawLocationAttribute.ATTRIBUTE_NAME, new Integer(RAW_LOCATION)); 
		attributeKindByName.put(RawLocationURIAttribute.ATTRIBUTE_NAME, new Integer(RAW_LOCATION_URI)); 
		attributeKindByName.put(DerivedAttributed.ATTRIBUTE_NAME, new Integer(DERIVED)); 
		attributeKindByName.put(LinkedAttribute.ATTRIBUTE_NAME, new Integer(LINKED)); 
		attributeKindByName.put(PhantomAttribute.ATTRIBUTE_NAME, new Integer(PHANTOM)); 
		attributeKindByName.put(ReadOnlyAttribute.ATTRIBUTE_NAME, new Integer(READ_ONLY)); 
		attributeKindByName.put(TeamPrivateMemberAttribute.ATTRIBUTE_NAME, new Integer(TEAM_PRIVATE_MEMBER)); 
	}
	
	public IWrappedAttribute create(int kind, IResource parent) {
		switch(kind) {
		case FILE_EXTENSION:
			return new FileExtensionAttribute(parent);
		case FULL_PATH:
			return new FullPathAttribute(parent);
		case LOCAL_TIMESTAMP:
			return new LocalTimeStampAttribute(parent);
		case LOCATION:
			return new LocationAttribute(parent);
		case LOCATION_URI:
			return new LocationURIAttribute(parent);
		case MODIFICATION_STAMP:
			return new ModificationStampAttribute(parent);
		case NAME:
			return new NameAttribute(parent);
		case PROJECT:
			return new ProjectAttribute(parent);
		case PROJECT_RELATIVE_PATH:
			return new ProjectRelativePathAttribute(parent);
		case RAW_LOCATION:
			return new RawLocationAttribute(parent);
		case RAW_LOCATION_URI:
			return new RawLocationURIAttribute(parent);
		case DERIVED:
			return new DerivedAttributed(parent);
		case LINKED:
			return new LinkedAttribute(parent);
		case PHANTOM:
			return new PhantomAttribute(parent);
		case READ_ONLY:
			return new ReadOnlyAttribute(parent);
		case TEAM_PRIVATE_MEMBER:
			return new TeamPrivateMemberAttribute(parent);
		}
		return null;
	}

	public IWrappedAttribute create(String name, IResource resource) {
		Integer kind = (Integer) attributeKindByName.get(name);
		if(kind != null) {
			return create(kind.intValue(), resource);
		} else {
			return null;
		}
	}
}
