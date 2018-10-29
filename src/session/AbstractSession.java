package session;

import java.util.Date;

import namingservice.INamingService;

public class AbstractSession {
	protected String sessionId;
		
		protected Date date;		
		protected INamingService namingService;		
		protected AbstractSession(INamingService ns, String Id) {
			this.namingService = ns;
			this.sessionId = Id;
			this.date = new Date();
		}
		
		public INamingService getNamingService() {
			return this.namingService;
		}
		
		public String getSessionId() {
			return this.sessionId;
		}
		
		public Date getDate() {
			return this.date;
		}
		
}

