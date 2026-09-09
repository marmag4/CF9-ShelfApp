package gr.aueb.shelfapp.controller;

import gr.aueb.shelfapp.dto.TipDto;
import gr.aueb.shelfapp.service.TipService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feature #3: a short motivational tip, optionally tailored to a category
 * (e.g. "Dairy") - a little encouragement to actually use up the pantry.
 */
@RestController
@RequestMapping("/api/tips")
public class TipController {

    private final TipService tipService;

    public TipController(TipService tipService) {
        this.tipService = tipService;
    }

    @GetMapping("/random")
    public TipDto getRandomTip(@RequestParam(required = false) Long categoryId) {
        return tipService.randomTip(categoryId);
    }

    /**
     * The full set of tips for a category (or the general set), so the
     * frontend can cycle through all of them - via "New tip" - without
     * repeating one until every tip in the set has been shown once.
     */
    @GetMapping("/all")
    public List<TipDto> getAllTips(@RequestParam(required = false) Long categoryId) {
        return tipService.allTips(categoryId);
    }
}
