// src/components/InventarioList.jsx
import React, { useEffect, useState } from 'react';
import { getInventario } from '../api/api';

const InventarioList = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await getInventario();
      setItems(response.data);
    } catch (error) {
      console.error("Error cargando inventario:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="p-10 text-center">Cargando SmartLogix...</div>;

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-blue-900">SmartLogix - Inventario</h1>
        <p className="text-gray-600">Gestión centralizada de productos</p>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {items.map((item) => (
          <div key={item.id} className="bg-white p-6 rounded-lg shadow-md border-t-4 border-blue-500">
            <span className="text-xs font-bold text-gray-400 uppercase">ID: {item.id}</span>
            <h2 className="text-xl font-semibold text-gray-800 mt-1">{item.nombre || 'Producto'}</h2>
            <p className="text-gray-600 mt-2">{item.descripcion || 'Sin descripción disponible.'}</p>
            <div className="mt-4 flex justify-between items-center">
              <span className="text-blue-600 font-bold">${item.precio}</span>
              <span className={`px-3 py-1 rounded-full text-sm ${item.stock > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                Stock: {item.stock}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default InventarioList;