package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("first-id");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreate() {
        when(productRepository.create(product)).thenReturn(product);
        Product result = productService.create(product);

        assertEquals(product.getProductId(), result.getProductId());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAll() {
        Product product2 = new Product();
        product2.setProductId("second-id");

        List<Product> productList = Arrays.asList(product, product2);
        Iterator<Product> iterator = productList.iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();
        assertEquals(2, result.size());
        assertEquals(product.getProductId(), result.get(0).getProductId());
        assertEquals("second-id", result.get(1).getProductId());
    }

    @Test
    void testFindById() {
        when(productRepository.findById("first-id")).thenReturn(product);

        Product result = productService.findById("first-id");
        assertNotNull(result);
        assertEquals("Sampo Cap Bambang", result.getProductName());
    }

    @Test
    void testUpdate() {
        productService.update(product.getProductId(), product);
        verify(productRepository, times(1)).update(product.getProductId(), product);
    }

    @Test
    void testDelete() {
        productService.deleteProductById(product.getProductId());
        verify(productRepository, times(1)).delete(product.getProductId());
    }
}