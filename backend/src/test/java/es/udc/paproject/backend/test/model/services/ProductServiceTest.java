package es.udc.paproject.backend.test.model.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Category;
import es.udc.paproject.backend.model.entities.CategoryDao;
import es.udc.paproject.backend.model.entities.Product;
import es.udc.paproject.backend.model.entities.ProductDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.ProductService;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceTest {
	
	private final Long NON_EXISTENT_ID = new Long(-1);
	
	@Autowired 
	private ProductDao productDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	

	private Product createProduct(String name, long duration, BigDecimal initialPrice,
			Category category, User user) {	
		return new Product(name, "descriptionProduct", duration, initialPrice,
			"shipmentInfo", category, user);
	}
	
	private User signUpUser(String userName) {
		
		User user = new User(userName, "password", "firstName", "lastName", userName + "@" + userName + ".com");
		
		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}
		
		return user;
		
	}
	//Añadimos un producto
	@Test
	public void addProductTest() throws InstanceNotFoundException {
		
		Category category1 = new Category("category1");
		Category category2 = new Category("category2");
		categoryDao.save(category1);
		categoryDao.save(category2);
		
		User user1 = signUpUser("user1");

		Product product = productService.addProduct(user1.getId(), "nombre", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
		
		
		Product productD = productDao.save(product);
		//productDao.save(product2);
		assertEquals(productD, product);
		
	}
	
	//Buscar un producto por ID que no exista
	/*@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentProduct() throws InstanceNotFoundException {
		productService.findProducts(NON_EXISTENT_ID, "wefwe", 0, 10);
	}*/
	
	//Buscar el producto por Keywords
	@Test
	public void testFindProductsByKeywords() throws InstanceNotFoundException {
		
		Category category1 = new Category("category1");
		categoryDao.save(category1);
		
		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		User user3 = signUpUser("user3");
		
		Product product = productService.addProduct(user1.getId(), "product 1", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
	
		Product product2 = productService.addProduct(user2.getId(), "X Product", "descripcion 2", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
	
		Product product3 = productService.addProduct(user3.getId(), "another", "descripcion 3", (long)10, 
				new BigDecimal(10), "Info", category1.getId());

		
		Block<Product> expectedBlock = new Block<>(Arrays.asList(product, product2), false);
		assertEquals(expectedBlock, productService.findProducts(null, "PrOdu", 0, 2));
		
	}
	
	//Buscar un producto un producto por categorias
	@Test
	public void testFindProductsByCategory() throws InstanceNotFoundException {
		
		Category category1 = new Category("category1");
		Category category2 = new Category("category2");
		categoryDao.save(category1);
		categoryDao.save(category2);
		
		User user1 = signUpUser("user1");

		Product product = productService.addProduct(user1.getId(), "product1", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
		
		Product product2 = productService.addProduct(user1.getId(), "product2", "descripcion 2", (long)10, 
				new BigDecimal(10), "Info", category2.getId());
		
		Block<Product> expectedBlock = new Block<>(Arrays.asList(product), false);
		
		assertEquals(expectedBlock, productService.findProducts(category1.getId(), null, 0, 1));
	}
	
	//Si buscamos producto y no ponemos ni keywords ni categoria nos muestra todos los productos
	@Test
	public void testFindAllProducts() throws InstanceNotFoundException {
		
		Category category1 = new Category("category1");
		Category category2 = new Category("category2");
		categoryDao.save(category1);
		categoryDao.save(category2);
		
		User user1 = signUpUser("user1");
		
		Product product = productService.addProduct(user1.getId(), "product1", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
	
		Product product2 = productService.addProduct(user1.getId(), "product2", "descripcion 2", (long)10, 
				new BigDecimal(10), "Info", category2.getId());
		
		Block<Product> expectedBlock = new Block<>(Arrays.asList(product, product2), false);
		
		assertEquals(expectedBlock, productService.findProducts(null, "", 0, 2));
		assertEquals(expectedBlock, productService.findProducts(null, null, 0, 2));
	}
	
	
	//Obtener las categorias
	//Mirar!!
	@Test 
	public void testFindAllCategories() {
		
		Category category1 = new Category("category1");
		Category category2 = new Category("category2");
		
		categoryDao.save(category2);
		categoryDao.save(category1);
		
		assertEquals(Arrays.asList(category1, category2), productService.findAllCategories());
		
	}
	
	 
	
	//Obtener el detalle de los productos
	@Test
	public void testGetProductDetail() throws InstanceNotFoundException {
		Category category1 = new Category("category1");
		categoryDao.save(category1);
		
		User user1 = signUpUser("user1");
		
		Product product1 = productService.addProduct(user1.getId(), "product1", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
		
		Product expectedProduct = productService.getProductDetail(product1.getId());
		
		assertEquals(product1,expectedProduct);
		
		//assertEquals(product1, expectedProduct);
		//assertEquals(product1.getName(), expectedProduct.getName());
		//assertEquals(product1.getCategory(), expectedProduct.getCategory());
		//assertEquals(product1.getDescriptionProduct(), expectedProduct.getDescriptionProduct());
		
	}
	
	//Consultar los productos anunciados 
	@Test
	public void testGetUserProducts() throws InstanceNotFoundException {
		
		Category category1 = new Category("category1");
		categoryDao.save(category1);
		
		User user1 = signUpUser("user1");

		Product product1 = productService.addProduct(user1.getId(), "product1", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
		
		Product product2 = productService.addProduct(user1.getId(), "product2", "descripcion", (long)10, 
				new BigDecimal(10), "Info", category1.getId());
		
		Block<Product> blockExpected = new Block<>(Arrays.asList(product1, product2), false);
				
		assertEquals(blockExpected, productService.getUserProducts(user1.getId(), 0, 2));
	}
	
}
