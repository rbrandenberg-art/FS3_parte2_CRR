import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // Puerto de tu Api_Gateway
});

export const getInventario = () => api.get('/inventario/model'); 
// Ajusta la ruta '/inventario/model' según tus RequestMapping en Spring