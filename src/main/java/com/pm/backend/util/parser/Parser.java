package com.pm.backend.util.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.File;
import java.util.Optional;

/**
 * Class containing static methods to get and search HTML documents
 * using jsoup.
 * @see https://jsoup.org/apidocs/index.html
 */
public class Parser {

	/**
	 * Connect to url and get parsed document
	 * @param  url Base url to connect to
	 * @return     Optional containing Jsoup document,
	 *             or an empty optional
	 */
	public static Optional<Document> loadFromUri(String url) {
		try {
			return Optional.of(
				Jsoup
					.connect(url)
					.get()
			);
		}
		catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
		return Optional.empty();
	}

	/**
	 * Loads utf-8 encoded html file from resources directory. Note
	 * that this likely won't be useful in a production environment,
	 * but is very useful for testing.
	 * @param  resourcesPath Path to file in resources directory
	 * @return               Optional containing Jsoup document,
	 *                       or an empty optional
	 */
	public static Optional<Document> loadFromFile(String resourcesPath) {
		try {
			File f = new File(
				Parser.class.getClassLoader().getResource(resourcesPath).getFile()
			);
			return Optional.of(Jsoup.parse(f, "UTF-8"));
		}
		catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
		return Optional.empty();
	}


	/**
	 * Find first text value of element based on css selector
	 * @param  doc      Jsoup document to look for selector in
	 * @param  selector CSS selector (jQuery style) to look up html value for
	 * @return          Optional value, with value of text if exists
	 */
	public static Optional<String> getTextFromElement(Document doc, String selector) {
		Elements elms = doc.select(selector);
		if (elms.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(
			elms.first().text()
		);
	}

	/**
	 * Given a selector, verify that it exists within doc
	 * @param  doc      Doc to look for selector in
	 * @param  selector selector
	 * @return          true if found, false otherwise
	 */
	public static boolean verifySelectorExists(Document doc, String selector) {
		return !(
			Parser
				.getTextFromElement(doc, selector)
				.isEmpty()
		);
	}
}