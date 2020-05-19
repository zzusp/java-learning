package annotation.config;

/**
 * @author Aaron.Sun
 * @description 模拟数据源配置
 * @date Created in 18:20 2020/5/18
 * @modified By
 */
public class DatasourceConfig {

	private String url;
	private String name;
	private String password;

	public DatasourceConfig() {
		System.out.println("url: " + url + " name: " + name + " password: " + password);
	}

	public String createDatasource() {
		return "url: " + url + " name: " + name + " password: " + password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DatasourceConfig{" +
				"url='" + url + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	public void init() {
		System.out.println("自定义配置方法--DatasourceConfig--初始化--init");
	}

	public void cleanup() {
		System.out.println("自定义配置方法--DatasourceConfig--销毁--cleanup");
	}
}
