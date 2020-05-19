package annotation.extension;

public class CustomSqlSessionFactory {
	private String datasource;

	public CustomSqlSessionFactory() {
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	@Override
	public String toString() {
		return "CustomSqlSessionFactory{" +
				"datasource='" + datasource + '\'' +
				'}';
	}
}
