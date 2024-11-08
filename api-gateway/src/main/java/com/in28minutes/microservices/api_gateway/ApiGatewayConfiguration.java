package com.in28minutes.microservices.api_gateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {

		Function<GatewayFilterSpec, UriSpec> filterFunction = 
				f -> f.addRequestHeader("MyHeader", "MyURI")
						.addRequestParameter("MyVersion", "V1");
		Function<PredicateSpec, Buildable<Route>> routeFunction 
			= p -> p.path("/get")
			.filters(filterFunction)
			.uri("http://httpbin.org:80");
		return builder.routes().route(routeFunction)
				.route(p -> p.path("/currency-exchange/**")
						.uri("lb://currency-exchange"))
				.route(p -> p.path("/currency-conversion/**")
						.uri("lb://currency-conversion"))
				.route(p -> p.path("/feign-currency-conversion/**")
						.uri("lb://currency-conversion"))
				.build();
	}

}
