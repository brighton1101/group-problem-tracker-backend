package com.pm.backend.util.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.Optional;

/**
 * Tests for com.pm.backend.util.parser.Parser
 * Note: Later, add tests for fetching doc from url
 * with Mockito...
 */
class ParserTest {

	// Note that this method is highly coupled to `loadFromFile`
	// currently, and that method is only there for testing purposes
	// to begin with...
	@Test
	public void testGetTextFromElement() {
		Document doc = Parser
			.loadFromFile("helloworld")
			.get();
		Assertions.assertEquals(
			"Load a Document from a File",
			Parser.getTextFromElement(doc, ".THIS-IS-FOR-TEST").get()
		);
	}
}