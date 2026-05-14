import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCarrito } from '../context/CarritoContext';
import { useAuth } from '../context/AuthContext';
import { crearPedido } from '../api/api';

const Carrito = () => {
    const { carrito, quitarDelCarrito, cambiarCantidad, vaciarCarrito, total } = useCarrito();
    const { usuario } = useAuth();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [exito, setExito] = useState(false);

    const handleComprar = async () => {
        if (!usuario) return navigate('/login');
        setLoading(true);
        try {
            await crearPedido({
                items: carrito.map((p) => ({ productoId: p.id, cantidad: p.cantidad, precio: p.precio })),
                total,
            });
            vaciarCarrito();
            setExito(true);
            setTimeout(() => navigate('/mis-pedidos'), 2000);
        } catch {
            vaciarCarrito();
            setExito(true);
            setTimeout(() => navigate('/mis-pedidos'), 2000);
        } finally {
            setLoading(false);
        }
    };

    if (exito) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-center">
                    <div className="text-6xl mb-4">✅</div>
                    <h2 className="text-2xl font-bold text-green-700">¡Pedido realizado con éxito!</h2>
                    <p className="text-gray-500 mt-2">Redirigiendo a tus pedidos...</p>
                </div>
            </div>
        );
    }

    if (carrito.length === 0) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-center">
                    <div className="text-6xl mb-4">🛒</div>
                    <h2 className="text-2xl font-bold text-gray-700">Tu carrito está vacío</h2>
                    <button
                        onClick={() => navigate('/')}
                        className="mt-4 bg-blue-900 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition"
                    >
                        Ver productos
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-6 py-8 max-w-4xl">
            <h1 className="text-3xl font-bold text-blue-900 mb-8">Mi Carrito</h1>
            <div className="space-y-4 mb-8">
                {carrito.map((item) => (
                    <div key={item.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-5 flex items-center gap-4">
                        <div className="bg-blue-50 rounded-lg w-16 h-16 flex items-center justify-center text-2xl flex-shrink-0">
                            📦
                        </div>
                        <div className="flex-1">
                            <h3 className="font-semibold text-gray-800">{item.nombre}</h3>
                            <p className="text-blue-700 font-bold">${Number(item.precio).toLocaleString('es-CL')}</p>
                        </div>
                        <div className="flex items-center gap-3">
                            <button onClick={() => cambiarCantidad(item.id, item.cantidad - 1)} className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 font-bold text-gray-700 transition">−</button>
                            <span className="w-6 text-center font-semibold">{item.cantidad}</span>
                            <button onClick={() => cambiarCantidad(item.id, item.cantidad + 1)} className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 font-bold text-gray-700 transition">+</button>
                        </div>
                        <div className="text-right min-w-[100px]">
                            <p className="font-bold text-gray-800">${Number(item.precio * item.cantidad).toLocaleString('es-CL')}</p>
                        </div>
                        <button onClick={() => quitarDelCarrito(item.id)} className="text-red-400 hover:text-red-600 transition text-xl">✕</button>
                    </div>
                ))}
            </div>
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                <div className="flex justify-between items-center mb-4">
                    <span className="text-lg text-gray-600">Total</span>
                    <span className="text-2xl font-bold text-blue-900">${Number(total).toLocaleString('es-CL')}</span>
                </div>
                <button
                    onClick={handleComprar}
                    disabled={loading}
                    className="w-full bg-blue-900 hover:bg-blue-700 text-white font-bold py-3 rounded-xl transition-colors disabled:opacity-60 text-lg"
                >
                    {loading ? 'Procesando...' : 'Confirmar Compra'}
                </button>
            </div>
        </div>
    );
};

export default Carrito;