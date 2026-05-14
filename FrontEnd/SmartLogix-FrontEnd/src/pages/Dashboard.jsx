import React from 'react';

const Dashboard = () => {
  return (
    <div className="page active">
      <div className="grid-4">
        <div className="metric">
          <div className="metric-label">Productos en stock</div>
          <div className="metric-value">248</div>
          <div className="metric-delta" style={{ color: 'var(--color-text-success)' }}>↑ 12 esta semana</div>
        </div>
        <div className="metric">
          <div className="metric-label">Pedidos activos</div>
          <div className="metric-value">34</div>
          <div className="metric-delta" style={{ color: 'var(--color-text-warning)' }}>8 pendientes</div>
        </div>
        <div className="metric">
          <div className="metric-label">Envíos en tránsito</div>
          <div className="metric-value">21</div>
          <div className="metric-delta" style={{ color: 'var(--color-text-secondary)' }}>3 hoy</div>
        </div>
        <div className="metric">
          <div className="metric-label">Usuarios registrados</div>
          <div className="metric-value">156</div>
          <div className="metric-delta" style={{ color: 'var(--color-text-success)' }}>↑ 4 este mes</div>
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-title">Pedidos recientes</div>
          <table>
            <thead>
              <tr><th>ID</th><th>Cliente</th><th>Estado</th><th>Total</th></tr>
            </thead>
            <tbody>
              <tr><td>#1042</td><td>Ana García</td><td><span className="badge badge-green">COMPLETADO</span></td><td>$84.900</td></tr>
              <tr><td>#1041</td><td>Luis Mora</td><td><span className="badge badge-amber">EN_PROCESO</span></td><td>$32.500</td></tr>
              <tr><td>#1040</td><td>Carmen Ríos</td><td><span className="badge badge-blue">PENDIENTE</span></td><td>$120.000</td></tr>
            </tbody>
          </table>
        </div>
        <div className="card">
          <div className="card-title">Envíos recientes</div>
          <table>
            <thead>
              <tr><th>Seguimiento</th><th>Pedido</th><th>Estado</th></tr>
            </thead>
            <tbody>
              <tr><td style={{ fontSize: '11px', color: 'var(--color-text-secondary)' }}>SL-20248A</td><td>#1042</td><td><span className="badge badge-green">ENTREGADO</span></td></tr>
              <tr><td style={{ fontSize: '11px', color: 'var(--color-text-secondary)' }}>SL-20249B</td><td>#1041</td><td><span className="badge badge-amber">EN_CAMINO</span></td></tr>
              <tr><td style={{ fontSize: '11px', color: 'var(--color-text-secondary)' }}>SL-20250C</td><td>#1040</td><td><span className="badge badge-blue">PREPARANDO</span></td></tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;