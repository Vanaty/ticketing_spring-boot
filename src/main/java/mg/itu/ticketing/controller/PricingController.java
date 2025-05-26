package mg.itu.ticketing.controller;

import mg.itu.ticketing.model.PricingRule;
import mg.itu.ticketing.repository.PricingRuleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pricing")
public class PricingController {
    private final PricingRuleRepository pricingRuleRepository;

    public PricingController(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }

    @GetMapping
    public String listPricingRules(Model model) {
        List<PricingRule> rules = pricingRuleRepository.findAll();
        model.addAttribute("rules", rules);
        return "index";
    }

    @GetMapping("/new")
    public String showPricingForm(Model model) {
        model.addAttribute("pricingRule", new PricingRule());
        return "pricing-form";
    }

    @PostMapping("/save")
    public String savePricingRule(@ModelAttribute PricingRule pricingRule) {
        pricingRuleRepository.save(pricingRule);
        return "redirect:/pricing";
    }
}
