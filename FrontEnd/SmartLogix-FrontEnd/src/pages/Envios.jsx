import React, { useState, useEffect } from 'react';
import { apiFetch } from '../api/apiService';
import Modal from '../components/Modal';

const Envios = () => {
  const [envios, setEnvios] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [envioActual, setEnvioActual] = useState(null);
  const [nuevoEstado, setNuevoEstado] = useState('PREPARANDO');

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    const data = await apiFetch('/api/envios');
    if (data) setEnvios(data);
  };

  const abrirModalEstado = (envio) => {
    setEnvioActual(envio);
    setNuevoEstado(envio.estado || 'PREPARANDO');
    setModalOpen(true);
  };

  const actualizarEstado = async () => {
    if (!envioActual) return;
    
    // Primero rastreamos para obtener el ID interno según tu lógica original
    const envioRastreado = await apiFetch(`/api/envios/rastrear/${envioActual.numeroSeguimiento}`);
    if (!envioRastreado) {
      alert('No se encontró el envío en el backend');
      return;
    }

    const res = await apiFetch(`/api/envios/${envioRastreado.id}/estado?estado=${nuevoEstado}`, { method: 'PATCH' });
    if (res) {
      cargarDatos();
      setModalOpen(false);
    }
  };

  const enviosFiltrados = envios.filter(e => 
    e.numeroSeguimiento?.toLowerCase().includes(busqueda.toLowerCase()) || 
    e.pedidoId?.toString().includes(busqueda)
  );

  const badgeMap = { ENTREGADO: 'badge-green', EN_CAMINO: 'badge-amber', PREPARANDO: 'badge-blue', CANCELADO: 'badge-red' };

  return (
    <div className="page active">
      <div className="search-bar">
        <input 
          type="text" 
          placeholder="Buscar por nº seguimiento..." 
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn" onClick={cargarDatos}><i className="ti ti-refresh"></i> Recargar</button>
      </div>

      <div className="card">
        <table>
          <thead><tr><th>Seguimiento</th><th>Pedido</th><th>Tipo</th><th>Destino</th><th>Estado</th><th></th></tr></thead>
          <tbody>
            {enviosFiltrados.map((e) => (
              <tr key={e.numeroSeguimiento}>
                <td style={{ fontSize: '12px' }}>{e.numeroSeguimiento || '—'}</td>
                <td>#{e.pedidoId || '?'}</td>
                <td>{e.tipo || '—'}</td>
                <td>{e.destino || '—'}</td>
                <td><span className={`badge ${badgeMap[e.estado] || 'badge-gray'}`}>{e.estado || '—'}</span></td>
                <td><i className="ti ti-edit row-action" onClick={() => abrirModalEstado(e)}></i></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)} title={`Cambiar estado — ${envioActual?.numeroSeguimiento}`}>
        <div className="form-row">
          <div className="form-label">Nuevo estado</div>
          <select className="form-control" value={nuevoEstado} onChange={(e) => setNuevoEstado(e.target.value)}>
            <option value="PREPARANDO">PREPARANDO</option>
            <option value="EN_CAMINO">EN_CAMINO</option>
            <option value="ENTREGADO">ENTREGADO</option>
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

export default Envios;