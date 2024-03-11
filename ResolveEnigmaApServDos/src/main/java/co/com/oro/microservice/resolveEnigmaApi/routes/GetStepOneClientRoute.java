package co.com.oro.microservice.resolveEnigmaApi.routes;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import org.apache.camel.Processor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.oro.microservice.resolveEnigmaApi.model.GetEnigmaStepResponse;
import co.com.oro.microservice.resolveEnigmaApi.model.JsonApiBodyResponseSuccess;
import co.com.oro.microservice.resolveEnigmaApi.model.client.*;

@Component
public class GetStepOneClientRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		  from("direct:get-step-one")
		  .routeId("routeIdd")
		  //.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		  //.setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
		  .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		  .to("freemarker:templates/GetStepOneClientTemplate.ftl")
		  .log("Response microservice step one: ${body}")
		  .hystrix()
		  .hystrixConfiguration().executionTimeoutInMilliseconds(2000).end()
          .to("http4://localhost:8082/v1/getOneEnigma/getOne")
          .convertBodyTo(String.class)
          .process(new Processor() {
              @Override
              public void process(Exchange exchange) throws Exception {
                  String jsonString = exchange.getIn().getBody(String.class);
                  ObjectMapper mapper = new ObjectMapper();
                  List<JsonApiBodyResponseSuccess> responseList = mapper.readValue(jsonString, new TypeReference<List<JsonApiBodyResponseSuccess>>(){});
                  if (responseList != null && !responseList.isEmpty()) {
                      JsonApiBodyResponseSuccess firstElement = responseList.get(0);
                      exchange.getIn().setBody(firstElement);
                      if(firstElement.getData().get(0).getAnswer().equals(" Step 2- Meter la jirafa")) {
                    	  exchange.setProperty("Step2", firstElement.getData().get(0).getAnswer());
                    	  exchange.setProperty("Error", "0000");
                    	  exchange.setProperty("DescError", "No error");
                      }else {
                    	  exchange.setProperty("Error", "0001");
                    	  exchange.setProperty("DescError", "Error consulting step one");
                      }
                  } else {
                      exchange.getIn().setBody(null); // or any default value if needed
                  }
              }
          })
          .endHystrix()
  	      .onFallback()
  	      .process(new Processor() {
  			@Override
  			public void process(Exchange exchange) throws Exception {
  				exchange.setProperty("error", "0002");
  				exchange.setProperty("DescError", "Error consulting the step one");
  				
  			}        	
          })
  	      .end()
		  .log("Response microservice step one two: ${body}")
		  .log("Response code ${exchangeProperty[error]}")
		  .log("Response description ${exchangeProperty[DescError]}")
		  .log("Response description ${exchangeProperty[Step2]}");
		  
		  
  }
}
