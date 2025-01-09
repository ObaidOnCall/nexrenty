package ma.crm.carental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import ma.crm.carental.conf.HintsConfig;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableAspectJAutoProxy
@EnableFeignClients
@ImportRuntimeHints(HintsConfig.class)
public class CarentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarentalApplication.class, args);
	}

}
