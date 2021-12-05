package com.sp.framework.registry.apollo.plus.manager;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.sp.framework.utilities.type.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("apolloUpdateDataManager")
public class ApolloUpdateDataManagerImpl implements ApolloUpdateDataManager {

	//当前appid
	@Value("${app.id}")
	private String appId;
	
	//当前环境
	@Value("${env}")
	private String env;
	
	ApolloOpenApiClient client =  null;
	
	private ApolloOpenApiClient getClient() {
		Config config = ConfigService.getAppConfig();
		String token = config.getProperty("app.token", "");
		String portalUrl = config.getProperty("portal.url", "");
		
		if(client == null) {
			return ApolloOpenApiClient.newBuilder()
			        .withPortalUrl(portalUrl)
			        .withToken(token)
			        .build();
		}else {
			return client;
		}
	}
	
	private String getChangeBy(){
		Config config = ConfigService.getAppConfig();
		return  config.getProperty("change.by", "");
	}
	
	@Override
	public void updateOrCreateItem(String key,String value) throws Exception {
		updateOrCreateItem(key, value, "", "notice");
	}
	
	@Override
	public void updateOrCreateItem(String key,String value,String clusterName,String namespaceName) throws Exception{
		try {
			client = getClient();
			OpenItemDTO dto = new OpenItemDTO();
			dto.setKey(key);
			dto.setValue(value);
			dto.setDataChangeLastModifiedTime(new Date());
			String changeBy = getChangeBy();
			dto.setDataChangeLastModifiedBy(changeBy);
			dto.setDataChangeCreatedBy(changeBy);
			
			if(StringUtil.isEmpty(namespaceName)) {
				namespaceName = "notice";
			}
			
			client.createOrUpdateItem(appId, env, clusterName, namespaceName, dto);;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void updateOrCreateItemAndPush(String key,String value) throws Exception {
		String changeBy = getChangeBy();
		String namespace = "notice";
		updateOrCreateItem(key, value, "", namespace);
		String releaseTitle = changeBy +" change: " + key +" =" + value;
		publishNamespace(releaseTitle,"",namespace);
	}
	
	@Override
	public void updateOrCreateItemAndPush(String key,String value,String clusterName,String namespaceName) throws Exception {
		if(StringUtil.isEmpty(namespaceName)) {
			namespaceName = "notice";
		}
		String changeBy = getChangeBy();
		updateOrCreateItem(key, value, clusterName, namespaceName);
		String releaseTitle = changeBy +" change: " + key +" =" + value;
		publishNamespace(releaseTitle,clusterName,namespaceName);
	}
	@Override
	public void publishNamespace(String releaseTitle,String clusterName,String namespaceName) {
		client = getClient();
		if(StringUtil.isEmpty(namespaceName)) {
			namespaceName = "notice";
		}
		String changeBy = getChangeBy();
		NamespaceReleaseDTO dto = new NamespaceReleaseDTO();
		dto.setReleaseTitle(releaseTitle);
		dto.setReleasedBy(changeBy);
		client.publishNamespace(appId, env, clusterName, namespaceName, dto);
	}
	
	@Override
	public void publishNamespace(String releaseTitle) {
		client = getClient();
		String changeBy = getChangeBy();
		NamespaceReleaseDTO dto = new NamespaceReleaseDTO();
		dto.setReleaseTitle(releaseTitle);
		dto.setReleasedBy(changeBy);
		client.publishNamespace(appId, env, "", "notice", dto);
	}
	
}
