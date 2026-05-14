import React, { useState, useEffect } from 'react';
import { apiFetch } from '../api/apiService';
import Modal from '../components/Modal';

const Usuarios = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [usuarioActual, setUsuarioActual] = useState(null);

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    const data = await apiFetch('/api/usuarios');
    if (data) setUsuarios(data);
  };

  const abrirModalDesactivar = (usuario) => {
    setUsuarioActual(usuario);
    setModalOpen(true);
  };

  const desactivarUsuario = async () => {
    if (!usuarioActual) return;
    const res = await apiFetch(`/api/usuarios/${usuarioActual.id}/desactivar`, { method: 'PATCH' });
    if (res !== null) {
      cargarDatos();
      setModalOpen(false);
    } else {
      alert("Error al desactivar el usuario");
    }
  };

  const usuariosFiltrados = usuarios.filter(u => 
    u.nombre?.toLowerCase().includes(busqueda.toLowerCase()) || 
    u.email?.toLowerCase().includes(busqueda.toLowerCase())
  );

  return (
    <div className="page active">
      <div className="search-bar">
        <input 
          type="text" 
          placeholder="Buscar usuario..." 
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn" onClick={cargarDatos}><i className="ti ti-refresh"></i> Recargar</button>
      </div>

      <div className="card">
        <table>
          <thead><tr><th>ID</th><th>Nombre</th><th>Email</th><th>Estado</th><th></th></tr></thead>
          <tbody>
            {usuariosFiltrados.map((u) => (
              <tr key={u.id}>
                <td>U-{u.id}</td>
                <td>{u.nombre || u.name || '—'}</td>
                <td>{u.email || '—'}</td>
                <td>
                  <span className={`badge ${u.activo !== false ? 'badge-green' : 'badge-gray'}`}>
                    {u.activo !== false ? 'Activo' : 'Inactivo'}
                  </span>
                </td>
                <td>
                  {u.activo !== false ? (
                    <i className="ti ti-user-off row-action" title="Desactivar" onClick={() => abrirModalDesactivar(u)}></i>
                  ) : (
                    <span style={{ fontSize: '12px', color: 'var(--color-text-tertiary)' }}>—</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)} title="Desactivar usuario">
        <p style={{ fontSize: '13px', color: 'var(--color-text-secondary)' }}>
          ¿Desactivar a <strong>{usuarioActual?.nombre}</strong>? Esta acción restringirá su acceso al sistema.
        </p>
        <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end', marginTop: '16px' }}>
          <button className="btn" onClick={() => setModalOpen(false)}>Cancelar</button>
          <button className="btn btn-primary" style={{ backgroundColor: 'var(--color-background-danger)', borderColor: 'var(--color-background-danger)' }} onClick={desactivarUsuario}>
            Desactivar
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default Usuarios;