import React, { useState, useEffect } from 'react';
import { apiFetch } from '../api/apiService';
import Modal from '../components/Modal';

const Pedidos = () => {
  const [pedidos, setPedidos] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [pedidoActual, setPedidoActual] = useState(null);
  const [nuevoEstado, setNuevoEstado] = useState('PENDIENTE');

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    const data = await apiFetch('/api/pedidos');
    if (data) setPedidos(data);
  };

  const abrirModalEstado = (pedido) => {
    setPedidoActual(pedido);
    setNuevoEstado(pedido.estado || 'PENDIENTE');
    setModalOpen(true);
  };

  const actualizarEstado = async () => {
    if (!pedidoActual) return;
    const res = await apiFetch(`/api/pedidos/${pedidoActual.id}/estado?estado=${nuevoEstado}`, { method: 'PATCH' });
    if (res) {
      cargarDatos();
      setModalOpen(false);
    } else {
      alert("Error al actualizar el estado");
    }
  };

  const pedidosFiltrados = pedidos.filter(p => 
    p.id?.toString().includes(busqueda) || 
    p.usuarioId?.toString().includes(busqueda)
  );

  const badgeMap = { COMPLETADO: 'badge-green', EN_PROCESO: 'badge-amber', PENDIENTE: 'badge-blue', CANCELADO: 'badge-red' };

  return (
    <div className="page active">
      <div className="search-bar">
        <input 
          type="text" 
          placeholder="Buscar pedido por ID..." 
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn" onClick={cargarDatos}><i className="ti ti-refresh"></i> Recargar</button>
      </div>

      <div className="card">
        <table>
          <thead><tr><th>ID</th><th>Cliente ID</th><th>Fecha</th><th>Total</th><th>Estado</th><th></th></tr></thead>
          <tbody>
            {pedidosFiltrados.map((p) => (
              <tr key={p.id}>
                <td>#{p.id}</td>
                <td>U-{p.usuarioId || '?'}</td>
                <td>{p.fecha || '—'}</td>
                <td>{p.total ? `$${p.total.toLocaleString('es-CL')}` : '—'}</td>
                <td><span className={`badge ${badgeMap[p.estado] || 'badge-gray'}`}>{p.estado || '—'}</span></td>
                <td><i className="ti ti-edit row-action" onClick={() => abrirModalEstado(p)}></i></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)} title={`Cambiar estado — Pedido #${pedidoActual?.id}`}>
        <div className="form-row">
          <div className="form-label">Nuevo estado</div>
          <select className="form-control" value={nuevoEstado} onChange={(e) => setNuevoEstado(e.target.value)}>
            <option value="PENDIENTE">PENDIENTE</option>
            <option value="EN_PROCESO">EN_PROCESO</option>
            <option value="COMPLETADO">COMPLETADO</option>
            <option value="CANCELADO">CANCELADO</option>
          </select>
        </div>
        <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end', marginTop: '16px' }}>
          <button className="btn" onClick={() => setModalOpen(false)}>Cancelar</button>
          <button className="btn btn-primary" onClick={actualizarEstado}>Guardar</button>
        </div>
      </Modal>
    </div>
  );
};

export default Pedidos;