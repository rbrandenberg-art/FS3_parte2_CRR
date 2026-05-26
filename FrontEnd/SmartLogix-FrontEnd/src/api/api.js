import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// ==========================================
// MICROSERVICIO: INVENTARIO
// ==========================================
const INVENTARIO_URL = '/api/inventario/inventario';

export const getInventario = () => api.get(INVENTARIO_URL);
export const getProductoById = (id) => api.get(`${INVENTARIO_URL}/${id}`);
export const crearProducto = (data) => api.post(INVENTARIO_URL, data);
export const actualizarProducto = (id, data) => api.put(`${INVENTARIO_URL}/${id}`, data);
export const eliminarProducto = (id) => api.delete(`${INVENTARIO_URL}/${id}`);

// Nuevos endpoints basados en tu InventarioController
export const verificarStock = (id, cantidad) => api.get(`${INVENTARIO_URL}/${id}/stock`, { params: { cantidad } });
export const reducirStock = (id, cantidad) => api.patch(`${INVENTARIO_URL}/${id}/reducir-stock`, null, { params: { cantidad } });

// ==========================================
// MICROSERVICIO: PEDIDO
// ==========================================
const PEDIDO_URL = '/api/pedidos';

export const getPedidos = () => api.get(PEDIDO_URL);
export const getPedidoById = (id) => api.get(`${PEDIDO_URL}/${id}`);
export const crearPedido = (data) => api.post(PEDIDO_URL, data);

// Nuevos endpoints basados en tu PedidoController
export const getPedidosByUsuario = (usuarioId) => api.get(`${PEDIDO_URL}/usuario/${usuarioId}`);
export const cambiarEstadoPedido = (id, estado) => api.patch(`${PEDIDO_URL}/${id}/estado`, null, { params: { estado } });
export const cancelarPedido = (id) => api.patch(`${PEDIDO_URL}/${id}/cancelar`);

// ==========================================
// MICROSERVICIO: USUARIO
// ==========================================
const USUARIO_URL = '/api/usuarios';

export const getUsuarios = () => api.get(USUARIO_URL);
export const getUsuarioById = (id) => api.get(`${USUARIO_URL}/${id}`);
export const getUsuarioByEmail = (email) => api.get(`${USUARIO_URL}/email/${email}`);
export const registrarUsuario = (data) => api.post(`${USUARIO_URL}/registrar`, data);
export const actualizarUsuario = (id, data) => api.put(`${USUARIO_URL}/${id}`, data);
export const desactivarUsuario = (id) => api.patch(`${USUARIO_URL}/${id}/desactivar`);

// Nota sobre Seguridad: Tu controlador no tiene un endpoint explícito para /login o /me. 
// Esto es normal si usas Spring Security (UsernamePasswordAuthenticationFilter).
// Mantengo esta ruta estándar asumiendo que tu filtro de seguridad escucha aquí:
export const loginUsuario = (data) => api.post(`${USUARIO_URL}/login`, data);
export const getUsuarioActual = () => api.get(`${USUARIO_URL}/me`);

// ==========================================
// MICROSERVICIO: ENVIO
// ==========================================
const ENVIO_URL = '/api/envios';

export const getEnvios = () => api.get(ENVIO_URL);
export const getEnvioById = (id) => api.get(`${ENVIO_URL}/${id}`);
export const getEnvioByPedido = (pedidoId) => api.get(`${ENVIO_URL}/pedido/${pedidoId}`);
export const rastrearEnvio = (numeroSeguimiento) => api.get(`${ENVIO_URL}/rastrear/${numeroSeguimiento}`);

// Atención aquí: Axios envía el 'data' en el body, y el 'tipo' en la URL como ?tipo=EXPRES
export const crearEnvio = (data, tipo) => api.post(ENVIO_URL, data, { params: { tipo } });

// Petición PATCH con un @RequestParam
export const cambiarEstadoEnvio = (id, estado) => api.patch(`${ENVIO_URL}/${id}/estado`, null, { params: { estado } });
export default api;