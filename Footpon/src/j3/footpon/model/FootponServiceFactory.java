package j3.footpon.model;

public class FootponServiceFactory {
	
	private static IFootponService _service;
	
	public static IFootponService getService(){
		
		if(_service == null){
			_service = new FakeFootponService();
		}
		return _service;
	} 
}
