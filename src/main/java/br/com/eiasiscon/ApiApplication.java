package br.com.eiasiscon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.Decimal128;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@SpringBootApplication
@EnableConfigurationProperties
public class ApiApplication extends SpringBootServletInitializer {

	  @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(ApiApplication.class);
	  }

	public static void main(String[] arg) {
		SpringApplication.run(ApiApplication.class, arg);
	}
	
	@ReadingConverter
	public class Decimal128ReadConverter implements Converter<Decimal128, BigDecimal> {
		  
		  public BigDecimal convert(Decimal128 s) {
              return s==null ? null : s.bigDecimalValue();
          }
	}
	/**
	@Bean
    CustomConversions customConverions() {
		
        Converter<Decimal128, BigDecimal> decimal128ToBigDecimal = new Converter<Decimal128, BigDecimal>() {
            @Override
            public BigDecimal convert(Decimal128 s) {
                return s==null ? null : s.bigDecimalValue();
            }
        };
        
        Converter<BigDecimal, Decimal128> bigDecimalToDecimal128 = new Converter<BigDecimal, Decimal128>() {
            @Override
            public Decimal128 convert(BigDecimal s) {
                return s==null ? null : new Decimal128(s);
            }
        };
        
        return new CustomConversions(Arrays.asList(decimal128ToBigDecimal, bigDecimalToDecimal128));
    }*/
	
	@Bean
    public MongoCustomConversions customConversions(){
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(BigDecimalWriterConverter.INSTANCE);
        converters.add(BigDecimalReaderConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @WritingConverter
    enum BigDecimalWriterConverter implements Converter<BigDecimal, Decimal128> {
        INSTANCE;
		@Override
		public Decimal128 convert(BigDecimal s) {
            return s==null ? null : new Decimal128(s);
        }
    }
    
    @ReadingConverter
    enum BigDecimalReaderConverter implements Converter<Decimal128, BigDecimal> {
        INSTANCE;
		@Override
		public BigDecimal convert(Decimal128 s) {
            return s==null ? null : s.bigDecimalValue();
        }
    }
    
}
