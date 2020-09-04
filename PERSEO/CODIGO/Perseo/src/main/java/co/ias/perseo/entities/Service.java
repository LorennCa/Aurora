package co.ias.perseo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sr_services")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Service implements SREntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer service_id;
	
	@ManyToOne
	@JoinColumn(name = "service_group_id", referencedColumnName = "service_group_id")
	@JsonBackReference
	private ServiceGroup service_group;
	
	@ManyToOne
	@JoinColumn(name = "parent_app_id")
	private Application application;
	
	@Column(name = "service_name")
	@ApiModelProperty(value = "")
	@NotNull
	private String service_name;
	
	@Column(name = "service_owner_id")
	@ApiModelProperty(value = "")
	private Integer service_owner_id;
	
	@Column(name = "service_protocol")
	@ApiModelProperty(value = "")
	private String service_protocol;
	
	@Column(name = "service_host_ip")
	@ApiModelProperty(value = "")
	@NotNull
	private String service_host_ip;
	
	@Column(name = "service_port")
	@ApiModelProperty(value = "")
	@NotNull
	private String service_port;
	
	@Column(name = "service_root_context")
	@ApiModelProperty(value = "")
	@NotNull
	private String service_root_context;
	
	@Column(name = "service_active")
	@NotNull
	private boolean service_active;
	
	@Column(name = "service_description")
	@ApiModelProperty(value = "")
	private String service_description;
	
	@Column(name = "service_author")
	@ApiModelProperty(value = "")
	private String service_author;
	
	@Column(name = "service_it_area")
	@ApiModelProperty(value = "")
	private String service_it_area;
	
	@Column(name = "registration_date")
	@ApiModelProperty(value = "")
	private Date registration_date;
}