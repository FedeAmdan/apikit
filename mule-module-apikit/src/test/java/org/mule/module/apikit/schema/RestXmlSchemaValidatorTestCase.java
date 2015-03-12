package org.mule.module.apikit.schema;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.mule.module.apikit.Configuration;
import org.mule.module.apikit.Router;
import org.mule.module.apikit.validation.RestXmlSchemaValidator;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

import com.jayway.restassured.RestAssured;

import org.junit.Rule;
import org.junit.Test;
import org.raml.parser.loader.DefaultResourceLoader;

public class RestXmlSchemaValidatorTestCase extends FunctionalTestCase
{
    @Rule
    public DynamicPort serverPort = new DynamicPort("port");

    @Override
    public int getTestTimeoutSecs()
    {
        return 6000;
    }

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/apikit/schema/withUriParams/api.xml";
    }

    @Test
    public void badRequestJson() throws Exception
    {
        given().body("{ \n" +
                     "\"parceiroId\": 16, \n" +
                     "\"produtoId\": 42612, \n" +
                     "\"qtd\": 1 \n" +
                     "} ").contentType("application/json")
                .expect().response().statusCode(201)
                .when().post("/cliente/11111111111/resgate");
    }
}
