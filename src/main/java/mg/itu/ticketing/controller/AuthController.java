package mg.itu.ticketing.controller;

import mg.itu.ticketing.entity.Utilisateur;
import mg.itu.ticketing.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult result, Model model) {
        if (utilisateurRepository.findByNom(utilisateur.getNom()) != null) {
            result.rejectValue("nom", null, "Nom d'utilisateur déjà utilisé");
            return "register";
        }
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        utilisateurRepository.save(utilisateur);
        return "redirect:/login?registered";
    }
}
