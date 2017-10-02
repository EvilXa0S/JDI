package com.epam.page.object.generator;

import com.epam.page.object.generator.finder.ElementsFinder;
import com.epam.page.object.generator.model.ElementAttribute;
import com.epam.page.object.generator.model.SearchRule;
import com.epam.page.object.generator.parser.JSONIntoRuleParser;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import org.json.simple.parser.ParseException;
import org.jsoup.select.Elements;
import org.openqa.selenium.support.FindBy;

@Retention(RetentionPolicy.RUNTIME)
@interface JPage {

	String url() default "";
	String title() default "";
}

class Button {

}

public class PageObjectGenerator {

	private String jsonPath;
	private List<String> urls;
	private String outputDir;

	public PageObjectGenerator() {
	}

	public PageObjectGenerator(String jsonPath, List<String> urls, String outputDir) {
		this.jsonPath = jsonPath;
		this.urls = urls;
		this.outputDir = outputDir;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void generateJavaFile() throws IOException, ParseException, IllegalArgumentException {
		List<SearchRule> searchRules = JSONIntoRuleParser.getRulesFromJSON(jsonPath);
		Map<SearchRule, Elements> searchResultsMap = ElementsFinder.searchElementsByRulesOnURLs(searchRules, urls);
		List<FieldSpec> fields = new ArrayList<>();
		int elementCounter = 0;

		for (SearchRule searchRule : searchResultsMap.keySet()) {
			List<String> resultList = searchRule.isSearchingByText()
				? searchResultsMap.get(searchRule).eachText()
				: searchResultsMap.get(searchRule).eachAttr("value");

			for (String element : resultList) {
				StringBuilder ruleDescription = new StringBuilder();

				ruleDescription.append("//");
				ruleDescription.append(searchRule.getTag());
				ruleDescription.append("[");

				if (searchRule.getClasses() != null && !searchRule.getClasses().isEmpty()) {
					ruleDescription.append("[@class='");
					for (String clazz : searchRule.getClasses()) {
						ruleDescription.append(clazz).append(" ");
					}
					ruleDescription.deleteCharAt(ruleDescription.lastIndexOf(" "));
					ruleDescription.append("' and ");
				}

				if (searchRule.getAttributes() != null && !searchRule.getAttributes().isEmpty()) {
					for (ElementAttribute elementAttribute : searchRule.getAttributes()) {
						ruleDescription.append("@").append(elementAttribute.getAttributeName())
							.append("='").append(elementAttribute.getAttributeValue()).append("'")
							.append(" and ");
					}
				}

				if (searchRule.isSearchingByText()) {
					ruleDescription.append("text()");
				} else {
					ruleDescription.append("@value");
				}

				ruleDescription.append("='").append(element).append("']");

				fields.add(FieldSpec.builder(Button.class, searchRule.getTag() + elementCounter)
					.addModifiers(Modifier.PUBLIC)
					.addAnnotation(AnnotationSpec.builder(FindBy.class)
						.addMember("xpath", "$S", ruleDescription)
						.build())
					.build());

				elementCounter++;
			}
		}

		TypeSpec mainPage = TypeSpec.classBuilder("MainPage")
			.addModifiers(Modifier.PUBLIC)
			.addAnnotation(AnnotationSpec.builder(JPage.class)
				.addMember("url", "$S", urls.get(0))
				.addMember("title", "$S", "Main Page")
				.build())
			.addFields(fields)
			.build();

		JavaFile javaFile = JavaFile.builder("com.epam.jdi.site.epam.pages", mainPage)
			.build();

		javaFile.writeTo(System.out);
		javaFile.writeTo(Paths.get(outputDir));
	}

}