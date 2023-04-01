package com.quizcreator.app.services.projectLocation;

import com.quizcreator.app.tools.FolderTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProjectLocationServiceImpl implements ProjectLocationService {
    private static Logger log = LogManager.getLogger(ProjectLocationServiceImpl.class);
    private final String appDataFolderLocation;
    private final FolderTools folderTools;

    public ProjectLocationServiceImpl(
            final FolderTools folderTools
    ) {
        this.appDataFolderLocation = folderTools.getAppDataFolderLocation();
        this.folderTools = folderTools;
    }

    private final String filename = "/recentlyEdited.xml";
    private final String elementName = "recentlyEdited";
    private final String locationAttribute = "location";
    private final String projectNameAttribute = "name";
    private final String rootElementName = "EasyQuizCreator";

    private HashMap<String,String> projectLocations;

    @Override
    public HashMap<String, String> loadProjectLocationsFromDisk() {
        projectLocations = new HashMap<>();
        File file = new File(appDataFolderLocation.concat(filename));
        if (!file.exists()) {
            return projectLocations;
        }
        SAXBuilder builder = new SAXBuilder();
        try {
            // XML to Java-readable Document by JDom SAXBuilder
            Document doc = builder.build(file);
            Element root = doc.getRootElement();

            // Parse the XML file and map content into internal structures
            final List<Element> list = root.getChild(elementName).getChildren();
            for (final Element e : list) {
                if(folderTools.isLocationPresent(e.getAttributeValue(locationAttribute))) {
                    projectLocations.put(e.getAttributeValue(locationAttribute), e.getAttributeValue(projectNameAttribute));
                }
            }
            log.debug("project locations successfully loaded");
        } catch (IOException | JDOMException io) {
            io.printStackTrace();
        }
        return projectLocations;
    }

    @Override
    public void addAndSaveOnDisk(final String projectLocation, final String projectName) {
        final var xmlLocation = appDataFolderLocation.concat(filename);
        final var document = new Document();
        final var root = new Element(rootElementName);
        projectLocations.put(projectLocation, projectName);
        final var projectsElement = new Element(elementName);
        String[] locations = projectLocations.keySet().toArray(new String[0]);
        for (String location : locations) {
            Element project = new Element("project");
            project.setAttribute(new Attribute(projectNameAttribute, projectLocations.get(location)));
            project.setAttribute(locationAttribute, location);
            projectsElement.addContent(project);
        }
        root.addContent(projectsElement);
        document.setRootElement(root);
        final var xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try {
            // Falls AppData Ordner nicht bereits existiert, dann erstellen
            File f = new File(appDataFolderLocation);
            if(!f.exists()) {
                f.mkdirs();
            }
            // oben aufgebautes doc-Dokument als XML Datei an der xmlLocation speichern
            final var xmlOutput =  new FileOutputStream(new File(xmlLocation));
            xmlOutputter.output(document, xmlOutput);
            xmlOutput.close();
            log.debug("Successfully saved project locations on disk");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearProjectLocationsOnDisk() {
        final var xmlLocation = appDataFolderLocation.concat(filename);
        File f = new File(appDataFolderLocation);
        if(!f.exists()) {
            return;
        }
        final var document = new Document();
        final var root = new Element(rootElementName);
        final var projectsElement = new Element(elementName);
        root.addContent(projectsElement);
        document.setRootElement(root);
        final var xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try {
            // oben aufgebautes doc-Dokument als XML Datei an der xmlLocation speichern
            final var xmlOutput =  new FileOutputStream(new File(xmlLocation));
            xmlOutputter.output(document, xmlOutput);
            xmlOutput.close();
            log.debug("Successfully cleared project locations on disk");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
