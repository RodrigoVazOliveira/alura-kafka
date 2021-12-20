package dev.rvz.servlets;

import dev.rvz.services.KafkaDispatcher;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateAllReportsService extends HttpServlet {
    private final KafkaDispatcher<String> batchKafkaDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        batchKafkaDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            batchKafkaDispatcher.sendMessage("SEND_MESSAGE_TO_ALL_USERS", "USER_GENERATE_READING_REPORT", "USER_GENERATE_READING_REPORT");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Sebt generate reports to all users!");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("Report requests generated");
    }
}
