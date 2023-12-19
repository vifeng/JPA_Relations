package net.codejava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import net.codejava.sale.Order;
import net.codejava.sale.OrderDetail;
import net.codejava.sale.OrderDetailID;
import net.codejava.sale.OrderDetailRepository;
import net.codejava.sale.OrderRepository;
import net.codejava.sale.Product;
import net.codejava.sale.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTests {
	
	@Autowired private OrderDetailRepository orderDetailRepo;
	@Autowired private ProductRepository productRepo;
	@Autowired private OrderRepository orderRepo;

	@Test
	public void testListAll() {
		List<OrderDetail> orderDetails = orderDetailRepo.findAll();
		
		assertThat(orderDetails).isNotEmpty();
		orderDetails.forEach(System.out::println);
	}
	
	@Test
	public void testAddOrderDetail() {
		Product product = new Product();
		product.setName("iPhone 15");
		product.setPrice(1199);
		 
		productRepo.save(product);
		
		Order order = new Order();
		order.setCustomerName("Nam Ha Minh");
		order.setStatus("In Progress");
		
		orderRepo.save(order);
		 
		OrderDetail orderDetail = new OrderDetail();
		 
		OrderDetailID orderDetailId = new OrderDetailID();
		orderDetailId.setOrder(order);
		orderDetailId.setProduct(product);
		 
		orderDetail.setId(orderDetailId);
		orderDetail.setQuantity(2);
		orderDetail.setUnitPrice(1199);
		orderDetail.setSubtotal(2398);

		OrderDetail savedOrderDetail = orderDetailRepo.save(orderDetail);
		
		assertThat(savedOrderDetail).isNotNull();
		assertThat(savedOrderDetail.getId().getProduct()).isEqualTo(product);
		assertThat(savedOrderDetail.getId().getOrder()).isEqualTo(order);
	}
}
