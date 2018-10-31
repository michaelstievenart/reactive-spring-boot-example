package com.michael.stievenart.reactivespringexample.web.endpoint;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

public class CaseInsensitiveRequestPredicate implements RequestPredicate {

    private final RequestPredicate target;

    CaseInsensitiveRequestPredicate(RequestPredicate target) {
        this.target = target;
    }

    @Override
    public boolean test(ServerRequest request) {
        return this.target.test(new LowerCaseUriServerRequestWrapper(request));
    }

    @Override
    public String toString(){
        return this.target.toString();
    }
}
