package evolution;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
	
	public static List<String> createTables(String path, String basePath) {
		List<String> createStatements = new LinkedList<>();
		List<Class<?>> classes = null;
		classes = FileUtil.classes(path, basePath, Arrays.asList(Entity.class), classes);
		for (Class<?> clazz : classes) {
			createStatements.add(createTable(clazz));
		}
		return createStatements;
	}
	
	@Test
	public void testCreateTables() {
		String basePath = "/Users/chenli/Desktop/Playground/Git/Pojo_Table/Pojo_Table/src/main/java";
		String path = "/Users/chenli/Desktop/Playground/Git/Pojo_Table/Pojo_Table/src/main/java/evolution";
		List<String> sqls = createTables(path, basePath);
		System.out.println(sqls);
	}
	
	public static StringBuilder removeComma(StringBuilder stringBuilder) {
		int length = stringBuilder.length();
		return stringBuilder.replace(length - 2, length, "");
	}
	
	public static String createTable(Class<?> clazz) {
		if (clazz.getAnnotation(Entity.class) == null) {
			return null;
		}
		StringBuilder createStatement = new StringBuilder("CREATE TABLE ");
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			createStatement.append(underScore(clazz.getSimpleName()) + "(");
		} else {
			createStatement.append(table.name() + "(");
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String underScoredFieldName = underScore(field.getName());
			String sqlType = sqlType(field.getType());
			Column column = field.getAnnotation(Column.class);
			if (field.getAnnotation(Id.class) != null) {
				createStatement.append(underScoredFieldName + " " + sqlType + " PRIMARY KEY, ");
				if (field.getAnnotation(GeneratedValue.class) != null) {
					removeComma(createStatement).append(" AUTO_INCREMENT, ");
				}
			} else if (column == null) {
				continue;
			} else {
				String columnName = column.name();
				if ("".equals(columnName)) {
					createStatement.append(underScoredFieldName + " " + sqlType + ", ");
				} else {
					createStatement.append(columnName + " " + sqlType + ", ");
				}
			}
		}
		return removeComma(createStatement).append(");").toString();
	}
	
	@Test
	public void testCreateTable() {
		System.out.println(Application.createTable(AnyEntity.class));
	}
}
