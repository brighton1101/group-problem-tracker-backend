package com.pm.backend.util.parser;

import org.jsoup.nodes.Document;

public class LeetcodeParser {

	private final String difficultyLevel = "span[data-cy='question-picker-list-question-difficulty']";
	private final String problemTitle = "title";
	private final String leetcodeBaseUrl = "https://leetcode.com";

	/**
	 * jsoup document to be parsed
	 */
	private Document document;

	private LeetcodeParser(Document d) {
		this.document = d;
	}

	/**
	 * [loadFromUri description]
	 * @param  uri Relative uri from leetcode base
	 *             ie
	 * @return     [description]
	 */
	public static Optional<LeetcodeParser> loadFromUri(String uri) {
		Optional<Document> doc = Parser.loadFromUri(leetcodeBaseUrl + uri);
		if (!doc.isPresent()) {
			return Optional.empty();
		}
		if (!doc.verifyLeetcodeDocument()) {
			return Optional.empty();
		}
		return new LeetcodeParser(doc.get());
	}

	/**
	 * Given a document containing Leetcode html, at the bare minimum we are verifying that the
	 * difficulty level exists and that the problem title exists
	 * @param  doc [description]
	 * @return     [description]
	 */
	private static boolean verifyLeetcodeDocument(Document doc) {
		boolean difficultyLevelExists = Parser.verifySelectorExists(doc, LeetcodeParser.difficultyLevel);
		boolean problemTitleExists = Parser.verifySelectorExists(doc, LeetcodeParser.difficultyLevel);
		return difficultyLevelExists && problemTitleExists;
	}
}