package evolution.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "any_entity")
public class AnyEntity {
	@Id
	@GeneratedValue
	private Integer id;
	@Column
	private String name;
	@Column
	private String gender;
	@Column(name = "dept")
	private String department;
}
