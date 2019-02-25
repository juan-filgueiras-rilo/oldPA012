package es.udc.paproject.backend.model.entities;

import javax.persistence.GenerationType;

public class Product {
	
	private Long id;
	private String name;
	private String descriptionProduct;
	private long bidTime;
	private float initialPrice;
	private String shipmentInfo;
	private Category category;
	private String descriptionShipmentInfo; 
	
	
	public Product() {
		
	}
	
	public Product(Long id, String name, String descriptionProduct, long bidTime, float initialPrice, String shipmentInfo,
			Category category, String descriptionShipmentInfo) {
		this.id = id;
		this.name = name;
		this.descriptionProduct = descriptionProduct; 
		this.bidTime = bidTime;
		this.initialPrice = initialPrice;
		this.shipmentInfo = shipmentInfo;
		this.category = category;
		this.descriptionShipmentInfo = descriptionShipmentInfo;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long Id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptionProduct() {
		return descriptionProduct;
	}

	public void setDescriptionProduct(String descriptionProduct) {
		this.descriptionProduct = descriptionProduct;
	}

	public long getBidTime() {
		return bidTime;
	}

	public void setBidTime(long bidTime) {
		this.bidTime = bidTime;
	}

	public float getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(float initialPrice) {
		this.initialPrice = initialPrice;
	}

	public String getShipmentInfo() {
		return shipmentInfo;
	}

	public void setShipmentInfo(String shipmentInfo) {
		this.shipmentInfo = shipmentInfo;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescriptionShipmentInfo() {
		return descriptionShipmentInfo;
	}

	public void setDescriptionShipmentInfo(String descriptionShipmentInfo) {
		this.descriptionShipmentInfo = descriptionShipmentInfo;
	}

	
}
