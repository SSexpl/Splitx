package split.controller.Public;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public")
public class healthCheck {

    @GetMapping("/probe/health")
    public String health()
    {
        return "Health and Running";
    }
}
