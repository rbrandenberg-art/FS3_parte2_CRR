// src/api/inventarioService.js
import { apiFetch } from './apiService';

// Aquí centralizamos las rutas específicas de este microservicio
export const InventarioAPI = {
  obtenerTodos: () => apiFetch('/api/inventario/inventario'),
  crear: (producto) => apiFetch('/api/inventario/inventario', { method: 'POST', body: JSON.stringify(producto) }),
  reducirStock: (id, cantidad) => apiFetch(`/api/inventario/inventario/${id}/reducir-stock?cantidad=${cantidad}`, { method: 'PATCH' })
};