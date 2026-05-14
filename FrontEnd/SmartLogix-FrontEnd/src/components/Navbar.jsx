import { Link, useNavigate } from 'react-router-dom';
import { useCarrito } from '../context/CarritoContext';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
    const { totalItems } = useCarrito();
    const { usuario, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="bg-blue-900 text-white shadow-lg sticky top-0 z-50">
            <div className="container mx-auto px-6 py-4 flex items-center justify-between">
                <Link to="/" className="text-2xl font-bold tracking-tight">
                    Smart<span className="text-blue-300">Logix</span>
                </Link>

                <div className="flex items-center gap-6">
                    <Link to="/" className="hover:text-blue-300 transition-colors font-medium">
                        Catálogo
                    </Link>

                    {usuario ? (
                        <>
                            <Link to="/mis-pedidos" className="hover:text-blue-300 transition-colors font-medium">
                                Mis Pedidos
                            </Link>
                            <span className="text-blue-300 text-sm">Hola, {usuario.nombre || usuario.email}</span>
                            <button
                                onClick={handleLogout}
                                className="text-sm bg-blue-700 hover:bg-blue-600 px-3 py-1 rounded transition-colors"
                            >
                                Salir
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/login" className="hover:text-blue-300 transition-colors font-medium">
                                Login
                            </Link>
                            <Link to="/registro" className="hover:text-blue-300 transition-colors font-medium">
                                Registro
                            </Link>
                        </>
                    )}

                    <Link to="/carrito" className="relative">
                        <span className="text-2xl">🛒</span>
                        {totalItems > 0 && (
                            <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold">
                                {totalItems}
                            </span>
                        )}
                    </Link>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;