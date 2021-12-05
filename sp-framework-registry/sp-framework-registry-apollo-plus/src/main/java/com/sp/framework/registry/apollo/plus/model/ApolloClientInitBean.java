package com.sp.framework.registry.apollo.plus.model;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApolloClientInitBean implements InitializingBean{
	
	@Autowired(required=false)
	private ConfigChangeListener configChangeListener;

	@Override
	public void afterPropertiesSet() throws Exception {
		String namespace = "notice";
		Config config = ConfigService.getConfig("notice");
		Set<String> names =  config.getPropertyNames();
		Map<String,ConfigChange> changes = getConfigChange(names);
		ConfigChangeEvent changeEvent = new ConfigChangeEvent(namespace, changes);
		if(names != null) {
			configChangeListener.onChange(changeEvent);
		}
	}

	public Map<String,ConfigChange> getConfigChange(Set<String> dataChangePaths){
		Map<String,ConfigChange> changes = null;
		if(dataChangePaths != null) {
			changes = new HashMap<String, ConfigChange>();
			Config config = ConfigService.getConfig("notice");
			for(String data:dataChangePaths) {
				String value = config.getProperty(data, "");
				ConfigChange dataChange = new ConfigChange("notice", data, value, value, PropertyChangeType.MODIFIED);
				changes.put(data, dataChange);
			}
		}

		return changes;
	}
}
