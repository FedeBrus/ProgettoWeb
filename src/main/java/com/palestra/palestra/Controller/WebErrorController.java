package com.palestra.palestra.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model page) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404) {
                page.addAttribute("error", "Errore 404: La pagina cercata non è stata trovata");
            } else if(statusCode == 403) {
                page.addAttribute("error", "Errore 403: Non si hanno i permessi per visualizzare questa pagina!");
            } else {
                page.addAttribute("error", String.format("Errore %d: Si è verificato un errore :(", statusCode));
            }
        }

        return "public/error";
    }
}
