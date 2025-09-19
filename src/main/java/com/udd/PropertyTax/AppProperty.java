package com.udd.PropertyTax;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource(value = "classpath:application.properties")

//IF WE ACCESS DATA FROM MORE THAN ONE PROPERTIES FILE
//@PropertySources(value = { @PropertySource(value = { "classpath:application.properties" }),
//		@PropertySource(value = { "classpath:application.properties" }) })

public class AppProperty {
  
//@Autowired	 
//private Environment environment;
//GET DATA FROM ENVIRONMENTS
//environment.getProperty("seafarer.url");

	@Value(value = "${pdf.path}")
	private String pdfPath;
	@Value(value = "${image.path}")
	private String imagePath;
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	
	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	} 

	
	@Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	 
 

}
