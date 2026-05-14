// src/components/Topbar.jsx
import React from 'react';
import { useLocation } from 'react-router-dom';

const Topbar = () => {
  const location = useLocation();

  // Diccionario para mapear la ruta (URL) al título que se mostrará
  const titles = {
    '/': 'Dashboard',
    '/inventario': 'Inventario',
    '/pedidos': 'Pedidos',
    '/envios': 'Envíos',
    '/usuarios': 'Usuarios'
  };

  // Obtenemos el título basado en la URL actual
  const pageTitle = titles[location.pathname] || 'SmartLogix';

  // Determinamos si debemos mostrar el botón "Nuevo" (no se muestra en el Dashboard)
  const showNewButton = location.pathname !== '/';

  return (
    <div className="topbar">
      <div className="topbar-title">{pageTitle}</div>
      <div className="topbar-actions">
        {showNewButton && (
          <button className="btn">
            <i className="ti ti-plus" aria-hidden="true"></i> Nuevo
          </button>
        )}
      </div>
    </div>
  );
};

export default Topbar;