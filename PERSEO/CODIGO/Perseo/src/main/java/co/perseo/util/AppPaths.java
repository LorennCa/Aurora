package co.perseo.util;

public interface AppPaths {
	public static final String SERVICE_REGISTRY_ROOT_CONTEXT = "/perseo";
	public static final String API_VERSION = "/v1";
	public static final String SERVICE_REGISTRY_API_RESOURCE = SERVICE_REGISTRY_ROOT_CONTEXT + API_VERSION;
	public static final String SR_SERVICES_LIST_BY_APP_PATH = "/getServicesByApp/{appId}";
	public static final String SR_SERVICES_BY_ID = "/serviceById/{serviceId}";
	public static final String SR_SERVICE_REGISTRATION_PATH = "/registerService";
	public static final String SR_SERVICE_DEREGISTRATION_PATH = "/deregisterService/{serviceId}";
	public static final String SR_SERVICE_GROUP_REGISTRATION_PATH = "/registerServiceGroup";
	public static final String SR_APPLICATION_REGISTRATION_PATH = "/registerApplication";
	public static final String PUBLISH_SERVICE_RESOURCE_URL = "/publishService";
	public static final String SR_ALL_SERVICES_PATH = "/services";
}