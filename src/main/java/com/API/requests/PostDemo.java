package com.API.requests;

import static com.jayway.restassured.RestAssured.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;
import com.API.Response.GetResponseForValidateUser;
import com.API.Response.ResponseDemo;
import com.API.ServiceEnum.ServiceEndpoint;
import com.API.Utils.ExcelOperation;
import com.API.Utils.FrameworkServices;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import net.minidev.json.JSONObject;

public class PostDemo {
	public String generateParentJson(XSSFWorkbook workbook, String sheetName, String scenarioID,ExcelOperation excelOperation) throws JsonParseException, JsonMappingException, IOException {
		try {
			JSONObject parentJsonObject=new JSONObject();
			LinkedHashMap<String, String>jsonMap=excelOperation.getScenarioData(workbook, "DT_DEMO", scenarioID).get(0);
			parentJsonObject.put("name",jsonMap.get("name"));
			parentJsonObject.put("salary",jsonMap.get("salary"));
			parentJsonObject.put("age", jsonMap.get("age"));
			ObjectMapper mapper=new ObjectMapper();
			Object json1 = mapper.readValue(parentJsonObject.toString(), Object.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json1);
			Reporter.log("<b><font size=4 color=green>Validate User</font></b>");
			Reporter.log("<b>Request is--></b>"+indented);
			return parentJsonObject.toString();

		}
		catch (Exception |AssertionError e) 
		{
			throw e;
		}
	}
	public void getDemo(XSSFWorkbook workbook,String sheetName,ExcelOperation excelOperation,String scenarioID) throws Exception {
		try {
			String uri=FrameworkServices.getConfigProperties().getProperty("ApplicationURI")+ServiceEndpoint.DEMO.getUrl();
			String json=generateParentJson(workbook,sheetName,scenarioID,excelOperation);

			Response res = given().	
					body(json).
					when().
					contentType(ContentType.JSON).post(uri);
			Reporter.log("<b>Response is--></b>"+res.asString());
			
			Gson gson=new Gson();
			ResponseDemo getAllApplicationResponse=gson.fromJson(res.asString(),ResponseDemo.class);
			JsonObject jsonObject=new Gson().fromJson(res.asString(), JsonObject.class);
			String result = ((JsonObject)((JsonObject) ((JsonObject) jsonObject.get("name")).get("salary")).get("age")).toString();
			Reporter.log("<b>name From Response is--></b>"+"<b>"+result+"</b>");
			//excelOperation.setDataIntoExcel(workbook, "DT_ValidatePasswordData", 1, "imageIndex",result);
			String salary= jsonObject.get("salary").toString().replace("\"", "");
			if(salary.equalsIgnoreCase("123")) {
				XSSFSheet sheet=workbook.getSheet("DT_DEMORESPONSE");
				Row headerRow=sheet.getRow(0);
				LinkedHashMap<String, Integer> headerPosition=new LinkedHashMap<>();
				for(int columnNo=0;columnNo<headerRow.getPhysicalNumberOfCells();columnNo++) {
					headerPosition.put(headerRow.getCell(columnNo).getStringCellValue(), columnNo);
				}
				for(int j=0;j<sheet.getPhysicalNumberOfRows();j++){
					XSSFRow	row = sheet.getRow(j);
					XSSFCell cell = row.getCell(headerPosition.get("ScenarioId"),Row.CREATE_NULL_AS_BLANK);
					if(cell.getStringCellValue().equalsIgnoreCase(scenarioID)) {
						excelOperation.updateValidatePasswordDataInExcel(workbook, "DT_DEMORESPONSE",scenarioID,"id", result);
					}
				}
				Assert.assertEquals("123",salary);
			}
		}
		catch(Exception e) {
			e.printStackTrace();	
			throw e;
		}
		}
	
	}
	


		
	



	

	



	
