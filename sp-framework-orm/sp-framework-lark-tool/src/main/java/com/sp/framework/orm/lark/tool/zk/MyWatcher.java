package com.sp.framework.orm.lark.tool.zk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
/**
 * 默认的监听器
 * @author alexlu
 *
 */
public class MyWatcher implements Watcher {

	protected Log				log					= LogFactory.getLog(getClass());
	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
	
		log.info("default-myWatcher path:"+event.getPath()+" state:"+event.getState());
	}

}
