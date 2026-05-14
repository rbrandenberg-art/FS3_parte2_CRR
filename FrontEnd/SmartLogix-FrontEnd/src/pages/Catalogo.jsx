import { useEffect, useState } from 'react';
import { getInventario } from '../api/api';
import { useCarrito } from '../context/CarritoContext';

const MOCK_PRODUCTOS = [
    { id: 1, nombre: 'Laptop Pro X', descripcion: 'Laptop de alto rendimiento para profesionales', precio: 1299990, stock: 5, categoria: 'Computación' },
    { id: 2, nombre: 'Monitor UltraWide', descripcion: 'Monitor 34" curvo para máxima productividad', precio: 599990, stock: 8, categoria: 'Periféricos' },
    { id: 3, nombre: 'Teclado Mecánico RGB', descripcion: 'Teclado mecánico con switches Cherry MX', precio: 89990, stock: 15, categoria: 'Periféricos' },
    { id: 4, nombre: 'Mouse Inalámbrico', descripcion: 'Mouse ergonómico con 90 días de batería', precio: 49990, stock: 20, categoria: 'Periféricos' },
    { id: 5, nombre: 'Auriculares Noise Cancelling', descripcion: 'Sonido premium con cancelación activa de ruido', precio: 249990, stock: 3, categoria: 'Audio' },
    { id: 6, nombre: 'Webcam 4K', descripcion: 'Cámara web 4K para videollamadas profesionales', precio: 129990, stock: 0, categoria: 'Accesorios' },
];

const Catalogo = () => {
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const { agregarAlCarrito } = useCarrito();
    const [agregado, setAgregado] = useState({});

    useEffect(() => {
        const fetchProductos = async () => {
            try {
                const res = await getInventario();
                setProductos(res.data);
            } catch {
                setProductos(MOCK_PRODUCTOS);
            } finally {
                setLoading(false);
            }
        };
        fetchProductos();
    }, []);

    const handleAgregar = (producto) => {
        agregarAlCarrito(producto);
        setAgregado((prev) => ({ ...prev, [producto.id]: true }));
        setTimeout(() => setAgregado((prev) => ({ ...prev, [producto.id]: false })), 1500);
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-900 mx-auto mb-4"></div>
                    <p className="text-gray-600">Cargando productos...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-6 py-8">
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-blue-900">Catálogo de Productos</h1>
                <p className="text-gray-500 mt-1">{productos.length} productos disponibles</p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {productos.map((producto) => (
                    <div key={producto.id} className="bg-white rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 overflow-hidden border border-gray-100">
                        <div className="bg-gradient-to-br from-blue-50 to-blue-100 h-40 flex items-center justify-center">
                            <span className="text-5xl">📦</span>
                        </div>
                        <div className="p-5">
                            <div className="flex items-center gap-2 mb-1">
                                <span className="text-xs text-gray-400 font-mono">ID: {producto.id}</span>
                                {producto.categoria && (
                                    <span className="text-xs bg-blue-100 text-blue-600 px-2 py-0.5 rounded-full">{producto.categoria}</span>
                                )}
                            </div>
                            <h2 className="text-lg font-bold text-gray-800 mt-1">{producto.nombre}</h2>
                            <p className="text-gray-500 text-sm mt-2 line-clamp-2">{producto.descripcion}</p>
                            <div className="mt-4 flex items-center justify-between">
                                <span className="text-xl font-bold text-blue-700">
                                    ${Number(producto.precio).toLocaleString('es-CL')}
                                </span>
                                <span className={`text-xs px-2 py-1 rounded-full font-medium ${producto.stock > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                                    {producto.stock > 0 ? `Stock: ${producto.stock}` : 'Sin stock'}
                                </span>
                            </div>
                            <button
                                onClick={() => handleAgregar(producto)}
                                disabled={producto.stock === 0}
                                className={`mt-4 w-full py-2 rounded-lg font-semibold transition-all duration-200 ${producto.stock === 0
                                    ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                                    : agregado[producto.id]
                                        ? 'bg-green-500 text-white'
                                        : 'bg-blue-900 hover:bg-blue-700 text-white'
                                    }`}
                            >
                                {agregado[producto.id] ? '✓ Agregado' : producto.stock === 0 ? 'Sin stock' : 'Agregar al carrito'}
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Catalogo;