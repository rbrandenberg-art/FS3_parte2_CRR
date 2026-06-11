// src/pages/Registro.jsx
// src/pages/Registro.jsx
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registrarUsuario } from '../api/api'; // Cliente Axios unificado
import { useAuth } from '../context/AuthContext'; // Contexto global de autenticación

const Registro = () => {
    const navigate = useNavigate();
    const { login } = useAuth();
    
    // CORRECCIÓN CONTRATO 3: El estado inicial debe mapear 1:1 los 5 campos del UsuarioRequestDTO
    const [form, setForm] = useState({
        nombre: '',
        apellido: '', // Agregado obligatorio según el documento
        email: '',
        password: '',
        telefono: ''  // Agregado opcional según el documento
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            // ACT: Envía exactamente el objeto plano requerido por el backend
            const res = await registrarUsuario(form);
            
            // CORRECCIÓN: El backend responde de forma plana con UsuarioResponseDTO 
            // Campos que retorna: id, nombre, apellido, email, rol. JAMÁS viaja contraseña ni token aquí.
            const usuarioRegistrado = res.data; 
            const tokenValido = 'session-active'; // Simulación local o controlada por interceptores

            login(usuarioRegistrado, tokenValido);
            
            // Redirección inmediata a la raíz del catálogo
            navigate('/');
        } catch (err) {
            console.error('Error al registrar usuario:', err);
            // Captura el mensaje de error de negocio (Ej: "El email ya está registrado" en un 409 Conflict)
            setError(err.response?.data?.mensaje || 'Error al intentar crear la cuenta. Inténtelo de nuevo.');
        } finally {
            loading && setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-blue-100 p-6">
            <div className="bg-white rounded-2xl shadow-xl border border-gray-100 p-8 w-full max-w-md">
                <div className="text-center mb-6">
                    <h1 className="text-3xl font-bold text-blue-900">SmartLogix</h1>
                    <p className="text-gray-500 text-sm mt-1">Crea tu cuenta en la plataforma logística</p>
                </div>

                {error && (
                    <div className="bg-red-50 text-red-600 text-sm p-3 rounded-lg mb-4 border border-red-100 text-center">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    {/* Fila divisoria para Nombre y Apellido obligatorios */}
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Nombre *</label>
                            <input
                                type="text"
                                name="nombre"
                                value={form.nombre}
                                onChange={handleChange}
                                placeholder="Ej. Juan"
                                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
                                required
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Apellido *</label>
                            <input
                                type="text"
                                name="apellido"
                                value={form.apellido}
                                onChange={handleChange}
                                placeholder="Ej. Pérez"
                                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
                                required
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Correo Electrónico *</label>
                        <input
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            placeholder="correo@smartlogix.com"
                            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña *</label>
                        <input
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            placeholder="••••••••"
                            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
                            required
                        />
                    </div>

                    {/* Teléfono opcional estipulado en la documentación técnica */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Teléfono (Opcional)</label>
                        <input
                            type="text"
                            name="telefono"
                            value={form.telefono}
                            onChange={handleChange}
                            placeholder="+56912345678"
                            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-blue-900 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg transition-colors disabled:opacity-60 mt-2"
                    >
                        {loading ? 'Registrando...' : 'Crear Cuenta'}
                    </button>
                </form>

                <p className="text-center text-sm text-gray-500 mt-6">
                    ¿Ya tienes cuenta?{' '}
                    <Link to="/login" className="text-blue-600 hover:underline font-medium">
                        Inicia sesión
                    </Link>
                </p>
            </div>
        </div>
    );
};

export default Registro;