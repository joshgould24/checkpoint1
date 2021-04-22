import java.io.*;               // import input-output

import javax.xml.XMLConstants;
import javax.xml.parsers.*;         // import parsers
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;           // import XPath
import javax.xml.validation.*;      // import validators
import javax.xml.transform.*;       // import DOM source classes

//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import org.w3c.dom.*;               // import DOM

/**
  DOM handler to read XML information, to create this, and to print it.

  @author   CSCU9T4, University of Stirling
  @version  11/03/20
*/
public class DOMMenu {

  /** Document builder */
  private static DocumentBuilder builder = null;

  /** XML document */
  private static Document document = null;

  /** XPath expression */
  private static XPath path = null;

  /** XML Schema for validation */
  private static Schema schema = null;

  /*----------------------------- General Methods ----------------------------*/

  /**
    Main program to call DOM parser.

    @param args         command-line arguments
  */
  public static void main(String[] args)  {
    // load XML file into "document"
    loadDocument(args[0]);
    
    // validates 
    validateDocument(args[0]);
    
    // print staff.xml using DOM methods and XPath queries
    printNodes();
  }

  /**
    Set global document by reading the given file.

    @param filename     XML file to read
  */
  private static void loadDocument(String filename) {
    try {
      // create a document builder
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builder = builderFactory.newDocumentBuilder();

      // create an XPath expression
      XPathFactory xpathFactory = XPathFactory.newInstance();
      path = xpathFactory.newXPath();

      // parse the document for later searching
      document = builder.parse(new File(filename));
    }
    catch (Exception exception) {
      System.err.println("could not load document " + exception);
    }
  }

  /*-------------------------- DOM and XPath Methods -------------------------*/
  /**
   Validate the document given a schema file
   @param filename XSD file to read
  */
  private static Boolean validateDocument(String filename)  {
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File(filename));
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
      return true;
    } catch (Exception e){
      System.err.println(e);
      System.err.println("Could not load schema or validate");
      return false;
    }
  }
  /**
    Print nodes using DOM methods and XPath queries.
  */
  private static void printNodes() {
	  // makes a nodelist of all the nodes with the specified tag name
	  NodeList itemList = document.getElementsByTagName("item");
	
	  // iterates through through node list
	  for(int i = 0; i < itemList.getLength(); i++) {
		  Node node = itemList.item(i);
		  
		  //if the node is an element
		  if(node.getNodeType()==Node.ELEMENT_NODE) {
			  // instantiates node in nodelist
			  Element item = (Element) node;
			  
			  //gets the string in the category tag
			  String category = item.getAttribute("category");
			  
			  // gets the info 
			  NodeList info = item.getChildNodes();
			  
			  for (int j = 0; j < info.getLength(); j++) {
				  Node n = info.item(j);
				  
				  if(n.getNodeType()==Node.ELEMENT_NODE) {
					  Element name = (Element) n;
					  System.out.println(category + " " + name.getTagName() + ": " + name.getTextContent());
				  }
			  }
		  }
		  System.out.println();
	  }
  }

  /**
    Get result of XPath query.

    @param query        XPath query
    @return         result of query
  */
  private static String query(String query) {
    String result = "";
    try {
      result = path.evaluate(query, document);
    }
    catch (Exception exception) {
      System.err.println("could not perform query - " + exception);
    }
    return(result);
  }
}
