import { useEffect, useState } from 'react';
import { getPedidos } from '../api/api';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const MOCK_PEDIDOS = [
    { id: 1, total: 1349980, estado: 'entregado', fecha: '2026-05-10', items: [{ nombre: 'Laptop Pro X', cantidad: 1 }, { nombre: 'Mouse Inalámbrico', cantidad: 1 }] },
    { id: 2, total: 89990, estado: 'en_camino', fecha: '2026-05-12', items: [{ nombre: 'Teclado Mecánico RGB', cantidad: 1 }] },
];

const estadoColor = {
    pendiente: 'bg-yellow-100 text-yellow-700',
    en_camino: 'bg-blue-100 text-blue-700',
    entregado: 'bg-green-100 text-green-700',
    cancelado: 'bg-red-100 text-red-700',
};

const estadoLabel = {
    pendiente: '⏳ Pendiente',
    en_camino: '🚚 En camino',
    entregado: '✅ Entregado',
    cancelado: '❌ Cancelado',
};

const MisPedidos = () => {
    const [pedidos, setPedidos] = useState([]);
    const [loading, setLoading] = useState(true);
    const { usuario } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!usuario) { navigate('/login'); return; }
        const fetchPedidos = async () => {
            try {
                const res = await getPedidos();
                setPedidos(res.data);
            } catch {
                setPedidos(MOCK_PEDIDOS);
            } finally {
                setLoading(false);
            }
        };
        fetchPedidos();
    }, [usuario, navigate]);

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-900"></div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-6 py-8 max-w-3xl">
            <h1 className="text-3xl font-bold text-blue-900 mb-8">Mis Pedidos</h1>
            {pedidos.length === 0 ? (
                <div className="text-center py-16">
                    <div className="text-5xl mb-4">📋</div>
                    <p className="text-gray-500">No tienes pedidos todavía</p>
                    <button onClick={() => navigate('/')} className="mt-4 bg-blue-900 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition">
                        Ver productos
                    </button>
                </div>
            ) : (
                <div className="space-y-4">
                    {pedidos.map((pedido) => (
                        <div key={pedido.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                            <div className="flex items-center justify-between mb-3">
                                <div>
                                    <span className="font-mono text-sm text-gray-400">Pedido #{pedido.id}</span>
                                    <p className="text-xs text-gray-400 mt-0.5">{pedido.fecha}</p>
                                </div>
                                <span className={`px-3 py-1 rounded-full text-sm font-medium ${estadoColor[pedido.estado] || 'bg-gray-100 text-gray-600'}`}>
                                    {estadoLabel[pedido.estado] || pedido.estado}
                                </span>
                            </div>
                            {pedido.items && (
                                <ul className="text-sm text-gray-600 mb-3 space-y-1">
                                    {pedido.items.map((item, i) => (
                                        <li key={i}>• {item.nombre} x{item.cantidad}</li>
                                    ))}
                                </ul>
                            )}
                            <div className="border-t pt-3 flex justify-between items-center">
                                <span className="text-gray-500 text-sm">Total</span>
                                <span className="font-bold text-blue-900 text-lg">${Number(pedido.total).toLocaleString('es-CL')}</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MisPedidos;