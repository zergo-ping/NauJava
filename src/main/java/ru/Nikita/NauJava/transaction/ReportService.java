package ru.Nikita.NauJava.transaction;

import ru.Nikita.NauJava.entity.ReportEntity;
import ru.Nikita.NauJava.entity.ReportStatus;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.ReportRepository;
import ru.Nikita.NauJava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    public String getReportContent(Long reportId) {
        return reportRepository.findById(reportId).map(ReportEntity::getContent).orElse(null);
    }

    public Long createReport() {
        ReportEntity report = new ReportEntity();
        report.setStatus(ReportStatus.CREATED);
        return reportRepository.save(report).getId();
    }

    public void generateReport(Long reportId) {
        CompletableFuture.runAsync(() -> {
            try {
                long startTime = System.currentTimeMillis();

                ExecutorService executor = newFixedThreadPool(2);
                try {
                    final long[] usersTime = new long[1];
                    final long[] listTime = new long[1];

                    Future<Long> countFuture = executor.submit(() -> {
                        long time = System.currentTimeMillis();
                        long count = userRepository.count();
                        usersTime[0] = System.currentTimeMillis() - time;
                        return count;
                    });

                    Future<List<UserEntity>> listFuture = executor.submit(() -> {
                        long t = System.currentTimeMillis();
                        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
                        listTime[0] = System.currentTimeMillis() - t;
                        return users;
                    });

                    long userCount = countFuture.get();
                    List<UserEntity> users = listFuture.get();

                    long totalTime = System.currentTimeMillis() - startTime;
                    String html = buildHtmlReport(userCount, usersTime[0], users, listTime[0], totalTime);

                    ReportEntity report = reportRepository.findById(reportId).orElseThrow();
                    report.setStatus(ReportStatus.COMPLETED);
                    report.setContent(html);
                    reportRepository.save(report);
                } finally {
                    executor.shutdown();
                }

            } catch (Exception e) {
                ReportEntity report = reportRepository.findById(reportId).orElseThrow();
                report.setStatus(ReportStatus.ERROR);
                report.setContent("Error: " + e.getMessage());
                reportRepository.save(report);
            }
        });
    }

    private String buildHtmlReport(long userCount, long usersTime, List<UserEntity> users,
                                   long listTime, long totalTime) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>")
            .append("<h1>Отчет статистики приложения</h1>")
            .append("<h2>Статистика</h2>")
            .append("<p>Всего пользователей: ").append(userCount).append("</p>")
            .append("<p>Время подсчета пользователей: ").append(usersTime).append(" мс</p>")
            .append("<p>Время получения списка пользователей: ").append(listTime).append(" мс</p>")
            .append("<p>Общее время: ").append(totalTime).append(" мс</p>")
            .append("<h2>Пользователи</h2>")
            .append("<table border='1'><tr><th>ID</th><th>Email</th><th>Имя</th><th>Роль</th></tr>");

        for (UserEntity u : users) {
            html.append("<tr><td>").append(u.getId()).append("</td><td>")
                .append(u.getEmail() != null ? u.getEmail() : "Не указан").append("</td><td>")
                .append(u.getFullName() != null ? u.getFullName() : "Не указано").append("</td><td>")
                .append(u.getRole() != null ? u.getRole() : "Не указана").append("</td></tr>");
        }

        html.append("</table></body></html>");
        return html.toString();
    }
}
