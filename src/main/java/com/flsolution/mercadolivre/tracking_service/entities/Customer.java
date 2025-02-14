package com.flsolution.mercadolivre.tracking_service.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends EntityBase {

	private static final long serialVersionUID = 4617900183301692612L;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String document;
	
	@Column(nullable = false)
	private String phoneNumber;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String address;
	
	@OneToMany(mappedBy = "customer")
	private List<Pack> packs;

}
