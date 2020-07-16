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
class Parser {

	/**
	 * Connect to url and get parsed document
	 * @param  url Base url to connect to
	 * @return     Jsoup Document
	 */
	static Optional<Document> getParsedHtml(String url) {
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
	 * @param  resourcesPath [description]
	 * @return               [description]
	 */
	static Optional<Document> loadFromFile(String resourcesPath) {
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
	static Optional<String> getTextFromElement(Document doc, String selector) {
		Elements elms = doc.select(selector);
		if (elms.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(
			elms.first().text()
		);
	}
}