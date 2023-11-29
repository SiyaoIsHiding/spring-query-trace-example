package tacos.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import com.datastax.oss.driver.api.core.cql.QueryTrace;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import lombok.extern.slf4j.Slf4j;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.domain.TacoOrder;

@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
@Slf4j
public class OrderController {

  private OrderRepository orderRepo;
  private IngredientRepository ingredientRepo;

  public OrderController(OrderRepository orderRepo, IngredientRepository ingredientRepo) {
    this.orderRepo = orderRepo;
    this.ingredientRepo = ingredientRepo;
  }

  @GetMapping("/current")
  public String orderForm() {
    return "orderForm";
  }

  @PostMapping
  public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) {
    if (errors.hasErrors()) {
      return "orderForm";
    }

    ResultSet rs = orderRepo.save(order);
    ExecutionInfo info = rs.getExecutionInfo();
    QueryTrace queryTrace = info.getQueryTrace();
    log.info("QueryTraceDurationMicros: {}", queryTrace.getDurationMicros());
    sessionStatus.setComplete();

    return "redirect:/";
  }

}
