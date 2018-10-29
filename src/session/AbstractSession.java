package session;

import namingservice.INamingService;

public abstract class AbstractSession {
	protected INamingService namingservice;
	
	public AbstractSession(INamingService namingservice){
		this.namingservice = namingservice;
	}
}
