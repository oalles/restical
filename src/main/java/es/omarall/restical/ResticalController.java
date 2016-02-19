package es.omarall.restical;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

@Controller
public class ResticalController {

    @Autowired
    private UidGenerator uidGenerator;

    /**
     * @param response
     * @param summary
     * @param start
     * @param end
     */

    @RequestMapping(value = "/ics", method = RequestMethod.GET)
    public void icsRequest(HttpServletResponse response,
            @RequestParam(value = "name", required = true) String summary,
            @RequestParam(value = "start", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        // Define VEvent
        VEvent event = (end == null)
                ? new VEvent(new Date(Date.from(
                        start.atZone(ZoneId.systemDefault()).toInstant())),
                summary)
                : new VEvent(
                        new Date(Date.from(start.atZone(ZoneId.systemDefault())
                                .toInstant())),
                        new Date(Date.from(end.atZone(ZoneId.systemDefault())
                                .toInstant())),
                        summary);
        event.getProperties().add(uidGenerator.generateUid());

        // event.validate();

        // Define iCal - ical4j
        Calendar calendar = new Calendar();
        calendar.getProperties()
                .add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getComponents().add(event);

        // add timezone information..
        // http://www.iana.org/time-zones

        // VTimeZone tz = VTimeZone.getDefault();
        // TzId tzParam = new TzId(
        // tz.getProperties().getProperty(Property.TZID).getValue());
        // christmas.getProperties().getProperty(Property.DTSTART).getParameters()
        // .add(tzParam);

        // Build response
        response.setContentType("text/calendar");
        response.setHeader("Content-Disposition",
                "inline; filename=calendar.ics");

        try {

            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, response.getOutputStream());
        } catch (Throwable t) {

            // easy boy.
            throw new ServerException();
        }

    }

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public @ResponseBody Greeting defaultMapping() {
        return new Greeting();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal server error")
    public class ServerException extends RuntimeException {
    }

    public class Greeting {
        private String message = "Welcome";

        public String getMessage() {
            return message;
        }

        public String getTestLink() {
            return testLink;
        }

        private String testLink = "http://localhost:8080/ics?start=2016-02-20T12:00:00&name=Call%20Mamma";

    }
}