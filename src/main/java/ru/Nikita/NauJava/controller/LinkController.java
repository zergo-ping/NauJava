package ru.Nikita.NauJava.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.LinkEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.service.FileService;
import ru.Nikita.NauJava.service.LinkService;
import ru.Nikita.NauJava.transaction.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

/**
 * MVC-контроллер для управления ссылками совместного доступа.
 *
 * <p>Предоставляет страницы: список ссылок пользователя, публичная страница ссылки,
 * удаление ссылки и скачивание файла по токену.</p>
 */
@Controller
@RequestMapping("/links")
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private final LinkService linkService;
    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public LinkController(LinkService linkService,
                          FileService fileService,
                          UserService userService) {
        this.linkService = linkService;
        this.fileService = fileService;
        this.userService = userService;
    }

    /**
     * Отображает список ссылок текущего пользователя.
     */
    @GetMapping
    public String listLinks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity user = getCurrentUser(userDetails);
        List<LinkEntity> links = linkService.getUserLinks(user);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        model.addAttribute("links", links);
        model.addAttribute("user", user);
        model.addAttribute("baseUrl", baseUrl);
        return "links/list";
    }

    /**
     * Создаёт новую ссылку на файл текущего пользователя.
     */
    @PostMapping("/create/{fileId}")
    public String createLink(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long fileId,
                             RedirectAttributes redirectAttributes) {
        UserEntity user = getCurrentUser(userDetails);
        try {
            LinkEntity link = linkService.createLinkForFile(user, fileId);
            redirectAttributes.addFlashAttribute("success",
                    "Ссылка создана: /links/" + link.getToken());
            redirectAttributes.addFlashAttribute("linkToken", link.getToken());
        } catch (Exception e) {
            logger.error("Ошибка создания ссылки", e);
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка создания ссылки: " + e.getMessage());
        }
        return "redirect:/links";
    }

    /**
     * Удаляет ссылку текущего пользователя.
     */
    @PostMapping("/delete/{id}")
    public String deleteLink(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        UserEntity user = getCurrentUser(userDetails);
        try {
            linkService.deleteLink(user, id);
            redirectAttributes.addFlashAttribute("success", "Ссылка удалена");
        } catch (Exception e) {
            logger.error("Ошибка удаления ссылки", e);
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/links";
    }

    /**
     * Публичная страница ссылки по токену.
     * Доступна без авторизации.
     */
    @GetMapping("/{token}")
    public String viewLink(@PathVariable String token, Model model) {
        Optional<LinkEntity> linkOpt = linkService.findByToken(token);
        if (linkOpt.isEmpty()) {
            model.addAttribute("error", "Ссылка не найдена");
            return "links/view";
        }

        LinkEntity link = linkOpt.get();
        model.addAttribute("link", link);
        model.addAttribute("valid", link.isValid());

        Optional<FileEntity> fileOpt = linkService.getLinkedFile(link);
        fileOpt.ifPresent(file -> model.addAttribute("file", file));

        return "links/view";
    }

    /**
     * Скачивание файла по публичной ссылке.
     * Доступно без авторизации.
     */
    @GetMapping("/{token}/download")
    public ResponseEntity<Resource> downloadByLink(@PathVariable String token) {
        LinkEntity link = linkService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена"));

        if (!link.isValid()) {
            throw new IllegalStateException("Ссылка недействительна или срок её действия истёк");
        }

        FileEntity file = linkService.getLinkedFile(link)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден"));

        Resource resource = fileService.downloadFile(file.getId());
        linkService.incrementDownloads(link);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        file.getMimeType() != null ? file.getMimeType() : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    private UserEntity getCurrentUser(UserDetails userDetails) {
        return userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
    }
}
