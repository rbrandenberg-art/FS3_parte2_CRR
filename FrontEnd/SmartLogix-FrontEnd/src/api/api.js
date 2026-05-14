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

// Inventario
export const getInventario = () => api.get('/inventario/model');
export const getProductoById = (id) => api.get(`/inventario/model/${id}`);
export const crearProducto = (data) => api.post('/inventario/model', data);
export const actualizarProducto = (id, data) => api.put(`/inventario/model/${id}`, data);
export const eliminarProducto = (id) => api.delete(`/inventario/model/${id}`);

// Usuario
export const loginUsuario = (data) => api.post('/usuario/login', data);
export const registrarUsuario = (data) => api.post('/usuario/register', data);
export const getUsuarioActual = () => api.get('/usuario/me');

// Pedido
export const crearPedido = (data) => api.post('/pedido/model', data);
export const getPedidos = () => api.get('/pedido/model');
export const getPedidoById = (id) => api.get(`/pedido/model/${id}`);

// Envio
export const getEnvioByPedido = (pedidoId) => api.get(`/envio/model/${pedidoId}`);

export default api;