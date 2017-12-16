package main.InternalUtils;

import main.InternalUtils.Enums.CategoryEnum;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CategoryParser {
	private static HashMap<String,CategoryEnum> categoryMap;

	private static String file_path;

	public static void Init(String path){
		file_path = path;

		categoryMap = new HashMap<>();

		JSONObject tmpObject = null;
		System.out.println(path);

		try {
			tmpObject =  new JSONObject(new String(Files.readAllBytes(Paths.get(file_path)), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Missing file CategoryMap.");
		}

		if (tmpObject == null)
			return;

		Iterator<String> keys = tmpObject.keys();

		while(keys.hasNext()){
			String tmpKey = keys.next();
			addCategoryPair(tmpKey,CategoryEnum.valueOf(tmpObject.getString(tmpKey)));
		}
	}

	public static CategoryEnum getCategory(String s){
		return categoryMap.getOrDefault(s, CategoryEnum.UNKNOWN);
	}

	public static void addCategoryPair(String value , CategoryEnum categoryName){
		if (categoryMap.containsKey(value)){
			categoryMap.remove(value);
		}

		categoryMap.putIfAbsent(value,categoryName);

		saveFile();
	}

	public static CategoryEnum[] getCategories(){
		return CategoryEnum.values();
	}
	public static String[] getCategoriesAsString(){
		return CategoryEnum.names();
	}


	private static void saveFile() {
		List<String> lines = Arrays.asList(JSONObject.valueToString(categoryMap));
		Path file = Paths.get(file_path);
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.err.println("Couldn't save file.");
		}
	}

}
