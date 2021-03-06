package spittr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spittr.Spittle;
import spittr.data.SpittleRepository;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/spittles")
public class SpittleController {
    private static final String MAX_LONG_AS_STRING = "9223372036854775807";
    private SpittleRepository spittleRepository;

    @Autowired
    public SpittleController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Spittle> spittles(
            @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
            @RequestParam(value = "count", defaultValue = "20") int count
    ) {
        return spittleRepository.findSpittles(max, count);
    }

    @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
    public String showSpittle(@PathVariable long spittleId, Model model) {
        Spittle spittle = spittleRepository.findOne(spittleId);
        //抛出spittle没有找到的异常
        if(spittle==null){
            throw new SpittleNotFoundException();
        }
        model.addAttribute(spittle);
        return "spittle";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveSpittle(SpittleForm form) throws Exception {
        spittleRepository.save(new Spittle(null, form.getMessage(), new Date(),
                form.getLongitude(), form.getLatitude()));
        return "redirect:/spittles";
    }

    /**
     * 处理出现DuplicateSpittleException异常的方法
     * @return
     */
    @ExceptionHandler(DuplicateSpittleException.class)
    public String handleDuplicateSpittle(){
        return "error/duplicate";
    }
}
