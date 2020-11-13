package co.aurora.utils;

public interface AppPaths {

	public static final String REST_API_VERSION_1 = "/v1";
	public static final String SEE_REST_API_PATH = "/Aurora";
	public static final String SEE_ROOT_CONTEXT = SEE_REST_API_PATH + REST_API_VERSION_1;
	public static final String SERVICE_ROOT_CONTEXT = "/Aurora/v1";
	
	public static final String INVENTARIO = SERVICE_ROOT_CONTEXT + "/productos";
	public static final String MOSTRAR_INVENTARIO = "/inventario";
	public static final String CREAR_PRODUCTO = "/crear";
	public static final String ACTUALIZAR_PRODUCTO = "/actualizar";
	
	public static final String CARRITO = SERVICE_ROOT_CONTEXT + "/carrito";
	public static final String BUSCAR_EN_CARRITO = "/buscar";
	public static final String AGREGAR_CARRITO = "/agregar";
	public static final String CONSULTAR_CARRITO = "/consultar";
	
	public static final String PEDIDOS = SERVICE_ROOT_CONTEXT + "/pedidos";
	public static final String PEDIDO_CONFIRMADO = "/pedido-confirmado";
	
	public static final String PRECOMPRA = SERVICE_ROOT_CONTEXT + "/precompra";
	
	public static final String ACCESORIOS = SERVICE_ROOT_CONTEXT + "/accesorios";
	public static final String BUSCAR_EN_ACCESORIOS = "/buscar";
	public static final String AGREGAR_ACCESORIO = "/agregar"; 
	
	public static final String DETALLES = SERVICE_ROOT_CONTEXT + "/detalles";
	public static final String CONSULTAR_DETALLES = "/consultar";
	
	public static final String COMPRAS = SERVICE_ROOT_CONTEXT + "/compras";
	public static final String CONFIRMAR = "/confirmar-compra";
	}	
