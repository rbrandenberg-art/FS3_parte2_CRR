// src/pages/Inventario.jsx
import React, { useState } from 'react';
import { useInventario } from '../hooks/useInventario'; // Importamos la Fachada

const Inventario = () => {
  // Consumimos la Fachada. El componente no sabe CÓMO llegan los datos, solo los usa.
  const { productos, cargando, error, recargar, agregarProducto } = useInventario();
  const [busqueda, setBusqueda] = useState('');

  if (cargando) return <div>Cargando inventario de SmartLogix...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="page active">
      <div className="search-bar">
        <input 
          type="text" 
          placeholder="Buscar producto..." 
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn" onClick={recargar}>Recargar</button>
      </div>

      <div className="card">
        <table>
          <thead>
             <tr><th>ID</th><th>Nombre</th><th>Stock</th></tr>
          </thead>
          <tbody>
            {productos.map(p => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.nombre}</td>
                <td>{p.cantidad}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Inventario;