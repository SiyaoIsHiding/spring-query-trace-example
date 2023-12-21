package tacos.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.data.QueryTraceCache;

@Controller
@RequestMapping("/trace")
public class LatestTraceController {
    private QueryTraceCache traceCache;

    public LatestTraceController(QueryTraceCache traceCache) {
        this.traceCache = traceCache;
    }

    @GetMapping
    public String showLatestTrace(Model model) {
        List<String> traceMessages = new ArrayList<>();
        traceCache.cache.forEach(event -> {
            String s = String.format("* %s on %s[%s] at %s (%sµs)", event.getActivity(), event.getSourceAddress(),
                    event.getThreadName(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date(event.getTimestamp())),
                    event.getSourceElapsedMicros());
            traceMessages.add(s);
        });
        model.addAttribute("trace", traceMessages);
        return "trace";
    }

}
