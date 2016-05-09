package br.com.devcase.treinamento.kalunga;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.*;

public class KalungaApplication {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		String preXml2 = restTemplate.getForObject("http://www.pbkids.com.br/nossas-lojas/dados", String.class);

		String preXml = "<html><head/><body><div class=\"main-content\">"
				+ "<div class=\"sub-content\">Sub content here</div>" + "<p>This is a randomic paragraph</p><br>"
				+ "Main content here </div>" + "<div class=\"test\">" + "<root>\n" + "<child>\n"
				+ "<subchild>.....</subchild>\n" + "</child>\n" + "<child>\n" + "<subchild>.....</subchild>\n"
				+ "</child>\n" + "</root>\n" + "</div>" + "</body></html>";
		String xml = findXml(preXml);
		// String preXml2 = "<html><head/><body><div class=\"main-content\">"
		// + "<div class=\"sub-content\">Sub content here</div>" + "<p>This is a
		// randomic paragraph</p><br>"
		// + "Main content here </div>" + "<div class=\"test\">" + "<root>\n" +
		// "<child>\n"
		// + "<subchild>.....</subchild>\n" + "</child>\n" + "<child>\n" +
		// "<subchild>.....</subchild>\n"
		// + "</child>\n" + "</root>\n" + "</div>" + "</body></html>";
		String xml2 = preXml2.split("<.*ajax-content-loader.*>")[1].split("<!-- ATENÇÃO.*")[0];
		System.out.println("------------------------------------\n\n");
		System.out.printf("%s", xml2);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml2));
			try {
				Document doc = db.parse(is);
				String message = String.valueOf(doc.getElementsByTagName("dados").getLength());
				System.out.println("\no tamanho da NodeList é :" + message + '\n');
				imprime(doc.getElementsByTagName("dados").item(0));

			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

	}

	private static String findXml(String preXml) {
		org.jsoup.nodes.Document document = Jsoup.parse(preXml);
		String xml = document.getElementsByClass("ajax-content-loader").html();
		System.out.println("Valor do HTML da div especificada : \n" + xml);
		return xml;

		// org.jsoup.nodes.Document doc = Jsoup.parse(s);
		// org.jsoup.nodes.Element element =
		// doc.getElementsByClass("ajax-content-loader").get(0);
		// String innerHtml = element.html();
		// System.out.printf("A string processada foi : \n %s \n", innerHtml);
		// return innerHtml;
	}

	private static void imprime(Node nd) {
		System.out.printf("%s:\t", nd.getNodeName());
		// System.out.println(nd.getTextContent());

		NodeList ndl = nd.getChildNodes();
		for (int i = 0; i < ndl.getLength(); i++) {
			Node atual = ndl.item(i);
			if (atual.getNodeType() == Node.ELEMENT_NODE) {
				imprime(atual);
			}
			if (atual.getNodeType() == Node.TEXT_NODE) {
				System.out.println(atual.getTextContent());
			}

		}
	}

}
