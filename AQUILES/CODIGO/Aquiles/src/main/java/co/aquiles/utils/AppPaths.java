package co.aquiles.utils;

public interface AppPaths {

	public static final String REST_API_VERSION_1 = "/v1";
	public static final String AQUILES_REST_API_PATH = "/Aquiles";
	public static final String AQUILES_ROOT_CONTEXT = AQUILES_REST_API_PATH + REST_API_VERSION_1;
	public static final String SERVICE_ROOT_CONTEXT = "/Aquiles/v1";
	
	public static final String INVENTARIO = SERVICE_ROOT_CONTEXT + "/productos";
	public static final String MOSTRAR_INVENTARIO = "/inventario";
	public static final String CREAR_PRODUCTO = "/crear";
	public static final String ACTUALIZAR_PRODUCTO = "/actualizar";
	
	public static final String CARRITO = SERVICE_ROOT_CONTEXT + "/carrito";
	public static final String BUSCAR_EN_CARRITO = "/buscar";
	public static final String AGREGAR_CARRITO = "/agregar";
	
	public static final String PRECOMPRA = SERVICE_ROOT_CONTEXT + "/precompra";
	public static final String CONSULTAR_CARRITO = "/consultar";
	
	public static final String PROVEEDORES = SERVICE_ROOT_CONTEXT + "/proveedores";
	public static final String PROVEEDOR = "/consultar";
	
	public static final String COMPRAS = SERVICE_ROOT_CONTEXT + "/compras";
	public static final String COMPRA = "/consultar";
	}	
