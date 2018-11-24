package com.aradata.plaidapp.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class GridFSConfiguration extends AbstractMongoConfiguration {

	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}

	@Override
	@Bean
	public MongoClient mongoClient() {
		return new MongoClient("localhost");
	}

	@Override
	protected String getDatabaseName() {
		return "test";
	}
}
