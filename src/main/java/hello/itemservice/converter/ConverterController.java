package hello.itemservice.converter;

import hello.itemservice.converter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConverterController {

    @ResponseBody
    @GetMapping("/hello/v2")
    public String helloV2(@RequestParam(required = false) Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }

    @ResponseBody
    @GetMapping("/convert/ip")
    public String convertIp(@RequestParam IpPort ip) {
        System.out.println("ipPort IP = " + ip.getIp());
        System.out.println("ipPort PORT = " + ip.getPort());
        return "ok";
    }


    @GetMapping("/converter/view")
    public String converterView(Model model) {

        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
        return "converter/view";

    }

    @GetMapping("/converter/form")
    public String converterForm(Model model) {
        IpPort ipPort = new IpPort("127.0.0.1", 8080);

        Form form = new Form(ipPort);
        model.addAttribute("form", form);
        return "converter/form";
    }

    @PostMapping("/converter/edit")
    public String converterEdit(@ModelAttribute Form form, Model model) {
        IpPort ipPort = form.getIpPort();
        model.addAttribute("ipPort", ipPort);
        return "converter/view";
    }

    @Data
    static class Form {
        private IpPort ipPort;

        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }
}
