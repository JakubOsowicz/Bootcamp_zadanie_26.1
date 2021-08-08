package pl.osowicz.task_manager;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            switch (statusCode) {
                case HttpStatus.BAD_REQUEST.value() -> {
                    return "error/400";
                }
                case HttpStatus.FORBIDDEN.value() -> {
                    return "error/403";
                }
                case HttpStatus.NOT_FOUND.value() -> {
                    return "error/404";
                }
                case HttpStatus.INTERNAL_SERVER_ERROR.value() -> {
                    return "error/500";
                }
                default -> throw new IllegalStateException("Unexpected value: " + statusCode);
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
