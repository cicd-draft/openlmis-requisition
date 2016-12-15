package org.openlmis.requisition.service.fulfillment;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.requisition.dto.OrderDto;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RunWith(MockitoJUnitRunner.class)
public class OrderFulfillmentServiceTest {

  @InjectMocks
  private OrderFulfillmentService orderFulfillmentService;

  @Mock
  private RestTemplate restTemplate;

  @Before
  public void setUp() {

    orderFulfillmentService = new OrderFulfillmentService(restTemplate) {
      @Override
      protected String obtainAccessToken() {
        return getToken();
      }
    };
    ReflectionTestUtils.setField(orderFulfillmentService, "fulfillmentUrl",
        "https://localhost");
  }

  @Test
  public void shouldUseProperUrl() throws Exception {
    OrderDto orderDto = generate();
    orderFulfillmentService.create(orderDto);

    Map<String, String> paramsCreate = new HashMap<>();
    paramsCreate.put("access_token", getToken());

    verify(restTemplate, times(1)).postForObject(Matchers.eq(buildUri(
        "https://localhost/api/orders/", paramsCreate)), any(),
        Matchers.eq(OrderDto.class));
  }

  private OrderDto generate() {
    OrderDto order = new OrderDto();
    order.setCreatedById(UUID.randomUUID());
    order.setProgramId(UUID.randomUUID());
    order.setRequestingFacilityId(UUID.randomUUID());
    order.setReceivingFacilityId(UUID.randomUUID());
    order.setSupplyingFacilityId(UUID.randomUUID());
    order.setOrderLineItems(new ArrayList<>());
    return order;
  }

  private String getToken() {
    return "418c89c5-7f21-4cd1-a63a-38c47892b0fe";
  }

  private URI buildUri(String url, Map<String, ?> params) {
    UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uri(URI.create(url));

    params.entrySet().forEach(e -> builder.queryParam(e.getKey(), e.getValue()));

    return builder.build(true).toUri();
  }

}