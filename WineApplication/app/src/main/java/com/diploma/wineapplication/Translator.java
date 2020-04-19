package com.diploma.wineapplication;

import com.diploma.wineapplication.parsing.Parse;
import com.diploma.wineapplication.parsing.ParseTextDetect;
import com.diploma.wineapplication.parsing.ParseTextTranslate;
import com.diploma.wineapplication.text.Text;
import com.diploma.wineapplication.text.TextTranslate;

public class Translator {
	private static Translator translator;

	private Translator() {
	}

	public synchronized static Translator getInstance() {

		if (translator == null) {
			translator = new Translator();
		}
		return translator;

	}

	public void translate(TextTranslate textTranslate) {

		Parse parse = new ParseTextTranslate(textTranslate);
		parse.parse();

	}

	public String translate(String text, String languageInput,
			String languageOutput) {

		Text input = new Text(text, languageInput);
		TextTranslate textTranslate = new TextTranslate(input, languageOutput);
		Parse parse = new ParseTextTranslate(textTranslate);
		parse.parse();
		return textTranslate.getOutput().getText();

	}

	public String detect(String text) {

		Text input = new Text();
        input.setText(text);
		Parse parse = new ParseTextDetect(input);
		parse.parse();
		return input.getLanguage();

	}

}
