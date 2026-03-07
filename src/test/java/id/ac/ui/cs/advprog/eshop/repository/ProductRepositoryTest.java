package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {
    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testCreateProductNoID() throws Exception {
        ProductRepository productRepository = new ProductRepository();
        Product product = new Product();
        product.setProductName("Sampo Cap Usep");
        product.setProductQuantity(50);
        product.setProductId(null);

        Product createdProduct = productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        assertNotNull(createdProduct.getProductId());
        assertFalse(createdProduct.getProductId().isEmpty());
        assertEquals(product.getProductId(), createdProduct.getProductId());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindByIdNotFound() {
        Product result = productRepository.findById("random-id");
        assertNull(result);
    }

    @Test
    void testFindByIdAfterSkipping() {
        Product first = new Product();
        first.setProductId("id-1");
        productRepository.create(first);

        Product second = new Product();
        second.setProductId("id-2");
        productRepository.create(second);

        Product result = productRepository.findById("id-2");
        assertEquals("id-2", result.getProductId());
    }

    @Test
    void testEditWithEmptyRepository() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang");

        Product result = productRepository.update("random-id", updatedProduct);
        assertNull(result);
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(50);
        productRepository.update(product.getProductId(), updatedProduct);

        Product result = productRepository.findById(product.getProductId());
        assertEquals("Sampo Cap Usep", result.getProductName());
        assertEquals(50, result.getProductQuantity());
    }

    @Test
    void testEditProductAfterSkipping() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Cahyo");
        updatedProduct.setProductQuantity(75);
        productRepository.update(product2.getProductId(), updatedProduct);

        Product result = productRepository.findById(product2.getProductId());
        assertEquals("Sampo Cap Cahyo", result.getProductName());
        assertEquals(75, result.getProductQuantity());
    }

    @Test
    void testEditProductNotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(50);

        Product result = productRepository.update("null-id", updatedProduct);
        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);
        productRepository.delete("eb558e9f-1c39-460e-8860-71af6af63bd6");

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductNotFound() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete("random-id");

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
    }
}