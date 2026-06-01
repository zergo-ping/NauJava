package ru.Nikita.NauJava.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.Nikita.NauJava.dto.FileUploadResult;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.service.FileService;
import ru.Nikita.NauJava.transaction.UserService;

import java.util.List;

/**
 * Контроллер для управления файлами пользователя через веб-интерфейс Thymeleaf.
 *
 * <p>Предоставляет страницы: список файлов, форма загрузки, скачивание и удаление.</p>
 */
@Controller
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    /**
     * Отображает список файлов текущего пользователя.
     */
    @GetMapping
    public String listFiles(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity user = getCurrentUser(userDetails);
        List<FileEntity> files = fileService.getUserFiles(user);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("baseUrl", baseUrl);
        return "files/list";
    }

    /**
     * Отображает форму загрузки файла.
     */
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        return "files/upload";
    }

    /**
     * Обрабатывает загрузку файла.
     */
    @PostMapping("/upload")
    public String uploadFile(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam(value = "fileName", required = false) String fileName,
                             RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Файл не выбран");
            return "redirect:/files/upload";
        }

        UserEntity user = getCurrentUser(userDetails);
        String actualName = (fileName != null && !fileName.isBlank()) ? fileName : file.getOriginalFilename();

        try {
            FileUploadResult result = fileService.uploadFile(user, file, actualName);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            redirectAttributes.addFlashAttribute("success",
                    "Файл успешно загружен. Публичная ссылка: " + baseUrl + "/links/" + result.linkToken());
            redirectAttributes.addFlashAttribute("linkToken", result.linkToken());
            redirectAttributes.addFlashAttribute("baseUrl", baseUrl);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/files/upload";
        } catch (Exception e) {
            logger.error("Ошибка загрузки файла", e);
            redirectAttributes.addFlashAttribute("error", "Ошибка загрузки файла: " + e.getMessage());
            return "redirect:/files/upload";
        }

        return "redirect:/files";
    }

    /**
     * Обрабатывает превышение максимально допустимого размера загружаемого файла.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e,
                                              RedirectAttributes redirectAttributes) {
        logger.warn("Попытка загрузить слишком большой файл", e);
        redirectAttributes.addFlashAttribute("error",
                "Файл слишком большой. Максимальный допустимый размер превышён.");
        return "redirect:/files/upload";
    }

    /**
     * Скачивает файл по идентификатору.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        FileEntity file = fileService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден"));

        Resource resource = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMimeType() != null ? file.getMimeType() : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    /**
     * Удаляет файл текущего пользователя.
     */
    @PostMapping("/delete/{id}")
    public String deleteFile(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        UserEntity user = getCurrentUser(userDetails);
        try {
            fileService.deleteFile(user, id);
            redirectAttributes.addFlashAttribute("success", "Файл удалён");
        } catch (Exception e) {
            logger.error("Ошибка удаления файла", e);
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/files";
    }

    private UserEntity getCurrentUser(UserDetails userDetails) {
        return userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
    }
}
