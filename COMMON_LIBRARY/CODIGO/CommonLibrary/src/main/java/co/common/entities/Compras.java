package co.common.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "compras")
@ApiModel("Model Proveedor")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Compras implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idCompra;
	
	@Column(name = "producto")
	private String producto;
	
	@Column(name = "producto_llego")
	private String proLlego;
	
	@Column(name = "fecha_compra")
	private Date fecCompra;
	
	@Column(name = "fecha_llegada")
	private Date fecLlegada;
	
	@Column(name = "total_dias")
	private int totDias;
	
	@Column(name = "valor_pagado_x_uni")
	private double valPagadoUni;
	
	@Column(name = "cantidad")
	private int cantidad;
	
	@Column(name = "total_pagado")
	private int totPagado;
}
