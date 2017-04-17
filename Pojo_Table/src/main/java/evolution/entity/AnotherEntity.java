package evolution.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AnotherEntity {
	@Id
	private String id;
	@Column(name = "name")
	private String name;
	private Date date;
}
