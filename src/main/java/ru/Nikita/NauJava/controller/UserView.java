package ru.Nikita.NauJava.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.Nikita.NauJava.dto.UserDTO;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.UserRepository;
import ru.Nikita.NauJava.service.FileService;
import ru.Nikita.NauJava.service.LinkService;
import ru.Nikita.NauJava.service.UserMappingService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Контроллер для отображения HTML страницы со списком пользователей.
 * Использует Thymeleaf для рендеринга шаблонов.
 */
@Controller
@RequestMapping("/custom/users/view")
public class UserView {

    private final UserRepository userRepository;
    private final UserMappingService userMappingService;
    private final LinkService linkService;
    private final FileService fileService;

    @Autowired
    public UserView(UserRepository userRepository, UserMappingService userMappingService, LinkService linkService, FileService fileService) {
        this.userRepository = userRepository;
        this.userMappingService = userMappingService;
        this.linkService = linkService;
        this.fileService = fileService;
    }

    @Transactional
    @GetMapping("/list")
    public String userListView(Model model) {
        Iterable<UserEntity> usersIterable = userRepository.findAll();
        List<UserDTO> userDTOs = StreamSupport.stream(usersIterable.spliterator(), false)
                .map(userMappingService::mapUserToDTO)
                .collect(Collectors.toList());
        model.addAttribute("users", userDTOs);
        return "userList";
    }

    @Transactional
    @PostMapping("/links/delete/{linkId}")
    public String deleteLink(@PathVariable Long linkId,
                             org.springframework.security.core.Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            UserEntity admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            linkService.deleteLink(admin, linkId);
            redirectAttributes.addFlashAttribute("success", "Ссылка успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении ссылки: " + e.getMessage());
        }
        return "redirect:/custom/users/view/list";
    }

    @Transactional
    @PostMapping("/role/{userId}")
    public String changeRole(@PathVariable Long userId,
                             @RequestParam("role") String role,
                             org.springframework.security.core.Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            UserEntity admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            if (!"ADMIN".equals(admin.getRole())) {
                redirectAttributes.addFlashAttribute("error", "Недостаточно прав");
                return "redirect:/custom/users/view/list";
            }

            UserEntity targetUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            if (targetUser.getId().equals(admin.getId())) {
                redirectAttributes.addFlashAttribute("error", "Нельзя изменить свою собственную роль");
                return "redirect:/custom/users/view/list";
            }

            targetUser.setRole(role);
            userRepository.save(targetUser);
            redirectAttributes.addFlashAttribute("success",
                    "Роль пользователя " + targetUser.getEmail() + " изменена на " + role);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при изменении роли: " + e.getMessage());
        }
        return "redirect:/custom/users/view/list";
    }

    @Transactional
    @PostMapping("/files/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId,
                             org.springframework.security.core.Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            UserEntity admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            if (!"ADMIN".equals(admin.getRole())) {
                redirectAttributes.addFlashAttribute("error", "Недостаточно прав");
                return "redirect:/custom/users/view/list";
            }

            fileService.deleteFile(admin, fileId);
            redirectAttributes.addFlashAttribute("success", "Файл успешно удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении файла: " + e.getMessage());
        }
        return "redirect:/custom/users/view/list";
    }
}