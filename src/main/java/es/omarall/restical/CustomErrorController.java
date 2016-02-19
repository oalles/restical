package es.omarall.restical;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    ErrorResponse error(HttpServletRequest request,
            HttpServletResponse response) {

        RequestAttributes requestAttributes = new ServletRequestAttributes(
                request);

        return new ErrorResponse(response.getStatus(),
                errorAttributes.getErrorAttributes(requestAttributes, false));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    public class ErrorResponse {
        private int error;

        public int getError() {
            return error;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        private Map<String, Object> attributes;

        public ErrorResponse(int error, Map<String, Object> attributes) {
            this.error = error;
            this.attributes = attributes;
        }
    }

}