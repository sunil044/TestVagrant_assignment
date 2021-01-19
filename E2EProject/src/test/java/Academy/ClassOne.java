package Academy;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.ReadPropFile;


public class ClassOne {
	
	Properties prop;
	
	@BeforeMethod
	public void setEndPoint()
	{
		RestAssured.baseURI = "https://api.punkapi.com/v2";
	}
	
	@Test(groups="Regression")
	public void getAllBeers()
	{
		
		Response response = given().when().get("beers");
		String res =response.then().assertThat().statusCode(200).extract().response().asString();
	    JsonPath js = ReUsableMethods.rawToJson(res);
		SoftAssert sf = ReUsableMethods.verifyResponse(js);
	    sf.assertAll();
	    
	}
	
	@Test(groups="Regression")
	public void getAllBeersAboveABV() throws IOException
	{   
		
		prop = ReadPropFile.readProp();
		String abvCount = prop.getProperty("abvCount");
		
		String res =given().queryParam("abv_gt", abvCount).when().get("beers").then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = ReUsableMethods.rawToJson(res);
		SoftAssert sf = ReUsableMethods.verifyResponse(js);	
		String[] abv = ReUsableMethods.abv.replaceAll("[\\[\\]]", "").split(",");
				
		for(String data : abv)
		{	
			data=data.trim();
			sf.assertFalse(Float.parseFloat(data)<=Integer.parseInt(abvCount), "Getting beers which has abv = "+data);
		}
	    sf.assertAll();
	    
	}
	
	@Test(groups="Smoke")
	public void verifyPagination() throws IOException
	{	
		
		String per_page = prop.getProperty("per_page");
		String res =given().queryParam("page", "2").queryParam("per_page", per_page).when().get("beers").then().assertThat().statusCode(200).extract().response().asString();
	    JsonPath js = ReUsableMethods.rawToJson(res);
		SoftAssert sf = ReUsableMethods.verifyResponse(js);	

		String[] id = ReUsableMethods.id.replaceAll("[\\[\\]]", "").split(",");
		System.out.println();
		
		sf.assertEquals(id.length, Integer.parseInt(per_page), "Returning beers more than expected");
		sf.assertAll();
	    	
	}
	
	@Test(groups="Smoke")
	public void GetAllBeersBrewedBeforeCertainDate() throws ParseException, IOException
	{
		prop = ReadPropFile.readProp();
		String brewedDate = prop.getProperty("brewedDate");
		String res =given().queryParam("brewed_before", brewedDate).when().get("beers").then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = ReUsableMethods.rawToJson(res);
		SoftAssert sf = ReUsableMethods.verifyResponse(js);	
		String[] brewed = js.getString("first_brewed").replaceAll("[\\[\\]]", "").split(",");
		SimpleDateFormat sdfo = new SimpleDateFormat("MM/yyyy");
		java.util.Date d1 = sdfo.parse(brewedDate);
	
		for(String data : brewed) {
			data=data.trim();
			java.util.Date d2 = sdfo.parse(data);
			sf.assertTrue(d1.compareTo(d2) > 0,brewedDate+" is before "+data);
		}
		
		sf.assertAll();
	}
	
}

