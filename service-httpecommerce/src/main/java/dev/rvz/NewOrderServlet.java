package dev.rvz;

import dev.rvz.models.deserializers.Email;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> kafkaDispatcherOrder = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> kafkaDispatcherEmail = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        kafkaDispatcherOrder.close();
        kafkaDispatcherOrder.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            //we are not caring about any security issues, we are only
            // showing how to use as a startup point
            String email = req.getParameter("email");
            BigDecimal ammount = BigDecimal.valueOf(Long.parseLong(req.getParameter("ammount")));
            Order order = new Order(UUID.randomUUID().toString(), UUID.randomUUID().toString(), ammount, email);
            Email emailCode = new Email(UUID.randomUUID().toString(), "Thank you, new order processing success!");
            kafkaDispatcherEmail.sendMessage("ECOMMERCE_SEND_MAIL", email, emailCode);
            kafkaDispatcherOrder.sendMessage("ECOMMERCE_NEW_ORDER", email, order);

            System.out.println("new order sent successitilly");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("new order sent successitilly");
        } catch (ExecutionException | InterruptedException e) {
            throw new ServletException(e);
        }
    }
}
