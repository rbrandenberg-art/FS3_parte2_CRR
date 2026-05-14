// src/App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Topbar from './components/Topbar';
import Dashboard from './pages/Dashboard';
import Inventario from './pages/Inventario';
import Pedidos from './pages/Pedidos';
import Envios from './pages/Envios';
import Usuario from './pages/Usuario';

function App() {
  return (
    <BrowserRouter>
      <div className="app-wrap">
        <div className="app">
          <Sidebar />
          <div className="main">
            <Topbar />
            <div className="content">
              <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/inventario" element={<Inventario />} />
                <Route path="/pedidos" element={<Pedidos />} />
                <Route path="/envios" element={<Envios />} />
                <Route path="/usuario" element={<Usuario />} />
              </Routes>
            </div>
          </div>
        </div>
      </div>
    </BrowserRouter>
  );
}

export default App;