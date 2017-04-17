package evolution;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.junit.Test;

import evolution.entity.AnyEntity;

public class Application {
	public static String underScore(String string) {
		return string;
	}
	
	public static String sqlType(Class<?> javaType) {
		if (javaType == Integer.class || javaType == int.class) {
			return "INT";
		} else if (javaType == String.class) {
			return "VARCHAR(20)";
		}
		return null;
	}
	
	public static String create(Class<?> clazz) {
		if (clazz.getAnnotation(Entity.class) == null) {
			return null;
		}
		StringBuilder create = new StringBuilder("CREATE TABLE ");
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			create.append(underScore(clazz.getSimpleName()) + "(");
		} else {
			create.append(table.name() + "(");
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String underScoredFieldName = underScore(field.getName());
			String sqlType = sqlType(field.getType());
			Column column = field.getAnnotation(Column.class);
			if (field.getAnnotation(Id.class) != null) {
				create.append(underScoredFieldName + " " + sqlType + " PRIMARY KEY ");
				if (field.getAnnotation(GeneratedValue.class) != null) {
					create.append("AUTO_INCREMENT, ");
				}
			} else if (column == null) {
				continue;
			} else {
				String columnName = column.name();
				if ("".equals(columnName)) {
					create.append(underScoredFieldName + " " + sqlType + ", ");
				} else {
					create.append(columnName + " " + sqlType + ", ");
				}
			}
		}
		return create.replace(create.length() - 2, create.length(), "").append(");").toString();
	}
	
	@Test
	public void test() {
		System.out.println(Application.create(AnyEntity.class));
	}
}
