package com.azilen.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ForbiddenAlertException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 5709495547407829616L;

    public ForbiddenAlertException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.FORBIDDEN);
    }
}
