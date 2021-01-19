package Academy;

import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	
	public static String id;
	public static String name;
	public static String description;
	public static String abv;

	public static JsonPath rawToJson(String res)
	{
		JsonPath js = new JsonPath(res);
		return js;
		
	}
	
	public static SoftAssert verifyResponse(JsonPath js)
	{
		
		id=js.getString("id");
		name=js.getString("name");
		description=js.getString("description");
	    abv=js.getString("abv");
	    
		SoftAssert sf = new SoftAssert();
	
		sf.assertFalse(id.isEmpty(), "id is Empty");
		sf.assertFalse(name.isEmpty(), "name is Empty");	
		sf.assertFalse(description.isEmpty(), "description is Empty");	
		sf.assertFalse(abv.isEmpty(), "abv is Empty");
		
		return sf;
		
	}
	

}

