// src/components/Sidebar.jsx
import React from 'react';
import { NavLink } from 'react-router-dom';

const Sidebar = () => {
  // Función auxiliar para mantener limpio el código de las clases
  const navClass = ({ isActive }) => `nav-item ${isActive ? 'active' : ''}`;

  return (
    <div className="sidebar">
      <div className="logo">
        <div className="logo-text">SmartLogix</div>
        <div className="logo-sub">Panel de control</div>
      </div>
      
      <nav className="nav">
        {/* Usamos 'to="/"' para la ruta principal */}
        <NavLink to="/" className={navClass}>
          <i className="ti ti-layout-dashboard" aria-hidden="true"></i> Dashboard
        </NavLink>
        
        <NavLink to="/inventario" className={navClass}>
          <i className="ti ti-box" aria-hidden="true"></i> Inventario
        </NavLink>
        
        <NavLink to="/pedidos" className={navClass}>
          <i className="ti ti-clipboard-list" aria-hidden="true"></i> Pedidos
        </NavLink>
        
        <NavLink to="/envios" className={navClass}>
          <i className="ti ti-truck" aria-hidden="true"></i> Envíos
        </NavLink>
        
        <NavLink to="/usuarios" className={navClass}>
          <i className="ti ti-users" aria-hidden="true"></i> Usuarios
        </NavLink>
      </nav>
    </div>
  );
};

export default Sidebar;