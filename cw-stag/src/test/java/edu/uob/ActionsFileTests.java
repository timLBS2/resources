package edu.uob;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ActionsFileTests {

  // Test to make sure that the basic actions file is readable
  @Test
  void testBasicActionsFileIsReadable() {
      try {
          DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();// Create a new document builder
          Document document = builder.parse("config" + File.separator + "basic-actions.xml");// Get the document from the builder
          Element root = document.getDocumentElement(); //Get the root element
          NodeList actions = root.getChildNodes();     // Get the list of actions
          // Get the first action (only the odd items are actually actions - 1, 3, 5 etc.)
          Element firstAction = (Element)actions.item(1);
          Element triggers = (Element)firstAction.getElementsByTagName("triggers").item(0); // Get the triggers element
          // Get the first trigger phrase
          String firstTriggerPhrase = triggers.getElementsByTagName("keyphrase").item(0).getTextContent();// Get "open"
      } catch(ParserConfigurationException pce) {
          fail("ParserConfigurationException was thrown when attempting to read basic actions file");
      } catch(SAXException saxe) {
          fail("SAXException was thrown when attempting to read basic actions file");
      } catch(IOException ioe) {
          fail("IOException was thrown when attempting to read basic actions file");
      }
  }

}
