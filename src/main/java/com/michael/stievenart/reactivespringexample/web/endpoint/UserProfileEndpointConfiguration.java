package com.michael.stievenart.reactivespringexample.web.endpoint;

import com.michael.stievenart.reactivespringexample.handler.UserProfileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserProfileEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(UserProfileHandler handler) {
        String base = "/profiles";
        String byId = base + "/{id}";

        return route(i(GET(base)), handler::all)
                .andRoute(i(GET(byId)), handler::getById)
                .andRoute(i(DELETE(byId)), handler::deleteById)
                .andRoute(i(POST(base)), handler::create)
                .andRoute(i(PUT(byId)), handler::updateById);

    }

    private static RequestPredicate i(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
