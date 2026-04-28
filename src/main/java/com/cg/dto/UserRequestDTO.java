package com.cg.dto;


public class UserRequestDTO {

    private String name;        
    private String email;       
    private Integer addressId;  

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
	public UserRequestDTO(String name, String email, Integer addressId) {
		super();
		this.name = name;
		this.email = email;
		this.addressId = addressId;
	}
	public UserRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}