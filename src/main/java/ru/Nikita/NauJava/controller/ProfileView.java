package ru.Nikita.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Nikita.NauJava.entity.StorageQuotaEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.QuotaRepository;
import ru.Nikita.NauJava.transaction.UserService;

/**
 * MVC-контроллер для отображения профиля пользователя.
 */
@Controller
@RequestMapping("/account")
public class ProfileView {

    private final UserService userService;
    private final QuotaRepository quotaRepository;

    @Autowired
    public ProfileView(UserService userService, QuotaRepository quotaRepository) {
        this.userService = userService;
        this.quotaRepository = quotaRepository;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        StorageQuotaEntity quota = quotaRepository.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("quota", quota);

        if (quota != null && quota.getMaxBytes() > 0) {
            double usedPercent = (quota.getUsedBytes() * 100.0) / quota.getMaxBytes();
            model.addAttribute("usedPercent", Math.round(usedPercent * 100.0) / 100.0);
        } else {
            model.addAttribute("usedPercent", 0.0);
        }

        return "profile";
    }
}
