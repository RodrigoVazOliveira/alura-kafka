package dev.rvz.servlets;

import dev.rvz.helpers.OrderDatabase;
import dev.rvz.models.CorrelationId;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> kafkaDispatcherOrder = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        kafkaDispatcherOrder.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            BigDecimal amount = BigDecimal.valueOf(Long.parseLong(req.getParameter("ammount")));
            String uuid = req.getParameter("uuid");
            Order order = new Order(UUID.randomUUID().toString(), uuid, amount, email);

            OrderDatabase orderDatabase = new OrderDatabase();

            if (orderDatabase.saveNew(order)) {
                kafkaDispatcherOrder.sendMessage("ECOMMERCE_NEW_ORDER", email,
                        new CorrelationId(NewOrderServlet.class.getSimpleName()),order);

                System.out.println("new order sent successitilly");

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("new order sent successfully.");
                orderDatabase.close();
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("Old order received!");
                orderDatabase.close();
            }
        } catch (ExecutionException | InterruptedException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
