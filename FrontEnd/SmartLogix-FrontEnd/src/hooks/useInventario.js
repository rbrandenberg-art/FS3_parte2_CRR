// src/hooks/useInventario.js
import { useState, useEffect } from 'react';
import { InventarioAPI } from '../api/inventarioService';

export const useInventario = () => {
  const [productos, setProductos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  // Cargar datos iniciales
  const cargarProductos = async () => {
    setCargando(true);
    try {
      const data = await InventarioAPI.obtenerTodos();
      if (data) setProductos(data);
    } catch (err) {
      setError("Error al cargar el inventario");
    } finally {
      setCargando(false);
    }
  };

  useEffect(() => {
    cargarProductos();
  }, []);

  // Métodos expuestos por la Fachada
  const agregarProducto = async (nuevoProducto) => {
    const exito = await InventarioAPI.crear(nuevoProducto);
    if (exito) await cargarProductos(); // Recarga la tabla automáticamente
    return exito;
  };

  const venderProducto = async (id, cantidad) => {
    const exito = await InventarioAPI.reducirStock(id, cantidad);
    if (exito) await cargarProductos();
    return exito;
  };

  // Esto es lo ÚNICO que el componente UI podrá ver
  return {
    productos,
    cargando,
    error,
    recargar: cargarProductos,
    agregarProducto,
    venderProducto
  };
};